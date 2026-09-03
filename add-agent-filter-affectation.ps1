# Ajoute le filtrage "mes affectations" pour l'Agent dans AffectationResource.java
$path = "src\main\java\com\dgi\gestionactifs\web\rest\AffectationResource.java"

if (-not (Test-Path $path)) {
    Write-Host "ERREUR : fichier introuvable a $path" -ForegroundColor Red
    exit 1
}

$content = Get-Content $path -Raw

if ($content -match 'isAgentOnly') {
    Write-Host "Deja present, rien a faire." -ForegroundColor Yellow
    exit 0
}

$lines = Get-Content $path

# --- Ajout des imports si absents ---
$importsToAdd = @()
if ($content -notmatch 'import com\.dgi\.gestionactifs\.security\.SecurityUtils;') {
    $importsToAdd += "import com.dgi.gestionactifs.security.SecurityUtils;"
}
if ($content -notmatch 'import tech\.jhipster\.service\.filter\.LongFilter;') {
    $importsToAdd += "import tech.jhipster.service.filter.LongFilter;"
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

# --- Insertion du filtrage juste avant l'appel a findByCriteria ---
$targetIndex = -1
for ($i = 0; $i -lt $lines.Count; $i++) {
    if ($lines[$i] -match '^\s*Page<AffectationDTO>\s+page\s*=\s*affectationQueryService\.findByCriteria\(criteria,\s*pageable\);\s*$') {
        $targetIndex = $i
        break
    }
}

if ($targetIndex -eq -1) {
    Write-Host "ATTENTION : ligne 'Page<AffectationDTO> page = affectationQueryService.findByCriteria(...)' non trouvee." -ForegroundColor Yellow
    Write-Host "Modification manuelle necessaire pour le filtrage." -ForegroundColor Yellow
    Set-Content -Path $path -Value $lines -Encoding UTF8
    exit 1
}

$filterBlock = @'
        boolean isAgentOnly =
            SecurityUtils.hasCurrentUserThisAuthority("ROLE_AGENT") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_TECHNICIEN") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_RESPONSABLE");

        if (isAgentOnly) {
            java.util.List<Long> affectationIds = affectationRepository
                .findByUtilisateur_Login(SecurityUtils.getCurrentUserLogin().orElse(""))
                .stream()
                .map(com.dgi.gestionactifs.domain.Affectation::getId)
                .distinct()
                .toList();
            LongFilter idFilter = new LongFilter();
            idFilter.setIn(affectationIds);
            criteria.setId(idFilter);
        }

'@ -split "`r?`n"

$newLines = New-Object System.Collections.Generic.List[string]
for ($i = 0; $i -lt $targetIndex; $i++) { $newLines.Add($lines[$i]) }
foreach ($fl in $filterBlock) { $newLines.Add($fl) }
for ($i = $targetIndex; $i -lt $lines.Count; $i++) { $newLines.Add($lines[$i]) }

Set-Content -Path $path -Value $newLines -Encoding UTF8
Write-Host "OK : filtrage Agent ajoute dans $path" -ForegroundColor Green
Write-Host "Verifie que le champ 'affectationRepository' existe bien dans la classe, puis compile." -ForegroundColor Yellow
