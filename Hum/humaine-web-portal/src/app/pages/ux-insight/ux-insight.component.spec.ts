import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UxInsightComponent } from './ux-insight.component';

describe('UxInsightComponent', () => {
  let component: UxInsightComponent;
  let fixture: ComponentFixture<UxInsightComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UxInsightComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UxInsightComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
