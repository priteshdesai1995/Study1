import { getData, DurationType, getLabels, getValueByKey, processData } from './../../core/_utility/common';
import { ChangeDetectorRef, Component, Inject, OnInit } from '@angular/core';
import { ChartDataSets, ChartOptions, Chart } from 'chart.js';
import { SingleDataSet, Label, monkeyPatchChartJsLegend, monkeyPatchChartJsTooltip, Color, Colors } from 'ng2-charts';
import { ChartType, CONFIGCONSTANTS, browsers } from '../../core/_constant/app-constant';
import { BounceRate, ITileChart, ITopCountries, LastRefreshTime } from '../../core/_model/live';
import { IResponse } from '../../core/_model/response';
import { errorHandler, successHandler } from '../../core/_utility/common';
import { ToasterService } from '../../core/_utility/notify.service';
import { LiveService } from './live.service';
import * as moment from 'moment-timezone';
import DateUtils from '../../core/_utility/date';
import { DefaultLayoutComponent } from '../../containers/default-layout/default-layout.component';
import * as _ from 'lodash';
@Component({
  selector: 'app-live',
  templateUrl: './live.component.html',
  styleUrls: ['./live.component.scss']
})
export class LiveComponent implements OnInit {
  initDone = false;
  lastRefreshTimestamp = moment.utc();
  public userHourlyChartData: any[] = processData(null,DurationType.TWENTY_FOUR_HOURS, '');
  public static graphDate: string = "";
  public duration: DurationType = DurationType.TWENTY_FOUR_HOURS;
  public apdexChartData: any[];
  public updateTimerInterval = null;
  private pad(num) {
    return `${num}`.length == 1 ? '0'+ num : num;
  }
  public get lastRefreshTime() {
    if (!this.initDone) return "00:00";
    const current = moment.utc();
    return `${this.pad(Math.floor(current.diff(this.lastRefreshTimestamp, 'hours') % 24))}:${this.pad(Math.floor(current.diff(this.lastRefreshTimestamp, 'minutes') % 60))}`
  }
  readonly titles = {
    "totalConversions": "Total Conversions",
    "overallConversionRate": "Overall Conversion Rate",
    "activeUsers": "Active Users",
    "totalSessions": "Total Sessions",
    "bounceRate": "Bounce Rate"
  }
  readonly tooltips = {
    "changeOfUsers": "The change in the amount of users on the website today compared to yesterday.",
    "topActiveStates": "The locations of most traffic coming to your website.",
    "activeDevicesUsed":"The distribution of sessions across device types.",
    "bounceRate":"The number of sessions ended without a favorable outcome."
  }

  setData(res) {
    this.apdexChartData = processData(res,this.duration, '')
  }

  getAbsouluteValue(val: number) {
    const value = Math.round(val * 10) / 10;
    return Math.abs(value);
  }
  readonly upAndDownImage = {
    "INCREASE": "/assets/img/up-arrow.svg",
    "DECREASE": "/assets/img/down arrow.svg"
  }
  getIncDecImage(val: number) {
    return val >= 0 ? this.upAndDownImage.DECREASE : val < 0 ? this.upAndDownImage.INCREASE : null;
  }
  getIncDecLabelClass(val: number) {
    return val >= 0 ? 'red-label' : val < 0 ? 'green-label' : null;
  }

  getChartNature(val: number, positiveOnZero = false) {
    if (positiveOnZero === true && val == 0)  this.chartTypes.INCREASE;
    return val >= 0 ? this.chartTypes.DECREASE : this.chartTypes.INCREASE;
  }

  chartData = {
    "totalConversions": {
      "labels": DateUtils.getLables(DurationType.TWENTY_FOUR_HOURS),
      "data": this.processTwentyFourHoursData(this.titles.totalConversions)
    },
    "overallConversionRate": {
      "labels": DateUtils.getLables(DurationType.TWENTY_FOUR_HOURS),
      "data": this.processTwentyFourHoursData(this.titles.overallConversionRate)
    },
    "activeUsers": {
      "labels": DateUtils.getLables(DurationType.TWENTY_FOUR_HOURS),
      "data": this.processTwentyFourHoursData(this.titles.activeUsers)
    },
    "totalSessions": {
      "labels": DateUtils.getLables(DurationType.TWENTY_FOUR_HOURS),
      "data": this.processTwentyFourHoursData(this.titles.totalSessions)
    },
    "bounceRate": {
      "labels": DateUtils.getLables(DurationType.TWENTY_FOUR_HOURS),
      "data": this.processTwentyFourHoursData(this.titles.bounceRate)
    }
  }
  pageLoadData: any = "page24";
  isTopLoading: boolean = false;
  isChartLoading: boolean = false;
  chartTypes = ChartType;
  isPageLoading: boolean = false;
  topCountriesData: ITopCountries;
  getTileChart: ITileChart;
  bounceRateChartData: BounceRate;
  interval: any;

  public lineChartData: ChartDataSets[] = [
  ];
  public lineChartLabels: Label[] = [
    browsers.CHROME,
    browsers.FIREFOX,
    browsers.IE,
    browsers.OPERA,
    browsers.SAFARI
  ];

  processTwentyFourHoursData(data = null, label: String = 'A') {
    return processData(data, DurationType.TWENTY_FOUR_HOURS)
  }
  getTwentyFourHoursLables() {
    return getLabels(DurationType.TWENTY_FOUR_HOURS);
  }
  constructor(@Inject(DefaultLayoutComponent) private layout: DefaultLayoutComponent,
    private liveService: LiveService,
    private cd: ChangeDetectorRef,
    private toaster: ToasterService) {
    this.layout.isWhiteBackground = true;

  }

  ngOnInit(){
    this.initDone = false;
    this.getLastRefreshTime();
    this.getTopCountries();
    this.getChartData();
    this.getBounceRateData();
    this.getTopCountries();
    this.getChartData();
    this.getBounceRateData();
    this.getActiveDevice();
  }

  getLastRefreshTime() {
    this.liveService.getLastRefreshTime().subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          let timeDate = getValueByKey(res, 'responseData', {});
          this.lastRefreshTimestamp = moment.utc(res.responseData);
          console.log(this.lastRefreshTimestamp, "this.lastRefreshTimestamp")
          this.refreshPageAndPostRefreshTime();
          this.initDone = true;
        }
      );
    },
      (error) => {
        console.error("something went wrong while getting last refresh time");
        errorHandler(this.toaster, error.error, () => { });
        this.lastRefreshTimestamp = moment.utc();
        this.refreshPageAndPostRefreshTime();
        this.initDone = true;
      });
  }

  clickRefreshButtonEventHandle(){  
    this.refreshPageAndPostRefreshTime();

    this.getTopCountries();
    this.getChartData();
    this.getBounceRateData();
    this.getTopCountries();
    this.getChartData();
    this.getBounceRateData();

    this.getActiveDevice();
    this.getUserHourly();
    this.getApdexData();

    this.processTwentyFourHoursData();
    this.getTwentyFourHoursLables();
    this.getLastRefreshTime();

  }

  refreshPageAndPostRefreshTime(){
    this.liveService.postPageRefreshTime().subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
        }
      );
    },
    (error) => {
      errorHandler(this.toaster, error.error, () => { });
    });
  }

  getActiveDevice() {
    this.isPageLoading = true;
    this.liveService.getActiveDevice().subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          this.isPageLoading = false;
          this.processBrowserData(res);
        }
      );
    },
      (error) => {
        this.isPageLoading = false;
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }

  processBrowserData(data: any, label: string = '', defaultValue = 0) {
    if (!data) {
      this.lineChartData = [
        { data: [], label: label, borderWidth: 1.5 },
      ]
      return;
    }
    const result = [];
    this.lineChartLabels.forEach(e => {
      const filterObj = data.filter(b => b.browser == e);
      if (_.size(filterObj) > 0 && filterObj[0].count > 0) {
        result.push(filterObj[0].count);
      } else {
        result.push(defaultValue);
      }
    });
    this.lineChartData = [
      { data: result, label: label, borderWidth: 1.5 },
    ]
  }

  getUserHourly() {
    this.liveService.getHourlyDashboard().subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          this.userHourlyChartData = processData(res.data,DurationType.TWENTY_FOUR_HOURS, '')
          LiveComponent.graphDate = res.date;
        }
      );
    },
      (error) => {
        this.isPageLoading = false;
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }

  getApdexData() {
    const param = "?duration=" + this.duration;
    this.liveService.getAPDEX(param).subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          this.setData(res.data);
        }
      );
    },
      (error) => {
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }

  getChartData() {
    this.liveService.getTileStatistics().subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          this.getTileChart = res;
          this.chartData.totalConversions.data = this.processTwentyFourHoursData(getValueByKey(res, 'totalConversions.data', {}), this.titles.totalConversions);
          this.chartData.overallConversionRate.data = this.processTwentyFourHoursData(getValueByKey(res, 'overallConversionRate.data', {}), this.titles.overallConversionRate);
          this.chartData.activeUsers.data = this.processTwentyFourHoursData(getValueByKey(res, 'activeUsers.data', {}), this.titles.activeUsers);
          this.chartData.totalSessions.data = this.processTwentyFourHoursData(getValueByKey(res, 'totalSessions.hourWiseData', {}), this.titles.totalSessions);
        }
      );
    },
      (error) => {
        this.isChartLoading = false;
        this.isPageLoading = false;
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }

  getTopCountries() {
    this.liveService.getTopCountries().subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          this.topCountriesData = res;
        }
      );
    },
      (error) => {
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }

  ngOnDestroy() {
    clearInterval(this.interval);
    this.layout.isWhiteBackground = false;

  }

  getBounceRateData() {
    this.isPageLoading = true;
    this.liveService.getBounceRate().subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          this.isPageLoading = false;
          this.chartData.bounceRate.data = this.processTwentyFourHoursData(getValueByKey(res, 'data', {}), this.titles.bounceRate);
          this.bounceRateChartData = res;
        }
      );
    },
      (error) => {
        this.isPageLoading = false;
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }
  
}
