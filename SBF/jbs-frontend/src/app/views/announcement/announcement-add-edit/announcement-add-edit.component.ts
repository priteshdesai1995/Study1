import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import * as moment from 'moment';
import { DatatableComponent, ColumnMode } from '@swimlane/ngx-datatable';
import { AnnouncementService } from '../../../_services/announcement.service';
import { LoaderService } from '../../../_services/loader.service';
import { MultilingualService } from '../../../_services/multilingual.service';

@Component({
  selector: 'app-announcement-add-edit',
  templateUrl: './announcement-add-edit.component.html',
  styleUrls: ['./announcement-add-edit.component.scss'],
})
export class AnnouncementAddEditComponent implements OnInit {
  @ViewChild('f', { static: true }) form: any;
  momentDateTime24Format: string;
  momentDateFormat: string;
  submitted = false;
  model = {
    announcement_title: {},
    announcement_description: {},
  };
  announcement_type: string;
  announcement_user_type = 'all';
  user_role = 'ALL';
  advanced_filter = 'all';
  push_image_resource: string;
  email_attachment_resource: any;
  emailAttachmentLabel: string;
  push_image: any;
  email_attachment: any = null;
  topicListData: any;
  routeSub: Subscription;
  announcementSub: Subscription;
  announcementSaveSub: Subscription;
  announcement_status = 'Active';
  announcement_name: string;
  isOpen = false;
  addEditCmsForm: FormGroup;
  pushImageSource: any;
  tableInitiated = false;
  ck_config = CONFIGCONSTANTS['CK-Editor-config'];
  selected_days = '';
  query = '';
  totalRecord = 0;
  isChecked = false;
  selectedUserList = [];
  sorting_order: number;
  order_drop_down_change;

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
  userList = [];
  sortParam = 'created_at';
  sortOrder = 'desc';
  // Get datatble configuration -- end
  languages = [];
  desc_error = {};
  constructor(
    private router: Router,
    private toastr: ToastrService,
    private announcementService: AnnouncementService,
    private loader: LoaderService,
    private multilingualService: MultilingualService
  ) {
    this.languages = this.multilingualService.getLanguage();
  }

  ngOnInit() {
    this.momentDateTime24Format = CONFIGCONSTANTS.momentDateTime24Format || 'MM/DD/YYYY hh:mm:ss';
    this.momentDateFormat = CONFIGCONSTANTS.momentDateFormat || 'MM/DD/YYYY';
    this.sorting_order = 0;
    this.order_drop_down_change = false;
  }

  /**
   * For Filter Apply
   */
  filter() {
    this.rerender(true);
  }

  openTree() {
    this.isOpen = this.isOpen === true ? false : true;
  }

  /**
   * Remove User From Checked List
   * @param id
   */
  removeUser(id) {
    const index = this.selectedUserList.findIndex((value) => value.id === id);
    this.selectedUserList.splice(index, 1);
    const index1 = this.userList.findIndex((value) => value.id === id);
    if (index1 !== -1) {
      this.userList[index1].selected = false;
    }

    let totalSelected = 0;
    this.userList.forEach((element) => {
      const ind = this.selectedUserList.findIndex((value) => value.id === element.id);
      if (ind !== -1) {
        totalSelected = totalSelected + 1;
      }
    });
    if (totalSelected === this.totalRecord) {
      this.isChecked = true;
    } else {
      this.isChecked = false;
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
  public rerender(goFirstPage): void {
    if (goFirstPage) {
      this.pageNumber = 0;
    }
    this.fetchUserTable();
  }
  resetSearch() {
    this.query = '';
    this.fetchUserTable();
  }
  /**
   * For Checkbox User List
   */
  fetchUserTable() {
    if (this.advanced_filter === 'all') {
      this.selectedUserList = [];
      this.userList = [];
    } else {
      const requestObject = {
        start: this.pageNumber * this.size,
        length: this.size,
        platform: this.announcement_user_type,
        query_string: this.query,
        registration_start_date: this.selected_days ? moment(this.selected_days['0']).format(this.momentDateFormat) : '',
        registration_end_date: this.selected_days ? moment(this.selected_days['1']).format(this.momentDateFormat) : '',
      };
      this.announcementService
        .getAnnouncementUserSelectionList(requestObject)
        .pipe(first())
        .subscribe(
          (resp) => {
            this.order_drop_down_change = false;
            this.isChecked = false;
            let totalSelected = 0;
            this.userList = resp.responseData;
            this.totalReords = resp.responseData.length;
            this.userList.forEach((element) => {
              element.first_name = element.userProfileEntity.firstName + ' '+ element.userProfileEntity.lastName;
              element.email = element.userProfileEntity.email;
              element.device_type = 'none';
              element.role = element.userRoleEntity.roleName;
              element.created_at = element.createDate;
              element.selected = false;
              const index = this.selectedUserList.findIndex((value) => value.id === element.userId);
              if (index !== -1) {
                element.selected = true;
                totalSelected = totalSelected + 1;
              }
            });
            if (totalSelected === resp.responseData.length) {
              this.isChecked = true;
            }
          },
          (error) => {
            console.log('Data not Fetch');
          }
        );
    }
  }

  /**
   * For Contact Number Double plus than remove one
   */
  removeAdditionalPlus(mobile) {
    return mobile.replace('++', '+');
  }

  /**
   * All Listed User Check
   */
  checkuncheckall() {
    if (this.isChecked === true) {
      this.isChecked = false;
      this.userList.forEach((element) => {
        element.selected = false;
        const index = this.selectedUserList.findIndex((value) => value.id === element.userId);
        if (index !== -1) {
          this.selectedUserList.splice(index, 1);
        }
      });
    } else {
      this.isChecked = true;
      this.userList.forEach((element) => {
        element.selected = true;
        const index = this.selectedUserList.findIndex((value) => value.id === element.userId);
        if (index === -1) {
          this.selectedUserList.push({
            id: element.userId,
            name: element.first_name,
          });
        }
      });
    }
  }

  /**
   * For single checkbox check
   * @param id
   * @param name
   * @param selected
   */
  checkunchecksingle(id, name, selected) {
    if (selected === true) {
      this.selectedUserList.push({ id: id, name: name });
    } else {
      const index = this.selectedUserList.findIndex((value) => value.id === id);
      this.selectedUserList.splice(index, 1);
    }
    if (this.userList.length === this.selectedUserList.length) {
      this.isChecked = true;
    } else {
      this.isChecked = false;
    }
  }

  /**
   * For save new Announcement
   */
  onAnnouncementSave() {
    this.submitted = true;
    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }
    this.languages.forEach((element) => {
      if (this.desc_error[element.locale] === true) {
        return;
      }
    });
    if (this.advanced_filter === 'only_selected' || this.advanced_filter === 'exclude_selected') {
      if (this.selectedUserList.length === 0) {
        this.toastr.error('Please select at least one user');
        this.submitted = false;
        return;
      }
    }
    const formdata:{}={};
    formdata['title'] = JSON.stringify(this.model.announcement_title);
    formdata['description'] = JSON.stringify(this.model.announcement_description);
    formdata['type'] = this.announcement_type;
    formdata['user_type'] = this.announcement_user_type;
    formdata['user_role'] = this.user_role;
    formdata['inclusion'] = this.advanced_filter;
    formdata['registration_start_date'] = this.selected_days ? moment(this.selected_days['0']).format(this.momentDateFormat) : '';
    formdata['registration_end_date'] = this.selected_days ? moment(this.selected_days['1']).format(this.momentDateFormat) : '';
    formdata['users'] = this.selectedUserList;
    for (let i = 0; i < this.selectedUserList.length; i++) {
      formdata['selections[' + i + ']'] = this.selectedUserList[i].id;
    }
    if (this.announcement_type === 'push' && this.push_image_resource) {
      formdata['push_image'] = this.push_image_resource;
    } else if (this.announcement_type === 'email' && this.email_attachment_resource) {
      formdata['email_attachment'] = this.email_attachment_resource;
    }
    this.createAnnouncement(formdata);
  }

  /**
   * for Create Api call with Parameters
   * @param formData
   */
  createAnnouncement(formData) {
    this.loader.showLoader();
    this.announcementService
      .createAnnouncement(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(['/manage-announcement/list']);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const errorData = error;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === 'VALIDATION_ERROR') {
              for (const key in errorData.errors) {
                if (key) {
                  this.toastr.error(errorData.errors[key][0]);
                }
              }
            } else {
              this.toastr.error(errorData.meta.message);
            }
          } else {
            this.router.navigate(['/manage-announcement/list']);
            this.toastr.error('Something went wrong please try again.');
          }
          this.submitted = false;
        }
      );
  }

  /**
   * For Select Push Image and Email Attachment
   * @param event
   */
  onSelectFile(event) {
    const img = event;
    if (event.target.files && event.target.files[0]) {
      const filesAmount = event.target.files.length;
      for (let i = 0; i < filesAmount; i++) {
        const reader = new FileReader();
        reader.readAsDataURL(event.target.files[i]);
        reader.onload = (file: any) => {
          this.push_image = event.target.files[i];
          this.pushImageSource = reader.result;
        };
      }
    }
  }

  /**
   * Attach a file
   * @param event
   */
  onSelectAttachmentFile(event) {
    const img = event;
    if (event.target.files && event.target.files[0]) {
      const filesAmount = event.target.files.length;
      for (let i = 0; i < filesAmount; i++) {
        const reader = new FileReader();
        this.email_attachment = event.target.files[i];
        this.emailAttachmentLabel = event.target.files[i].name;
      }
    }
  }

  /**
   * For reset Email and push image Attachment
   * @param type
   */
  resetMedia(type) {
    if (type === 'email') {
      this.email_attachment = null;
      this.emailAttachmentLabel = null;
      this.email_attachment_resource = null;
    } else {
      this.push_image = null;
      this.pushImageSource = null;
      this.push_image_resource = null;
    }
  }
  getDescData(type, lan_direction) {
    if (lan_direction === 'LTR') {
      const a = this.model.announcement_description[type].substring(
        this.model.announcement_description[type].indexOf('<body>') + 6,
        this.model.announcement_description[type].indexOf('</body>')
      );
      this.desc_error[type] = a.length > 0 ? false : true;
    }
    if (lan_direction === 'RTL') {
      const a = this.model.announcement_description[type].substring(
        this.model.announcement_description[type].indexOf('<body dir="rtl">') + 16,
        this.model.announcement_description[type].indexOf('</body>')
      );
      this.desc_error[type] = a.length > 0 ? false : true;
    }
  }
  resetDescription() {
    this.model.announcement_description = {};
  }
}
