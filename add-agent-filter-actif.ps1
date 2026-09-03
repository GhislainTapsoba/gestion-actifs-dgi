# Ajoute le filtrage "mes actifs" pour l'Agent dans ActifResource.java
$path = "src\main\java\com\dgi\gestionactifs\web\rest\ActifResource.java"

if (-not (Test-Path $path)) {
    Write-Host "ERREUR : fichier introuvable a $path" -ForegroundColor Red
    exit 1
}

$content = Get-Content $path -Raw

if ($content -match 'isAgentOnly') {
    Write-Host "Deja present, rien a faire." -ForegroundColor Yellow
    exit 0
}

# --- 1. Ajout des imports ---
$oldImport = "import com.dgi.gestionactifs.repository.ActifRepository;"
$newImport = @"
import com.dgi.gestionactifs.repository.ActifRepository;
import com.dgi.gestionactifs.repository.AffectationRepository;
import com.dgi.gestionactifs.security.SecurityUtils;
"@
if ($content -notmatch [regex]::Escape($oldImport + "`nimport com.dgi.gestionactifs.repository.AffectationRepository;")) {
    $content = $content.Replace($oldImport, $newImport.Trim())
}

if ($content -notmatch 'import tech\.jhipster\.service\.filter\.LongFilter;') {
    $oldImport2 = "import org.springframework.web.bind.annotation.*;"
    $newImport2 = "import org.springframework.web.bind.annotation.*;`nimport tech.jhipster.service.filter.LongFilter;"
    $content = $content.Replace($oldImport2, $newImport2)
}

# --- 2. Ajout du champ + modification du constructeur ---
$oldCtor = @'
    private final ActifService actifService;

    private final ActifRepository actifRepository;

    private final ActifQueryService actifQueryService;

    public ActifResource(ActifService actifService, ActifRepository actifRepository, ActifQueryService actifQueryService) {
        this.actifService = actifService;
        this.actifRepository = actifRepository;
        this.actifQueryService = actifQueryService;
    }
'@

$newCtor = @'
    private final ActifService actifService;

    private final ActifRepository actifRepository;

    private final ActifQueryService actifQueryService;

    private final AffectationRepository affectationRepository;

    public ActifResource(
        ActifService actifService,
        ActifRepository actifRepository,
        ActifQueryService actifQueryService,
        AffectationRepository affectationRepository
    ) {
        this.actifService = actifService;
        this.actifRepository = actifRepository;
        this.actifQueryService = actifQueryService;
        this.affectationRepository = affectationRepository;
    }
'@

if ($content -match [regex]::Escape($oldCtor)) {
    $content = $content.Replace($oldCtor, $newCtor)
} else {
    Write-Host "ATTENTION : constructeur non trouve tel qu'attendu - modification manuelle necessaire pour le constructeur." -ForegroundColor Yellow
}

# --- 3. Ajout du filtrage dans getAllActifs ---
$oldMethod = @'
        LOG.debug("REST request to get Actifs by criteria: {}", criteria);

        Page<ActifDTO> page = actifQueryService.findByCriteria(criteria, pageable);
'@

$newMethod = @'
        LOG.debug("REST request to get Actifs by criteria: {}", criteria);

        boolean isAgentOnly =
            SecurityUtils.hasCurrentUserThisAuthority("ROLE_AGENT") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_TECHNICIEN") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_RESPONSABLE");

        if (isAgentOnly) {
            java.util.List<Long> actifIds = affectationRepository
                .findByUtilisateur_LoginAndDateRestitutionIsNull(SecurityUtils.getCurrentUserLogin().orElse(""))
                .stream()
                .map(affectation -> affectation.getActif().getId())
                .distinct()
                .toList();
            LongFilter idFilter = new LongFilter();
            idFilter.setIn(actifIds);
            criteria.setId(idFilter);
        }

        Page<ActifDTO> page = actifQueryService.findByCriteria(criteria, pageable);
'@

if ($content -match [regex]::Escape($oldMethod)) {
    $content = $content.Replace($oldMethod, $newMethod)
} else {
    Write-Host "ATTENTION : methode getAllActifs non trouvee telle qu'attendue - modification manuelle necessaire pour le filtrage." -ForegroundColor Yellow
}

Set-Content -Path $path -Value $content -Encoding UTF8 -NoNewline
Write-Host "OK : filtrage Agent ajoute dans $path (verifie les eventuels avertissements ci-dessus)" -ForegroundColor Green
