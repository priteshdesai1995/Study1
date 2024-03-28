import { AuthenticationService } from './../../../services/authentication.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { NzTableComponent } from 'ng-zorro-antd/table';
import { CONFIGCONSTANTS, RouteURLIdentity, SortDirection } from 'src/app/_constant/app.constant';
import { errorHandler, getColumnValue, isEmpty, successHandler } from 'src/app/utility/common';
import { IResponse } from 'src/app/_model/response';
import { DataTableConfig } from 'src/app/_model/DataTableConfig';
import { ToasterService } from 'src/app/utility/notify.service';
import { DataService } from 'src/app/services/data-service.service';
import * as _ from 'lodash';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { APIType } from '../../_enums/APITYPE';
import { DataTableColumn } from 'src/app/_model/DataTableColumn';

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
  @Input() viewChange = "View Customer";
  @Input() idKey = "accountId";
  @Input() expandViewPersonaPath = "";
  @Input() expandViewPersonaKey = "bigFiveId";
  @Output() onGroupSave = new EventEmitter();
  @Output() onGroupDelete = new EventEmitter();
  @Input() setCount: boolean = false;
  @Input() isInsight: boolean = false;
  @Input() imageBoxWidth = "100px";
  @Input() isURLImage = false;
  @Input() isAllDelete = false;
  readonly size = 8;
  @Input() serverSide = false;
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
  currentPage = 0;
  totalCount = 0;

  // avatarImg = CONFIGCONSTANTS.avatarImgBaseUrl;
  avatarImg = '';
  isListLoader: boolean = false;
  isLoadSubmit: boolean = false;
  listOfCurrentPageData: any[] = [];
  isVisible = false;
  tableId: number;
  expandSet = new Set<number>();
  isCustomers: boolean = true;
  constructor(
    // private userGroupService: UserGroupService,
    private toaster: ToasterService,
    private dataService: DataService,
    private route: ActivatedRoute,
    private router: Router,
    private auth: AuthenticationService
  ) {
    this.getConfigData();
  }


  getConfigData() {
    if (this.route.snapshot.data.identity === RouteURLIdentity.CUSTOMERS) {
      this.isCustomers = true;
    }
  }
  ngOnInit(): void {
    if (this.config.defaultSorting && this.config.defaultSortingDirection) {
      this.selectedSort = this.config.defaultSorting;
      this.selectedSortDirection = this.config.defaultSortingDirection;
    }
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
  sorting(data: any, order: string = SortDirection.ASC) {
    if (this.selectedSort === data) {
      order = this.switchOrder(this.selectedSortDirection);
    }
    const isEmpty = order === null;
    if (order === null) {
      order = this.switchOrder(this.selectedSortDirection);
      if (data !== this.config.defaultSorting) {
        data = this.config.defaultSorting;
        order = this.config.defaultSortingDirection;
      } else {
        order = this.switchOrder(this.selectedSortDirection, true);
      }
    }
    this.selectedSort = data;
    this.selectedSortDirection = order;
    if (!this.serverSide) {
      this.data = _.orderBy(
        [...this.data],
        [(e) => this.getSorting(e[data])],
        [order]
      );
    } else {
      this.listData();
    }
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

  listData() {
    if (this.config.renderFromMockData) {
      return false;
    }
    this.isListLoader = true;
    let dataObj = {};
    let sort = {};
    if (this.serverSide) {
      sort = {
        field: this.selectedSort || this.config.defaultSorting,
        direction: (this.selectedSortDirection || this.config.defaultSortingDirection).toUpperCase()
      };
      dataObj = {
        size: this.pageSize,
        page: this.currentPage,
        sort
      };
    }
    this.getAPIObject(dataObj).pipe(
      map((data) => {
        return data;
      })
    ).subscribe(
      (res: IResponse<any[]>) => {
        successHandler(this.toaster, res, null, (isSuccess: boolean) => {
          this.isListLoader = false;
          if (isSuccess) {
            let data = res.responseData;
            if (this.serverSide) {
              this.totalCount = data['totalElements'];
              data = data['content'];
            }
            this.data = data.map((element) => {
              return {
                ...element,
                checked: false
              };
            });

            if (this.inItDataFormatter) {
              this.inItDataFormatter(this.data);
            }
            this.resetPageCheckBoxStates();
            if (!this.serverSide) {
              this.sorting(this.config.defaultSorting, this.config.defaultSortingDirection);
            }
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


  getAPIObject(data: any = {}): Observable<any> {
    switch (this.config.listAPIType) {
      case APIType.GET:
        return this.dataService.get(this.config.listAPI);
      case APIType.POST:
        return this.dataService.post(this.config.listAPI, data);
      default:
        return this.dataService.get(this.config.listAPI);
    }
  }

  sortBy(data) {
    // console.log(data);
    // this.userGroupService.userGroupList().subscribe(
    //   (res: IResponse<IUserGroupList[]>) => {
    //     successHandler(this.toaster, res, null, (isSuccess: boolean) => {
    //       if (isSuccess) {
    //         this.data = res.responseData.filter(
    //           (e: IUserGroupList) => e.bigFive === data
    //         );
    //       }
    //     });
    //   },
    //   (error) => {
    //     errorHandler(this.toaster, error.error, () => {
    //       this.isLoadSubmit = false;
    //     });
    //   }
    // );
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
    let customerId = null;
    if (this.isCustomers) {
      customerId = this.tableId;
      this.tableId = null;
    }
    if (this.config.renderFromMockData) {
      this.isVisible = false;
      return false;
    }
    if (!this.tableId) {
      deleteAPI = this.config.deleteMultipleAPI;
      if (this.isCustomers && customerId) {
        param = {
          ids: [customerId]
        }
      }
      else {
        param = {
          ids: [...this.setOfCheckedId]
        }
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
                this.listData();
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
    // this.dataService
    //   .post(URLS.aiGeneratedUserGroupSave, { groupIds })
    //   .pipe(
    //     map((data) => {
    //       return data;
    //     })
    //   )
    //   .subscribe(
    //     (res: any) => {
    //       successHandler(
    //         this.toaster,
    //         res,
    //         res.responseData.message,
    //         (isSuccess: boolean) => {
    //           this.isVisible = false;
    //           this.isdeleteLoad = false;
    //           if (isSuccess) {
    //             this.listData();
    //             this.isSavedLoader = false;
    //             this.onGroupSave.emit(null);
    //           }
    //         }
    //       );
    //     },
    //     (error) => {
    //       this.isSavedLoader = false;
    //       this.isdeleteLoad = false;
    //       this.isVisible = false;
    //       errorHandler(this.toaster, error.error, () => { });
    //     }
    //   );

  }
  public get isDataEmpty() {
    return _.size(this.data) === 0;
  }
  public rerender() {
    this.listData();
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

  onPaginationChange(event) {
    this.currentPage = event - 1;
    this.listData();
  }
}
