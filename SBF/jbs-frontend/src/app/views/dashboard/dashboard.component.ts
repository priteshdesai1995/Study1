import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { DashboardService } from '../../_services/dashboard.service';
import { LoaderService } from '../../_services/loader.service';
import { LanguageService } from '../../_services/language.service';
import { TranslateService } from '@ngx-translate/core';
import { CONFIGCONSTANTS } from '../../config/app-constants';
import * as moment from 'moment';

@Component({
  templateUrl: 'dashboard.component.html',
})
export class DashboardComponent implements OnInit, OnDestroy {
  installCount = 0;
  uninstallCount = 0;
  todayInstallCount = 0;
  todayUninstallCount = 0;
  count_date = [new Date(new Date().setDate(new Date().getDate() - 30)), new Date()];
  readonly dateRangeConfig = CONFIGCONSTANTS.dateRangeConfig;
  private subscription: Subscription;
  private Language = localStorage.getItem('lan') || 'en';

  public constructor(
    private dashboardService: DashboardService,
    private toastr: ToastrService,
    private loader: LoaderService,
    private languageSwitcher: LanguageService,
    private translate: TranslateService
  ) {
    this.subscription = this.languageSwitcher.getLanguage().subscribe((lan) => {
      this.translate.setDefaultLang(lan);
      this.translate.use(lan);
    });
    this.languageSwitcher.changeLanguage(this.Language);
  }
  ngOnInit(): void {
    this.refreshData();
  }
  getInstallUninstallCount(type) {
    const data = {
      event_type: type,
      from_date: this.count_date ? moment(this.count_date[0]).format('YYYY-MM-DD') : '',
      to_date: this.count_date ? moment(this.count_date[1]).format('YYYY-MM-DD') : '',
    };
    // this.dashboardService.getAllTotalCount(data).subscribe(
    //   (res: any) => {
    //     const resData = res.data[0];
    //     if (type === 'install') {
    //       this.installCount = resData ? resData.totalcount : 0;
    //     } else if (type === 'uninstall') {
    //       this.uninstallCount = resData ? resData.totalcount : 0;
    //     }
    //   },
    //   (error) => {
    //     const statusError = error;
    //     if (statusError && statusError.meta) {
    //       this.toastr.error(statusError.meta.message);
    //     } else {
    //       this.toastr.error('Something went wrong please try again.');
    //     }
    //   }
    // );
  }
  getTodayCount(type) {
    const data = {
      event_type: type,
      from_date: moment().format('YYYY-MM-DD'),
      to_date: moment().format('YYYY-MM-DD'),
    };
    // this.dashboardService.getAllTotalCount(data).subscribe(
    //   (res: any) => {
    //     const resData = res.data[0];
    //     if (type === 'install') {
    //       this.todayInstallCount = resData ? resData.totalcount : 0;
    //     } else if (type === 'uninstall') {
    //       this.todayUninstallCount = resData ? resData.totalcount : 0;
    //     }
    //   },
    //   (error) => {
    //     const statusError = error;
    //     if (statusError && statusError.meta) {
    //       this.toastr.error(statusError.meta.message);
    //     } else {
    //       this.toastr.error('Something went wrong please try again.');
    //     }
    //   }
    // );
  }
  refreshData() {
    this.getTodayCount('install');
    this.getTodayCount('uninstall');
    // this.getInstallUninstallCount('install');
    // this.getInstallUninstallCount('uninstall');
  }
  searchApplyCount() {
    // this.getInstallUninstallCount('install');
    // this.getInstallUninstallCount('uninstall');
  }
  resetSearchCount() {
    this.count_date = [new Date(new Date().setDate(new Date().getDate() - 30)), new Date()];
    // this.getInstallUninstallCount('install');
    // this.getInstallUninstallCount('uninstall');
  }
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
