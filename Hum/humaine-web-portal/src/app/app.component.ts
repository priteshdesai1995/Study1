import { Component, OnInit } from '@angular/core';
import { IdleServiceService } from './services/idle-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  arr = [5, 6]

  deletearr = [{
    "name": "ww", val: 5,

  },
  {
    "name": "ww", val: 6,

  },
  {
    "name": "ww", val: 4,

  }]
  constructor(private idleSErvice: IdleServiceService) {

  }
  ngOnInit() {
    this.idleSErvice.startIdleSErvice();
  }
}
