import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ThinBarGraphComponent } from './thin-bar-graph.component';

describe('ThinBarGraphComponent', () => {
  let component: ThinBarGraphComponent;
  let fixture: ComponentFixture<ThinBarGraphComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ThinBarGraphComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ThinBarGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
