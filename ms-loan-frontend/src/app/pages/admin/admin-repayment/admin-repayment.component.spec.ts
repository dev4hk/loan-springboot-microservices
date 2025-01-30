import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRepaymentComponent } from './admin-repayment.component';

describe('AdminRepaymentComponent', () => {
  let component: AdminRepaymentComponent;
  let fixture: ComponentFixture<AdminRepaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminRepaymentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminRepaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
