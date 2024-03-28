import { Component, OnInit, Renderer2, ViewChild, ElementRef, ViewEncapsulation, Input, HostListener } from '@angular/core';
import { UsaStateMapObject, USAStates, Pin } from './map-config';
import * as _ from 'lodash';
import { formatNumber, isEmpty, successHandler, errorHandler } from '../../_utility/common';
import { couldStartTrivia } from 'typescript';
import { IDashboard, LeastStateData, StateDatum } from '../../_model/dashboard';
import { Observable, of } from 'rxjs';
import { CONFIGCONSTANTS, SortDirection } from '../../_constant/app-constant';
import { DataTableColumn } from '../../_model/DataTableColumn';
import { DataTableConfig } from '../../_model/DataTableConfig';
import { StateInsight } from '../../_model/StateInsights';
import { MapService } from './../map/map.service';
import { ToasterService } from '../../_utility/notify.service';
import { StateName } from '../../_model/dashboard';
import { IResponse } from '../../_model/response';
import { map } from 'd3';

export interface ISuccessJourney {
  monthlyPercentage: number;
  todayPercentage: number;
}

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class MapComponent implements OnInit {
  readonly dotRadius = 3;
  stateDataTable: DataTableConfig;
  apiCallInProcess = false;
  isVisible:boolean = false;
  isPageLoading : boolean = false;
  defaultImageURL="../../assets/img/not_found_img.png"

  dashboardData: IDashboard = new IDashboard();
  graphData = [];
  @ViewChild('container') containerEle: ElementRef;
  @ViewChild('svgContainer') svgMap: ElementRef;
  @Input() scale = 1.1;
  @Input() smallMap: boolean = false;
  @Input() set data(dataArr: any[]) {
    if (!dataArr) dataArr = [];
    this.graphData = dataArr;
    this._data = [];
    this._data = dataArr.filter(i => i.state != null).map(e => new Pin(this.usaStatesIds[this.getStateFromString(e.state)], e.totalSoldAmount));
    this.addPins(true);

  }
  _data: Pin[] = [];
  stateData: StateDatum[] = [];
  leastStateData : LeastStateData[] = [];
  stateName : StateName;
  getStateFromString(inp: string) {
    return inp.toUpperCase().split(" ").join("");
  }
  readonly states = UsaStateMapObject;
  readonly usaStatesIds = USAStates;
  constructor(private renderer: Renderer2, 
    private mapService : MapService,
    private toaster: ToasterService) { 
  }

  ngOnInit(): void {
    this._data = [];
    this.stateDataTable = new DataTableConfig('', null, '', '', '', [
      new DataTableColumn("State", "state", false, false, CONFIGCONSTANTS.listTableDefaultValue, null, false, null),
      new DataTableColumn("Sold Amount", "totalSoldAmount", false, true, CONFIGCONSTANTS.listTableDefaultValue),
    ], [], "", "", "", 'bigFive', SortDirection.DESC, false, false, false, false, false, false, '200px', '', false, false, false);

  }

  ngAfterViewInit(): void {
  }

  addPins(removeElements = false) {
    if (removeElements === true) this.removeElements();
    this._data.forEach(ele => {
      this.addPin(ele);
    })
    this.checkForOverlap();
  }
  removeElements() {
    const elements = document.querySelectorAll(".hum-graph-pin");
    elements.forEach(e => {
      e.parentNode.removeChild(e);
    });
  }

  onImageError(event) {
    (event.target as HTMLImageElement).src = this.defaultImageURL;
  }

  stateInsightData:StateInsight = new StateInsight();
  argStateName = "";
  popularProduts : any = [];
  stateSuccesssJourneyValue;
  journeyData: ISuccessJourney;
  stateSuceesJourneyType = [
    { key: 'TODAY', name: "Today", isChecked: true },
    { key: 'MONTHLY', name: "Monthly", isChecked: false }
  ];

  getStateInsights(stateName){
    stateName = stateName.replace(/([A-Z])/g, ' $1').trim();    
    let ofIndex = stateName.indexOf("of"); 
    let spaceChar = stateName.charAt(ofIndex + 2);
    if(ofIndex != -1 && spaceChar){
      stateName = stateName.substring(0,ofIndex) + " " + stateName.substring(ofIndex);
    }
    console.log("state name => "+ stateName );
    
    this.apiCallInProcess = true;
    this.isPageLoading = true;
    this.mapService.getStateInsights(stateName).subscribe((res: IResponse<StateInsight>) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          this.apiCallInProcess = false;
          this.isPageLoading = true;
          this.stateName = stateName;
          this.popularProduts = res.responseData.popularProducts;
          this.stateInsightData.popularProducts = res.responseData.popularProducts;
          this.stateInsightData.leastPopularProducts = res.responseData.leastPopularProducts;
          this.stateInsightData.todayPercentage = res.responseData.todayPercentage;
          this.stateInsightData.monthlyPercentage = res.responseData.monthlyPercentage;
          this.stateInsightData.monthlyProductDataByState = res.responseData.monthlyProductDataByState;
          this.journeyData = res.responseData;
          this.stateSuccesssJourneyValue = Math.round(this.journeyData.todayPercentage * 10) / 10; 
          this.isPageLoading = false;
        }
      );
    },
    (error) => {
      console.log("some error happened => "+ error);
      this.apiCallInProcess = false;
      this.isPageLoading = true;
      errorHandler(this.toaster, error.error, () => { });
    }
    );
  }

  journeyChange(e) {
    if (e === "TODAY") {
      this.stateSuccesssJourneyValue = Math.round(this.journeyData.todayPercentage * 10) / 10;
    }
    else if (e === "MONTHLY") {
      this.stateSuccesssJourneyValue = Math.round(this.journeyData.monthlyPercentage * 10) / 10;
    }
  }

  stateMonthlyListSwitch(type, data: any){

      this.popularProduts = data;

      var popularProduct =  document.getElementById("state-most-popular-product-btn");
      var leastPopularProduct =  document.getElementById("state-least-popular-product-btn");

      var popularProductText =  document.getElementById("state-popular-item-text");
      var leastPopularProductText =  document.getElementById("state-unpopular-item-text");      

      if (type == "least") {

        popularProduct.style.background = "#754beb4f";
        leastPopularProduct.style.background = "#754BEB";

        popularProductText.style.color = "black";
        leastPopularProductText.style.color = "white";
        
      }else if (type == "monthly") {
        popularProduct.style.background = "#754BEB";
        leastPopularProduct.style.background = "#754beb4f";
        
        popularProductText.style.color = "white";
        leastPopularProductText.style.color = "black";
      }

  }

  openStateInsights(e:string){
    this.isVisible = true;
    this.argStateName = e;
    this.getStateInsights(this.argStateName);
  }

  handleCancel(){
    // Assigh null to all data when we close the pop up 
    this.stateSuccesssJourneyValue = null;
    this.stateName = null;
    this.popularProduts = null;
    this.stateInsightData.popularProducts = null;
    this.stateInsightData.leastPopularProducts = null;
    this.stateInsightData.todayPercentage = null;
    this.stateInsightData.monthlyPercentage = null;
    this.stateInsightData.monthlyProductDataByState = null;
    this.isVisible = false;
  }


  addPin(data: Pin) {
    var idMapping = _.invert(this.usaStatesIds);
    const stateObj = idMapping[data.state];
    if (stateObj) {
      const text = this.getStateName(data.state);
            
      if (isEmpty(text)) return;
      const path = document.querySelector(`#${data.state}`);
      let { x, y, width, height, top, left } = path.getBoundingClientRect();
      const parentDiv = this.svgMap.nativeElement.getBoundingClientRect();
      let cx = (left - parentDiv.left) + (width / 3);
      let cy = (top - parentDiv.top) + (height / 3);
      let div;
      div = this.renderer.createElement('div');
      this.renderer.setProperty(div, "id", this.getId(data.state));
      this.renderer.setProperty(div, "width", '10px');
      this.renderer.setProperty(div, "height", '10px');
      this.renderer.addClass(div, "hum-graph-pin");
      if (this.smallMap) {
        this.renderer.setProperty(div, "innerHTML", `
          <div class="main-content">
            <div class="out">
            <div class="inner-a">
                <div class="inner-green-circle">
    
                </div>
            </div>
            </div>
            <div class="values">
              <div class="outer-circle">
                  <div class="inner-bend">
                      <div class="inner-green-circle">
          
                      </div>
                      <div class="inner-text">
                          ${formatNumber(data.value, 2)}
                      </div>
                  </div>
              </div>
              <div class="point-text">
                ${text}
              </div>
            </div>
          </div>
      `);
      }
      else {
        let outerCircleDiv = this.renderer.createElement('div');
        this.renderer.setProperty(outerCircleDiv, "id", "statePin");
        this.renderer.addClass(outerCircleDiv, "outer-circle");

        this.renderer.setProperty(outerCircleDiv, "innerHTML", `
          <div id="amount-n-arrow">  
            <div class="arrow-div">
                <p class="insight-text">
                  View Insights
                </p>
                <img class="arrow-icon"src="/assets/icon/gray-play-icon.png">
            </div>
              <div class="inner-bend">
                  <div class="inner-purple-circle">
      
                  </div>
                  <div class="inner-text">
                      $${formatNumber(data.value, 2)}
                  </div>
              </div>
          </div>
      `);
        
      let pointTextDiv = this.renderer.createElement('div');
      this.renderer.setProperty(pointTextDiv, "id", "state-name");
      this.renderer.addClass(pointTextDiv, "point-text");

      this.renderer.setProperty(pointTextDiv, "innerHTML", `
            ${text}
      `);

      this.renderer.appendChild(div, outerCircleDiv);
      this.renderer.appendChild(div, pointTextDiv);

      this.renderer.listen(outerCircleDiv, 'click', ()=>{          
        this.openStateInsights(this.getStateName(data.state));
      });

    }

      this.renderer.setProperty(div, "style", `position: absolute; left:${cx}px;top:${cy}px;`);
      this.renderer.appendChild(this.svgMap.nativeElement, div);
    }
  }
  getId(state: USAStates) {
    return `pin-${state}`;
  }
  getStateName(state: USAStates) {
    const data = _.filter(this.states, (s) => {
      return _.isEqual(state, s.id);
    });
    if (_.size(data) === 0) return null;
    return data[0].name;
  } 

  @HostListener('window:resize', ['$event'])
  onResize(event) { 
    this.addPins(true);
  }
  elementsOverlap(element1: Element, element2: Element) {
    const domRect1 = element1.getBoundingClientRect();
    const domRect2 = element2.getBoundingClientRect();
    return !(
      domRect1.top > domRect2.bottom ||
      domRect1.right < domRect2.left ||
      domRect1.bottom < domRect2.top ||
      domRect1.left > domRect2.right
    );
  }

  checkForOverlap() {
    const elements = document.getElementsByClassName("hum-graph-pin");
    for (let i = 0;i < elements.length; i++) {
      for (let j= i + 1; j < elements.length; j++) {
        console.log(`${elements[i].id}:${elements[j].id}=> ${this.elementsOverlap(elements[i], elements[j])}`)
          if (this.elementsOverlap(elements[i], elements[j])) {
            const domRect1 = elements[i].getBoundingClientRect();
            const domRect2 = elements[j].getBoundingClientRect();
            if (domRect1.left < domRect2.left) {
              this.addClass(elements[i], "override-pull-left");
              this.addClass(elements[j], "override-pull-right");
            } else {
              this.addClass(elements[j], "override-pull-left");
              this.addClass(elements[i], "override-pull-right");
            }
          }
      }
    }
  }
  private addClass = (ele: Element,clss: string) => {
      if (ele.classList.contains("override-pull-left") || ele.classList.contains("override-pull-right")) {
          return;
      }
      this.renderer.addClass(ele, clss);
  }
 
}

