import { TestBed } from '@angular/core/testing';

import { ProductIntelligenceService } from './product-intelligence.service';

describe('ProductIntelligenceService', () => {
  let service: ProductIntelligenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductIntelligenceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
