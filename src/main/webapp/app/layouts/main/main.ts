import { Component, DOCUMENT, OnInit, Renderer2, RendererFactory2, inject, computed } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';

import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import { AccountService } from 'app/core/auth';
import Footer from '../footer/footer';
import PageRibbon from '../profiles/page-ribbon';
import Sidebar from '../sidebar/sidebar';
import { SidebarStateService } from '../sidebar/sidebar-state.service';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.html',
  providers: [AppPageTitleStrategy],
  imports: [RouterOutlet, Footer, PageRibbon, Sidebar],
})
export default class Main implements OnInit {
  private readonly renderer: Renderer2;
  private readonly htmlElement: HTMLElement;

  private readonly router = inject(Router);
  private readonly appPageTitleStrategy = inject(AppPageTitleStrategy);
  private readonly accountService = inject(AccountService);
  private readonly document = inject(DOCUMENT);
  private readonly translateService = inject(TranslateService);
  private readonly rootRenderer = inject(RendererFactory2);
  private readonly sidebarState = inject(SidebarStateService);

  readonly isAuthenticated = computed(() => this.accountService.account() !== null);
  readonly isSidebarCollapsed = this.sidebarState.collapsed;

  constructor() {
    this.htmlElement = this.document.documentElement;
    this.renderer = this.rootRenderer.createRenderer(this.htmlElement, null);
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe();

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.appPageTitleStrategy.updateTitle(this.router.routerState.snapshot);
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(this.htmlElement, 'lang', langChangeEvent.lang);
    });
  }
}
