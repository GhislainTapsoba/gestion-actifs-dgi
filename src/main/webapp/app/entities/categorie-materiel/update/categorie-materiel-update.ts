import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICategorieMateriel } from '../categorie-materiel.model';
import { CategorieMaterielService } from '../service/categorie-materiel.service';

import { CategorieMaterielFormGroup, CategorieMaterielFormService } from './categorie-materiel-form.service';

@Component({
  selector: 'jhi-categorie-materiel-update',
  templateUrl: './categorie-materiel-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CategorieMaterielUpdate implements OnInit {
  readonly isSaving = signal(false);
  categorieMateriel: ICategorieMateriel | null = null;

  protected categorieMaterielService = inject(CategorieMaterielService);
  protected categorieMaterielFormService = inject(CategorieMaterielFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CategorieMaterielFormGroup = this.categorieMaterielFormService.createCategorieMaterielFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categorieMateriel }) => {
      this.categorieMateriel = categorieMateriel;
      if (categorieMateriel) {
        this.updateForm(categorieMateriel);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const categorieMateriel = this.categorieMaterielFormService.getCategorieMateriel(this.editForm);
    if (categorieMateriel.id === null) {
      this.subscribeToSaveResponse(this.categorieMaterielService.create(categorieMateriel));
    } else {
      this.subscribeToSaveResponse(this.categorieMaterielService.update(categorieMateriel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICategorieMateriel | null>): void {
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

  protected updateForm(categorieMateriel: ICategorieMateriel): void {
    this.categorieMateriel = categorieMateriel;
    this.categorieMaterielFormService.resetForm(this.editForm, categorieMateriel);
  }
}
