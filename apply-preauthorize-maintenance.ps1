# Script : ajoute les annotations @PreAuthorize sur MaintenanceResource.java
# selon la matrice : Admin=R, Technicien=CRU, Responsable=R, Agent=C (signalement) + R
# A lancer depuis la racine du projet.

$path = "src\main\java\com\dgi\gestionactifs\web\rest\MaintenanceResource.java"

if (-not (Test-Path $path)) {
    Write-Host "ERREUR : fichier introuvable a $path" -ForegroundColor Red
    exit 1
}

$lines = Get-Content $path

$patterns = @(
    @{ Regex = '^@PostMapping\((""|"/maintenances")\)$';                    Rule = '@PreAuthorize("hasAuthority(''ROLE_TECHNICIEN'') or hasAuthority(''ROLE_AGENT'')")' },
    @{ Regex = '^@PutMapping\((?:"/\{id\}"|"/maintenances/\{id\}")\)$';     Rule = '@PreAuthorize("hasAuthority(''ROLE_TECHNICIEN'') or hasAuthority(''ROLE_RESPONSABLE'')")' },
    @{ Regex = '^@DeleteMapping\((?:"/\{id\}"|"/maintenances/\{id\}")\)$';  Rule = '@PreAuthorize("hasAuthority(''ROLE_ADMIN'')")' },
    @{ Regex = '^@GetMapping\((""|"/maintenances")\)$';                     Rule = '@PreAuthorize("hasAuthority(''ROLE_ADMIN'') or hasAuthority(''ROLE_TECHNICIEN'') or hasAuthority(''ROLE_RESPONSABLE'') or hasAuthority(''ROLE_AGENT'')")' },
    @{ Regex = '^@GetMapping\((?:"/\{id\}"|"/maintenances/\{id\}")\)$';     Rule = '@PreAuthorize("hasAuthority(''ROLE_ADMIN'') or hasAuthority(''ROLE_TECHNICIEN'') or hasAuthority(''ROLE_RESPONSABLE'') or hasAuthority(''ROLE_AGENT'')")' }
)

$newLines = New-Object System.Collections.Generic.List[string]

foreach ($line in $lines) {
    $trimmed = $line.Trim()
    $matched = $patterns | Where-Object { $trimmed -match $_.Regex } | Select-Object -First 1
    if ($matched) {
        $alreadyThere = $newLines.Count -gt 0 -and $newLines[$newLines.Count - 1].Trim() -eq $matched.Rule
        if (-not $alreadyThere) {
            $indent = $line.Substring(0, $line.Length - $line.TrimStart().Length)
            $newLines.Add($indent + $matched.Rule)
        }
    }
    $newLines.Add($line)
}

$joined = $newLines -join "`n"
if ($joined -notmatch 'import org\.springframework\.security\.access\.prepost\.PreAuthorize;') {
    for ($i = 0; $i -lt $newLines.Count; $i++) {
        if ($newLines[$i] -match '^package ') {
            $newLines.Insert($i + 1, "")
            $newLines.Insert($i + 2, "import org.springframework.security.access.prepost.PreAuthorize;")
            break
        }
    }
}

Set-Content -Path $path -Value $newLines -Encoding UTF8
Write-Host "OK : annotations @PreAuthorize ajoutees dans $path" -ForegroundColor Green
Write-Host "Verifie le resultat (git status / git diff) et relance .\mvnw." -ForegroundColor Yellow
