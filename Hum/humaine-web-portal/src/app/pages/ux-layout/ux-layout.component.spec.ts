import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UxLayoutComponent } from './ux-layout.component';

describe('UxLayoutComponent', () => {
  let component: UxLayoutComponent;
  let fixture: ComponentFixture<UxLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UxLayoutComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UxLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
