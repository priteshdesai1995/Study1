import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { FaqService } from './../../../_services/faq-service';
import { Faq } from './../../../model/faq';
import { first } from 'rxjs/operators';
import { BsModalService, ModalDirective } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { ToastrService } from 'ngx-toastr';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import { DatatableComponent, ColumnMode } from '@swimlane/ngx-datatable';
import { LoaderService } from '../../../_services/loader.service';
import { FilterStorageService } from '../../../_services/filter-storage.service';

@Component({
  selector: 'app-faq-list',
  templateUrl: './faq-list.component.html',
  styleUrls: ['./faq-list.component.scss'],
})
export class FaqListComponent implements OnInit {
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
  faqList: Faq[] = [];
  global_search = '';
  private filteredData = [];
  // Get datatble configuration -- end

  private changeStatusId: number;
  private deleteFaqId: number;
  private changeStatusType: string;
  private changedStatus: string;
  momentDateTime24Format: string;
  sortParam = '';
  sortOrder = '';
  pageNumber = 0;
  constructor(
    private faqService: FaqService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private loader: LoaderService,
    private filterService: FilterStorageService
  ) {}

  ngOnInit() {
    this.global_search = this.filterService.getSingleState('faqFilter', this.global_search);
    this.size = this.filterService.getSingleState('faqSize', this.size);
    this.sortParam = this.filterService.getSingleState('faqSortParam', this.sortParam);
    this.sortOrder = this.filterService.getSingleState('faqSortOrder', this.sortOrder);
    this.pageNumber = this.filterService.getSingleState('faqPageNo', this.pageNumber);
    this.getAllFaqList();
    this.momentDateTime24Format = CONFIGCONSTANTS.momentDateTime24Format || 'MM/DD/YYYY hh:mm:ss';
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
    this.filterService.saveSingleState('faqSortParam', this.sortParam);
    this.filterService.saveSingleState('faqSortOrder', this.sortOrder);
  }
  /**
   * Populate the table with new data based on the page number
   * @param page The page to select
   */
  public setPage(pageInfo) {
    this.pageNumber = pageInfo.offset;
    this.filterService.saveSingleState('faqPageNo', this.pageNumber);
  }
  /**
   * Show number of records in datatable
   * @param value show total entries value 10,25,50,100
   */
  public changeLimit(value) {
    this.size = value;
    this.filterService.saveSingleState('faqSize', this.size);
  }
  /**
   * Get FAQ list data
   */
  private getAllFaqList(): void {
    this.faqService
      .getAllFaqList()
      .pipe(first())
      .subscribe(
        (data) => {
          if (!data.responseData) {
            this.faqList = [];
          } else {
            this.faqList = data.responseData;
            this.filteredData = data.responseData;
            this.filterDatatable();
          }
        },
        (error) => {
          console.log(error);
        }
      );
  }

  openModal(template: TemplateRef<any>, id, status) {
    this.modalRef = this.modalService.show(template, { class: 'modal-md' });
    this.changeStatusId = id;
    this.deleteFaqId = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
  }
  /**
   * Change FAQ status Active or Inactive
   */
  public changeStatus() {
    this.changedStatus = this.changeStatusType === 'Active' ? 'Inactive' : 'Active';
    this.faqService
      .changeFaqStatus(this.changedStatus, this.changeStatusId)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.toastr.success(data.meta.message);
            this.rerender();
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
  /**
   * Delete FAQ
   */
  public deleteFAQ() {
    this.faqService
      .deleteFaq(this.deleteFaqId)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.toastr.success(data.meta.message);
            this.rerender();
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

  private rerender(): void {
    this.getAllFaqList();
  }
  /**
   * Datatabe global search
   * @param event get search input value
   */
  public filterDatatable() {
    // const val = event.target.value.toLowerCase();
    const val = this.global_search.toLowerCase();
    // get the key names of each column in the dataset
    const keys = ['question', 'status'];
    // assign filtered matches to the active datatable
    this.faqList = this.filteredData.filter(function (item) {
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
    this.filterService.saveSingleState('faqFilter', this.global_search);
  }
}
