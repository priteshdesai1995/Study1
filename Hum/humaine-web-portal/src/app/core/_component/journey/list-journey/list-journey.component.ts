import { MenuLinks } from './../../../_enums/MenuLinks';
import { Component, OnInit, ViewChild } from '@angular/core';
import { URLS } from '../../../_constant/api.config';
import { BigFive, CONFIGCONSTANTS, RouteURLIdentity, SortDirection } from '../../../_constant/app-constant';
import { APIType } from '../../../_model/APIType';
import { DataTableColumn } from '../../../_model/DataTableColumn';
import { DataTableConfig } from '../../../_model/DataTableConfig';
import { ListTableComponent } from '../../list-table/list-table.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-list-journey',
  templateUrl: './list-journey.component.html',
  styleUrls: ['./list-journey.component.scss']
})
export class ListJourneyComponent implements OnInit {
  @ViewChild("journeyList") journeyList: ListTableComponent;
  @ViewChild("savedAIJourneyList") savedAIJourneyList: ListTableComponent;
  detailPath: string = '';
  menuLinks = MenuLinks;
  journeyListConfig: DataTableConfig;
  isMockData: boolean = false;
  mockData = [];
  listURL : string = '';
  isCheckbox: boolean = false;
  savedAiJourneyConfig: DataTableConfig;
  saveAiData = [];
  isShowGenerate: boolean = false;
  isShowSave: boolean = false;
  scrollHeight: string = '720px';
  aiJourney: boolean = false;
  myJourney: boolean = false;
  isDeleteShow: boolean = true;
  deleteAPIURL: string = '';
  deleteMultipleJourneyAPIURL: string = '';
  constructor(private route: ActivatedRoute) {
    this.getConfigData();
    this.journeyListConfig = new DataTableConfig(this.listURL, APIType.GET, this.deleteAPIURL, this.detailPath, this.aiJourney ? "AI Generated Journeys" : '', [
      new DataTableColumn("GROUP NAME", "groupName", false, true, "-", null, true),
      new DataTableColumn("BIG FIVE", "bigFive"),
      new DataTableColumn("JOURNEY STEPS", "journeySteps", false, true, '-', null, false, null, true),
      new DataTableColumn("JOURNEY TIME	", "journeyTime",false, true,'-','mins'),
      new DataTableColumn("JOURNEY SUCCESS", "successRate", false, true, '-', '%'),
    ], [
      ...CONFIGCONSTANTS.sortByList,
      { name: "Journey Steps", value: BigFive.JOURNEY_STEPS },
      { name: "Journey Time", value: BigFive.JOURNEY_TIME },
      { name: "Journey Success", value: BigFive.JOURNEY_SUCCESS }
    ], "Delete Journey?", "Do you want to delete this Journey?", "Yes, Delete This Journey", this.myJourney ? '':'id', this.myJourney ? null :SortDirection.DESC, this.isMockData, false, true , this.aiJourney ? true : false, this.aiJourney ? true : false, true, this.scrollHeight,"850px", true, true,false, !this.aiJourney ? this.deleteMultipleJourneyAPIURL : '','','Do you want to delete selected Journey?','Yes, Delete selected Journey');

    this.savedAiJourneyConfig = new DataTableConfig('', APIType.GET, URLS.deleteUser, this.detailPath, 'Saved AI Generated Journeys', [
      new DataTableColumn("", "icon", true),
      new DataTableColumn("GROUP NAME", "name"),
      new DataTableColumn("BIG FIVE", "bigFive"),
      new DataTableColumn("SUCCESS MATCH", "successMatch", false, true, '-', '%'),

    ], [...CONFIGCONSTANTS.sortByList], "Delete Journey?", "Do you want to delete this journey?", "Yes, Delete This Journey", 'groupId', SortDirection.DESC, true,false,false,false,false,false,'850px');
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
    if (this.route.snapshot.data.identity === RouteURLIdentity.AI_GENERATED_JOURNEY) {
      this.aiJourney = true;
      this.isShowGenerate = true;
      this.isShowSave = true;
      this.scrollHeight = '550px';
    } else if(this.route.snapshot.data.identity === RouteURLIdentity.JOURNEY_ANAYSIS){
      this.myJourney = true;
    }
  }

  ngOnInit(): void {
    if(this.aiJourney){
      this.isDeleteShow = false;
    }
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

  onUserGroupSave() {
    this.savedAIJourneyList.rerender();

  }

  onUserGroupDelete() {
    this.journeyList.rerender();
  }
}