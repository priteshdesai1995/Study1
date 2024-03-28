import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import { AnnouncementService } from '../../../_services/announcement.service';
import { DatatableComponent, ColumnMode } from '@swimlane/ngx-datatable';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-announcement-receivers',
  templateUrl: './announcement-receivers.component.html',
  styleUrls: ['./announcement-receivers.component.scss'],
})
export class AnnouncementReceiversComponent implements OnInit {
  private __announcementUuid = '';
  @Input('announcement-uuid')
  set announcementUuid(announcementUuid: string) {
    this.__announcementUuid = announcementUuid;
    this.rerender();
  }
  get announcementUuid() {
    return this.__announcementUuid;
  }

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
  @ViewChild(DatatableComponent, { static: false })
  datatable: DatatableComponent;
  userList = [];
  global_search = '';
  private filteredData = [];
  // Get datatble configuration -- end

  date_formate = CONFIGCONSTANTS['momentDateTime24Format'];
  notificationStatus: any = {
    '0': 'In-Progress',
    '1': 'Sent',
    '2': 'Failed',
  };

  constructor(private announcementService: AnnouncementService) {}

  ngOnInit() {
    this.getReceiverDetails();
  }

  /**
   * Get Receiver Details
   */
  public getReceiverDetails() {
    if (this.announcementUuid) {
      this.announcementService
        .getAnnouncementUserData(this.announcementUuid)
        .pipe(first())
        .subscribe(
          (resp) => {
            this.userList = resp.responseData;
            this.filteredData = resp.responseData;
            this.userList.forEach((element) => {
              element.name = element.user.userProfileEntity.firstName + ' '+ element.user.userProfileEntity.lastName;
              element.email = element.user.userProfileEntity.email;
              element.role = element.user.userRoleEntity.roleName;
              element.notification_status = element.user.userRoleEntity.status;
            });
          },
          (error) => {
            console.log(error);
          }
        );
    }
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
   * Show number of records in datatable
   * @param value show total entries value 10,25,50,100
   */
  public changeLimit(value) {
    this.size = value;
  }

  /**
   * API call and refresh datatable value
   * @param goFirstPage set first page when param value true
   */
  private rerender(): void {
    this.getReceiverDetails();
  }

  /**
   * Datatabe global search
   * @param event get search input value
   */
  public filterDatatable(event) {
    const val = event.target.value.toLowerCase();
    // get the key names of each column in the dataset
    const keys = ['name', 'email', 'role'];
    // assign filtered matches to the active datatable
    this.userList = this.filteredData.filter(function (item) {
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
