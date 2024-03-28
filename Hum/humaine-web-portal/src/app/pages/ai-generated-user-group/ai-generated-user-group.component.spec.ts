import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AIGeneratedUserGroupComponent } from './ai-generated-user-group.component';

describe('AIGeneratedUserGroupComponent', () => {
  let component: AIGeneratedUserGroupComponent;
  let fixture: ComponentFixture<AIGeneratedUserGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AIGeneratedUserGroupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AIGeneratedUserGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
