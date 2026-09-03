# Correctif : ajoute le filtrage "mes actifs" pour l'Agent dans ActifResource.java
# (version corrigee, basee sur le contenu reel actuel du fichier)
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

# --- 1. Constructeur : ajout du champ affectationRepository ---
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

if ($content.Contains($oldCtor)) {
    $content = $content.Replace($oldCtor, $newCtor)
    Write-Host "Constructeur mis a jour." -ForegroundColor Green
} else {
    Write-Host "ATTENTION : constructeur toujours introuvable tel quel." -ForegroundColor Red
}

# --- 2. Filtrage dans getAllActifs ---
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
'@

if ($content.Contains($oldMethod)) {
    $content = $content.Replace($oldMethod, $newMethod)
    Write-Host "Filtrage getAllActifs ajoute." -ForegroundColor Green
} else {
    Write-Host "ATTENTION : methode getAllActifs toujours introuvable telle quelle." -ForegroundColor Red
}

Set-Content -Path $path -Value $content -Encoding UTF8 -NoNewline
Write-Host "Termine. Verifie le resultat et compile." -ForegroundColor Cyan
