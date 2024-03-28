import { Component, OnInit, ViewChild } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import { ColumnMode, DatatableComponent } from '@swimlane/ngx-datatable';
import { SurveyService } from '../../../_services/survey.service';
import { ToastrService } from 'ngx-toastr';
import { LoaderService } from '../../../_services/loader.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-user-survey-list',
  templateUrl: './user-survey-list.component.html',
  styleUrls: ['./user-survey-list.component.scss'],
})
export class UserSurveyListComponent implements OnInit {
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
  sortParam = '';
  sortOrder = '';
  // Get datatble configuration -- end

  title = '';
  survey_status = '';
  momentDateFormat: string;
  constructor(
    private surveyService: SurveyService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private loader: LoaderService
  ) {}

  ngOnInit() {
    this.getActiveSurveyList();
    this.momentDateFormat = CONFIGCONSTANTS.momentDateFormat || 'MM/DD/YYYY';
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
  private getActiveSurveyList() {
    this.loadingIndicator = true;
    this.surveyService
      .getActiveSurveyList({
        title: this.title,
        has_submitted: this.survey_status,
        start: this.pageNumber * this.size,
        length: this.size,
        sort_param: this.sortParam,
        sort_type: this.sortOrder,
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          const respData = resp.data['original'];
          this.surveyList = respData.data;
          this.totalReords = respData.recordsTotal;
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
    this.title = '';
    this.survey_status = '';
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
    this.getActiveSurveyList();
  }
  public getRowClass(row) {
    return {
      'survey-bg-green': row.has_submitted === 'yes',
      'survey-bg-red': row.has_submitted === 'no',
    };
  }
}
