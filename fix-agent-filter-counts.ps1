# Applique le meme filtrage Agent aux endpoints /count (Actif, Transfert, Maintenance)
# pour que le futur dashboard affiche des chiffres coherents selon le role.

function Fix-CountEndpoint {
    param($path, $oldBlock, $newBlock, $label)

    if (-not (Test-Path $path)) {
        Write-Host "ERREUR : fichier introuvable a $path" -ForegroundColor Red
        return
    }
    $raw = Get-Content $path -Raw
    $content = $raw -replace "`r`n", "`n"
    if ($content -match 'isAgentOnly.*count', 'i') { }
    if ($content.Contains(($newBlock -replace "`r`n","`n"))) {
        Write-Host "$label : deja present." -ForegroundColor Yellow
        return
    }
    $old = $oldBlock -replace "`r`n", "`n"
    $new = $newBlock -replace "`r`n", "`n"
    if ($content.Contains($old)) {
        $content = $content.Replace($old, $new)
        $final = $content -replace "`n", "`r`n"
        Set-Content -Path $path -Value $final -Encoding UTF8 -NoNewline
        Write-Host "$label : filtrage ajoute au endpoint /count." -ForegroundColor Green
    } else {
        Write-Host "$label : ATTENTION bloc introuvable, verifier manuellement." -ForegroundColor Red
    }
}

# --- Actif ---
Fix-CountEndpoint `
    -path "src\main\java\com\dgi\gestionactifs\web\rest\ActifResource.java" `
    -oldBlock @'
    @GetMapping("/count")
    public ResponseEntity<Long> countActifs(ActifCriteria criteria) {
        LOG.debug("REST request to count Actifs by criteria: {}", criteria);
        return ResponseEntity.ok().body(actifQueryService.countByCriteria(criteria));
    }
'@ `
    -newBlock @'
    @GetMapping("/count")
    public ResponseEntity<Long> countActifs(ActifCriteria criteria) {
        LOG.debug("REST request to count Actifs by criteria: {}", criteria);
        boolean isAgentOnlyCount =
            SecurityUtils.hasCurrentUserThisAuthority("ROLE_AGENT") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_TECHNICIEN") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_RESPONSABLE");
        if (isAgentOnlyCount) {
            List<Long> actifIds = affectationRepository
                .findByUtilisateur_LoginAndDateRestitutionIsNull(SecurityUtils.getCurrentUserLogin().orElse(""))
                .stream()
                .map(affectation -> affectation.getActif().getId())
                .distinct()
                .toList();
            LongFilter idFilter = new LongFilter();
            idFilter.setIn(actifIds);
            criteria.setId(idFilter);
        }
        return ResponseEntity.ok().body(actifQueryService.countByCriteria(criteria));
    }
'@ `
    -label "Actif"

# --- Transfert ---
Fix-CountEndpoint `
    -path "src\main\java\com\dgi\gestionactifs\web\rest\TransfertResource.java" `
    -oldBlock @'
    @GetMapping("/count")
    public ResponseEntity<Long> countTransferts(TransfertCriteria criteria) {
        LOG.debug("REST request to count Transferts by criteria: {}", criteria);
        return ResponseEntity.ok().body(transfertQueryService.countByCriteria(criteria));
    }
'@ `
    -newBlock @'
    @GetMapping("/count")
    public ResponseEntity<Long> countTransferts(TransfertCriteria criteria) {
        LOG.debug("REST request to count Transferts by criteria: {}", criteria);
        boolean isAgentOnlyCount =
            SecurityUtils.hasCurrentUserThisAuthority("ROLE_AGENT") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_TECHNICIEN") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_RESPONSABLE");
        if (isAgentOnlyCount) {
            List<Long> actifIds = affectationRepository
                .findByUtilisateur_LoginAndDateRestitutionIsNull(SecurityUtils.getCurrentUserLogin().orElse(""))
                .stream()
                .map(affectation -> affectation.getActif().getId())
                .distinct()
                .toList();
            LongFilter actifIdFilter = new LongFilter();
            actifIdFilter.setIn(actifIds);
            criteria.setActifId(actifIdFilter);
        }
        return ResponseEntity.ok().body(transfertQueryService.countByCriteria(criteria));
    }
'@ `
    -label "Transfert"

# --- Maintenance ---
Fix-CountEndpoint `
    -path "src\main\java\com\dgi\gestionactifs\web\rest\MaintenanceResource.java" `
    -oldBlock @'
    @GetMapping("/count")
    public ResponseEntity<Long> countMaintenances(MaintenanceCriteria criteria) {
        LOG.debug("REST request to count Maintenances by criteria: {}", criteria);
        return ResponseEntity.ok().body(maintenanceQueryService.countByCriteria(criteria));
    }
'@ `
    -newBlock @'
    @GetMapping("/count")
    public ResponseEntity<Long> countMaintenances(MaintenanceCriteria criteria) {
        LOG.debug("REST request to count Maintenances by criteria: {}", criteria);
        boolean isAgentOnlyCount =
            SecurityUtils.hasCurrentUserThisAuthority("ROLE_AGENT") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_TECHNICIEN") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_RESPONSABLE");
        if (isAgentOnlyCount) {
            List<Long> actifIds = affectationRepository
                .findByUtilisateur_LoginAndDateRestitutionIsNull(SecurityUtils.getCurrentUserLogin().orElse(""))
                .stream()
                .map(affectation -> affectation.getActif().getId())
                .distinct()
                .toList();
            LongFilter actifIdFilter = new LongFilter();
            actifIdFilter.setIn(actifIds);
            criteria.setActifId(actifIdFilter);
        }
        return ResponseEntity.ok().body(maintenanceQueryService.countByCriteria(criteria));
    }
'@ `
    -label "Maintenance"

Write-Host "Termine. Verifie les messages ci-dessus et compile avec .\mvnw" -ForegroundColor Cyan
