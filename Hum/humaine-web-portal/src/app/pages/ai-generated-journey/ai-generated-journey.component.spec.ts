import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AIGeneratedJourneyComponent } from './ai-generated-journey.component';

describe('AIGeneratedJourneyComponent', () => {
  let component: AIGeneratedJourneyComponent;
  let fixture: ComponentFixture<AIGeneratedJourneyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AIGeneratedJourneyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AIGeneratedJourneyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
