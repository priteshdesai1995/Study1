import { Component, HostListener, OnInit } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { Router } from '@angular/router';
import { CONFIGCONSTANTS } from 'src/app/_constant/app.constant';
import { isEmpty } from 'src/app/utility/common';

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

  }

  public isRouteActive(url: string, exactMatch = true): boolean {
    return this.router.isActive(url, exactMatch);
  }

  logout() {
    this.authService.logout();
  }

  onRouteClick(submenuData: {}, link: string) {
    this.router.navigateByUrl(link);
  }
}
