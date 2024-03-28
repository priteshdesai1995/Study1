import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { first } from 'rxjs/operators';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { ToastrService } from 'ngx-toastr';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import { DatatableComponent, ColumnMode } from '@swimlane/ngx-datatable';
import { SuggestionService } from '../../../_services/suggestion.service';

@Component({
  selector: 'app-user-suggestion-list',
  templateUrl: './user-suggestion-list.component.html',
  styleUrls: ['./user-suggestion-list.component.scss'],
})
export class UserSuggestionListComponent implements OnInit {
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
  suggestionList = [];
  global_search = '';
  private filteredData = [];
  // Get datatble configuration -- end
  momentDateTime24Format: string;

  constructor(private suggestionService: SuggestionService, private toastr: ToastrService, private modalService: BsModalService) {}

  ngOnInit() {
    this.getAllSuggestionList();
    this.momentDateTime24Format = CONFIGCONSTANTS.momentDateTime24Format || 'MM/DD/YYYY hh:mm:ss';
  }
  /**
   * Show number of records in datatable
   * @param value show total entries value 10,25,50,100
   */
  public changeLimit(value) {
    this.size = value;
  }
  /**
   * Get Suggestion list data
   */
  private getAllSuggestionList(): void {
    this.suggestionService
      .getUserSuggestionList()
      .pipe(first())
      .subscribe((data) => {
        if (!data.data) {
          this.suggestionList = [];
        } else {
          this.suggestionList = data.data;
          this.filteredData = data.data;
        }
      });
  }

  /**
   * Datatabe global search
   * @param event get search input value
   */
  public filterDatatable(event) {
    const val = event.target.value.toLowerCase();
    // get the key names of each column in the dataset
    const keys = ['category_name', 'information'];
    // assign filtered matches to the active datatable
    this.suggestionList = this.filteredData.filter(function (item) {
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
    this.datatable.offset = 0;
  }
}
