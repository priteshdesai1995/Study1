import { Component, Inject, OnInit } from '@angular/core';
import { DefaultLayoutComponent } from '../../containers/default-layout/default-layout.component';
import { URLS } from '../../core/_constant/api.config';
import { ChartType, CONFIGCONSTANTS, SortDirection } from '../../core/_constant/app-constant';
import { APIType } from '../../core/_model/APIType';
import { DataTableColumn } from '../../core/_model/DataTableColumn';
import { DataTableConfig } from '../../core/_model/DataTableConfig';
import { IResponse } from '../../core/_model/response';
import { IUXInsight, IUXInsightProduct } from '../../core/_model/UXInsight';
import { errorHandler, successHandler } from '../../core/_utility/common';
import { ToasterService } from '../../core/_utility/notify.service';
import { UXInsightService } from './ux-insight.service';

@Component({
  selector: 'app-ux-insight',
  templateUrl: './ux-insight.component.html',
  styleUrls: ['./ux-insight.component.scss']
})
export class UxInsightComponent implements OnInit {
  chartTypes = ChartType;
  UXInsightData: IUXInsight = new IUXInsight();
  isPageLoading: boolean = false;
  UXInsightProduct: IUXInsightProduct = new IUXInsightProduct();
  purchasProduct: DataTableConfig;
  productViewed: DataTableConfig;
  viewedCategory: DataTableConfig;

  constructor(@Inject(DefaultLayoutComponent) private layout: DefaultLayoutComponent,
    private uxService: UXInsightService,
    private toaster: ToasterService
  ) {
    this.layout.isWhiteBackground = true;
  }

  ngOnInit(): void {
    this.getUXInsight();
    this.getUXInsightProduct();
    this.setDataConfiguration();
  }

  setDataConfiguration() {
    this.purchasProduct = new DataTableConfig('', APIType.GET, null, "", "Most Purchased 5 Products (Since Last 24 Hours)", [
      new DataTableColumn("", "image", true, false),
      new DataTableColumn("TITLE", "productName", false, false, '-', '', false, null, true),
      new DataTableColumn("NO OF CLICKS", "clickCount"),
      new DataTableColumn("NO OF PURCHASE", "purchasedCount")
    ], [], "", "", "", '', SortDirection.DESC, true, false, false, false, false, false, "", '400px', false, false, false);

    this.productViewed = new DataTableConfig('', APIType.GET, null, "", " Most Viewed 5 Products (Since Last 24 Hours)", [
      new DataTableColumn("", "image", true, false),
      new DataTableColumn("TITLE", "productName", false, false, '-', '', false, null, true),
      new DataTableColumn("NO OF CLICKS", "clickCount"),
      new DataTableColumn("NO OF PURCHASE", "purchaseCount")
    ], null, "", "", "", '', SortDirection.DESC, true, false, false, false, false, false, "", '400px', false, false, false);

    this.viewedCategory = new DataTableConfig('', APIType.GET, null, "", "Most Viewed 5 Category (Since Last 24 Hours)", [
      new DataTableColumn("TITLE", "category", false, false, '-', '', false, null, true),
      new DataTableColumn("NO OF CLICKS", "clickCount"),
    ], null, "", "", "", '', SortDirection.DESC, true, false, false, false, false, false, "", '400px', false, false, false);
  }

  ngOnDestroy() {
    this.layout.isWhiteBackground = false;
  }

  getUXInsight() {
    this.uxService.getUXInsight().subscribe((res: IResponse<IUXInsight>) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          if (isSuccess) {
            // this.isPageLoading = false;
            this.UXInsightData = res.responseData
          }
        }
      );
    },
      (error) => {
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }

  getUXInsightProduct() {
    this.isPageLoading = true;
    this.uxService.getUXInsightProduct().subscribe((res: IResponse<IUXInsightProduct>) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          if (isSuccess) {
            this.isPageLoading = false;
            this.UXInsightProduct = res.responseData;
            this.UXInsightProduct.eventCounts = this.getBarProducts(this.UXInsightProduct.eventCounts);
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

  getBarProducts(data) {
    data.forEach(function (item, index) {
      if (item.eventName === 'START' || item.eventName === 'END') {
        data.splice(index, 2);
      }
    });
    return data;
  }
}
