import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { LanguageService } from '../../_services/language.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-manage-survey',
  templateUrl: './manage-survey.component.html',
  styleUrls: ['./manage-survey.component.scss'],
})
export class ManageSurveyComponent implements OnInit, OnDestroy {
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
