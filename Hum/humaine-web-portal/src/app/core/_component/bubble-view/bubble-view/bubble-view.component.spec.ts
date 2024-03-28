import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BubbleViewComponent } from './bubble-view.component';

describe('BubbleViewComponent', () => {
  let component: BubbleViewComponent;
  let fixture: ComponentFixture<BubbleViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BubbleViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BubbleViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
