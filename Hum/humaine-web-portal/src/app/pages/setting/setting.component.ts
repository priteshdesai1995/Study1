import { Component, Inject, OnInit } from '@angular/core';
import { DefaultLayoutComponent } from '../../containers/default-layout/default-layout.component';
import { IResponse } from '../../core/_model/response';
import { errorHandler, successHandler } from '../../core/_utility/common';
import { ToasterService } from '../../core/_utility/notify.service';
import { SettingService } from './setting.service';

@Component({
  selector: 'app-setting',
  templateUrl: './setting.component.html',
  styleUrls: ['./setting.component.scss']
})
export class SettingComponent implements OnInit {
  isPageLoading = false;
  api_key: string = '';
  isAPILoading = false;
  isVisible:boolean = false;
  constructor(@Inject(DefaultLayoutComponent) private layout: DefaultLayoutComponent,
    private settingService: SettingService,
    private toaster: ToasterService) {
  }

  panels = [
    {
      active: true,
      name: 'Shopify Integration',
      img: 'assets/Integrations/1156660_ecommerce_logo_shopify_icon.svg',
      childPanel: [
        {
          active: false,
          desc: 'This is panel header 1-1'
        }
      ]
    },
    {
      active: false,
      img: 'assets/Integrations/Magento_Logo.svg',
      name: 'Magento Integration'
    },
    {
      active: false,
      img: 'assets/Integrations/Drupal.svg',
      name: 'Drupal Commerce Integration'
    }
  ];

  ngOnInit(): void {
    this.getAPIKEY();
  }

  /* To copy Text from Textbox */
  copyInputMessage(val) {
    let selBox = document.createElement('textarea');
    selBox.style.position = 'fixed';
    selBox.style.left = '0';
    selBox.style.top = '0';
    selBox.style.opacity = '0';
    selBox.value = val;
    document.body.appendChild(selBox);
    selBox.focus();
    selBox.select();
    document.execCommand('copy');
    document.body.removeChild(selBox);

  }

  ngOnDestroy() {
  }

  getAPIKEY() {
    this.isPageLoading = true;
    this.settingService.getAPIKey().subscribe((res: IResponse<any>) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          if (isSuccess) {
            this.api_key = res.responseData.apiKey
            this.isPageLoading = false;
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

  regenerate() {
    this.isAPILoading = true;
    this.settingService.updateAPIKEY().subscribe((res: IResponse<any>) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          if (isSuccess) {
            this.isAPILoading = false;
            this.isVisible = false
            this.api_key = res.responseData.apiKey
          }
        }
      );
    },
      (error) => {
        this.isAPILoading = false;
        this.isVisible = false
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }

  confirmRegenerate(){
    this.isVisible = true;
  }

  cancelConfirm(){
    this.isVisible = false;
  }
}
