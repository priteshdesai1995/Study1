import { TestBed } from '@angular/core/testing';

import { UXInsightService } from './ux-insight.service';

describe('UXInsightService', () => {
  let service: UXInsightService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UXInsightService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
