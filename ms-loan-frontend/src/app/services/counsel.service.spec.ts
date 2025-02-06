import { TestBed } from '@angular/core/testing';

import { CounselService } from './counsel.service';

describe('CounselService', () => {
  let service: CounselService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CounselService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
