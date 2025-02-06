import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TermsAgreementDialogComponent } from './terms-agreement-dialog.component';

describe('TermsAgreementDialogComponent', () => {
  let component: TermsAgreementDialogComponent;
  let fixture: ComponentFixture<TermsAgreementDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TermsAgreementDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TermsAgreementDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
