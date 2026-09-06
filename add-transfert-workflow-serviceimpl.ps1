# Ajoute l'implementation de valider() et rejeter() dans TransfertServiceImpl.java
$path = "src\main\java\com\dgi\gestionactifs\service\impl\TransfertServiceImpl.java"

if (-not (Test-Path $path)) {
    Write-Host "ERREUR : fichier introuvable a $path" -ForegroundColor Red
    exit 1
}

$content = Get-Content $path -Raw

if ($content -match 'public TransfertDTO valider\(Long id\)') {
    Write-Host "Deja present, rien a faire." -ForegroundColor Yellow
    exit 0
}

$lines = Get-Content $path

# --- Ajout des imports manquants juste apres la ligne 'package ...;' ---
$importsToAdd = @()
if ($content -notmatch 'import com\.dgi\.gestionactifs\.web\.rest\.errors\.BadRequestAlertException;') {
    $importsToAdd += "import com.dgi.gestionactifs.web.rest.errors.BadRequestAlertException;"
}
if ($content -notmatch 'import com\.dgi\.gestionactifs\.domain\.enumeration\.StatutTransfert;') {
    $importsToAdd += "import com.dgi.gestionactifs.domain.enumeration.StatutTransfert;"
}
if ($content -notmatch 'import java\.time\.LocalDate;') {
    $importsToAdd += "import java.time.LocalDate;"
}

if ($importsToAdd.Count -gt 0) {
    for ($i = 0; $i -lt $lines.Count; $i++) {
        if ($lines[$i] -match '^package ') {
            $insertAt = $i + 1
            $newLines = New-Object System.Collections.Generic.List[string]
            for ($j = 0; $j -lt $insertAt; $j++) { $newLines.Add($lines[$j]) }
            $newLines.Add("")
            foreach ($imp in $importsToAdd) { $newLines.Add($imp) }
            for ($j = $insertAt; $j -lt $lines.Count; $j++) { $newLines.Add($lines[$j]) }
            $lines = $newLines
            break
        }
    }
}

# --- Insertion des methodes avant la derniere accolade fermante ---
$lastBraceIndex = -1
for ($i = $lines.Count - 1; $i -ge 0; $i--) {
    if ($lines[$i].Trim() -eq '}') {
        $lastBraceIndex = $i
        break
    }
}

if ($lastBraceIndex -eq -1) {
    Write-Host "ERREUR : impossible de localiser l'accolade fermante finale." -ForegroundColor Red
    exit 1
}

$methodBlock = @'

    @Override
    public TransfertDTO valider(Long id) {
        LOG.debug("Request to valider Transfert : {}", id);
        Transfert transfert = transfertRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Transfert introuvable", "transfert", "idnotfound"));
        if (transfert.getStatut() != StatutTransfert.EN_ATTENTE) {
            throw new BadRequestAlertException("Seul un transfert en attente peut etre valide", "transfert", "statutinvalide");
        }
        transfert.setStatut(StatutTransfert.VALIDE);
        transfert.setDateTraitement(LocalDate.now());
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
            throw new BadRequestAlertException("Seul un transfert en attente peut etre rejete", "transfert", "statutinvalide");
        }
        transfert.setStatut(StatutTransfert.REJETE);
        transfert.setCommentaireRejet(commentaireRejet);
        transfert.setDateTraitement(LocalDate.now());
        return transfertMapper.toDto(transfertRepository.save(transfert));
    }
'@ -split "`r?`n"

$newLines = New-Object System.Collections.Generic.List[string]
for ($i = 0; $i -lt $lastBraceIndex; $i++) { $newLines.Add($lines[$i]) }
foreach ($ml in $methodBlock) { $newLines.Add($ml) }
for ($i = $lastBraceIndex; $i -lt $lines.Count; $i++) { $newLines.Add($lines[$i]) }

Set-Content -Path $path -Value $newLines -Encoding UTF8
Write-Host "OK : implementation valider/rejeter ajoutee dans $path" -ForegroundColor Green
Write-Host "Verifie les noms des champs (transfertRepository, transfertMapper) et compile." -ForegroundColor Yellow
