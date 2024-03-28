import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductIntelligenceComponent } from './product-intelligence.component';

describe('ProductIntelligenceComponent', () => {
  let component: ProductIntelligenceComponent;
  let fixture: ComponentFixture<ProductIntelligenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductIntelligenceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductIntelligenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
