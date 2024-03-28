import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateJourneyComponent } from './create-journey.component';

describe('CreateJourneyComponent', () => {
  let component: CreateJourneyComponent;
  let fixture: ComponentFixture<CreateJourneyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateJourneyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateJourneyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
