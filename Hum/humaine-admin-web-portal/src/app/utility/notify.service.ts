  import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ToasterService {

  constructor(private toastr: ToastrService) { }

  successMsg(data: any, type?: string) {
    this.toastr.success(data);
  }

  errorMsg(data: any, type?: string) {
    this.toastr.error(data)
  }

  infoMsg(data: any, type?: string) {
    this.toastr.error(data)
  }

  warningMsg(data: any, type?: string) {
    this.toastr.info(data);
  }
}