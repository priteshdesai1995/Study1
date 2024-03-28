import { getValueByKey } from './../../_utility/common';
import { Component, OnInit, Host, Optional, Inject, Input } from '@angular/core';
import { DefaultLayoutComponent } from '../../../containers/default-layout/default-layout.component';
import { CardConfig } from '../../_model/cardConfig';
import { successHandler, errorHandler } from '../../_utility/common';
import { IResponse } from '../../_model/response';
import { Details, IPersona } from '../../_model/persona';
import { map } from 'rxjs/operators';
import { DataService } from '../../../services/data-service.service';
import { ToasterService } from '../../_utility/notify.service';
import { ActivatedRoute } from '@angular/router';
import { PersonaCardTitle } from '../../_constant/app-constant';
import { stringify } from 'querystring';

@Component({
  selector: 'view-details',
  templateUrl: './view-Persona.component.html',
  styleUrls: ['./view-Persona.component.scss'],
})

export class ViewPersonaComponent implements OnInit {
  getValueByKey = getValueByKey;
  allowDemoGraphicData = false;
  isPageLoading :boolean = false;
  URL: string;
  showMerkindelIntelligence =  false;
  merkindeIntelligenceDetailsURL = null;
  val = 20;
  allowEditGroupName: boolean = false;
  editURL: string;
  editGroupURL:string;
  id: number;
  @Input() withMultipleGroups: boolean = false;
  personaData: IPersona = new IPersona();
  
  popularProducts: any[] = [];

  
  //responseData : any = [];

  isEditing: boolean = false;
  editSaveBtnChange = 'Edit';
  isEditLoader: boolean = false;
  cardData : Details = new Details();

  cardTitles = PersonaCardTitle;
  breadCrumb : string;
  interaction = [
    { name: "Overseeing Builds", value: 40 },
    { name: "Writings Specs", value: 40 },
    { name: "Design Features", value: 40 },
    { name: "Meetings ", value: 40 },
    { name: "User Testing", value: 40 },
  ];
  
  constructor(@Inject(DefaultLayoutComponent) private layout: DefaultLayoutComponent,
    private dataService: DataService,
    private toaster: ToasterService,
    private route: ActivatedRoute) {
    this.layout.isWhiteBackground = true;
    this.id = this.route.snapshot.params['id'];
    if (this.route.snapshot.data.isMockData) {
      this.personaData.ageGroup = "32",
        this.personaData.bigFive = "Neurotics",
        this.personaData.gender = "Female",
        this.personaData.education = 'Education',
        this.personaData.job = "UI | UX Designer",
        this.personaData.state = "California",
        this.personaData.familySize = "0 - 6 >",
        this.personaData.income = "$50k - 100k"
        this.personaData.details.personality.data = ['Phasellus dignissim, tellus in pellentesque mollis, mauris orci dignissim nisl, id gravida nunc enim quis nibh. Maecenas convallis eros a ante dignissim, vitae elementum metus facilisis. Cras in maximus sem. Praesent libero augue, ornare eget quam sed, volutpat suscipit arcu.'],
        this.personaData.details.goals.data = ["Duis porta, ligula rhoncus euismod pretium.", "Pellentesque tincidunt tristique neque, eget venenatis enim gravida quis. Fusce at egestas libero.", "Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean"],
        this.personaData.details.frustrations.data = ["Duis porta, ligula rhoncus euismod pretium.", "Pellentesque tincidunt tristique neque, eget venenatis enim gravida quis. Fusce at egestas libero.", "Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean", "Duis porta, ligula rhoncus euismod pretium.", "Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean"],
       
        this.personaData.details.values.data = ["power",
          "achievement",
          "hedonism",
          "stimulation",
          "self-direction",
          "universalism",
        ],
        this.personaData.details.persuasiveStrategies.data= ["authority",
          "commitment",
          "consensus",
          "liking",
          "reciprocity",
          "scarcity"]
    }
  }

  async ngOnInit() {
    this.getCofigDataFromRoute();
    if (!this.route.snapshot.data.isMockData)
    {
    this.isPageLoading = true;
        const res =  await (Promise as any).allSettled([this.getPersonaDetail(),this.getMostPopulerProducts()]);
        this.isPageLoading = false;
    }

  }

  ngOnDestroy() {
    this.layout.isWhiteBackground = false;
  }

  getCofigDataFromRoute() {
    this.URL = this.route.snapshot.data.apiURL;
    if (this.route.snapshot.data.allowEditGroupName) {
      this.allowEditGroupName = this.route.snapshot.data.allowEditGroupName;
    }
    if (this.route.snapshot.data.editURL) {
      this.editURL = this.route.snapshot.data.editURL;
    }
    if (this.route.snapshot.data.editGroupAPI) {
      this.editGroupURL = this.route.snapshot.data.editGroupAPI;
    }

    if (this.route.snapshot.data.withMultipleGroups) {
      this.withMultipleGroups = this.route.snapshot.data.withMultipleGroups;
    }

    if(this.route.snapshot.data.breadcrumb){
      this.breadCrumb = this.route.snapshot.data.breadcrumb;
    }

    if(this.route.snapshot.data.showMerkindelIntelligence){
      this.showMerkindelIntelligence = this.route.snapshot.data.showMerkindelIntelligence;
    }
    if(this.route.snapshot.data.merkindeIntelligenceDetailsURL){
      this.merkindeIntelligenceDetailsURL = this.route.snapshot.data.merkindeIntelligenceDetailsURL;
    }
    /*if (this.route.snapshot.data.allowDemoGraphicData) {
      this.allowDemoGraphicData = this.route.snapshot.data.allowDemoGraphicData;
    }*/
  }

  getPersonaDetail() {
    return new Promise((resolve, reject) => {
      if (!this.URL) {
        return resolve(null);
      }
      this.dataService.get(this.URL + "/" + this.id).pipe(
        map((data) => {
          return data;
        })
      ).subscribe(
        (res: IResponse<IPersona>) => {
          successHandler(this.toaster, res, null, (isSuccess: boolean) => {
            if (isSuccess) {
              
              this.personaData = res.responseData;
              this.cardData.personality.data = res.responseData.details.personality as any;
              this.cardData.persuasiveStrategies.data = res.responseData.details.persuasiveStrategies as any;
              this.cardData.motivationToBuy.data = res.responseData.details.motivationToBuy as any;
              this.cardData.values.data = res.responseData.details.values as any;
              this.cardData.goals.data = res.responseData.details.goals as any;
              this.cardData.frustrations.data = res.responseData.details.frustrations as any;
              resolve(null);  
            }
          });
        },
        (error) => {
          errorHandler(this.toaster, error.error, () => {
            reject(error.error)
          });
        }
      );  
    });
    
  }

  getMostPopulerProducts() {
    return new Promise((resolve, reject) => { 
      if (!this.showMerkindelIntelligence || !this.merkindeIntelligenceDetailsURL) {
        return resolve(null);
      }
      this.dataService.get(this.merkindeIntelligenceDetailsURL + this.id).pipe(
        map((data) => {
          return data;
        })
      ).subscribe(
        (res: IResponse<IPersona>) => {
          successHandler(this.toaster, res, null, (isSuccess: boolean) => {
            if (isSuccess) {
              this.popularProducts = res.responseData as any;
              resolve(res);
            }
          });
        },
        (error) => {
          errorHandler(this.toaster, error.error, () => {
            reject(error);
          });
        }
      );
    })
  }

  editTextChange(){
    this.editSaveBtnChange = "Save"
    this.isEditing = true;

  }
  editGroupName() {
    this.isEditLoader = true;
    this.editSaveBtnChange = "Save";
    if (!this.editGroupURL) {
      return;
    }
    this.dataService.post(this.editGroupURL + "/" + this.id,{
      name: this.personaData.group.name
    }).pipe(
      map((data) => {
        return data;
      })
    ).subscribe(
      (res: IResponse<IPersona>) => {
        successHandler(this.toaster, res, null, (isSuccess: boolean) => {
          if (isSuccess) {
            this.editSaveBtnChange = "Edit"
            this.isEditing = false;
            this.isEditLoader = false;
          }
        });
      },
      (error) => {
        errorHandler(this.toaster, error.error, () => {
          this.editSaveBtnChange = "Edit";
          this.isEditLoader = false;
        });
      }
    );
  }
  isArray(value: any){
    return Array.isArray(value);
  }
  getData(normalData: any, multipleData: any) {
    if (this.withMultipleGroups && false) {
      return multipleData;
    }
    return normalData;
  }
  getDataFromKey(data: any, key) {
    if (key) {
      return getValueByKey(data, key, null);
    }

    console.log("getPersonaDetails");

    var groupPersonaValue = document.getElementById("group-section-persona-value")  ;
    var agePersonaValue = document.getElementById("age-section-persona-value") ;
    var statePersonaValue = document.getElementById("state-section-persona-value") ;
    var familyPersonaValue = document.getElementById("family-size-section-persona-value") ;
    var genderPersonaValue = document.getElementById("gender-section-persona-value");
    var educationPersonaValue = document.getElementById("education-section-persona-value") ;
    var bigFivePersonaValue = document.getElementById("big-fiv-section-persona-value") ;
    
    var groupDiv = document.getElementById("group-section");
    var ageDiv = document.getElementById("age-section");
    var stateDiv = document.getElementById("state-section");
    var familySizeDiv = document.getElementById("family-size-section");
    var genderDiv = document.getElementById("gender-section");
    var educationDiv = document.getElementById("education-section");
    var bigFiveDiv = document.getElementById("big-fiv-section");
   

    if(groupPersonaValue == null || groupPersonaValue.textContent == "N/A" ){
      groupDiv.style.display = "none";
    }

    if(agePersonaValue == null || agePersonaValue.textContent == "N/A"){
      ageDiv.style.display = "none";
    }
    if(statePersonaValue == null || statePersonaValue.textContent == "N/A"){
      stateDiv.style.display = "none";
    }
    if(familyPersonaValue == null || familyPersonaValue.textContent == "N/A"){
      familySizeDiv.style.display = "none";
    }
    if(genderPersonaValue == null || genderPersonaValue.textContent == "N/A"){
      genderDiv.style.display = "none";
    }
    if(educationPersonaValue == null || educationPersonaValue.textContent == "N/A"){
      educationDiv.style.display = "none";
    }

     if(bigFivePersonaValue == null || bigFivePersonaValue.textContent == "N/A"){
      bigFiveDiv.style.display = "none";
    }
    return data;
  }
  get isMerchendiseIntellVisible() {
    return !this.allowEditGroupName && !this.isEditing
  }
  
}
