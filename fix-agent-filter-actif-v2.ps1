# Correctif v2 : gere les differences de fin de ligne (CRLF vs LF)
$path = "src\main\java\com\dgi\gestionactifs\web\rest\ActifResource.java"

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

# --- 1. Constructeur ---
$oldCtor = (@'
    private final ActifService actifService;

    private final ActifRepository actifRepository;

    private final ActifQueryService actifQueryService;

    public ActifResource(ActifService actifService, ActifRepository actifRepository, ActifQueryService actifQueryService) {
        this.actifService = actifService;
        this.actifRepository = actifRepository;
        this.actifQueryService = actifQueryService;
    }
'@) -replace "`r`n", "`n"

$newCtor = (@'
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
'@) -replace "`r`n", "`n"

if ($content.Contains($oldCtor)) {
    $content = $content.Replace($oldCtor, $newCtor)
    Write-Host "Constructeur mis a jour." -ForegroundColor Green
} else {
    Write-Host "ATTENTION : constructeur toujours introuvable (meme apres normalisation)." -ForegroundColor Red
}

# --- 2. Filtrage dans getAllActifs ---
$oldMethod = (@'
        LOG.debug("REST request to get Actifs by criteria: {}", criteria);

        Page<ActifDTO> page = actifQueryService.findByCriteria(criteria, pageable);
'@) -replace "`r`n", "`n"

$newMethod = (@'
        LOG.debug("REST request to get Actifs by criteria: {}", criteria);

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
            LongFilter idFilter = new LongFilter();
            idFilter.setIn(actifIds);
            criteria.setId(idFilter);
        }

        Page<ActifDTO> page = actifQueryService.findByCriteria(criteria, pageable);
'@) -replace "`r`n", "`n"

if ($content.Contains($oldMethod)) {
    $content = $content.Replace($oldMethod, $newMethod)
    Write-Host "Filtrage getAllActifs ajoute." -ForegroundColor Green
} else {
    Write-Host "ATTENTION : methode getAllActifs toujours introuvable (meme apres normalisation)." -ForegroundColor Red
}

# --- Reconversion en CRLF (standard Windows) avant sauvegarde ---
$finalContent = $content -replace "`n", "`r`n"
Set-Content -Path $path -Value $finalContent -Encoding UTF8 -NoNewline
Write-Host "Termine. Verifie le resultat et compile." -ForegroundColor Cyan
