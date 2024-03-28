import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewPersonaComponent } from './view-Persona.component';

describe('ViewPersonaComponent', () => {
  let component: ViewPersonaComponent;
  let fixture: ComponentFixture<ViewPersonaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewPersonaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewPersonaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
