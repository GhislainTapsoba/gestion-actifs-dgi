import { HttpHeaders } from '@angular/common/http';
import { Component, computed, effect, inject, signal, untracked } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap/pagination';
import { combineLatest, filter, map, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEMS_PER_PAGE, ITEM_DELETED_EVENT, PAGE_HEADER, SORT, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config';
import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { ItemCount } from 'app/shared/pagination';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { RapportDeleteDialog } from '../delete/rapport-delete-dialog';
import { IRapport } from '../rapport.model';
import { RapportService } from '../service/rapport.service';

@Component({
  selector: 'jhi-rapport',
  templateUrl: './rapport.html',
  imports: [
    RouterLink,
    FontAwesomeModule,
    AlertError,
    Alert,
    SortDirective,
    SortByDirective,
    FormatMediumDatetimePipe,
    NgbPagination,
    ItemCount,
  ],
})
export class Rapport {
  readonly rapports = signal<IRapport[]>([]);
  readonly selectedType = signal<string>('ALL');
  readonly searchTerm = signal<string>('');

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  readonly itemsPerPage = signal(ITEMS_PER_PAGE);
  readonly totalItems = signal(0);
  readonly page = signal(1);

  readonly router = inject(Router);
  protected readonly rapportService = inject(RapportService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.rapportService.rapportsResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly activatedRouteState = toSignal(
    combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      map(([queryParamMap, data]) => ({ queryParamMap, data })),
    ),
    { initialValue: { queryParamMap: this.activatedRoute.snapshot.queryParamMap, data: this.activatedRoute.snapshot.data } },
  );
  protected readonly sortService = inject(SortService);
  protected readonly filterOptions = toSignal(this.filters.filterChanges);
  protected modalService = inject(NgbModal);

  readonly filteredRapports = computed(() => {
    let list = this.rapports();
    const type = this.selectedType();
    const term = this.searchTerm().trim().toLowerCase();

    if (type !== 'ALL') {
      list = list.filter(r => r.typeRapport === type);
    }
    if (term) {
      list = list.filter(
        r =>
          r.titre?.toLowerCase().includes(term) ||
          r.description?.toLowerCase().includes(term) ||
          r.generePar?.toLowerCase().includes(term) ||
          r.formatExport?.toLowerCase().includes(term),
      );
    }
    return list;
  });

  // KPIs
  readonly totalCount = computed(() => this.rapports().length);
  readonly inventaireCount = computed(() => this.rapports().filter(r => r.typeRapport === 'INVENTAIRE').length);
  readonly maintenanceCount = computed(() => this.rapports().filter(r => r.typeRapport === 'MAINTENANCE').length);
  readonly transfertCount = computed(() => this.rapports().filter(r => r.typeRapport === 'TRANSFERT').length);

  constructor() {
    effect(() => {
      const headers = this.rapportService.rapportsResource.headers();
      if (headers) {
        this.fillComponentAttributesFromResponseHeader(headers);
      }
    });
    effect(() => {
      this.rapports.set(this.fillComponentAttributesFromResponseBody([...this.rapportService.rapports()]));
    });
    effect(() => {
      const activatedRouteState = this.activatedRouteState();
      untracked(() => {
        this.fillComponentAttributeFromRoute(activatedRouteState.queryParamMap, activatedRouteState.data);
        this.load();
      });
    });

    effect(() => {
      const filterOptions = this.filterOptions();
      if (filterOptions) {
        untracked(() => {
          this.handleNavigation(1, this.sortState(), filterOptions);
        });
      }
    });
  }

  trackId = (item: IRapport): number => this.rapportService.getRapportIdentifier(item);

  delete(rapport: IRapport): void {
    const modalRef = this.modalService.open(RapportDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.rapport = rapport;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page(), event, this.filters.filterOptions);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState(), this.filters.filterOptions);
  }

  telecharger(rapport: IRapport): void {
    const content = `RAPPORT OFFICIEL - DGI
=========================================
Identifiant: #${rapport.id}
Titre: ${rapport.titre}
Type: ${rapport.typeRapport}
Date de génération: ${rapport.dateGeneration?.format('YYYY-MM-DD HH:mm:ss') ?? 'N/A'}
Généré par: ${rapport.generePar ?? 'Système'}
Format: ${rapport.formatExport ?? 'TEXT'}

Description:
${rapport.description ?? 'Aucune description disponible.'}

Paramètres d'extraction:
${rapport.parametres ?? 'Paramètres par défaut.'}

=========================================
Document généré automatiquement par le Système de Gestion des Actifs de la DGI.
`;
    const extension = rapport.formatExport?.toLowerCase() === 'csv' ? 'csv' : 'txt';
    const mime = rapport.formatExport?.toLowerCase() === 'csv' ? 'text/csv' : 'text/plain';
    const blob = new Blob([content], { type: `${mime};charset=utf-8` });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `rapport_${rapport.id}_${(rapport.titre ?? 'document').toLowerCase().replace(/[^a-z0-9]/g, '_')}.${extension}`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page.set(+(page ?? 1));
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    this.filters.initializeFromParams(params);
  }

  protected fillComponentAttributesFromResponseBody(data: IRapport[]): IRapport[] {
    return data;
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems.set(Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER)));
  }

  protected queryBackend(): void {
    const pageToLoad: number = this.page();
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage(),
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    for (const filterOption of this.filters.filterOptions) {
      queryObject[filterOption.name] = filterOption.values;
    }
    this.rapportService.rapportsParams.set(queryObject);
  }

  protected handleNavigation(page: number, sortState: SortState, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage(),
      sort: this.sortService.buildSortParam(sortState),
    };

    if (filterOptions) {
      for (const filterOption of filterOptions) {
        queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
      }
    }

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
