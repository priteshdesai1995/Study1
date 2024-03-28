import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { first } from 'rxjs/operators';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { ToastrService } from 'ngx-toastr';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import { DatatableComponent, ColumnMode } from '@swimlane/ngx-datatable';
import { ActivityTrackingService } from '../../../_services/activity-tracking.service';
import * as moment from 'moment';
import { FilterStorageService } from '../../../_services/filter-storage.service';

@Component({
  selector: 'app-list-activity-tracking',
  templateUrl: './list-activity-tracking.component.html',
  styleUrls: ['./list-activity-tracking.component.scss'],
})
export class ListActivityTrackingComponent implements OnInit {
  modalRef: BsModalRef;
  // Get datatble configuration -- start
  private loadingIndicator = false;
  reorderable = CONFIGCONSTANTS.datatableConfig.reorderable;
  scrollbarH = CONFIGCONSTANTS.datatableConfig.scrollbarH;
  serverSorting = CONFIGCONSTANTS.datatableConfig.serverSorting;
  serverPaging = CONFIGCONSTANTS.datatableConfig.serverPaging;
  piningRight = CONFIGCONSTANTS.datatableConfig.piningRight;
  headerHeight = CONFIGCONSTANTS.datatableConfig.headerHeight;
  footerHeight = CONFIGCONSTANTS.datatableConfig.footerHeight;
  rowHeight = CONFIGCONSTANTS.datatableConfig.rowHeight;
  limitList: any[] = CONFIGCONSTANTS.datatableConfig.limitList;
  ColumnMode = ColumnMode;
  totalReords = CONFIGCONSTANTS.datatableConfig.page.totalReords;
  pageNumber = CONFIGCONSTANTS.datatableConfig.page.pageNumber;
  size = CONFIGCONSTANTS.datatableConfig.page.size;
  dtMessages = CONFIGCONSTANTS.datatableConfig.dtMessages;
  @ViewChild(DatatableComponent, { static: false })
  datatable: DatatableComponent;
  activityList = [];
  sortParam = 'created_at';
  sortOrder = 'desc';
  // Get datatble configuration -- end

  momentDateTime24Format: string;
  momentDateFormat: string;
  filter: any = {
    name: '',
    email: '',
    activity_desc: '',
    select_date: '',
  };
  from_date = '';
  to_date = '';
  activityId: number;
  maxDate: any;

  constructor(
    private modalService: BsModalService,
    private toastr: ToastrService,
    private activityTrackingService: ActivityTrackingService,
    private filterService: FilterStorageService
  ) {
    this.maxDate = new Date();
    this.maxDate.setDate(this.maxDate.getDate());
  }

  ngOnInit() {
    this.filter = this.filterService.getState('activityFilter', this.filter);
    if (this.filter.select_date) {
      this.filter.select_date = [new Date(this.filter.select_date[0]), new Date(this.filter.select_date[1])];
    }
    this.sortParam = this.filterService.getSingleState('activitySortParam', this.sortParam);
    this.sortOrder = this.filterService.getSingleState('activitySortOrder', this.sortOrder);
    this.pageNumber = this.filterService.getSingleState('activityPageNo', this.pageNumber);
    this.size = this.filterService.getSingleState('activitySize', this.size);
    this.getAllActivityTrackingList();
    this.momentDateTime24Format = 'MM/DD/YYYY hh:mm:ss';
    this.momentDateFormat = CONFIGCONSTANTS.momentDateFormat || 'MM/DD/YYYY';
  }

  /**
   * Get status class from configuration
   * @param status Active/Inactive/Pending
   */
  public getStatusClass(status) {
    if (status) {
      return CONFIGCONSTANTS.statusClass[status];
    } else {
      return 'badge-primary';
    }
  }
  /**
   * Sort datatable fields
   * @param event event was triggered, start sort sequence
   */
  public onSort(event) {
    this.sortParam = event.sorts[0].prop;
    this.sortOrder = event.sorts[0].dir;
    this.rerender(false);
  }

  /**
   * Populate the table with new data based on the page number
   * @param page The page to select
   */
  public setPage(pageInfo) {
    this.pageNumber = pageInfo.offset;
    this.rerender(false);
  }
  /**
   * Show number of records in datatable
   * @param value show total entries value 10,25,50,100
   */
  public changeLimit(value) {
    this.size = value;
    this.rerender(true);
  }

  /**
   * API call and refresh datatable value
   * @param goFirstPage set first page when param value true
   */
  private rerender(goFirstPage): void {
    if (goFirstPage) {
      this.pageNumber = 0;
    }
    this.getAllActivityTrackingList();
  }

  private getAllActivityTrackingList() {
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState('activityFilter', this.filter);
    this.filterService.saveSingleState('activitySortParam', this.sortParam);
    this.filterService.saveSingleState('activitySortOrder', this.sortOrder);
    this.filterService.saveSingleState('activityPageNo', this.pageNumber);
    this.filterService.saveSingleState('activitySize', this.size);
    this.loadingIndicator = true;
    this.activityTrackingService
      .getAllActivityTrackingList({
        name: this.filter.name,
        email: this.filter.email,
        activity_desc: this.filter.activity_desc,
        from_date: this.filter.select_date ? moment(this.filter.select_date['0']).format('YYYY-MM-DD hh:mm:ss') : '',
        to_date: this.filter.select_date ? moment(this.filter.select_date['1']).format('YYYY-MM-DD hh:mm:ss') : '',
        endRec: this.size,
        startRec: this.pageNumber * this.size,
        sort_param: this.sortParam,
        sort_type: this.sortOrder,
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.activityList = resp.responseData;
          this.totalReords = resp.responseData.length;
        },
        (error) => {
          this.loadingIndicator = false;
        }
      );
  }

  public searchApply() {
    this.rerender(true);
  }

  public resetSearch() {
    this.filter.name = '';
    this.filter.email = '';
    this.filter.select_date = '';
    this.filter.activity_desc = '';
    this.rerender(true);
  }

  /* Open any Modal Popup */
  openModal(template: TemplateRef<any>, id) {
    this.modalRef = this.modalService.show(template, { class: 'modal-md' });
    this.activityId = id;
  }

  decline(): void {
    this.modalRef.hide();
  }

  /**
   * Delete Activity Log
   */
  public deleteActivityLog() {
    this.activityTrackingService
      .deleteActivity(this.activityId)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.toastr.success(data.meta.message);
            this.rerender(true);
          }
        },
        (error) => {
          const statusError = error;
          if (statusError && statusError.meta) {
            this.toastr.error(statusError.meta.message);
          } else {
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }
}
