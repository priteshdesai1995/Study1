import { TestBed } from '@angular/core/testing';

import { ViewUserGroupService } from './view-user-group.service';

describe('ViewUserGroupService', () => {
  let service: ViewUserGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ViewUserGroupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
