import { DataTableColumn } from './../../core/_model/DataTableColumn';
import { APIType } from './../../core/_model/APIType';
import { DataTableConfig } from './../../core/_model/DataTableConfig';
import { ListTableComponent } from './../../core/_component/list-table/list-table.component';
import {
  BigFive,
  SortDirection,
  UserGroupModuleType,
} from './../../core/_constant/app-constant';
import {
  Component,
  OnInit,
  ViewChildren,
  QueryList,
  ElementRef,
  ViewChild,
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CONFIGCONSTANTS } from '../../core/_constant/app-constant';
import { Router } from '@angular/router';
import { UserGroupService } from './user-group.service';
import {
  Attribute,
  Gender,
  IcognitiveData,
  IUserGroupCreate,
  IDemographicData,
  IUserGroupList,
  IUserGroupListData,
} from '../../core/_model/user-group';
import { CommonService } from '../../services/common.service';
import { IState } from '../../core/_model/country';
import { ToasterService } from '../../core/_utility/notify.service';
import { IResponse, ErrorList } from '../../core/_model/response';
import { successHandler, errorHandler } from '../../core/_utility/common';
import * as _ from 'lodash';
import { NzPlacementType } from 'ng-zorro-antd/dropdown';
import { NzModalService } from 'ng-zorro-antd/modal';
import { URLS } from '../../core/_constant/api.config';

@Component({
  selector: 'app-create-user-group',
  templateUrl: './create-user-group.component.html',
  styleUrls: ['./create-user-group.component.scss'],
})
export class CreateUserGroupComponent implements OnInit {
  @ViewChildren('checkboxes') checkboxes: QueryList<ElementRef>;
  @ViewChild(ListTableComponent) listDataTable: ListTableComponent;

  isVisible = false;
  tableId: number;

  validateForm!: FormGroup;
  controlArray: Array<{ index: number; show: boolean }> = [];
  isCollapse = true;
  factor = CONFIGCONSTANTS.factorExternal;
  newGroupForm: FormGroup;
  isGroupSubmit: boolean = false;
  sortOptions = [{ key: 'Big Five', value: 'five' }];
  isLoadSubmit: boolean = false;
  isList: boolean = false;
  settingForm?: FormGroup;
  indeterminate = false;
  fixedColumn = false;
  scrollX: string | null = null;
  scrollY: string | null = null;
  ethnicityList: Attribute[] = [];
  ageGroupList: Attribute[] = [];
  familySizeList: Attribute[] = [];
  educationList: Attribute[] = [];
  bigFiveList: Attribute[] = [];
  genderList: Gender[] = [];
  otherList: Gender[] = [];
  valueList: Attribute[] = [];
  persuasiveList: Attribute[] = [];
  motivationList: Attribute[] = [];
  states: IState[] = [];
  userGroupList: IUserGroupListData[] = [];
  isMotivationEmpty: boolean = false;
  isPersuasiveEmpty: boolean = false;
  isValuesEmpty: boolean = false;
  pageLimit = 4;
  avatarImg = CONFIGCONSTANTS.avatarImgBaseUrl;
  moduleType = UserGroupModuleType;
  sortByList = CONFIGCONSTANTS.sortByList;
  actionOption = [
    { key: 'Edit', value: 'edit' },
    { key: 'Delete', value: 'delete' },
  ];
  isListLoader: boolean = false;
  total = 5;
  pageSize = 400;
  pageIndex = 6;

  dataTableConfig: DataTableConfig = new DataTableConfig(URLS.UserList, APIType.GET, URLS.deleteUser, 'viewPersona/', 'Saved User Groups', [
    new DataTableColumn("","icon",true),
    new DataTableColumn("GROUP NAME","name",false,true,'-',null,true),
    new DataTableColumn("BIG FIVE","bigFive"),
    new DataTableColumn("SUCCESS MATCH","successMatch",false,true, '-', '%'),
    new DataTableColumn("USERS","userCount",false,true, '-')

  ],[...CONFIGCONSTANTS.sortByList,
    { name: "Users", value: BigFive.USERS },
  ],"Delete User Group?","Do you want to delete this user group?","Yes, Delete This User Group", 'groupId', SortDirection.DESC,false,false,true,false,false,true,'700px','1000px',true,false,false,URLS.deleteMultipleUserGroup,'','Do you want to delete selected user group?','Yes, Delete selected User Group');
  constructor(
    private router: Router,
    private userGroupService: UserGroupService,
    private formBuilder: FormBuilder,
    private commonService: CommonService,
    private toaster: ToasterService,
    private modalService: NzModalService
  ) {}

  ngOnInit(): void {
    this.getDemographicData();
    this.getStates();
    this.newGroupForm = this.formBuilder.group({
      name: ['', Validators.required],
      gender: [null],
      ageGroup: ['', Validators.required],
      state: ['', Validators.required],
      ethnicity: [null],
      education: [null, [Validators.required]],
      familySize: [null, [Validators.required]],
      isExternalFactor: [false],
      bigFive: [null, Validators.required],
      values: [null, Validators.required],
      persuasiveStratergies: [null, Validators.required],
      motivationToBuy: [null, Validators.required],
    });
  }
  ngAfterViewInit(){
    this.sortByList = [];
  }

  uncheckAll() {
    this.checkboxes.forEach((element) => {
      element.nativeElement.checked = false;
    });
  }

  getStates() {
    this.commonService.getStates().subscribe((res: IState[]) => {
      this.states = res;
    });
  }

  getDemographicData() {
    this.userGroupService.getDemographicData().subscribe(
      (res: IResponse<IDemographicData>) => {
        successHandler(this.toaster, res, null, (isSuccess: boolean) => {
          if (isSuccess) {
            this.ageGroupList = res.responseData.ageGroup;
            this.ethnicityList = res.responseData.ethnicity;
            this.familySizeList = res.responseData.familySize;
            this.bigFiveList = res.responseData.bigFive;
            this.educationList = res.responseData.education;
            this.genderList = res.responseData.gender.filter(
              (obj) => obj.isOther == false
            );
            this.otherList = res.responseData.gender.filter(
              (obj) => obj.isOther == true
            );
            this.genderList.push({
              id: 0,
              value: 'Other',
              isOther: true,
            });
          }
        });
      },
      (error) => {
        errorHandler(this.toaster, error.error, () => {
          this.isLoadSubmit = false;
        });
      }
    );
  }

  get errorControl() {
    return this.newGroupForm.controls;
  }

  genderChange(e: any) {
    this.newGroupForm.value.gender = e.target.value;
    this.isList = false;
    if (e.target.value == 0) {
      this.isList = true;
    } else {
      this.newGroupForm.controls.gender.setValue(parseInt(e.target.value));
    }
  }

  bigFiveChange(e: Attribute) {
    this.persuasiveList = [];
    this.valueList = [];
    this.motivationList = [];
    this.isMotivationEmpty = false;
    this.isValuesEmpty = false;
    this.isPersuasiveEmpty = false;
    this.newGroupForm.controls.persuasiveStratergies.setValue('');
    this.newGroupForm.controls.values.setValue('');
    this.newGroupForm.controls.motivationToBuy.setValue('');
    this.userGroupService.getCognitiveData(e.id).subscribe(
      (res: IResponse<IcognitiveData>) => {
        successHandler(this.toaster, res, null, (isSuccess: boolean) => {
          if (isSuccess) {
            this.persuasiveList = res.responseData.persuasiveStrategies;
            this.motivationList = res.responseData.motivationToBuy;
            this.valueList = res.responseData.values;
            if (
              this.persuasiveList.length == 0 &&
              res.responseData.persuasiveStrategiesEmpty
            ) {
              this.persuasiveList.push(
                res.responseData.persuasiveStrategiesEmpty
              );
              this.isPersuasiveEmpty = true;
              this.newGroupForm.controls.persuasiveStratergies.setValue(
                res.responseData.persuasiveStrategiesEmpty.id
              );
            }
            if (
              this.motivationList.length == 0 &&
              res.responseData.motivationToBuyEmpty
            ) {
              this.motivationList.push(res.responseData.motivationToBuyEmpty);
              this.isMotivationEmpty = true;
              this.newGroupForm.controls.motivationToBuy.setValue(
                res.responseData.motivationToBuyEmpty.id
              );
            }
            if (this.valueList.length == 0 && res.responseData.valuesEmpty) {
              this.valueList.push(res.responseData.valuesEmpty);
              this.isValuesEmpty = true;
              this.newGroupForm.controls.values.setValue(
                res.responseData.valuesEmpty.id
              );
            }
          }
        });
      },
      (error) => {
        errorHandler(this.toaster, error.error, () => {
          this.isLoadSubmit = false;
        });
      }
    );
  }

  onAttributeClear(
    event: any,
    isValues = false,
    isPersuasive = false,
    isMotivation = false
  ) {
    if (
      isMotivation &&
      this.isMotivationEmpty &&
      this.motivationList.length > 0
    ) {
      this.newGroupForm.controls.motivationToBuy.setValue(
        this.motivationList[0].id
      );
      return false;
    }
    if (
      isPersuasive &&
      this.isPersuasiveEmpty &&
      this.persuasiveList.length > 0
    ) {
      this.newGroupForm.controls.persuasiveStratergies.setValue(
        this.persuasiveList[0].id
      );
      return false;
    }
    if (isValues && this.isValuesEmpty && this.valueList.length > 0) {
      this.newGroupForm.controls.values.setValue(this.valueList[0].id);
      return false;
    }
  }

  addNewGroup() {
    this.isGroupSubmit = true;
    let data: IUserGroupCreate = this.newGroupForm.value;
    if (this.newGroupForm.valid) {
      this.isLoadSubmit = true;
      this.userGroupService.addUserGroup(data).subscribe(
        (res: IResponse<any>) => {
          successHandler(
            this.toaster,
            res,
            res.responseData.message,
            (isSuccess: boolean) => {
              if (isSuccess) {
                //this.listDataTable.listData();
                this.listDataTable.rerender();
                this.resetUserForm();
              }
              this.isLoadSubmit = false;
            }
          );
        },
        (error) => {
          errorHandler(this.toaster, error.error, () => {
            this.isLoadSubmit = false;
          });
        }
      );
    }
  }

  resetUserForm() {
    this.newGroupForm.reset();
    this.uncheckAll();
    this.isPersuasiveEmpty = false;
    this.isMotivationEmpty = false;
    this.isValuesEmpty = false;
    this.persuasiveList = [];
    this.valueList = [];
    this.motivationList = [];
    this.newGroupForm.controls.isExternalFactor.setValue(false);
    this.isGroupSubmit = false;
  }
}
