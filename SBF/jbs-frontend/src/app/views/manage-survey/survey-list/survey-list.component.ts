import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import { DatatableComponent, ColumnMode } from '@swimlane/ngx-datatable';
import { SurveyService } from '../../../_services/survey.service';
import { ToastrService } from 'ngx-toastr';
import { LoaderService } from '../../../_services/loader.service';
import { first } from 'rxjs/operators';
import { SurveyUserReportComponent } from './user-report.component';
import { FilterStorageService } from '../../../_services/filter-storage.service';

@Component({
  selector: 'app-survey-list',
  templateUrl: './survey-list.component.html',
  styleUrls: ['./survey-list.component.scss'],
})
export class SurveyListComponent implements OnInit {
  modalRef: BsModalRef;
  // Get datatble configuration -- start
  loadingIndicator = false;
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
  @ViewChild(DatatableComponent, { static: false }) datatable: DatatableComponent;
  surveyList = [];
  sortParam = 'start_date';
  sortOrder = 'desc';
  // Get datatble configuration -- end
  filter = {
    title: '',
    status: '',
    survey_status: '',
  };
  momentDateFormat: string;
  private surveyId: number;
  private changeStatusType: string;
  private changedStatus: string;
  constructor(
    private surveyService: SurveyService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private filterService: FilterStorageService,
    private loader: LoaderService
  ) {}

  ngOnInit() {
    this.filter = this.filterService.getState('adSurveyFilter', this.filter);
    this.sortParam = this.filterService.getSingleState('adSurveySortParam', this.sortParam);
    this.sortOrder = this.filterService.getSingleState('adSurveySortOrder', this.sortOrder);
    this.pageNumber = this.filterService.getSingleState('adSurveyPageNo', this.pageNumber);
    this.size = this.filterService.getSingleState('adSurveySize', this.size);
    this.getSurveyList();
    this.momentDateFormat = 'MM/DD/YYYY';
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
   * Get Survey list data
   */
  private getSurveyList() {
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState('adSurveyFilter', this.filter);
    this.filterService.saveSingleState('adSurveySortParam', this.sortParam);
    this.filterService.saveSingleState('adSurveySortOrder', this.sortOrder);
    this.filterService.saveSingleState('adSurveyPageNo', this.pageNumber);
    this.filterService.saveSingleState('adSurveySize', this.size);
    this.loadingIndicator = true;
    this.surveyService
      .getSurveyList({
        title: this.filter.title,
        status: this.filter.status,
        survey_status: this.filter.survey_status,
        startRec: this.pageNumber * this.size,
        endRec: this.size,
        sort_param: this.sortParam,
        sort_type: this.sortOrder,
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.surveyList = resp.responseData;
          this.totalReords = resp.responseData.length;
        },
        (error) => {
          this.loadingIndicator = false;
        }
      );
  }

  /* Open any Modal Popup */
  openModal(template: TemplateRef<any>, id, status) {
    this.modalRef = this.modalService.show(template, { class: 'modal-md' });
    this.surveyId = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
  }
  /**
   * Change user status Active or Inactive
   */
  public changeStatus() {
    this.changedStatus = this.changeStatusType === 'Active' ? 'Inactive' : 'Active';
    this.loader.showLoader();
    this.surveyService
      .changeSurveyStatus(this.changedStatus, this.surveyId)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.toastr.success(data.meta.message);
            this.rerender(false);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const statusError = error;
          if (statusError && statusError.meta) {
            this.toastr.error(statusError.meta.message);
          } else {
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }
  /**
   * Delete survey
   */
  public deleteSurvey() {
    this.loader.showLoader();
    this.surveyService
      .deleteSurvey(this.surveyId)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.toastr.success(data.meta.message);
            this.rerender(true);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const statusError = error;
          if (statusError && statusError.meta) {
            this.toastr.error(statusError.meta.message);
          } else {
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }

  public searchApply() {
    this.rerender(true);
  }

  public resetSearch() {
    this.filter = {
      title: '',
      status: '',
      survey_status: '',
    };
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
    this.getSurveyList();
  }
  public getRowClass(row) {
    return {
      'survey-bg-green': row.is_completed === 'no' && row.is_started === 'yes',
      'survey-bg-red': row.is_completed === 'yes' && row.is_started === 'yes',
      'survey-bg-yellow': row.is_completed === 'no' && row.is_started === 'no',
    };
  }
  /* Open any Modal Popup */
  openModalUserReport(uuid) {
    const initialState = {
      surveyUuid: uuid,
    };
    this.modalRef = this.modalService.show(SurveyUserReportComponent, {
      class: 'modal-lg',
      initialState,
    });
  }
}
