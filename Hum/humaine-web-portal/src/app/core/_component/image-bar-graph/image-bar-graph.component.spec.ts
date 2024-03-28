import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageBarGraphComponent } from './image-bar-graph.component';

describe('ImageBarGraphComponent', () => {
  let component: ImageBarGraphComponent;
  let fixture: ComponentFixture<ImageBarGraphComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImageBarGraphComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImageBarGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
