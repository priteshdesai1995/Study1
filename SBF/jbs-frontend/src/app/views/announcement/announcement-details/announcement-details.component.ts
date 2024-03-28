import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Router, ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/operators';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import * as moment from 'moment';
import { AnnouncementService } from '../../../_services/announcement.service';
import { LoaderService } from '../../../_services/loader.service';
import { ColumnMode, DatatableComponent } from '@swimlane/ngx-datatable';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { PopupImageOpenComponent } from './popup-image-open.component';
import { MultilingualService } from '../../../_services/multilingual.service';

@Component({
  selector: 'app-announcement-details',
  templateUrl: './announcement-details.component.html',
  styleUrls: ['./announcement-details.component.scss'],
})
export class AnnouncementDetailsComponent implements OnInit {
  @ViewChild('iframe', { static: false }) iframe: ElementRef;
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
  @ViewChild(DatatableComponent, { static: false })
  datatable: DatatableComponent;
  userList = [];
  global_search = '';
  private filteredData = [];
  // Get datatble configuration -- end

  selectedStatus: any = '';
  types: any = {
    push: 'Push',
    email: 'Email',
    sms: 'SMS',
  };
  platforms: any = {
    all: 'All',
    android: 'Android',
    ios: 'IOS',
    web: 'Web',
  };
  status: any = {
    '-1': 'Pending',
    '0': 'In-Progress',
    '1': 'Announced',
  };
  announcement = {
    _id: '',
    titleEN: '', 
    titleAR: '', 
    descriptionEN: '', 
    descriptionAR: '', 
    title: {},
    description: {},
    status: '',
    type: '',
    user_type: '',
    uuid: '',
    email_attachment: '',
    email_attachment_url: '',
    push_image: '',
    push_image_url: '',
    user_role: '',
    query_string: '',
    inclusion: '',
    registration_start_date: '',
    registration_end_date: '',
  };
  inclusion = {
    exclude_selected: 'User to Exclude',
    only_selected: 'Only Send To',
    all: 'All',
  };
  userRole = {
    USER: 'User',
    ALL: 'All',
  };
  _uuid: String;
  languages = [];
  constructor(
    private route: ActivatedRoute,
    private announcementService: AnnouncementService,
    private toastr: ToastrService,
    private router: Router,
    private loader: LoaderService,
    private modalService: BsModalService,
    private multilingualService: MultilingualService
  ) {
    this.languages = this.multilingualService.getLanguage();
  }

  async ngOnInit() {
    this.route.params.subscribe((params) => {
      this._uuid = params['id'];
    });
    this.getAnnouncementDetails();
  }

  /**
   * Announcement List Details
   */
  public getAnnouncementDetails() {
    this.loader.showLoader();
    this.announcementService
      .getAnnouncementDetailsById(this._uuid)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          const resData = data.responseData;
          this.announcement = {
            _id: resData.announcementId,
            type: resData.type,
            titleEN: resData.titleEN,
            titleAR: resData.titleAR,
            descriptionEN: resData.descriptionEN,
            descriptionAR: resData.descriptionAR,
            title: {},
            description: {},
            user_type: resData.targetPlatform,
            status: resData.status,
            uuid: resData.announcementId,
            email_attachment: resData.email_attachment,
            email_attachment_url: resData.email_attachment_url,
            push_image: resData.push_image,
            push_image_url: resData.push_image_url,
            user_role: resData.user_role,
            query_string: resData.query_string,
            inclusion: resData.advancedFilters,
            registration_start_date: resData.registration_start_date,
            registration_end_date: resData.registration_end_date,
          };
          this.userList = resData.selection_set;
          this.filteredData = resData.selection_set;

          // resData.translations.forEach((element) => {
          //   this.announcement.title[element.locale] = element.title;
          //   this.announcement.description[element.locale] = element.description;
          // });
        },
        (error) => {
          if (error.errors !== undefined) {
            Object.keys(error.errors).forEach((key) => {
              this.toastr.error(error.errors[key]);
            });
          } else {
            const statusError = error;
            if (statusError && statusError.meta) {
              this.toastr.error(statusError.meta.message);
            } else {
              this.toastr.error('Something went wrong please try again.');
            }
          }
          this.router.navigate(['/manage-announcement/list']);
        }
      );
  }
  /**
   * For Date Format Display
   * @param date
   */
  getDateInformate(date) {
    return moment(date).format(CONFIGCONSTANTS['momentDateTime24Format']);
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
   * Datatabe global search
   * @param event get search input value
   */
  public filterDatatable(event) {
    const val = event.target.value.toLowerCase();
    // get the key names of each column in the dataset
    const keys = ['first_name', 'email', 'device_type', 'role', 'created_at'];
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

  openModalForImage(img) {
    const initialState = {
      image: img,
    };
    this.modalRef = this.modalService.show(PopupImageOpenComponent, {
      class: 'modal-lg',
      initialState,
    });
  }
}
