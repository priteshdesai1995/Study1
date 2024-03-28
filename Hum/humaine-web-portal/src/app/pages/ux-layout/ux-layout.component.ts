import { Component, OnInit } from '@angular/core';
import { CONFIGCONSTANTS } from '../../core/_constant/app-constant';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-ux-layout',
  templateUrl: './ux-layout.component.html',
  styleUrls: ['./ux-layout.component.scss']
})
export class UxLayoutComponent implements OnInit {
  accountDetails:any;
  bigFiveList = CONFIGCONSTANTS.bigFiveFilter;
  bigFiveImage : string = '/assets/img/Agreeableness-wireframe.svg';
  selectedLayout = CONFIGCONSTANTS.bigFiveFilter[0].value;
  isImageLoading:boolean = false;

  constructor(private authService :AuthenticationService) { }

  ngOnInit(): void {
    this.getAccounData()
  }

  getAccounData() {
    let localData = JSON.parse(this.authService.getDecryptedString(localStorage.getItem('user')));
    this.accountDetails = localData.account;
    console.log(this.accountDetails)
  }

  bigFiveChange(val:any){   
    this.bigFiveImage = "";
    this.isImageLoading = true;
    setTimeout(() => {
      this.isImageLoading = false;
      this.bigFiveImage = val.img;
    }, 150);
  }
}
