$path = "src\main\webapp\app\home\home.scss"
$content = @'
.dashboard {
  max-width: 960px;
  margin: 0 auto;
  padding: 2rem 1rem 3rem;
}

.dashboard-header {
  border-bottom: 1px solid #e2e5ea;
  padding-bottom: 1.25rem;
  margin-bottom: 2rem;

  h1 {
    font-size: 1.75rem;
    font-weight: 700;
    color: #1f2933;
    margin: 0 0 0.35rem;
  }
}

.dashboard-subtitle {
  color: #5b6672;
  font-size: 0.95rem;
  margin: 0;
}

.dashboard-login {
  padding: 3rem 0;
  text-align: center;

  h1 {
    font-size: 1.6rem;
    color: #1f2933;
  }
}

.stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.1rem;
  margin-bottom: 2.5rem;
}

.stat-card {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
  background: #ffffff;
  border: 1px solid #e2e5ea;
  border-left: 4px solid #2c3e50;
  border-radius: 4px;
  padding: 1.1rem 1.25rem;
  text-decoration: none;
  transition: border-color 0.15s ease;

  &:hover {
    border-color: #2c3e50;
    text-decoration: none;
  }

  &--warning {
    border-left-color: #b7791f;

    &:hover {
      border-color: #b7791f;
    }
  }
}

.stat-card__value {
  font-size: 2.1rem;
  font-weight: 700;
  color: #1f2933;
  line-height: 1;
}

.stat-card__label {
  font-size: 0.85rem;
  color: #5b6672;
}

.quick-links {
  h2 {
    font-size: 1.1rem;
    font-weight: 600;
    color: #1f2933;
    margin-bottom: 0.9rem;
  }
}

.quick-links__grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 0.75rem;
}

.quick-link {
  display: block;
  padding: 0.75rem 1rem;
  background: #ffffff;
  border: 1px solid #e2e5ea;
  border-radius: 4px;
  color: #2c3e50;
  font-size: 0.9rem;
  font-weight: 500;
  text-decoration: none;
  transition:
    background 0.15s ease,
    border-color 0.15s ease;

  &:hover {
    background: #f5f6f8;
    border-color: #2c3e50;
    color: #2c3e50;
    text-decoration: none;
  }
}
'@
Set-Content -Path $path -Value $content -Encoding UTF8
Write-Host "OK : home.scss remplace par le style du dashboard" -ForegroundColor Green
