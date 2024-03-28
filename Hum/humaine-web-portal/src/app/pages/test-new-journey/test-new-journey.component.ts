import { AuthenticationService } from './../../services/authentication.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-test-new-journey',
  templateUrl: './test-new-journey.component.html',
  styleUrls: ['./test-new-journey.component.scss']
})
export class TestNewJourneyComponent implements OnInit {

  constructor(private router:Router, private auth: AuthenticationService) { }

  ngOnInit(): void {
    
  }
  get data() {
    return this.auth.getStoreData();
  }
}
