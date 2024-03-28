import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSurveyListComponent } from './user-survey-list.component';

describe('UserSurveyListComponent', () => {
  let component: UserSurveyListComponent;
  let fixture: ComponentFixture<UserSurveyListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [UserSurveyListComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserSurveyListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
