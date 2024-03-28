import { isEmpty } from './../../core/_utility/common';
import { Component, HostListener, OnInit } from '@angular/core';
import { CONFIGCONSTANTS } from '../../core/_constant/app-constant';
import { AuthenticationService } from '../../services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-default-layout',
  templateUrl: './default-layout.component.html',
  styleUrls: ['./default-layout.component.scss'],

})
export class DefaultLayoutComponent implements OnInit {
  isCollapsed = false;
  overlay = false;
  menus = CONFIGCONSTANTS.sideMenus;
  accountDetails: any;
  selectedMenu: string = '';
  isWhiteBackground: boolean = false;
  scrHeight: any;
  scrWidth: any;

  @HostListener('window:resize', ['$event'])
  getScreenSize(event?) {
    this.scrHeight = window.innerHeight;
    this.scrWidth = window.innerWidth;
    this.isCollapsed = false;
    this.isCollapsed = true;
    if (this.scrWidth < 100) {
    }
    if (this.scrWidth > 1020) {
      this.isCollapsed = false;
    }

  }
  @HostListener("click")
  clicked() {
    // if(this.isCollapsed == false){
    //   this.isCollapsed = true;

    // }
  }
  constructor(public authService: AuthenticationService,
    private router: Router) {
    this.getScreenSize();

  }

  ngOnInit(): void {
    this.getAccounData();

  }

  public isRouteActive(url: string, exactMatch = true): boolean {
    return this.router.isActive(url, exactMatch);
  }

  logout() {
    this.authService.logout();
  }

  getAccounData() {
    let localData = JSON.parse(this.authService.getDecryptedString(localStorage.getItem('user')));
    this.accountDetails = localData.account;
  }

  onRouteClick(submenuData: {}, link: string) {
    if (isEmpty(submenuData) && isEmpty(link))
      return

    if (!isEmpty(submenuData) && submenuData['countBasedRedirection'] === true && !isEmpty(submenuData['menuKey']) && !isEmpty(submenuData['createURL']) && !isEmpty(submenuData['listURL'])) {
      const count = this.authService.getMenuCount(submenuData['menuKey']);
      if (count > 0) {
        this.router.navigateByUrl(submenuData['listURL']);
        return;
      }
      this.router.navigateByUrl(submenuData['createURL']);
      return;
    }
    this.router.navigateByUrl(link);
  }
}
