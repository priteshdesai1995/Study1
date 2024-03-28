import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CmsPageViewComponent } from './cms-page-view.component';

describe('CmsPageViewComponent', () => {
  let component: CmsPageViewComponent;
  let fixture: ComponentFixture<CmsPageViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CmsPageViewComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CmsPageViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
