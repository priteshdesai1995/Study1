import { Component, OnDestroy, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { EmailService } from './../../../_services/email-service';
import { Email } from './../../../model/email';
import { first } from 'rxjs/operators';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import { ColumnMode, DatatableComponent } from '@swimlane/ngx-datatable';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { ToastrService } from 'ngx-toastr';
import { BsModalService } from 'ngx-bootstrap/modal';
import { FilterStorageService } from '../../../_services/filter-storage.service';
import { LoaderService } from '../../../_services/loader.service';

@Component({
  selector: 'app-email-list',
  templateUrl: './email-list.component.html',
  styleUrls: ['./email-list.component.scss'],
})
export class EmailListComponent implements OnInit {
  private modalRef: BsModalRef;
  // Get datatble configuration -- start
  reorderable = CONFIGCONSTANTS.datatableConfig.reorderable;
  scrollbarH = CONFIGCONSTANTS.datatableConfig.scrollbarH;
  piningRight = CONFIGCONSTANTS.datatableConfig.piningRight;
  headerHeight = CONFIGCONSTANTS.datatableConfig.headerHeight;
  footerHeight = CONFIGCONSTANTS.datatableConfig.footerHeight;
  rowHeight = CONFIGCONSTANTS.datatableConfig.rowHeight;
  limitList: any[] = CONFIGCONSTANTS.datatableConfig.limitList;
  ColumnMode = ColumnMode;
  size = CONFIGCONSTANTS.datatableConfig.page.size;
  dtMessages = CONFIGCONSTANTS.datatableConfig.dtMessages;
  @ViewChild(DatatableComponent, { static: false }) datatable: DatatableComponent;
  emailList: any[] = [];
  global_search = '';
  private filteredData = [];
  // Get datatble configuration -- end

  private changeStatusId: number;
  private changeStatusType: string;
  private changedStatus: string;
  loadingIndicator = false;
  sortParam = '';
  sortOrder = '';
  pageNumber = 0;
  constructor(
    private emailService: EmailService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private filterService: FilterStorageService,
    private loader: LoaderService
  ) {}

  ngOnInit() {
    this.global_search = this.filterService.getSingleState('emailFilter', this.global_search);
    this.size = this.filterService.getSingleState('emailSize', this.size);
    this.sortParam = this.filterService.getSingleState('emailSortParam', this.sortParam);
    this.sortOrder = this.filterService.getSingleState('emailSortOrder', this.sortOrder);
    this.pageNumber = this.filterService.getSingleState('emailPageNo', this.pageNumber);
    this.getAllEmailList();
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
    this.filterService.saveSingleState('emailSortParam', this.sortParam);
    this.filterService.saveSingleState('emailSortOrder', this.sortOrder);
  }
  /**
   * Populate the table with new data based on the page number
   * @param page The page to select
   */
  public setPage(pageInfo) {
    this.pageNumber = pageInfo.offset;
    this.filterService.saveSingleState('emailPageNo', this.pageNumber);
  }
  /**
   * Show number of records in datatable
   * @param value show total entries value 10,25,50,100
   */
  public changeLimit(value) {
    this.size = value;
    this.filterService.saveSingleState('emailSize', this.size);
  }
  /**
   * Get Email Template list data
   */
  private getAllEmailList(): void {
    this.loadingIndicator = true;
    this.emailService
      .getAllEmailList()
      .pipe(first())
      .subscribe(
        (data) => {
          this.loadingIndicator = false;
          if (!data.responseData) {
            this.emailList = [];
          } else {
            this.emailList = data.responseData;
            for (var i in this.emailList) {
              this.emailList[i].emailSubject = this.emailList[i].template[0].emailSubject;
            }  
            this.filteredData = data.responseData;
            for (var i in this.filteredData) {
              this.filteredData[i].emailSubject = this.filteredData[i].template[0].emailSubject;
            }
            this.filterDatatable();
          }
        },
        (error) => {
          this.loadingIndicator = false;
          console.log(error);
        }
      );
  }
  /**
   * Datatabe global search
   * @param event get search input value
   */
  public filterDatatable() {
    // const val = event.target.value.toLowerCase();
    const val = this.global_search.toLowerCase();
    // get the key names of each column in the dataset
    const keys = ['emailSubject', 'status', 'templatePurpose', 'templateType'];
    // assign filtered matches to the active datatable
    this.emailList = this.filteredData.filter(function (item) {
      // iterate through each row's column data
      for (let i = 0; i < keys.length; i++) {
        // check for a match
        if ((item[keys[i]] && item[keys[i]].toString().toLowerCase().indexOf(val) !== -1) || !val) {
          // found match, return true to add to result set
          return true;
        }
      }
    });
    // whenever the filter changes, always go back to the first page
    // this.datatable.offset = 0;
    this.filterService.saveSingleState('emailFilter', this.global_search);
  }

  openModal(template: TemplateRef<any>, id, status) {
    this.modalRef = this.modalService.show(template, { class: 'modal-md' });
    this.changeStatusId = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
  }
  private rerender(): void {
    this.getAllEmailList();
  }
  /**
   * Change FAQ status Active or Inactive
   */
  public changeStatus() {
    this.loader.showLoader();
    this.changedStatus = this.changeStatusType === 'Active' ? 'Inactive' : 'Active';
    this.emailService
      .changeEmailStatus(this.changedStatus, this.changeStatusId)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.toastr.success(data.meta.message);
            this.rerender();
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
}
