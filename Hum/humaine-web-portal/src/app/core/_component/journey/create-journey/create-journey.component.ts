import { MenuLinks } from './../../../_enums/MenuLinks';
import { AuthenticationService } from './../../../../services/authentication.service';
import { Observable } from 'rxjs';
import { ErrorList } from './../../../_model/response';
import { NgForm } from '@angular/forms';
import { CONFIGCONSTANTS } from './../../../_constant/app-constant';
import { Component, ElementRef, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { map } from 'rxjs/operators';
import { DataService } from '../../../../services/data-service.service';
import { URLS } from '../../../_constant/api.config';
import { ChartType, RouteURLIdentity } from '../../../_constant/app-constant';
import { IResponse } from '../../../_model/response';
import { IElements, ISaveTestJourney, ITestJourneyUser } from '../../../_model/testJourney';
import { errorHandler, isEmpty, successHandler } from '../../../_utility/common';
import { ToasterService } from '../../../_utility/notify.service';
import * as _ from 'lodash'
import { textChangeRangeIsUnchanged } from 'typescript';
export interface ISingleEmenet {
  name: string;
  value: string;
}
@Component({
  selector: 'create-journey',
  templateUrl: './create-journey.component.html',
  styleUrls: ['./create-journey.component.scss']
})
export class CreateJourneyComponent implements OnInit {
  readonly noOfVisibleItems: number = 5;
  readonly stepsKeys = CONFIGCONSTANTS.elementMasterKeys;
  readonly notSkippableSteps = CONFIGCONSTANTS.elementMasterNotSkippable;
  chartTypes = ChartType;
  @ViewChild('template') template: TemplateRef<{}>;
  selected = "Product";
  menuCountKey: MenuLinks = null;
  isPageLoading: boolean = false;
  isOwnernership: boolean = false;
  isCart: boolean = false;
  isDecision: boolean = false;
  isBuy: boolean = false;
  createURL: string = null;
  URL: string = '';
  testJourneyUserURl: string = '';
  elements: IElements[] = [];
  testJourneyUserList: ITestJourneyUser[] = [];
  isListTable: boolean = false;
  isView: boolean = false;
  isEdit: boolean = false;
  editURL: string = '';
  viewPageURL: string = '';
  isCreate: boolean = false;
  saveElementData: any = [];
  saveTestJourney: ISaveTestJourney = new ISaveTestJourney();
  isJourneySbmit: boolean = false;
  isLoading = false;
  journeyID: number = null;
  journeyDetailURL: string;
  IsJourney: boolean = false;
  groupTitle: string = 'Select User Group';
  groupLabel: string = 'Created User Groups';
  IsJourneyAnalysis: boolean = false;
  IsTestJourney: boolean = false;
  IsAiJourney: boolean = false;
  selectedSteps: number = 0;
  totalSteps: number = 0;
  constructor(private notification: NzNotificationService,
    private route: ActivatedRoute,
    private dataService: DataService,
    private toaster: ToasterService,
    private router: Router,
    private auth: AuthenticationService) {
    this.getConfigData();
  }

  getConfigData() {
    this.IsJourney = false;
    if (this.route.snapshot.data.identity === RouteURLIdentity.JOURNEY_ANAYSIS) {
      this.groupTitle = "User Group";
      this.IsJourneyAnalysis = true;
    }
    if (this.route.snapshot.data.identity === RouteURLIdentity.AI_GENERATED_JOURNEY) {
      this.groupTitle = "";
      this.IsJourney = true;
      this.IsAiJourney = true;
    }
    if (this.route.snapshot.data.identity === RouteURLIdentity.TEST_JOURNEY) {
      this.IsTestJourney = true;
    }

    if (this.route.snapshot.data.apiURL) {
      this.URL = this.route.snapshot.data.apiURL;
    }
    if (this.route.snapshot.data.userURL) {
      this.testJourneyUserURl = this.route.snapshot.data.userURL;
    }
    if (this.route.snapshot.data.createURL) {
      this.createURL = this.route.snapshot.data.createURL;
    }
    if (this.route.snapshot.data.detailURL) {
      this.journeyDetailURL = this.route.snapshot.data.detailURL;
    }
    if (this.route.snapshot.params['id']) {
      this.journeyID = this.route.snapshot.params['id'];
    }
    if (this.route.snapshot.data.isView) {
      this.isView = this.route.snapshot.data.isView;
      this.IsJourney = true;
    }
    if (this.route.snapshot.data.isCreate) {
      this.isCreate = this.route.snapshot.data.isCreate;
      this.menuCountKey = this.route.snapshot.data.menuCountKey || null;
    }
    if (this.route.snapshot.data.isEdit) {
      this.isEdit = this.route.snapshot.data.isEdit;
      this.IsJourney = true;
    }
    if (this.route.snapshot.data.editURL) {
      this.editURL = this.route.snapshot.data.editURL;
    }
    if (this.route.snapshot.data.viewPageURL) {
      this.viewPageURL = this.route.snapshot.data.viewPageURL;
    }
    if (this.IsAiJourney) {
      this.saveElementData = {
        bigFive: "Conscientiousness",
        decison: "Menu Item",
        firstInterest: "Blog Post",
        groupId: 27,
        groupName: "Test",
        id: 5,
        journeySteps: 3,
        journeyTime: null,
        purchaseAddCart: null,
        purchaseBuy: "Buy",
        purchaseOwnership: null,
        successRate: null,
        totalJourneySteps: 5

      }
      this.journeyID = null;
    }
  }

  ngOnInit(): void {
    this.resetPage(null);
    this.getTestJourneyUserList();
    this.getElementsData();
  }
  
  getElementsData() {
    if (!this.URL) {
      return;
    }
    this.isPageLoading = true;
    this.dataService.get(this.URL).pipe(
      map((data) => {
        return data;
      })
    ).subscribe(
      (res: IResponse<IElements[]>) => {
        successHandler(this.toaster, res, null, (isSuccess: boolean) => {
          if (isSuccess) {
            this.elements = res.responseData;
            this.elements.forEach((element: IElements) => {
              element.isSkip = false;
              element.showMore = false
            });
            this.getJourneyDetailById();
            this.isPageLoading = false;
          }
        });
      },
      (error) => {
        errorHandler(this.toaster, error.error, () => {
          this.isPageLoading = false;
        });
      }
    );
  }

  getTestJourneyUserList() {
    if (!this.testJourneyUserURl) {
      return;
    }
    this.dataService.get(this.testJourneyUserURl).pipe(
      map((data) => {
        return data;
      })
    ).subscribe(
      (res: IResponse<ITestJourneyUser[]>) => {
        successHandler(this.toaster, res, null, (isSuccess: boolean) => {
          if (isSuccess) {
            this.testJourneyUserList = res.responseData;
          }
        });
      },
      (error) => {
        errorHandler(this.toaster, error.error, () => {
        });
      }
    );
  }

  createBasicNotification(template: TemplateRef<{}>): void {
    this.notification.template(template, {
      nzDuration: 0
    });
  }

  skipChange(data: IElements) {
    if (this.isView) return;
    data.isSkip = !data.isSkip;
    this.saveTestJourney[this.stepsKeys[data.id]] = "";
    return data;
  }

  viewSavedList() {
    if (!this.isListNavigationButtonVisible) return;
    this.router.navigateByUrl(this.getListPageURL);
  }

  get getListPageURL() {
    return this.route.snapshot.data.listingRoute;
  }

  get isListNavigationButtonVisible() {
    return !isEmpty(this.getListPageURL);
  }
  saveTestNewJourneyFunc() {
    this.isJourneySbmit = true;
    if (!this.saveTestJourney) {
      return;
    }
    this.dataService.post(URLS.saveTestNewJourney, this.saveTestJourney).pipe(
      map((data) => {
        return data;
      })
    ).subscribe(
      (res: IResponse<ISaveTestJourney>) => {
        successHandler(this.toaster, res, null, (isSuccess: boolean) => {
          if (isSuccess) {
            this.isJourneySbmit = false;
          }
        });
      },
      (error) => {
        errorHandler(this.toaster, error.error, () => {
          this.isJourneySbmit = false;
        });
      }
    );
  }

  apply(f: NgForm) {
    if (!f.valid) {
      this.toaster.errorMsg("Please fill or select the data. ")
      return true;
    };
    this.saveJourney(f);
  }

  setJourneyStepsState() {
    setTimeout(() => {
      for (let ele of this.elements) {
        if (this.notSkippableSteps.includes(ele.id)) {
          ele.isSkip = false;
          continue;
        }
        ele.isSkip = isEmpty(this.saveTestJourney[this.stepsKeys[ele.id]])
        if (!ele.isSkip && !_.map(ele.values, 'value').includes(this.saveTestJourney[this.stepsKeys[ele.id]])) {
          ele.isSkip = true;
          this.saveTestJourney[this.stepsKeys[ele.id]] = "";
        }
      }
    }, 0);

  }

  saveJourney(f: NgForm) {
    if (this.IsAiJourney || this.IsJourneyAnalysis) {
      return false;
    };
    if (this.isLoading) return false;
    this.isLoading = true;
    this.dataService.post(this.createURL, {
      ...this.saveTestJourney
    }).pipe(
      map((data) => {
        return data;
      })
    ).subscribe(
      (res: IResponse<ErrorList>) => {
        successHandler(this.toaster, res, null, (isSuccess: boolean) => {
          this.isLoading = false;
          if (isSuccess) {
            this.toaster.successMsg(res.responseData.message, "Success");
            if (this.isCreate === true && !isEmpty(this.menuCountKey)) {
              this.auth.incrementMenuCount(this.menuCountKey);
            }
            // this.resetPage(f);
          }
        });
      },
      (error) => {
        errorHandler(this.toaster, error.error, () => {
          this.isLoading = false;
        });
      }
    );
  }

  applyJourney() {
    this.selectedSteps = 0;
    this.IsJourney = true;
    for (var i in this.stepsKeys) {
      if (this.saveTestJourney[this.stepsKeys[i]]) {
        this.selectedSteps++;
      }
    }
    this.totalSteps = this.elements.length;
  }

  resetPage(f: NgForm) {
    if (f) {
      f.reset();
      f.resetForm();
    }
    this.elements.forEach((element: IElements) => {
      element.isSkip = false;
    });
  }


  getJourneyDetailById() {
    if (!this.journeyID) return false;
    this.dataService.get(this.journeyDetailURL + '/' + this.journeyID).pipe(
      map((data) => {
        return data;
      })
    ).subscribe(
      (res: IResponse<ISaveTestJourney>) => {
        successHandler(this.toaster, res, null, (isSuccess: boolean) => {
          this.isLoading = false;
          if (isSuccess) {
            this.saveTestJourney = res.responseData;
            if (this.IsJourneyAnalysis) {
              this.saveTestJourney['groupName'] = "UG" + this.saveTestJourney['groupName'];
            }
            this.setJourneyStepsState();
            this.scrollListToSelectedElement();
          }
        });
      },
      (error) => {
        errorHandler(this.toaster, error.error, () => {
          this.isLoading = false;
        });
      }
    );
  }
  navigateToEditPage() {
    if (this.isCreate) return;
    this.router.navigateByUrl(`/customer-journey/test-new-journey/edit/${this.journeyID}`);
  }
  navigateToViewPersona(saveTestJourney: any) {
    let group;
    if (this.IsTestJourney) {
      group = saveTestJourney.groupId;
      this.router.navigateByUrl(`/customer-journey/test-new-journey/viewPersona/${group}`);
    } else if (this.IsJourneyAnalysis) {
      group = saveTestJourney.groupName;
      this.router.navigateByUrl(`/customer-journey/my-journey-analysis/viewPersona/${group}`);
    }
  }
  updateJourney(f: NgForm) {
    if (f.invalid || this.isLoading || !this.editURL || this.isCreate) {
      return false
    };
    this.isJourneySbmit = true;
    this.dataService.put(`${this.editURL}/${this.journeyID}`, {
      ...this.requestBody
    }).pipe(
      map((data) => {
        return data;
      })
    ).subscribe(
      (res: IResponse<ErrorList>) => {
        successHandler(this.toaster, res, null, (isSuccess: boolean) => {
          this.isJourneySbmit = false;
          if (isSuccess) {
            this.toaster.successMsg(res.responseData.message, "Success");
            this.router.navigateByUrl(`${this.viewPageURL}/${this.journeyID}`)
          }
        });
      },
      (error) => {
        errorHandler(this.toaster, error.error, () => {
          this.isJourneySbmit = false;
        });
      }
    );
  }
  get requestBody() {
    return _.pickBy(this.saveTestJourney, (value, key) => {
      return !isEmpty(value);
    });
  }

  seeDetail(ele: IElements) {
    ele.showMore = !ele.showMore;
  } 

  scrollListToSelectedElement() {
    this.elements.forEach(item => {
      if (!isEmpty(this.saveTestJourney[this.stepsKeys[item.id]])) {
        const index = item.values.findIndex(val => {
          return val.value === this.saveTestJourney[this.stepsKeys[item.id]];
        })
        if (index > -1) {
          let wait = 0;
            if (index > this.noOfVisibleItems) {
              item.showMore = true;
              wait = 200;
            }
            const parentScrollDiv = document.getElementById(`elelist-${item.id}`);
            if (parentScrollDiv) {
              setTimeout(() => {
                const checkedEle = parentScrollDiv.querySelector("input:checked");
                if (checkedEle) {
                  parentScrollDiv.scrollTop = (checkedEle as any).offsetTop - parentScrollDiv.offsetTop - 170;
                }
              }, wait);
            }
        }
      }
    });
  }
}
