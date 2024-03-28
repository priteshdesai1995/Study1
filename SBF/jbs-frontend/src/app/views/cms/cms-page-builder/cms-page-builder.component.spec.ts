import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CmsPageBuilderComponent } from './cms-page-builder.component';

describe('CmsPageBuilderComponent', () => {
  let component: CmsPageBuilderComponent;
  let fixture: ComponentFixture<CmsPageBuilderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CmsPageBuilderComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CmsPageBuilderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
