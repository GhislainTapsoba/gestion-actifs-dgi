# Ajoute une methode de recherche par login utilisateur dans AffectationRepository.java
$path = "src\main\java\com\dgi\gestionactifs\repository\AffectationRepository.java"

if (-not (Test-Path $path)) {
    Write-Host "ERREUR : fichier introuvable a $path" -ForegroundColor Red
    exit 1
}

$content = Get-Content $path -Raw

if ($content -match 'findByUtilisateur_LoginAndDateRestitutionIsNull') {
    Write-Host "Deja present, rien a faire." -ForegroundColor Yellow
    exit 0
}

$lines = Get-Content $path
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

$newLines = New-Object System.Collections.Generic.List[string]
for ($i = 0; $i -lt $lastBraceIndex; $i++) { $newLines.Add($lines[$i]) }
$newLines.Add("")
$newLines.Add("    /**")
$newLines.Add("     * Trouve les affectations actives (sans date de restitution) d'un utilisateur donne.")
$newLines.Add("     * Utilise pour le filtrage 'mes actifs' cote Agent.")
$newLines.Add("     */")
$newLines.Add("    java.util.List<com.dgi.gestionactifs.domain.Affectation> findByUtilisateur_LoginAndDateRestitutionIsNull(String login);")
for ($i = $lastBraceIndex; $i -lt $lines.Count; $i++) { $newLines.Add($lines[$i]) }

Set-Content -Path $path -Value $newLines -Encoding UTF8
Write-Host "OK : methode findByUtilisateur_LoginAndDateRestitutionIsNull ajoutee dans $path" -ForegroundColor Green
