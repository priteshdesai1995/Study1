import { Injectable } from '@angular/core';
import { DEFAULT_INTERRUPTSOURCES, Idle } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';
import { ToasterService } from '../core/_utility/notify.service';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class IdleServiceService {
  timed: boolean = false;
  idleState = 'Not started';
  timedOut = false;
  lastPing?: Date = null;
  countdown: any;
  object = {
    "onIdleStart" : null,
    "onIdleEnd":null,
    "onTimeout" : null,
    "onTimeoutWarning" : null
  }
  constructor(private idle: Idle, private keepalive: Keepalive, private authService: AuthenticationService, private toaster: ToasterService) { }

  startIdleSErvice() {
   
    if (!(this.idleState === "Not started" && this.authService.isAuthenticated())) { return };      // sets an idle timeout of 5 seconds, for testing purposes.
 
    this.idle.setIdle(1800);
      // sets a timeout period of 5 seconds. after 10 seconds of inactivity, the user will be considered timed out.
      this.idle.setTimeout(5);
      // sets the default interrupts, in this case, things like clicks, scrolls, touches to the document
      this.idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);
     this.object.onIdleEnd = this.idle.onIdleEnd.subscribe(() => this.idleState = 'No longer idle.');
     this.object.onTimeout = this.idle.onTimeout.subscribe(() => {
        this.idleState = 'Timed out!';
        this.timedOut = true;
      });

     this.object.onIdleStart =  this.idle.onIdleStart.subscribe(() => this.idleState = 'You\'ve gone idle!');
     this.object.onTimeoutWarning =  this.idle.onTimeoutWarning.subscribe((countdown) => {
        this.idleState = 'You will timevxc out in ' + countdown + ' seconds!';
        if (countdown == 1) {
          this.timed = true;
          this.stopIdleService();
          this.authService.logout();
          this.toaster.warningMsg("You session is expired ")
        }
      });
      this.reset();
  }

  reset() {
    this.idle.watch();
    this.idleState = 'Started';
    this.timedOut = false;
  }

  stopIdleService() {
    this.idle.stop();
    this.countdown = 0;
    this.timed = false;
    this.timedOut = false;
    this.lastPing = null;
    this.idleState = 'Not started';
    Object.keys(this.object).forEach(element => {
      if (this.object[element]) {
        this.object[element].unsubscribe();
      }
    });
  }
}
