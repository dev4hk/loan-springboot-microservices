import { TestBed } from '@angular/core/testing';

import { RepaymentService } from './repayment.service';

describe('RepaymentService', () => {
  let service: RepaymentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RepaymentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
