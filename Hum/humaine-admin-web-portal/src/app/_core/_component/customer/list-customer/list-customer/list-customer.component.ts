import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SortDirection } from 'src/app/_constant/app.constant';
import { APIType } from 'src/app/_core/_enums/APITYPE';
import { DataTableColumn } from 'src/app/_model/DataTableColumn';
import { DataTableConfig } from 'src/app/_model/DataTableConfig';
import { ListTableComponent } from '../../../list-table/list-table.component';

@Component({
  selector: 'app-list-customer',
  templateUrl: './list-customer.component.html',
  styleUrls: ['./list-customer.component.scss']
})
export class ListCustomerComponent implements OnInit {

  @ViewChild("journeyList") journeyList: ListTableComponent;
  detailPath: string = '';
  // menuLinks = MenuLinks;
  journeyListConfig: DataTableConfig;
  isMockData: boolean = false;
  mockData = [];
  listURL : string = '';
  isCheckbox: boolean = false;
  saveAiData = [];
  isShowGenerate: boolean = false;
  isShowSave: boolean = false;
  scrollHeight: string = '720px';
  isDeleteShow: boolean = true;
  deleteAPIURL: string = '';
  deleteMultipleJourneyAPIURL: string = '';
  isView: boolean = false;

  constructor(private route: ActivatedRoute) {
    this.getConfigData();
    this.journeyListConfig = new DataTableConfig(this.listURL, APIType.POST, this.deleteAPIURL, this.detailPath,  "" , [
      new DataTableColumn("FULL NAME", "fullName", false, true, "-", null, true, null, null, '200px'),
      new DataTableColumn("EMAIL", "email", false, true, '-', null, false, null, null, '300px'),
      new DataTableColumn("URL", "url", false, true, '-', null, false, null, null, '250px'),
      new DataTableColumn("ADDRESS", "address", false, true, '-', null, false, null, null, '200px'),
      new DataTableColumn("CITY", "city", false, true, '-', null, false, null, null, '100px'),
      new DataTableColumn("STATE", "state", false, true, '-', null, false, null, null, '100px'),
      new DataTableColumn("REGISTERED ON", "registeredOn", false, true, '-', null, false, null, null, '150px'),
      new DataTableColumn("STATUS", "status", false, true, '-', null, false, null, true, '100px'),
  ], [
      { name: "Full Name", value: 'fullName' },
      { name: "Email", value: 'email' },
      { name: "Url", value: 'url' },
      { name: "Address", value: 'address' },
      { name: "City", value: 'city' },
      { name: "State", value: 'state' },
      { name: "Registered On", value: 'registeredOn' },
      { name: "Status", value: 'status' },
    ], "Delete Customer?", "Do you want to delete this Customer?", "Yes, Delete This Customer", 'registeredOn', SortDirection.DESC, this.isMockData, true, true , false,  false, true, this.scrollHeight,"850px", true, true,false, this.deleteMultipleJourneyAPIURL,'','Do you want to delete selected Customer?','Yes, Delete selected Customer');
   }

  getConfigData() {
    this.isCheckbox = false;
    this.isShowGenerate = false;
    this.isShowSave = false;
    this.scrollHeight = '720px';
    if (this.route.snapshot.data.detailPath) {
      this.detailPath = this.route.snapshot.data.detailPath;
    }
    if(this.route.snapshot.data.listURL){
      this.listURL = this.route.snapshot.data.listURL;
    }
    if(this.route.snapshot.data.deleteURL){
      this.deleteAPIURL = this.route.snapshot.data.deleteURL;
    }
    if(this.route.snapshot.data.deleteMultipleJourneyAPIURL){
      this.deleteMultipleJourneyAPIURL = this.route.snapshot.data.deleteMultipleJourneyAPIURL;
    }
    if (this.route.snapshot.data.isMock) {
      this.isMockData = this.route.snapshot.data.isMock;
      this.getMockdata();
    }
    if (this.route.snapshot.data.isView) {
      this.isView = this.route.snapshot.data.isView;
    }    
  }

  ngOnInit(): void {
  
  }

  getMockdata() {
    this.mockData = [
      {
        bigFive: "Neuroticism",
        decison: "Menu Item",
        firstInterest: "Blog Post",
        groupId: 27,
        groupName: "Group 1",
        id: 5,
        journeySteps: 5,
        journeyTime: "8 mins",
        purchaseAddCart: null,
        purchaseBuy: "Buy",
        purchaseOwnership: null,
        successRate: 22,
        totalJourneySteps: 5
      },
      {
        bigFive: "Agreableness",
        decison: "Menu Item",
        firstInterest: "Blog Post",
        groupId: 27,
        groupName: "Group 2",
        id: 3,
        journeySteps: 5,
        journeyTime: "8 mins",
        purchaseAddCart: null,
        purchaseBuy: "Buy",
        purchaseOwnership: null,
        successRate: 27,
        totalJourneySteps: 5
      },
      {
        bigFive: "Openness",
        decison: "Menu Item",
        firstInterest: "Blog Post",
        groupId: 27,
        groupName: "Group 3",
        id: 6,
        journeySteps: 5,
        journeyTime: "8 mins",
        purchaseAddCart: null,
        purchaseBuy: "Buy",
        purchaseOwnership: null,
        successRate: 78,
        totalJourneySteps: 5
      },
      {
        bigFive: "Conscientiousness",
        decison: "Menu Item",
        firstInterest: "Blog Post",
        groupId: 27,
        groupName: "Group 6",
        id: 1,
        journeySteps: 3,
        journeyTime: "8 mins",
        purchaseAddCart: null,
        purchaseBuy: "Buy",
        purchaseOwnership: null,
        successRate: 44,
        totalJourneySteps: 5
      },
      {
        bigFive: "Extraversion",
        decison: "Menu Item",
        firstInterest: "Blog Post",
        groupId: 27,
        groupName: "Group 7",
        id: 8,
        journeySteps: 2,
        journeyTime: "8 mins",
        purchaseAddCart: null,
        purchaseBuy: "Buy",
        purchaseOwnership: null,
        successRate: 6,
        totalJourneySteps: 5
      },
      {
        bigFive: "Conscientiousness",
        decison: "Menu Item",
        firstInterest: "Blog Post",
        groupId: 27,
        groupName: "Group 8",
        id: 99,
        journeySteps: 3,
        journeyTime: "8 mins",
        purchaseAddCart: null,
        purchaseBuy: "Buy",
        purchaseOwnership: null,
        successRate: 44,
        totalJourneySteps: 5
      }
    ]
    this.saveAiData = [
      {
        ageGroup: "25 - 34",
        bigFive: "Conscientiousness",
        groupId: 28,
        icon: "30.png",
        isExternalFactor: false,
        motivationToBuy: "Compulsive",
        name: "Group 1",
        persuasiveStratergies: "Liking",
        state: "Alaska",
        successMatch: null
      },
      {
        ageGroup: "25 - 34",
        bigFive: "Extraversion",
        groupId: 28,
        icon: "21.png",
        isExternalFactor: false,
        motivationToBuy: "Compulsive",
        name: "Group 2",
        persuasiveStratergies: "Liking",
        state: "Alaska",
        successMatch: null
      },
      {
        ageGroup: "25 - 34",
        bigFive: "Neuroticism",
        groupId: 28,
        icon: "25.png",
        isExternalFactor: false,
        motivationToBuy: "Compulsive",
        name: "Group 3",
        persuasiveStratergies: "Liking",
        state: "Alaska",
        successMatch: null
      }
    ]
  }

  onUserGroupDelete() {
    this.journeyList.rerender();
  }

}
