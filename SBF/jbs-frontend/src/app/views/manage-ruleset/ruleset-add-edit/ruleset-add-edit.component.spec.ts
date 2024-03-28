import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RulesetAddEditComponent } from './ruleset-add-edit.component';

describe('RulesetAddEditComponent', () => {
  let component: RulesetAddEditComponent;
  let fixture: ComponentFixture<RulesetAddEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RulesetAddEditComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RulesetAddEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
