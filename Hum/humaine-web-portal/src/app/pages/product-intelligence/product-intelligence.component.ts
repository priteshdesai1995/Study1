import { SortDirection } from './../../core/_constant/app-constant';
import { SortOrder } from './../../core/_model/ProductIntelligenceListRequest';
import { ToasterService } from './../../core/_utility/notify.service';
import { successHandler, errorHandler, processData } from './../../core/_utility/common';
import { IResponse } from './../../core/_model/response';
import { ProductIntelligence } from './../../core/_model/ProductIntelligence';
import { ProductIntelligenceService } from './../../services/product-intelligence.service';
import { Component, OnInit, ViewEncapsulation, HostListener } from '@angular/core';
import { CONFIGCONSTANTS } from '../../core/_constant/app-constant';
@Component({
  selector: 'app-product-intelligence',
  templateUrl: './product-intelligence.component.html',
  styleUrls: ['./product-intelligence.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ProductIntelligenceComponent implements OnInit {
  productFilterBy = CONFIGCONSTANTS.productFilterBy;
  defaultImageURL="../../assets/img/not_found_img.png"
  selectedFilter = "";
  sortField="totalQty";
  bigFiveKeys = CONFIGCONSTANTS.ProductIntelligenceGroupConfig;
  sortDirection=SortOrder.DESC;
  currentPage = 0;
  apiCallInProcess = false;
  readonly size = 8;
  sortMatch = [
    {
      id: "DESC",
      name: "Most Successful"
    },
    {
      id: "ASC",
      name: "Least Successful"
    }
  ]
  data:ProductIntelligence = new ProductIntelligence([],-1);
  constructor(private productIntelligenceService: ProductIntelligenceService,
    private toaster: ToasterService) { }

  ngOnInit(): void {
    this.getProductList();
  }

  getProductList() {
    this.getProductData(this.currentPage, this.size);
  }  

  onSortChangeApiCall() {
    this.getProductData(0, this.size, true);
  }  
  onChange() {
    this.getProductData(this.currentPage, this.size, true);
  }

  getProductData(page, size, clearBeforeAssign= false) {
    // if (this.apiCallInProcess === true || (clearBeforeAssign === false && this.checkIfAPiCallEnds() === true)) return;
    if (clearBeforeAssign === true) {
      this.currentPage = 0;
    }
    this.apiCallInProcess = true;
    this.productIntelligenceService.getProuductList(page, size, this.sortField,this.selectedFilter, this.sortDirection === "DESC").subscribe((res: IResponse<ProductIntelligence>) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          this.apiCallInProcess = false;
          this.data.products = [];
          this.data.totalCount = res.responseData.totalCount;
          Array.prototype.push.apply(this.data.products, res.responseData.products)
        }
      );
    },
      (error) => {
        errorHandler(this.toaster, error.error, () => { 
          console.log("image fail to load");
          
          this.apiCallInProcess = false;
        });
      }
    );
  }
  checkIfAPiCallEnds() {
    let result = false;
    if (this.data.totalCount > -1) {
      let totalResultPage = this.data.totalCount/this.size;
      if (totalResultPage < this.currentPage) {
        result = true;
      }
    }
    return result;
  }
  get getPage() {
    return this.currentPage+1;
  }
  get getTotalCount() {
    if (this.data.totalCount <= 0) return 0;
    return this.data.totalCount;
  }
  onPaginationChange(event) {
    this.currentPage = event-1;
    this.getProductList();
  }
  onImageError(event) {
    (event.target as HTMLImageElement).src = this.defaultImageURL;
  }
}
