import { AuthenticationService } from './../../../services/authentication.service';
import { MenuLinks } from './../../_enums/MenuLinks';
import { DataTableColumn } from './../../_model/DataTableColumn';
import { Observable } from 'rxjs';
import { DataService } from './../../../services/data-service.service';
import { DataTableConfig } from './../../_model/DataTableConfig';
import { ToasterService } from './../../_utility/notify.service';
import { IResponse } from './../../_model/response';
import { UserGroupService } from './../../../pages/create-user-group/user-group.service';
import {
  IUserGroupListData,
  IDemographicData,
  IUserGroupList,
} from './../../_model/user-group';
import {
  UserGroupModuleType,
  CONFIGCONSTANTS,
  BigFive,
  SortDirection,
  RouteURLIdentity,
} from './../../_constant/app-constant';
import { Component, Input, OnInit, OnChanges, SimpleChanges, ViewChild, AfterViewInit, Output, EventEmitter, TemplateRef } from '@angular/core';
import * as _ from 'lodash';
import { errorHandler, successHandler, getColumnValue, isEmpty } from '../../_utility/common';
import { map } from 'rxjs/operators';
import { APIType } from '../../_model/APIType';
import { NzTableComponent } from 'ng-zorro-antd/table';
import { URLS } from '../../_constant/api.config';
import { ActivatedRoute, Router } from '@angular/router';
@Component({
  selector: 'list-table',
  templateUrl: './list-table.component.html',
  styleUrls: ['./list-table.component.scss'],
})
export class ListTableComponent implements OnInit {
  @Input() templateRefs: { [key: string]: TemplateRef<any> } = {};
  @Input() config: DataTableConfig;
  @Input() allowExpand = false;
  @Input() isAction = true;
  @Input() isSortBy = true;
  @Input() inItDataFormatter;
  @Input() viewChange = "View Persona";
  @Input() idKey = "groupId";
  @Input() expandViewPersonaPath = "";
  @Input() expandViewPersonaKey = "bigFiveId";
  @Output() onGroupSave = new EventEmitter();
  @Output() onGroupDelete = new EventEmitter();
  @Input() setCount: boolean = false;
  @Input() setCountMenuKey: MenuLinks = null;
  @Input() isInsight: boolean = false;
  @Input() imageBoxWidth = "100px";
  @Input() isURLImage = false;
  @Input() isAllDelete = false;

  @ViewChild(NzTableComponent) table: NzTableComponent;
  isdeleteLoad: boolean = false;
  selectedSort = '';
  selectedSortDirection = '';
  indeterminate = false;
  checked = false;
  setOfCheckedId = new Set<any>();
  @Input() data: any[] = [];
  isSavedLoader: boolean = false;
  public SortDirection = SortDirection;
  isEmpty = isEmpty;
  sortByList = CONFIGCONSTANTS.sortByList;
  @Input() pageSize = 10;

  avatarImg = CONFIGCONSTANTS.avatarImgBaseUrl;
  isListLoader: boolean = false;
  isLoadSubmit: boolean = false;
  listOfCurrentPageData: any[] = [];
  isVisible = false;
  tableId: number;
  expandSet = new Set<number>();
  isNewJourney: boolean = false;
  isAiJourney: boolean = false;
  journeyAnalysis: boolean = false;
  isAiUserGroup: boolean = false;
  constructor(
    private userGroupService: UserGroupService,
    private toaster: ToasterService,
    private dataService: DataService,
    private route: ActivatedRoute,
    private router: Router,
    private auth: AuthenticationService
  ) {
    this.getConfigData();
  }


  getConfigData() {
    this.isAiJourney = false;
    this.isNewJourney = false;
    if (this.route.snapshot.data.identity === RouteURLIdentity.TEST_JOURNEY) {
      this.isNewJourney = true;
    }
    if (this.route.snapshot.data.identity === RouteURLIdentity.AI_GENERATED_JOURNEY) {
      this.isAiJourney = true;
    }
    if (this.route.snapshot.data.identity === RouteURLIdentity.JOURNEY_ANAYSIS) {
      this.journeyAnalysis = true;
    }
    if (this.route.snapshot.data.identity === RouteURLIdentity.AI_GENERATED_USER_GROUP) {
      this.isAiUserGroup = true;
    }
    
  }
  ngOnInit(): void {
    this.sortByList = [];
    this.sortByList = CONFIGCONSTANTS.sortByList;
    this.resetPageCheckBoxStates();
    this.listData();
  }

  resetPageCheckBoxStates() {
    this.setOfCheckedId.clear();
    this.refreshCheckedStatus()
  }

  sortDataTableColumn(data: string, sortable: boolean) {
    if (!sortable) return false;
    this.sorting(data);
  }
  sorting(data: any, order: string = SortDirection.ASC, sortForceFully = false) {
    if (sortForceFully === true) {
      this.data = _.orderBy(
        [...this.data],
        [(e) => this.getSorting(e[data])],
        [order]
      );
      return;  
    }
    if (this.selectedSort === data) {
      order = this.switchOrder(this.selectedSortDirection);
    }
    if (order === null) {
      if (data !== this.config.defaultSorting) {
        data = this.config.defaultSorting;
        order = this.config.defaultSortingDirection;
      } else {
        order = this.switchOrder(this.selectedSortDirection, true);
      }
    }
    this.selectedSort = data;
    this.selectedSortDirection = order;
    this.data = _.orderBy(
      [...this.data],
      [(e) => this.getSorting(e[data])],
      [order]
    );
  }

  switchOrder(order, isDefault = false) {
    switch (order) {
      case null:
        return SortDirection.ASC;
      case SortDirection.ASC:
        return SortDirection.DESC;
      case SortDirection.DESC:
        if (isDefault) return SortDirection.ASC;
        return null;
      default: return SortDirection.ASC;
    }
  }

  getSorting(data) {
    if (typeof data === 'number') { return data; }
    if (this.isArray(data)) {
      data = this.getListHtml(data, '');
    }
    data = getColumnValue(data);
    return (data || '').toString().toLowerCase();
  }

  listData(sortForceFully = false) {
    if (this.config.renderFromMockData) {
      return false;
    }
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
            this.data = res.responseData.map((element) => {
              return {
                ...element,
                checked: false
              };
            });
            if (this.journeyAnalysis) {
              this.data = this.mapGroupNameForJourneyAnalysis(this.data);
            }
            if (this.inItDataFormatter) {
              this.inItDataFormatter(this.data);
            }
            this.resetPageCheckBoxStates();
            this.sorting(this.config.defaultSorting, this.config.defaultSortingDirection, sortForceFully);
            this.setListCount(_.size(this.data));
          }
        });
      },
      (error) => {
        this.isListLoader = false;
        errorHandler(this.toaster, error.error, () => {
          this.isLoadSubmit = false;
        });
      }
    );
  }
  mapGroupNameForJourneyAnalysis(data: any[]): any[] {
    return data.map((element) => {
      return {
        ...element, groupId: 'UG' + element.groupId, groupName: 'UG' + element.groupName
      }
    });
  }

  getAPIObject(): Observable<any> {
    switch (this.config.listAPIType) {
      case APIType.GET:
        return this.dataService.get(this.config.listAPI);
      case APIType.POST:
        return this.dataService.post(this.config.listAPI, {});
      default:
        return this.dataService.get(this.config.listAPI);
    }
  }

  sortBy(data) {
    this.userGroupService.userGroupList().subscribe(
      (res: IResponse<IUserGroupList[]>) => {
        successHandler(this.toaster, res, null, (isSuccess: boolean) => {
          if (isSuccess) {
            this.data = res.responseData.filter(
              (e: IUserGroupList) => e.bigFive === data
            );
          }
        });
      },
      (error) => {
        errorHandler(this.toaster, error.error, () => {
          this.isLoadSubmit = false;
        });
      }
    );
  }

  get scrollValue() {
    if (this.config.isScroll) {
      return { x: '100px', 'y': this.config.scrollHeight };
    }
    return {};
  }

  showModal(id) {
    this.isVisible = true;
    this.tableId = id;
  }


  handleCancel(): void {
    if (this.isListLoader) return;
    this.isVisible = false;
  }

  deleteConfirm() {
    let deleteAPI = this.config.deleteAPI;
    let param = {
      ids: []
    }
    if (this.config.renderFromMockData) {
      this.isVisible = false;
      return false;
    }
    if (!this.tableId) {
      deleteAPI = this.config.deleteMultipleAPI;
      param = {
        ids: [...this.setOfCheckedId]
      }
    }

    this.isdeleteLoad = true;
    this.dataService
      .delete(deleteAPI, this.tableId ? this.tableId : null, param)
      .pipe(
        map((data) => {
          return data;
        })
      )
      .subscribe(
        (res: any) => {
          successHandler(
            this.toaster,
            res,
            res.responseData.message,
            (isSuccess: boolean) => {
              this.isVisible = false;
              this.isdeleteLoad = false;
              if (isSuccess) {
                this.rerender();
                this.onGroupDelete.emit(null);
              }
            }
          );
        },
        (error) => {
          this.isdeleteLoad = false;
          this.isVisible = false;
          errorHandler(this.toaster, error.error, () => { });
        }
      );
  }


  getData(data) {
    if (data === CONFIGCONSTANTS.emptyValue) return '';
    return data;
  }

  updateCheckedSet(id: number, checked: boolean): void {
    if (checked) {
      this.setOfCheckedId.add(id);
    } else {
      this.setOfCheckedId.delete(id);
    }
  }

  onCurrentPageDataChange(listOfCurrentPageData: any[]): void {
    this.listOfCurrentPageData = listOfCurrentPageData;
    this.refreshCheckedStatus();
  }

  refreshCheckedStatus(): void {
    const listOfEnabledData = this.listOfCurrentPageData;
    this.checked = listOfEnabledData.every((e) => this.setOfCheckedId.has(e[this.idKey]));
    if (this.isDataEmpty === true) {
      this.checked = false;
    }
    this.indeterminate = listOfEnabledData.some((e) => this.setOfCheckedId.has(e[this.idKey])) && !this.checked;
  }

  onItemChecked(id: number, checked: boolean): void {
    this.updateCheckedSet(id, checked);
    this.refreshCheckedStatus();
  }

  onAllChecked(checked: boolean): void {
    this.listOfCurrentPageData
      .forEach((e) => {
        this.updateCheckedSet(e[this.idKey], checked)
      });
    this.refreshCheckedStatus();
  }

  get pageSizeValue() {
    if (!this.config.paginate) {
      return _.size(this.data);
    }
    return this.pageSize;
  }

  saveData() {
    if (this.config.renderFromMockData) {
      return false;
    }
    this.isSavedLoader = true;
    let groupIds = Array.from(this.setOfCheckedId);
    this.dataService
      .post(URLS.aiGeneratedUserGroupSave, { groupIds })
      .pipe(
        map((data) => {
          return data;
        })
      )
      .subscribe(
        (res: any) => {
          successHandler(
            this.toaster,
            res,
            res.responseData.message,
            (isSuccess: boolean) => {
              this.isVisible = false;
              this.isdeleteLoad = false;
              if (isSuccess) {
                this.listData();
                this.isSavedLoader = false;
                this.onGroupSave.emit(null);
              }
            }
          );
        },
        (error) => {
          this.isSavedLoader = false;
          this.isdeleteLoad = false;
          this.isVisible = false;
          errorHandler(this.toaster, error.error, () => { });
        }
      );

  }
  public get isDataEmpty() {
    return _.size(this.data) === 0;
  }
  public rerender() {
    this.listData(true);
  }

  onExpandChange(id: number, checked: boolean): void {
    if (checked) {
      this.expandSet.add(id);
    } else {
      this.expandSet.delete(id);
    }
  }
  getKey(column: DataTableColumn) {
    if (this.allowExpand) {
      return column.expandKey;
    }
    return column.key;
  }
  isArray(value: any) {
    return Array.isArray(value);
  }
  getListHtml(data: any[], defaultValue: string) {
    return data.map(e => this.getData(e) || defaultValue).join(", ");
  }


  getViewPersonaPath(data, expandRow = false) {
    if (expandRow) {
      return this.expandViewPersonaPath + data[this.expandViewPersonaKey];
    }
    return this.config.viewPersonaPath + data[this.idKey];
  }

  navigateByUrl(url) {
    this.router.navigateByUrl(url);
  }
  setListCount(size: number) {
    if (!this.setCount || this.setCount && this.isEmpty(this.setCountMenuKey)) return;
    this.auth.setMenuCount(this.setCountMenuKey, size);
  }
}
