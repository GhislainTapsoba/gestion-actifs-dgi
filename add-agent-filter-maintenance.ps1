# Ajoute le filtrage "mes maintenances" (actifs qui me sont affectes) pour l'Agent
$path = "src\main\java\com\dgi\gestionactifs\web\rest\MaintenanceResource.java"

if (-not (Test-Path $path)) {
    Write-Host "ERREUR : fichier introuvable a $path" -ForegroundColor Red
    exit 1
}

$rawContent = Get-Content $path -Raw
$content = $rawContent -replace "`r`n", "`n"

if ($content -match 'isAgentOnly') {
    Write-Host "Deja present, rien a faire." -ForegroundColor Yellow
    exit 0
}

# --- 1. Imports ---
$oldImports = (@'
import com.dgi.gestionactifs.repository.MaintenanceRepository;
import com.dgi.gestionactifs.service.MaintenanceQueryService;
'@) -replace "`r`n", "`n"

$newImports = (@'
import com.dgi.gestionactifs.repository.AffectationRepository;
import com.dgi.gestionactifs.repository.MaintenanceRepository;
import com.dgi.gestionactifs.security.SecurityUtils;
import com.dgi.gestionactifs.service.MaintenanceQueryService;
'@) -replace "`r`n", "`n"

if ($content.Contains($oldImports)) {
    $content = $content.Replace($oldImports, $newImports)
} else {
    Write-Host "ATTENTION : bloc d'imports introuvable." -ForegroundColor Red
}

if ($content -notmatch 'import tech\.jhipster\.service\.filter\.LongFilter;') {
    $oldImp2 = "import org.springframework.web.bind.annotation.*;"
    $newImp2 = "import org.springframework.web.bind.annotation.*;`nimport tech.jhipster.service.filter.LongFilter;"
    $content = $content.Replace($oldImp2, $newImp2)
}

# --- 2. Constructeur ---
$oldCtor = (@'
    private final MaintenanceService maintenanceService;

    private final MaintenanceRepository maintenanceRepository;

    private final MaintenanceQueryService maintenanceQueryService;

    public MaintenanceResource(
        MaintenanceService maintenanceService,
        MaintenanceRepository maintenanceRepository,
        MaintenanceQueryService maintenanceQueryService
    ) {
        this.maintenanceService = maintenanceService;
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceQueryService = maintenanceQueryService;
    }
'@) -replace "`r`n", "`n"

$newCtor = (@'
    private final MaintenanceService maintenanceService;

    private final MaintenanceRepository maintenanceRepository;

    private final MaintenanceQueryService maintenanceQueryService;

    private final AffectationRepository affectationRepository;

    public MaintenanceResource(
        MaintenanceService maintenanceService,
        MaintenanceRepository maintenanceRepository,
        MaintenanceQueryService maintenanceQueryService,
        AffectationRepository affectationRepository
    ) {
        this.maintenanceService = maintenanceService;
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceQueryService = maintenanceQueryService;
        this.affectationRepository = affectationRepository;
    }
'@) -replace "`r`n", "`n"

if ($content.Contains($oldCtor)) {
    $content = $content.Replace($oldCtor, $newCtor)
    Write-Host "Constructeur mis a jour." -ForegroundColor Green
} else {
    Write-Host "ATTENTION : constructeur introuvable." -ForegroundColor Red
}

# --- 3. Filtrage dans getAllMaintenances ---
$oldMethod = (@'
        LOG.debug("REST request to get Maintenances by criteria: {}", criteria);

        Page<MaintenanceDTO> page = maintenanceQueryService.findByCriteria(criteria, pageable);
'@) -replace "`r`n", "`n"

$newMethod = (@'
        LOG.debug("REST request to get Maintenances by criteria: {}", criteria);

        boolean isAgentOnly =
            SecurityUtils.hasCurrentUserThisAuthority("ROLE_AGENT") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_TECHNICIEN") &&
            !SecurityUtils.hasCurrentUserThisAuthority("ROLE_RESPONSABLE");

        if (isAgentOnly) {
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

        Page<MaintenanceDTO> page = maintenanceQueryService.findByCriteria(criteria, pageable);
'@) -replace "`r`n", "`n"

if ($content.Contains($oldMethod)) {
    $content = $content.Replace($oldMethod, $newMethod)
    Write-Host "Filtrage getAllMaintenances ajoute." -ForegroundColor Green
} else {
    Write-Host "ATTENTION : methode getAllMaintenances introuvable." -ForegroundColor Red
}

$finalContent = $content -replace "`n", "`r`n"
Set-Content -Path $path -Value $finalContent -Encoding UTF8 -NoNewline
Write-Host "Termine. Verifie et compile." -ForegroundColor Cyan
