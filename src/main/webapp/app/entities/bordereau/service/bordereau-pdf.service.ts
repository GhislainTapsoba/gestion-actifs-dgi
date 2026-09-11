import { Injectable } from '@angular/core';
import { IBordereau } from '../bordereau.model';

@Injectable({ providedIn: 'root' })
export class BordereauPdfService {
  imprimerBordereau(bordereau: IBordereau): void {
    const titre =
      bordereau.typeBordereau === 'TRANSFERT' ? "BORDEREAU OFFICIEL DE TRANSFERT D'ACTIFS" : "BORDEREAU OFFICIEL D'AFFECTATION D'ACTIFS";

    const ref = bordereau.numero || `BDG-${String(bordereau.id).padStart(5, '0')}`;
    const dateEmission = bordereau.dateEmission ? bordereau.dateEmission.format('DD/MM/YYYY') : new Date().toLocaleDateString('fr-FR');
    const dateValidation = bordereau.dateValidation ? bordereau.dateValidation.format('DD/MM/YYYY') : '—';
    const statut = bordereau.statutValidation || 'VALIDE';
    const emetteurId = bordereau.emetteur?.id ? `#${bordereau.emetteur.id}` : 'Direction Informatique';
    const operationLiee = bordereau.transfert?.id
      ? `Transfert #${bordereau.transfert.id}`
      : bordereau.affectation?.id
        ? `Affectation #${bordereau.affectation.id}`
        : 'Attribution directe';

    const printWindow = window.open('', '_blank', 'width=850,height=1000');
    if (!printWindow) {
      alert('Veuillez autoriser les fenêtres contextuelles (popups) pour imprimer le bordereau.');
      return;
    }

    printWindow.document.write(`
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <title>${titre} - ${ref}</title>
  <style>
    @page {
      size: A4;
      margin: 15mm 20mm;
    }
    body {
      font-family: 'Segoe UI', Arial, sans-serif;
      color: #1e293b;
      margin: 0;
      padding: 20px;
      font-size: 13px;
      line-height: 1.5;
    }
    .header {
      display: flex;
      justify-content: space-between;
      border-bottom: 2px solid #0f172a;
      padding-bottom: 12px;
      margin-bottom: 20px;
    }
    .header-left {
      text-align: left;
    }
    .header-left h3 {
      margin: 0;
      font-size: 14px;
      font-weight: 800;
      letter-spacing: 0.05em;
      color: #0f172a;
    }
    .header-left p {
      margin: 2px 0;
      font-size: 11px;
      color: #475569;
    }
    .header-right {
      text-align: right;
    }
    .header-right .republic {
      font-weight: bold;
      font-size: 12px;
    }
    .header-right .motto {
      font-style: italic;
      font-size: 10px;
      color: #64748b;
    }
    .doc-title {
      text-align: center;
      margin: 25px 0 20px;
      background: #f1f5f9;
      border: 1px solid #cbd5e1;
      padding: 12px;
      border-radius: 6px;
    }
    .doc-title h1 {
      margin: 0;
      font-size: 17px;
      font-weight: 800;
      color: #1e3a8a;
      letter-spacing: 0.04em;
    }
    .doc-title .doc-ref {
      margin-top: 4px;
      font-size: 13px;
      font-weight: 600;
      color: #334155;
    }
    .meta-table {
      width: 100%;
      border-collapse: collapse;
      margin-bottom: 24px;
    }
    .meta-table th, .meta-table td {
      border: 1px solid #cbd5e1;
      padding: 8px 12px;
      font-size: 12px;
    }
    .meta-table th {
      background-color: #f8fafc;
      width: 25%;
      color: #334155;
      text-align: left;
    }
    .meta-table td {
      width: 25%;
      font-weight: 600;
    }
    .badge {
      display: inline-block;
      padding: 3px 8px;
      border-radius: 4px;
      font-size: 11px;
      font-weight: bold;
      background: #dcfce7;
      color: #166534;
      border: 1px solid #bbf7d0;
    }
    .badge--warning {
      background: #fef9c3;
      color: #854d0e;
      border-color: #fef08a;
    }
    .attestation {
      margin: 25px 0;
      padding: 14px;
      background: #fafafa;
      border-left: 4px solid #2563eb;
      font-size: 12px;
      font-style: italic;
      color: #334155;
    }
    .signatures {
      margin-top: 40px;
      display: flex;
      justify-content: space-between;
      gap: 15px;
    }
    .sig-box {
      flex: 1;
      border: 1px solid #94a3b8;
      border-radius: 6px;
      padding: 10px;
      min-height: 120px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }
    .sig-box-title {
      font-size: 11px;
      font-weight: 700;
      text-align: center;
      text-transform: uppercase;
      color: #0f172a;
      border-bottom: 1px dashed #cbd5e1;
      padding-bottom: 6px;
    }
    .sig-space {
      height: 65px;
    }
    .sig-date {
      font-size: 10px;
      color: #64748b;
      text-align: center;
    }
    .footer {
      margin-top: 40px;
      border-top: 1px solid #e2e8f0;
      padding-top: 8px;
      font-size: 9px;
      color: #94a3b8;
      text-align: center;
    }
    .no-print {
      margin-bottom: 15px;
      text-align: right;
    }
    .btn-print {
      background: #2563eb;
      color: white;
      border: none;
      padding: 8px 16px;
      border-radius: 5px;
      font-size: 13px;
      font-weight: 600;
      cursor: pointer;
    }
    @media print {
      .no-print {
        display: none;
      }
      body {
        padding: 0;
      }
    }
  </style>
</head>
<body>
  <div class="no-print">
    <button class="btn-print" onclick="window.print()">Imprimer le document</button>
  </div>

  <div class="header">
    <div class="header-left">
      <h3>DIRECTION GÉNÉRALE DES IMPÔTS</h3>
      <p>Direction des Systèmes d'Information</p>
      <p>Service de Gestion du Patrimoine et des Actifs</p>
    </div>
    <div class="header-right">
      <div class="republic">BURKINA FASO</div>
      <div class="motto">Unité - Progrès - Justice</div>
    </div>
  </div>

  <div class="doc-title">
    <h1>${titre}</h1>
    <div class="doc-ref">Référence officielle : N° ${ref}</div>
  </div>

  <table class="meta-table">
    <tr>
      <th>Numéro de Bordereau</th>
      <td>${ref}</td>
      <th>Type d'opération</th>
      <td>${bordereau.typeBordereau || 'AFFECTATION'}</td>
    </tr>
    <tr>
      <th>Date d'émission</th>
      <td>${dateEmission}</td>
      <th>Statut validation</th>
      <td>
        <span class="badge ${statut === 'EN_ATTENTE' ? 'badge--warning' : ''}">${statut}</span>
      </td>
    </tr>
    <tr>
      <th>Opération rattachée</th>
      <td>${operationLiee}</td>
      <th>Date de validation</th>
      <td>${dateValidation}</td>
    </tr>
    <tr>
      <th>Émetteur / Responsable</th>
      <td>${emetteurId}</td>
      <th>Organisme</th>
      <td>DGI - Siège Central</td>
    </tr>
  </table>

  <div class="attestation">
    Le présent bordereau certifie la prise en charge, la régularité administrative et la conformité physique des mouvements d'actifs informatiques susmentionnés, conformément aux dispositions réglementaires régissant le parc matériel de la Direction Générale des Impôts (DGI).
  </div>

  <div class="signatures">
    <div class="sig-box">
      <div class="sig-box-title">Service Émetteur / Cédant</div>
      <div class="sig-space"></div>
      <div class="sig-date">Date & Signature</div>
    </div>
    <div class="sig-box">
      <div class="sig-box-title">Bénéficiaire / Récepteur</div>
      <div class="sig-space"></div>
      <div class="sig-date">Date & Signature</div>
    </div>
    <div class="sig-box">
      <div class="sig-box-title">Visa Contrôle Patrimoine DGI</div>
      <div class="sig-space"></div>
      <div class="sig-date">Date, Visa et Cachet</div>
    </div>
  </div>

  <div class="footer">
    Application de Gestion des Actifs Informatiques — Direction Générale des Impôts (DGI) — Document officiel généré le ${new Date().toLocaleDateString('fr-FR')} à ${new Date().toLocaleTimeString('fr-FR')}
  </div>

  <script>
    window.onload = function() {
      // Auto déclenchement de la boite de dialogue d'impression après chargement
      setTimeout(function() {
        window.print();
      }, 500);
    };
  </script>
</body>
</html>
    `);
    printWindow.document.close();
  }
}
