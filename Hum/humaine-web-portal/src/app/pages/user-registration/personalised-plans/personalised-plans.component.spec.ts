import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonalisedPlansComponent } from './personalised-plans.component';

describe('PersonalisedPlansComponent', () => {
  let component: PersonalisedPlansComponent;
  let fixture: ComponentFixture<PersonalisedPlansComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PersonalisedPlansComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PersonalisedPlansComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
