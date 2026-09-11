import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IActif } from '../actif.model';

@Component({
  selector: 'jhi-actif-detail',
  templateUrl: './actif-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class ActifDetail {
  readonly actif = input<IActif | null>(null);

  imprimerEtiquette(): void {
    const a = this.actif();
    if (!a) return;

    const printWin = window.open('', '_blank', 'width=500,height=400');
    if (!printWin) {
      alert("Veuillez autoriser les fenêtres popups pour imprimer l'étiquette.");
      return;
    }

    const code = a.codeBarre || a.codeInventaire || `ACT-${a.id}`;
    const designation = a.designation || 'Équipement informatique';
    const numSerie = a.numeroSerie ? `S/N: ${a.numeroSerie}` : '';

    printWin.document.write(`
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <title>Étiquette Inventaire - ${code}</title>
  <style>
    @page {
      size: 80mm 45mm;
      margin: 2mm;
    }
    body {
      font-family: Arial, Helvetica, sans-serif;
      margin: 0;
      padding: 6px;
      width: 76mm;
      height: 40mm;
      box-sizing: border-box;
      border: 2px solid #0f172a;
      border-radius: 4px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      text-align: center;
    }
    .tag-header {
      border-bottom: 1px solid #0f172a;
      padding-bottom: 2px;
    }
    .tag-dgi {
      font-size: 8pt;
      font-weight: 800;
      letter-spacing: 0.05em;
      color: #0f172a;
      margin: 0;
    }
    .tag-sub {
      font-size: 6pt;
      color: #475569;
      margin: 0;
      text-transform: uppercase;
    }
    .tag-body {
      padding: 2px 0;
    }
    .tag-desig {
      font-size: 8pt;
      font-weight: 700;
      color: #1e293b;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    .tag-meta {
      font-size: 6.5pt;
      color: #64748b;
    }
    .tag-barcode {
      display: flex;
      flex-direction: column;
      align-items: center;
      margin-top: 2px;
    }
    .barcode-svg {
      height: 16mm;
      width: 65mm;
    }
    .barcode-text {
      font-family: monospace;
      font-size: 8pt;
      font-weight: 800;
      letter-spacing: 0.15em;
    }
    @media print {
      body {
        border: 2px solid #000;
      }
    }
  </style>
</head>
<body>
  <div class="tag-header">
    <div class="tag-dgi">DIRECTION GÉNÉRALE DES IMPÔTS</div>
    <div class="tag-sub">Propriété DGI — Ne pas détacher</div>
  </div>
  <div class="tag-body">
    <div class="tag-desig">${designation}</div>
    <div class="tag-meta">${a.marque || ''} ${a.modele || ''} ${numSerie ? ' | ' + numSerie : ''}</div>
  </div>
  <div class="tag-barcode">
    <svg class="barcode-svg" viewBox="0 0 200 40">
      <rect x="0" y="0" width="200" height="40" fill="#fff" />
      <g fill="#000">
        <rect x="5" y="0" width="3" height="35" /><rect x="10" y="0" width="2" height="35" /><rect x="15" y="0" width="4" height="35" />
        <rect x="22" y="0" width="2" height="35" /><rect x="27" y="0" width="5" height="35" /><rect x="35" y="0" width="2" height="35" />
        <rect x="40" y="0" width="4" height="35" /><rect x="47" y="0" width="3" height="35" /><rect x="53" y="0" width="2" height="35" />
        <rect x="58" y="0" width="5" height="35" /><rect x="66" y="0" width="2" height="35" /><rect x="71" y="0" width="3" height="35" />
        <rect x="77" y="0" width="4" height="35" /><rect x="84" y="0" width="2" height="35" /><rect x="89" y="0" width="5" height="35" />
        <rect x="97" y="0" width="3" height="35" /><rect x="103" y="0" width="2" height="35" /><rect x="108" y="0" width="4" height="35" />
        <rect x="115" y="0" width="3" height="35" /><rect x="121" y="0" width="5" height="35" /><rect x="129" y="0" width="2" height="35" />
        <rect x="134" y="0" width="4" height="35" /><rect x="141" y="0" width="2" height="35" /><rect x="146" y="0" width="5" height="35" />
        <rect x="154" y="0" width="3" height="35" /><rect x="160" y="0" width="2" height="35" /><rect x="165" y="0" width="4" height="35" />
        <rect x="172" y="0" width="5" height="35" /><rect x="180" y="0" width="2" height="35" /><rect x="185" y="0" width="4" height="35" /><rect x="192" y="0" width="3" height="35" />
      </g>
    </svg>
    <div class="barcode-text">* ${code} *</div>
  </div>
  <script>
    window.onload = function() {
      setTimeout(function() { window.print(); }, 400);
    };
  </script>
</body>
</html>
    `);
    printWin.document.close();
  }

  previousState(): void {
    globalThis.history.back();
  }
}
