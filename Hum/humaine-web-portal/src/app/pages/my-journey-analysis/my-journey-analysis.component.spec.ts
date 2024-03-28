import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyJourneyAnalysisComponent } from './my-journey-analysis.component';

describe('MyJourneyAnalysisComponent', () => {
  let component: MyJourneyAnalysisComponent;
  let fixture: ComponentFixture<MyJourneyAnalysisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MyJourneyAnalysisComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MyJourneyAnalysisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
