import { Component, OnInit, OnDestroy } from '@angular/core';
import { LanguageService } from '../../_services/language.service';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-manage-banner',
  templateUrl: './manage-banner.component.html',
  styleUrls: ['./manage-banner.component.scss'],
})
export class ManageBannerComponent implements OnInit, OnDestroy {
  private subscription: Subscription;
  private Language = localStorage.getItem('lan') || 'en';
  constructor(private languageSwitcher: LanguageService, private translate: TranslateService) {
    this.subscription = this.languageSwitcher.getLanguage().subscribe((lan) => {
      this.translate.setDefaultLang(lan);
      this.translate.use(lan);
    });
    this.languageSwitcher.changeLanguage(this.Language);
  }

  ngOnInit() {}

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
