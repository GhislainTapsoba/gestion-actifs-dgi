package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.Transfert;
import com.dgi.gestionactifs.domain.enumeration.StatutTransfert;
import com.dgi.gestionactifs.repository.TransfertRepository;
import com.dgi.gestionactifs.repository.UserRepository;
import com.dgi.gestionactifs.security.SecurityUtils;
import com.dgi.gestionactifs.service.TransfertService;
import com.dgi.gestionactifs.service.dto.TransfertDTO;
import com.dgi.gestionactifs.service.mapper.TransfertMapper;
import com.dgi.gestionactifs.web.rest.errors.BadRequestAlertException;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.Transfert}.
 */
@Service
@Transactional
public class TransfertServiceImpl implements TransfertService {

    private static final Logger LOG = LoggerFactory.getLogger(TransfertServiceImpl.class);

    private final TransfertRepository transfertRepository;

    private final TransfertMapper transfertMapper;

    private final UserRepository userRepository;

    public TransfertServiceImpl(TransfertRepository transfertRepository, TransfertMapper transfertMapper, UserRepository userRepository) {
        this.transfertRepository = transfertRepository;
        this.transfertMapper = transfertMapper;
        this.userRepository = userRepository;
    }

    @Override
    public TransfertDTO save(TransfertDTO transfertDTO) {
        LOG.debug("Request to save Transfert : {}", transfertDTO);
        Transfert transfert = transfertMapper.toEntity(transfertDTO);
        transfert = transfertRepository.save(transfert);
        return transfertMapper.toDto(transfert);
    }

    @Override
    public TransfertDTO update(TransfertDTO transfertDTO) {
        LOG.debug("Request to update Transfert : {}", transfertDTO);
        Transfert transfert = transfertMapper.toEntity(transfertDTO);
        transfert = transfertRepository.save(transfert);
        return transfertMapper.toDto(transfert);
    }

    @Override
    public Optional<TransfertDTO> partialUpdate(TransfertDTO transfertDTO) {
        LOG.debug("Request to partially update Transfert : {}", transfertDTO);

        return transfertRepository
            .findById(transfertDTO.getId())
            .map(existingTransfert -> {
                transfertMapper.partialUpdate(existingTransfert, transfertDTO);

                return existingTransfert;
            })
            .map(transfertRepository::save)
            .map(transfertMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransfertDTO> findOne(Long id) {
        LOG.debug("Request to get Transfert : {}", id);
        return transfertRepository.findById(id).map(transfertMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Transfert : {}", id);
        transfertRepository.deleteById(id);
    }

    @Override
    public TransfertDTO valider(Long id) {
        LOG.debug("Request to valider Transfert : {}", id);
        Transfert transfert = transfertRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Transfert introuvable", "transfert", "idnotfound"));
        if (transfert.getStatut() != StatutTransfert.EN_ATTENTE) {
            throw new BadRequestAlertException("Seul un transfert en attente peut être validé", "transfert", "statutinvalide");
        }
        transfert.setStatut(StatutTransfert.VALIDE);
        transfert.setDateTraitement(LocalDate.now());
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(transfert::setValidateur);
        return transfertMapper.toDto(transfertRepository.save(transfert));
    }

    @Override
    public TransfertDTO rejeter(Long id, String commentaireRejet) {
        LOG.debug("Request to rejeter Transfert : {}", id);
        if (commentaireRejet == null || commentaireRejet.isBlank()) {
            throw new BadRequestAlertException("Un commentaire de rejet est obligatoire", "transfert", "commentairerequis");
        }
        Transfert transfert = transfertRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Transfert introuvable", "transfert", "idnotfound"));
        if (transfert.getStatut() != StatutTransfert.EN_ATTENTE) {
            throw new BadRequestAlertException("Seul un transfert en attente peut être rejeté", "transfert", "statutinvalide");
        }
        transfert.setStatut(StatutTransfert.REJETE);
        transfert.setCommentaireRejet(commentaireRejet.trim());
        transfert.setDateTraitement(LocalDate.now());
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(transfert::setValidateur);
        return transfertMapper.toDto(transfertRepository.save(transfert));
    }
}
