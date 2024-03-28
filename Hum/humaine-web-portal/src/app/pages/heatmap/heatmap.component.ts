import { Component, OnInit } from '@angular/core';
import { CONFIGCONSTANTS } from '../../core/_constant/app-constant';
import { Cart, Desktop, Device, IDeviceType, IHeatMapImages } from '../../core/_model/heatMap';
import { IResponse } from '../../core/_model/response';
import { errorHandler, successHandler } from '../../core/_utility/common';
import { ToasterService } from '../../core/_utility/notify.service';
import { HeatmapService } from './heatmap.service';

@Component({
  selector: 'app-heatmap',
  templateUrl: './heatmap.component.html',
  styleUrls: ['./heatmap.component.scss']
})
export class HeatmapComponent implements OnInit {
  zoomData = CONFIGCONSTANTS.HeatMapZoomData;
  deviceType = CONFIGCONSTANTS.DeviceType;
  interactionType = CONFIGCONSTANTS.HeatmapInteractionTYpe;
  selectedDevice = CONFIGCONSTANTS.DeviceType[0].id;
  heatMap: Cart;
  heatMapItem: Desktop = new Desktop();
  SlideIndex: number = 0;
  heatMapImages: IDeviceType = new IDeviceType();
  data: any;
  keys: any = [];
  slideImage: any;
  isImageLoading: boolean = false;
  selectedZoom = 1;
  isLoading = false;
  date = new Date();

  constructor(
    private heatMapService: HeatmapService,
    private toaster: ToasterService
  ) { }

  ngOnInit(): void {
    this.getHeatmaps();
  }

  getHeatmaps() {
    this.heatMapService.getHeatmapImages().subscribe((res: IResponse<IHeatMapImages>) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          if (res.responseData) {
            console.log(res.responseData)
            this.date = res.responseData['createdOn'];
            this.data = res.responseData['pages'];
            this.heatMap = this.getHeatItem(res.responseData['pages']);
            if (this.heatMap) {

              this.heatMapItem = this.heatMap.desktop;
              this.getHeatMapSignedImges(this.heatMapItem)
            }
          }

        }
      );
    },
      (error) => {
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }

  deviceChangeClick(e) {
    this.slideImage = '';
    this.isImageLoading = true;
    this.deviceImageChange(e);
  }

  deviceImageChange(e:any) {
    this.deviceType.forEach(element => {
      if (e['id'] === element.id) {
        e['isChecked'] = true;
        this.selectedDevice = element.id;
      }
      else {
        element['isChecked'] = false;
        if (e['id'] === Device.Desktop) {
          e['image'] = '/assets/img/desktop.svg';
        }
        if (e['id'] === Device.Tablet) {
          e['image'] = '/assets/img/tablet.svg';
        }
        if (e['id'] === Device.Mobile) {
          e['image'] = '/assets/img/mobile.svg'
        }
      }
    });
    setTimeout(() => {
      switch (e['id']) {
        case Device.Desktop:
          this.slideImage = this.heatMapImages.desktop;
          break;
        case Device.Tablet:
          this.slideImage = this.heatMapImages.tablet;
          break;
        case Device.Mobile:
          this.slideImage = this.heatMapImages.mobile;
          break;
      }
      this.isImageLoading = false;
    }, 200);
  }
  getHeatItem(data: IHeatMapImages) {
    this.keys = Object.keys(data);
    return data[this.keys[this.SlideIndex]];
  }

  imageSlide(data) {
    this.SlideIndex++;
    if (this.keys.length === this.SlideIndex)
      this.SlideIndex = 0;
    Object.keys(this.data)
    this.heatMap = this.data[this.keys[this.SlideIndex]];
    this.heatMapItem = this.heatMap.desktop;
    this.getHeatMapSignedImges(this.heatMapItem);
  }

  getHeatMapSignedImges(data: Desktop) {
    this.isImageLoading = true;
    this.slideImage = '';
    this.heatMapService.getHeatmapSigned({
      "category": data.category
    }).subscribe((res: IResponse<IDeviceType>) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          if (res.responseData) {
            this.heatMapImages = res.responseData;
            if (this.heatMapImages) {
              this.slideImage = this.heatMapImages.desktop;
              this.selectedDevice = CONFIGCONSTANTS.DeviceType[0].id;
              this.deviceImageChange(CONFIGCONSTANTS.DeviceType[0]);
              this.isImageLoading = false;
            }
          }
        }

      );
    },
      (error) => {
        errorHandler(this.toaster, error.error, () => { });
        this.isImageLoading = false;
      }
    );
  }

  zoomChange(e) {
    let zoomArr = [0.5, 0.75, 1, 1.25, 1.5];
    let element = document.getElementById('img');
    let value = element.getBoundingClientRect().width / element.offsetWidth;
    let indexofArr = 4;
    let val = this.selectedZoom;
    val = Number(val)
    indexofArr = zoomArr.indexOf(val);
    switch (val) {
      case 0.5:
        element.style['transform'] = `scale(${val})  translate(50%,0)`;
        break;
      case 0.75:
        element.style['transform'] = `scale(${val})  translate(10%,0)`;
        break;
      default:
        element.style['transform'] = `scale(${val})`;
        break;
    }
  }


}
