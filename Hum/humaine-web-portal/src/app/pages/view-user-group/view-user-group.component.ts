import { APIType } from './../../core/_model/APIType';
import { DataTableConfig } from './../../core/_model/DataTableConfig';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { UserGroupService } from '../create-user-group/user-group.service';
import * as _ from 'lodash';
import { ToasterService } from '../../core/_utility/notify.service';
import { BigFive, CONFIGCONSTANTS, UserGroupModuleType, ChartType, SortDirection, MyUserViewOption } from './../../core/_constant/app-constant';
import { ListTableComponent } from '../../core/_component/list-table/list-table.component';
import { DataTableColumn } from '../../core/_model/DataTableColumn';
import { URLS } from '../../core/_constant/api.config';
import { errorHandler, successHandler } from '../../core/_utility/common';
import { IResponse } from '../../core/_model/response';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { DataService } from '../../services/data-service.service';
import { DefaultLayoutComponent } from '../../containers/default-layout/default-layout.component';
import { ViewUserGroupService } from './view-user-group.service';
import { IviewUserGroupStatistics } from '../../core/_model/user-group';
import { getData, DurationType, getLabels, getValueByKey, processData } from './../../core/_utility/common';
import DateUtils from '../../core/_utility/date';
import { ITileChart } from '../../core/_model/live';

@Component({
    selector: 'app-view-user-group',
    templateUrl: './view-user-group.component.html',
    styleUrls: ['./view-user-group.component.scss']
})

export class ViewUserGroupComponent implements OnInit {
    @ViewChild(ListTableComponent) listDataTable: ListTableComponent;
    sortByList = [...CONFIGCONSTANTS.sortByList, {
        name: "Values", value: BigFive.VALUES
    },
    {
        name: "Persuasive Stratergies", value: BigFive.PERSUASIVE_STRATERGY
    },
    {
        name: "Users", value: BigFive.NOOFUSER
    }];
    isListLoader: boolean = false;

    initDataFormatter(data: any[]) {
        data.forEach(element => {
            element['noOfUser']=element['totalUserCount'];
        });
    }
    isVisible = false;
    checked = false;
    allChecked = false;
    data: any;
    moduleType = UserGroupModuleType;
    indeterminate = false;
    avatarImg = CONFIGCONSTANTS.avatarImgBaseUrl;
    viewList = CONFIGCONSTANTS.viewList;
    ViewUserOption = MyUserViewOption;
    selectedView: string = MyUserViewOption.BUBBLE;
    selectedSort: number = 5;
    sortViewList = CONFIGCONSTANTS.sortByNumber;
    chartTypes = ChartType;
    dotPosition = 'top';
    statisticsData: any;
    userListBubble: any = [];
    ViewGroupstatisticsData:IviewUserGroupStatistics;
    getTileChart: ITileChart;
    localData = [
        "1.0526315789473684",
        "2.3041474654377883",
        "3.6734693877551026",
        "1.153846153846154",
        "4.104477611940299",
        "3.214285714285714",
        "3.4934497816593884",
        "2.1834061135371177",
        "5.521472392638037",
        "2.7131782945736433",
        "4.591836734693878",
        "4.265402843601896",
        "2.5",

    ]
    readonly titles = {
        "averageSpend": "Average Spend",
        "overallConversionRate": "Overall Conversion Rate",
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
    return val >= 0 ? this.upAndDownImage.INCREASE : val < 0 ? this.upAndDownImage.DECREASE : null;
  }
  getIncDecLabelClass(val: number) {
    return val >= 0 ? 'green-label' : val < 0 ? 'red-label' : null;
  }

  getChartNature(val: number) {
    return val >= 0 ? this.chartTypes.INCREASE : this.chartTypes.DECREASE;
  }

  statiticsChartData = {
    "averageSpend": {
      "labels": DateUtils.getLables(DurationType.TWENTY_FOUR_HOURS),
      "data": this.processTwentyFourHoursData(this.titles.averageSpend)
    },
    "overallConversionRate": {
      "labels": DateUtils.getLables(DurationType.TWENTY_FOUR_HOURS),
      "data": this.processTwentyFourHoursData(this.titles.overallConversionRate)
    },
  
  }

  
  processTwentyFourHoursData(data = null, label: String = 'A') {
    return processData(data, DurationType.TWENTY_FOUR_HOURS)
  }
  getTwentyFourHoursLables() {
    return getLabels(DurationType.TWENTY_FOUR_HOURS);
  }
    dataTableConfig: DataTableConfig = new DataTableConfig(URLS.myUserGroupList, APIType.GET, '', 'viewPersona/', 'Users Groups List View', [
        new DataTableColumn("GROUP NAME", "name", false, false, CONFIGCONSTANTS.listTableDefaultValue, null, false, null),
        new DataTableColumn("BIG FIVE", "bigFive", false, true, CONFIGCONSTANTS.listTableDefaultValue, null, false, "bigFive"),
        new DataTableColumn("VALUES", "values", false, true, CONFIGCONSTANTS.listTableDefaultValue, null, false, "values"),

        new DataTableColumn("PERSUASIVE STRATERGY", "persuasiveStratergies", false, true, CONFIGCONSTANTS.listTableDefaultValue, null, false, "persuasiveStratergies"),
        new DataTableColumn("SUCCESS MATCH", "successMatch", false, true, CONFIGCONSTANTS.listTableDefaultValue, '%', false, "successMatch"),
        new DataTableColumn("USERS", "noOfUser", false, true, CONFIGCONSTANTS.listTableDefaultValue, null, false, "noOfUser")

    ], [...this.sortBySortingOptions], "Delete User Group?", "Do you want to delete this user group?", "Yes, Delete This User Group", 'bigFive', SortDirection.DESC, false, false, false, false, false, false, '', "1000px", false);
    constructor(@Inject(DefaultLayoutComponent) private layout: DefaultLayoutComponent,
        private userGroupService: UserGroupService,
        private toaster: ToasterService,
        private viewUserGroupService: ViewUserGroupService,
        private dataService: DataService) {
            this.layout.isWhiteBackground = true;
        const items: any = [
            { Year: "2009", Europe: 31 },
            { Year: "2010", Europe: 43 },
            { Year: "2011", Europe: 66 },
            { Year: "2012", Europe: 69 },
            { Year: "2013", Europe: 58 },
            { Year: "2014", Europe: 40 },
            { Year: "2015", Europe: 78 },
            { Year: "2016", Europe: 13 },
            { Year: "2017", Europe: 78 },
            { Year: "2018", Europe: 40 },
            { Year: "2019", Europe: 80 }
        ];
        this.data = [items];
    }

    ngOnInit() {
        this.UserListData();
        this.getUserGroupStatistics();
    }
    sortingByNumber(e: any) {

    }
    viewChange(e: any) {
        this.layout.isWhiteBackground = true;
        if (e === this.ViewUserOption.LIST) {
            this.layout.isWhiteBackground = false ;
        }

    }

    get sortBySortingOptions() {
        const options = [...this.sortByList];
        const index = options.findIndex(e => e.value == BigFive.GROUP_NAME);
        if (index > -1) {
            options.splice(index, 1)
        }
        return options;
    }

    UserListData() {
        this.isListLoader = true;
        this.getAPIObject().pipe(
            map((data) => {
                return data;
            })
        ).subscribe(
            (res: IResponse<any[]>) => {
                successHandler(this.toaster, res, null, (isSuccess: boolean) => {
                    this.isListLoader = false;
                    if (isSuccess) {
                        this.userListBubble = res.responseData.map((element) => {
                            return {
                                ...element,
                                checked: false
                            };
                        });
                    }
                });
            },
            (error) => {
                this.isListLoader = false;
                errorHandler(this.toaster, error.error, () => {
                });
            }
        );

    }

    getUserGroupStatistics() {
        this.viewUserGroupService.getUserGroupStatistics().subscribe((res: IResponse<any>) => {
            successHandler(
                this.toaster,
                res,
                null,
                (isSuccess: boolean) => {
                    this.getTileChart = res.responseData;
                        this.ViewGroupstatisticsData = res.responseData;
                        this.statiticsChartData.overallConversionRate.data = this.processTwentyFourHoursData(getValueByKey(res.responseData, 'overallConversionRate.data', {}), this.titles.overallConversionRate);

                        this.statiticsChartData.averageSpend.data = this.processTwentyFourHoursData(getValueByKey(res.responseData, 'averageSpend.data', {}), this.titles.averageSpend);

                }
            );
        },
            (error) => {
                errorHandler(this.toaster, error.error, () => { });
            }
        );

    }

    getAPIObject(): Observable<any> {
        return this.dataService.get(URLS.myUserGroupList);

    }
    ngOnDestroy() {
        this.layout.isWhiteBackground = false;
    }
}
