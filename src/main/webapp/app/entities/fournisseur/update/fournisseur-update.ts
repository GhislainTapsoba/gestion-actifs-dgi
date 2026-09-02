import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFournisseur } from '../fournisseur.model';
import { FournisseurService } from '../service/fournisseur.service';

import { FournisseurFormGroup, FournisseurFormService } from './fournisseur-form.service';

@Component({
  selector: 'jhi-fournisseur-update',
  templateUrl: './fournisseur-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FournisseurUpdate implements OnInit {
  readonly isSaving = signal(false);
  fournisseur: IFournisseur | null = null;

  protected fournisseurService = inject(FournisseurService);
  protected fournisseurFormService = inject(FournisseurFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FournisseurFormGroup = this.fournisseurFormService.createFournisseurFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fournisseur }) => {
      this.fournisseur = fournisseur;
      if (fournisseur) {
        this.updateForm(fournisseur);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const fournisseur = this.fournisseurFormService.getFournisseur(this.editForm);
    if (fournisseur.id === null) {
      this.subscribeToSaveResponse(this.fournisseurService.create(fournisseur));
    } else {
      this.subscribeToSaveResponse(this.fournisseurService.update(fournisseur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFournisseur | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(fournisseur: IFournisseur): void {
    this.fournisseur = fournisseur;
    this.fournisseurFormService.resetForm(this.editForm, fournisseur);
  }
}
