import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApdexGraphComponent } from './apdex-graph.component';

describe('ApdexGraphComponent', () => {
  let component: ApdexGraphComponent;
  let fixture: ComponentFixture<ApdexGraphComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApdexGraphComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApdexGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
