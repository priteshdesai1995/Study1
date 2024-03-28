import { Component, Inject, OnInit } from '@angular/core';
import { DefaultLayoutComponent } from 'src/app/containers/default-layout/default-layout.component';
import { HomeService } from 'src/app/services/home.service';
import { errorHandler, successHandler } from 'src/app/utility/common';
import { ToasterService } from 'src/app/utility/notify.service';
import { IResponse } from 'src/app/_model/response';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  isPageLoading: boolean = false;
  registeredCustomers: number = 0;
  constructor(@Inject(DefaultLayoutComponent) private layout: DefaultLayoutComponent,
   private homeService: HomeService,  private toaster: ToasterService) { 
    this.layout.isWhiteBackground = true;

  }

  ngOnInit(): void {
    this.getRegisteredCustomers();
  }
  getRegisteredCustomers(){
    this.isPageLoading = true;
    this.homeService.getRegisteredCustomers().subscribe((res: IResponse<number>) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          if (isSuccess) {
            this.isPageLoading = false;
            this.registeredCustomers = res.responseData;
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

}
