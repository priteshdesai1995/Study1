import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { filter, map, mergeMap, first } from 'rxjs/operators';
import { CONFIGCONSTANTS } from './config/app-constants';
import { Title } from '@angular/platform-browser';
import { SettingsService } from './_services/settings-service';

@Component({
  // tslint:disable-next-line
  selector: 'body',
  template: '<router-outlet></router-outlet>',
})
export class AppComponent implements OnInit {
  appTitle: string = CONFIGCONSTANTS.siteName;
  fullTitle: string;
  constructor(
    private router: Router,
    private titleService: Title,
    private activatedRoute: ActivatedRoute,
    private settingsservice: SettingsService
  ) {}

  ngOnInit() {
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
        return;
      }
      window.scrollTo(0, 0);
    });
    // this.getImageData();
    // set dynamic html title
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        map(() => {
          let route = this.activatedRoute.firstChild;
          let child = route;
          while (child) {
            if (child.firstChild) {
              child = child.firstChild;
              route = child;
            } else {
              child = null;
            }
          }
          return route;
        }),
        mergeMap((route) => route.data)
      )
      .subscribe((data) => {
        if (data.title) {
          this.fullTitle = this.appTitle + ' | ' + data.title;
        } else {
          this.fullTitle = this.appTitle;
        }
        this.titleService.setTitle(this.fullTitle);
      });
  }
  getImageData() {
    this.settingsservice
      .getSettingsImageDataURL()
      .pipe(first())
      .subscribe((data: any) => {
        let logoURL, faviconURL;
        this.appTitle = data.data.site_name ? data.data.site_name : CONFIGCONSTANTS.siteName;
        if (data.data.logo) {
          logoURL = data.data.logo;
        }
        if (data.data.favicon32) {
          faviconURL = data.data.favicon32;
          this.settingsservice.changeFavicon(faviconURL);
        }
        this.settingsservice.setSettingsData(this.appTitle, logoURL, faviconURL);
      });
  }
}
