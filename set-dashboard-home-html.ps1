$path = "src\main\webapp\app\home\home.html"
$content = @'
<div class="dashboard">
  @if (account(); as accountRef) {
    <header class="dashboard-header">
      <h1>Gestion des actifs informatiques</h1>
      <p class="dashboard-subtitle">Direction Generale des Impots &mdash; connecte en tant que {{ accountRef.login }}</p>
    </header>

    <section class="stat-grid">
      @for (card of cards(); track card.label) {
        <a class="stat-card" [class.stat-card--warning]="card.accent === 'warning'" [routerLink]="card.routerLink">
          <span class="stat-card__value">{{ card.value ?? '—' }}</span>
          <span class="stat-card__label">{{ card.label }}</span>
        </a>
      }
    </section>

    <section class="quick-links">
      <h2>Acces rapides</h2>
      <div class="quick-links__grid">
        <a routerLink="/actif" class="quick-link">Inventaire des actifs</a>
        <a routerLink="/affectation" class="quick-link">Affectations</a>
        <a routerLink="/transfert" class="quick-link">Transferts</a>
        <a routerLink="/maintenance" class="quick-link">Maintenance</a>
      </div>
    </section>
  } @else {
    <div class="dashboard-login">
      <h1>Gestion des actifs informatiques</h1>
      <p class="dashboard-subtitle">Direction Generale des Impots</p>
      <p>
        <span jhiTranslate="global.messages.info.authenticated.prefix">Si vous voulez vous </span>
        <a class="alert-link" (click)="login()" jhiTranslate="global.messages.info.authenticated.link">connecter</a>
      </p>
    </div>
  }
</div>
'@
Set-Content -Path $path -Value $content -Encoding UTF8
Write-Host "OK : home.html remplace par le nouveau dashboard" -ForegroundColor Green
