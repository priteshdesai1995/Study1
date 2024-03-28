import { Component, OnInit, ElementRef, TemplateRef, QueryList, ViewChildren } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { ManageuserService } from './../../../_services/manageuser-service';
import { Subscription, Subject } from 'rxjs';
import { first } from 'rxjs/operators';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import { trigger } from '@angular/animations';
import { fadeOut, fadeIn } from '../../../utils/animations/fade-animations';

@Component({
  selector: 'app-manage-user-view',
  animations: [trigger('fadeOut', fadeOut()), trigger('fadeIn', fadeIn(':enter'))],
  templateUrl: './manage-user-view.component.html',
  styleUrls: ['./manage-user-view.component.scss', './ghost/ghost-item.component.scss'],
})
export class ManageUserViewComponent implements OnInit {
  model: any = {};
  url = 'assets/default-user-image.png';
  private _id: number;
  editMode = false;
  private editUserId: number;
  submitted = false;
  private routeSub: Subscription;
  dateFormat: string;

  constructor(private route: ActivatedRoute, private manageuserservice: ManageuserService) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params['id'];
      this.editMode = params['id'] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 1000);
    });
    this.dateFormat = CONFIGCONSTANTS.dateFormat || 'MM/dd/yyyy hh:mm:ss';
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
   * Selected input value in edit mode
   */
  initForm() {
    if (this.editMode) {
      this.manageuserservice
        .getManagerUserById(this._id)
        .pipe(first())
        .subscribe((response) => {
          this.editUserId = response.responseData.userId || null;
          this.model.fullname = response.responseData.firstName + ' ' + response.responseData.lastName || '';
          this.model.username = response.responseData.username || '';
          this.model.email = response.responseData.email || '';
          this.model.mobileNumber = response.responseData.cellPhone || '';
          this.model.dob = response.responseData.dateOfBirth || '';
          this.model.gender = response.responseData.gender || '';
          this.model.status = response.responseData.status || '';

          if (response.responseData.profile_image) {
            this.url = response.data.profile_photo;
            this.model.profile_photo = response.data.profile_image;
          } else {
            this.url = 'assets/default-user-image.png';
            this.model.profile_photo = null;
          }
        });
    }
  }
}
