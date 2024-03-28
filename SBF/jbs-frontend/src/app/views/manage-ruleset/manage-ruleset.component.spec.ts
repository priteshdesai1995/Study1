import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageRulesetComponent } from './manage-ruleset.component';

describe('ManageRulesetComponent', () => {
  let component: ManageRulesetComponent;
  let fixture: ComponentFixture<ManageRulesetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ManageRulesetComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageRulesetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
