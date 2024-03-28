import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  TemplateRef,
} from "@angular/core";
import { RolePermissionService } from "./../../_services/role-permission.service";
import { Role } from "./../../model/role";
import { first } from "rxjs/operators";
import { BsModalService, ModalDirective } from "ngx-bootstrap/modal";
import { BsModalRef } from "ngx-bootstrap/modal/bs-modal-ref.service";
import { ToastrService } from "ngx-toastr";
import { TreeviewItem, TreeviewConfig, TreeviewHelper } from "ngx-treeview";
import { CONFIGCONSTANTS } from "../../config/app-constants";
import { ManageuserService } from "../../_services/manageuser-service";
import { DatatableComponent, ColumnMode } from "@swimlane/ngx-datatable";
import { Subscription } from "rxjs";
import { LanguageService } from "../../_services/language.service";
import { TranslateService } from "@ngx-translate/core";

@Component({
  selector: "app-role-permission",
  templateUrl: "./role-permission.component.html",
  styleUrls: ["./role-permission.component.scss"],
})
export class RolePermissionComponent implements OnInit {
  private modalRef: BsModalRef;
  @ViewChild("myModal", { static: true }) public myModal: ModalDirective;
  keyValuePermissionArr = [];
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
  roleList: Role[] = [];
  global_search = "";
  private filteredData = [];
  // Get datatble configuration -- end

  private changeStatusId: number;
  private changeStatusType: string;
  private changedStatus: string;
  private subscription: Subscription;
  private Language = localStorage.getItem("lan") || "en";
  @ViewChild("roleForm", { static: true }) form: any;
  model: any = new Role("", "ACTIVE");
  roleModalId: number;
  roleModalTitle: string;

  currRoleId: number = null;

  dropdownEnabled = true;
  items: TreeviewItem[];
  allPermissions: any[];
  values: number[];
  config = TreeviewConfig.create({
    hasAllCheckBox: true,
    hasFilter: false,
    hasCollapseExpand: true,
    decoupleChildFromParent: false,
    maxHeight: 500,
  });

  isPermissionSub = false;

  /***
   * 'key' [means disabledArray index] : contains the key that you want to disable
   * 'child_role' : contains the array of keys on which this array_key is
   *                checked or unchecked and it will depend on the boolean parameter
   *                'strict_check'. if we have more then one keys and if we set
   *                strict_check to 'true' then Parent Key would be selected if all it's
   *                key elements are selected together and if we set strict_check to 'false'
   *                then Parent Key would be depended to any one of selection of key elements.
   * 'strict_check' : optional parameter default value false
   */
  disabledArray = CONFIGCONSTANTS.rolePermissionDisabled;

  constructor(
    private rolePermissionService: RolePermissionService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private manageuserService: ManageuserService,
    private languageSwitcher: LanguageService,
    private translate: TranslateService
  ) {
    this.subscription = this.languageSwitcher.getLanguage().subscribe((lan) => {
      this.translate.setDefaultLang(lan);
      this.translate.use(lan);
    });
    this.languageSwitcher.changeLanguage(this.Language);
  }

  ngOnInit() {
    this.getAllRoleList();
  }
  /**
   * Get status class from configuration
   * @param status Active/Inactive/Pending
   */
  public getStatusClass(status) {
    if (status) {
      return CONFIGCONSTANTS.statusClass[status];
    } else {
      return "badge-primary";
    }
  }
  /**
   * Show number of records in datatable
   * @param value show total entries value 10,25,50,100
   */
  public changeLimit(value) {
    this.size = value;
  }
  onHidden(): void {
    this.form.reset();
  }
  /**
   * Get Role list data
   */
  private getAllRoleList(): void {
    this.rolePermissionService
      .getAllRoleList()
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.responseData.length === 0) {
            this.roleList = [];
          } else {
            this.roleList = data.responseData;
            // console.log(JSON.stringify(this.roleList));
            this.filteredData = data.responseData;
            if (this.roleList.length) {
              this.currRoleId = data.responseData[0].id;
              this.getRolePermissions(this.currRoleId);
            } else {
              this.currRoleId = null;
            }
          }
        },
        (error) => {
          console.log(error);
        }
      );
  }

  addRole() {
    this.roleModalTitle = "Add New Role";
    this.model = new Role("", "ACTIVE");
    this.roleModalId = null;
    this.form.resetForm();
    this.myModal.show();
  }

  editRole(id, name, status) {
    this.roleModalTitle = "Update Role [" + name + "]";
    this.model = new Role(name, status);
    this.roleModalId = id;
    this.myModal.show();
  }
  /**
   * Create/Update Role data
   */
  public saveRole() {
    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }
    const data = {
      name: this.manageuserService.trimText(this.model.name),
      status: this.model.status,
    };
    if (this.roleModalId) {
      this.updateRole(data, this.roleModalId);
    } else {
      this.createRole(data);
    }
  }

  private createRole(formData) {
    this.rolePermissionService
      .createRole(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.myModal.hide();
            this.rerender();
          }
        },
        (error) => {
          const errorData = error;
          const self = this;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === "VALIDATION_ERROR") {
              for (const key in errorData.errors) {
                if (key) {
                  self.toastr.error(errorData.errors[key][0]);
                }
              }
            } else {
              self.toastr.error(errorData.meta.message);
            }
          } else {
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  private updateRole(formData, id) {
    this.rolePermissionService
      .updateRole(formData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.myModal.hide();
            this.rerender();
          }
        },
        (error) => {
          const errorData = error;
          const self = this;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === "VALIDATION_ERROR") {
              for (const key in errorData.errors) {
                if (key) {
                  self.toastr.error(errorData.errors[key][0]);
                }
              }
            } else {
              self.toastr.error(errorData.meta.message);
            }
          } else {
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  private rerender(): void {
    this.getAllRoleList();
  }

  openModal(template: TemplateRef<any>, id, status) {
    this.modalRef = this.modalService.show(template, { class: "modal-md" });
    this.changeStatusId = id;
    this.changeStatusType = status;
  }

  confirm(): void {
    this.changeStatus(this.changeStatusId, this.changeStatusType);
  }

  decline(): void {
    this.modalRef.hide();
  }
  /**
   * Change CMS status Active or Inactive
   */
  public changeStatus(id, status) {
    this.changedStatus = status === "ACTIVE" ? "INACTIVE" : "ACTIVE";
    this.rolePermissionService
      .changeRoleStatus(this.changedStatus, id)
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
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }
  /**
   * Get role wise permission data
   * @param id Role id
   */
  public getRolePermissions(id) {
    const items: TreeviewItem[] = [];
    if (!id) {
      this.items = items;
      return;
    }
    const newArray = [];
    this.rolePermissionService.getRolePermissions(id).subscribe((data) => {
      this.allPermissions = data.responseData;
      this.keyValuePermissionArr = [];

      for (const permission of this.allPermissions) {
        if (permission.parent === "#") {
          newArray[permission.id] = {
            text: permission.text,
            value: null,
            children: [],
          };
        }
      }

      for (const permission of this.allPermissions) {
        if (permission.parent !== "#") {
          this.keyValuePermissionArr[permission.permission_key] =
            permission.text;

          newArray[permission.parent].children.push({
            text: permission.text,
            value: permission.id,
            checked: permission.state.selected,
          });
        }
      }

      for (const par in newArray) {
        if (newArray.hasOwnProperty(par)) {
          const item = new TreeviewItem(newArray[par]);
          items.push(item);
        }
      }
      this.items = items;
    });
  }

  onFilterChange(value: string) {
    console.log("filter:", value);
  }
  /**
   * save role wise permission
   */
  public savePermission() {
    this.isPermissionSub = true;
    if (!this.currRoleId) {
      this.toastr.error("Please select role");
      this.isPermissionSub = false;
      return;
    } else if (!this.values.length) {
      this.toastr.error("No permission seleted for operation");
      this.isPermissionSub = false;
      return;
    }

    this.rolePermissionService
      .assignPermissions(this.values, this.currRoleId)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status === true) {
            this.toastr.success(data.meta.message);
          }
          this.isPermissionSub = false;
        },
        (error) => {
          const statusError = error;
          if (statusError && statusError.meta) {
            this.toastr.error(statusError.meta.message);
          } else {
            this.toastr.error("Something went wrong please try again.");
          }
          this.isPermissionSub = false;
        }
      );
  }
  /**
   * Datatabe global search
   * @param event get search input value
   */
  public filterDatatable(event) {
    const val = event.target.value.toLowerCase();
    // get the key names of each column in the dataset
    const keys = ["roleName", "status"];
    // assign filtered matches to the active datatable
    this.roleList = this.filteredData.filter(function (item) {
      // iterate through each row's column data
      for (let i = 0; i < keys.length; i++) {
        // check for a match
        if (
          (item[keys[i]] &&
            item[keys[i]].toString().toLowerCase().indexOf(val) !== -1) ||
          !val
        ) {
          // found match, return true to add to result set
          return true;
        }
      }
    });
    // whenever the filter changes, always go back to the first page
    this.datatable.offset = 0;
  }

  onSelectedChange($event, treeview) {
    let array = [];
    array = array.concat(treeview.selection.checkedItems);
    array = array.concat(treeview.selection.uncheckedItems);
    this.processArray(array, $event, this.disabledArray);
    this.values = $event;
  }
  processArray(array, $event, disabledArray) {
    for (const key in disabledArray) {
      if (
        key &&
        this.checkAllKeysValid(key, disabledArray[key]["child_role"])
      ) {
        const id = this.getPermissionValueByText(
          array,
          this.keyValuePermissionArr[key]
        );
        const strict =
          disabledArray[key]["strict_check"] !== undefined &&
          typeof disabledArray[key]["strict_check"] === "boolean"
            ? disabledArray[key]["strict_check"]
            : false;
        const operator = strict === true ? "&&" : "||";
        let condition = "";
        disabledArray[key]["child_role"].forEach((element) => {
          const checkId = this.getPermissionValueByText(
            array,
            this.keyValuePermissionArr[element]
          );
          if (condition.length > 0) {
            condition += " " + operator + " " + $event.includes(checkId);
          } else {
            condition += $event.includes(checkId);
          }
        });
        if (eval(condition)) {
          TreeviewHelper.findItemInList(this.items, id).disabled = false;
          TreeviewHelper.findItemInList(this.items, id).checked = true;
          TreeviewHelper.findItemInList(this.items, id).disabled = true;
          if (!$event.includes(id)) {
            $event.push(id);
          }
        } else {
          TreeviewHelper.findItemInList(this.items, id).disabled = false;
          TreeviewHelper.findItemInList(this.items, id).checked = false;
          TreeviewHelper.findItemInList(this.items, id).disabled = true;
          if ($event.includes(id)) {
            $event.splice($event.indexOf(id), 1);
          }
        }
      }
    }
  }
  getPermissionValueByText(array, text) {
    return array.filter(
      (ele) => ele.text.toUpperCase() === text.toUpperCase()
    )[0].value;
  }

  checkAllKeysValid(key, values) {
    let result = false;
    const keys = Object.keys(this.keyValuePermissionArr);

    if (keys.includes(key)) {
      result = values.every((element, index) => {
        if (!keys.includes(element)) {
          return false;
        } else {
          return true;
        }
      });
    }
    return result;
  }
}
