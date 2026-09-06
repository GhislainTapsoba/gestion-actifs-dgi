# Ajoute les endpoints REST /valider et /rejeter dans TransfertResource.java
$path = "src\main\java\com\dgi\gestionactifs\web\rest\TransfertResource.java"

if (-not (Test-Path $path)) {
    Write-Host "ERREUR : fichier introuvable a $path" -ForegroundColor Red
    exit 1
}

$content = Get-Content $path -Raw

if ($content -match 'validerTransfert') {
    Write-Host "Deja present, rien a faire." -ForegroundColor Yellow
    exit 0
}

$lines = Get-Content $path

# --- Ajout de l'import java.util.Map si absent ---
if ($content -notmatch 'import java\.util\.Map;') {
    for ($i = 0; $i -lt $lines.Count; $i++) {
        if ($lines[$i] -match '^import java\.util\.List;') {
            $newLines = New-Object System.Collections.Generic.List[string]
            for ($j = 0; $j -le $i; $j++) { $newLines.Add($lines[$j]) }
            $newLines.Add("import java.util.Map;")
            for ($j = $i + 1; $j -lt $lines.Count; $j++) { $newLines.Add($lines[$j]) }
            $lines = $newLines
            break
        }
    }
}

# --- Insertion des endpoints avant la derniere accolade fermante ---
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

    /**
     * {@code PATCH  /transferts/:id/valider} : valide un transfert en attente.
     *
     * @param id l'id du transfert a valider.
     * @return le transfert mis a jour.
     */
    @PreAuthorize("hasAuthority('ROLE_RESPONSABLE')")
    @PatchMapping("/{id}/valider")
    public ResponseEntity<TransfertDTO> validerTransfert(@PathVariable Long id) {
        LOG.debug("REST request to valider Transfert : {}", id);
        TransfertDTO result = transfertService.valider(id);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PATCH  /transferts/:id/rejeter} : rejette un transfert en attente.
     *
     * @param id l'id du transfert a rejeter.
     * @param body doit contenir la cle "commentaireRejet" (obligatoire).
     * @return le transfert mis a jour.
     */
    @PreAuthorize("hasAuthority('ROLE_RESPONSABLE')")
    @PatchMapping("/{id}/rejeter")
    public ResponseEntity<TransfertDTO> rejeterTransfert(@PathVariable Long id, @RequestBody Map<String, String> body) {
        LOG.debug("REST request to rejeter Transfert : {}", id);
        TransfertDTO result = transfertService.rejeter(id, body.get("commentaireRejet"));
        return ResponseEntity.ok(result);
    }
'@ -split "`r?`n"

$newLines = New-Object System.Collections.Generic.List[string]
for ($i = 0; $i -lt $lastBraceIndex; $i++) { $newLines.Add($lines[$i]) }
foreach ($ml in $methodBlock) { $newLines.Add($ml) }
for ($i = $lastBraceIndex; $i -lt $lines.Count; $i++) { $newLines.Add($lines[$i]) }

Set-Content -Path $path -Value $newLines -Encoding UTF8
Write-Host "OK : endpoints valider/rejeter ajoutes dans $path" -ForegroundColor Green
Write-Host "Verifie que transfertService est bien le nom du champ injecte, puis compile." -ForegroundColor Yellow
