import { Component, Inject, OnInit } from '@angular/core';
import { HomeService } from './home.service';
import { ToasterService } from '../../core/_utility/notify.service';
import { DefaultLayoutComponent } from '../../containers/default-layout/default-layout.component';
import { IResponse } from '../../core/_model/response';
import { IDashboard } from '../../core/_model/dashboard';
import { errorHandler, successHandler } from '../../core/_utility/common';
import { DataTableConfig } from '../../core/_model/DataTableConfig';
import { DataTableColumn } from '../../core/_model/DataTableColumn';
import { CONFIGCONSTANTS, SortDirection } from '../../core/_constant/app-constant';

export interface ISuccessJourney {
  monthlyPercentage: number;
  todayPercentage: number;
  backClick : boolean;
  
}
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  providers: [HomeService, ToasterService]

})
export class HomeComponent implements OnInit {
  selectstate ;
  stateOPtion = [
    {name:'Best Performing',value:'popular'},
    {name: 'Least Performing',value:'least'}
  ]
  no = 1234567891011;
  popularProduts : any = [];
  leastStateData : any = [];
  dashboardData: IDashboard = new IDashboard();
  suceesJourneyType = [
    { key: 'TODAY', name: "Today", isChecked: true },
    { key: 'MONTHLY', name: "Monthly", isChecked: false }
  ];
  journeyValue;
  isList: boolean = false;
  isPageLoading: boolean = false;
  journeyData: ISuccessJourney;
  successsJourneyValue;
  successJourneyInitialValue: string ='Today';
  stateData: DataTableConfig = new DataTableConfig('', null, '', '', '', [
    new DataTableColumn("State", "state", false, false, CONFIGCONSTANTS.listTableDefaultValue, null, false, null),
    new DataTableColumn("Sold Amount", "totalSoldAmount", false, true, CONFIGCONSTANTS.listTableDefaultValue),
  ], [], "", "", "", 'bigFive', SortDirection.DESC, true, false, false, false, false, false, '200px', '200px', false, false, false);

  public mapData : any ;
  constructor(@Inject(DefaultLayoutComponent) private layout: DefaultLayoutComponent,
    private homeService: HomeService, private toaster: ToasterService) {
    this.layout.isWhiteBackground = true;
  }

  ngOnInit(): void {
    this.getDashboard();
    this.getDashboardJourney();
  }

  ngOnDestroy() {
    this.layout.isWhiteBackground = false;
  }

  getDashboard() {
    this.isPageLoading = true;
    this.homeService.getDashboard().subscribe((res: IResponse<IDashboard>) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          if (isSuccess) {
            this.isPageLoading = false;
            this.dashboardData = res.responseData;
            this.mapData = this.dashboardData.stateData;
            this.popularProduts = this.dashboardData.popularProducts;
          }
        }
      );
    },
      (error) => {
        this.isPageLoading = false;
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }


  getDashboardJourney() {
    const params = {
      "period": "TODAY"
    };
    this.homeService.getDashboardJourney(params).subscribe((res: IResponse<any>) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          if (isSuccess) {
            this.isPageLoading = false;
            this.journeyData = res.responseData
            this.successsJourneyValue = Math.round(this.journeyData.todayPercentage * 10) / 10;          }
        }
      );
    },
      (error) => {
        this.isPageLoading = false;
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }
  journeyChange(e) {
    if (e === "TODAY") {
      this.successsJourneyValue = Math.round(this.journeyData.todayPercentage * 10) / 10;
    }
    else if (e === "MONTHLY") {
      this.successsJourneyValue = Math.round(this.journeyData.monthlyPercentage * 10) / 10;
    }

    this.suceesJourneyType.find(Item => {
      Item.isChecked = false;
      if (Item.key === e) {
        Item.isChecked = true;
      }
      else Item.isChecked = false;
    });
    }

    monthlyListSwitch(type , data: any){
      this.popularProduts = data;
      var popularProduct =  document.getElementById("most-popular-product-btn");
      var leastPopularProduct =  document.getElementById("least-popular-product-btn");

      var popularProductText =  document.getElementById("popular-item-text");
      var leastPopularProductText =  document.getElementById("unpopular-item-text");


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
    onOptionsSelected(event){
      let val = event;
      if(val ==='popular'){
        this.mapData = this.dashboardData.stateData;
      }
      else if(val === 'least'){
        this.mapData = this.dashboardData.leastStateData;
      } 
    }
}
