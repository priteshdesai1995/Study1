import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestNewJourneyComponent } from './test-new-journey.component';

describe('TestNewJourneyComponent', () => {
  let component: TestNewJourneyComponent;
  let fixture: ComponentFixture<TestNewJourneyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TestNewJourneyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TestNewJourneyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
