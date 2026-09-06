# Ajoute les methodes valider() et rejeter() a l'interface TransfertService.java
$path = "src\main\java\com\dgi\gestionactifs\service\TransfertService.java"

if (-not (Test-Path $path)) {
    Write-Host "ERREUR : fichier introuvable a $path" -ForegroundColor Red
    exit 1
}

$content = Get-Content $path -Raw

if ($content -match 'TransfertDTO valider\(Long id\);') {
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
for ($i = 0; $i -lt $lastBraceIndex; $i++) {
    $newLines.Add($lines[$i])
}
$newLines.Add("")
$newLines.Add("    /**")
$newLines.Add("     * Valide un transfert en attente.")
$newLines.Add("     * @param id l'id du transfert.")
$newLines.Add("     * @return le transfert mis a jour.")
$newLines.Add("     */")
$newLines.Add("    TransfertDTO valider(Long id);")
$newLines.Add("")
$newLines.Add("    /**")
$newLines.Add("     * Rejette un transfert en attente avec un commentaire obligatoire.")
$newLines.Add("     * @param id l'id du transfert.")
$newLines.Add("     * @param commentaireRejet le motif du rejet, obligatoire.")
$newLines.Add("     * @return le transfert mis a jour.")
$newLines.Add("     */")
$newLines.Add("    TransfertDTO rejeter(Long id, String commentaireRejet);")
for ($i = $lastBraceIndex; $i -lt $lines.Count; $i++) {
    $newLines.Add($lines[$i])
}

Set-Content -Path $path -Value $newLines -Encoding UTF8
Write-Host "OK : methodes valider/rejeter ajoutees a l'interface $path" -ForegroundColor Green
