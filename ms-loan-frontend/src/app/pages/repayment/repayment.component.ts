import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ApplicationService } from '../../services/application.service';
import { BalanceService } from '../../services/balance.service';
import { RepaymentService } from '../../services/repayment.service';
import { ApplicationResponseDto } from '../../dtos/application-response-dto';
import { BalanceResponseDto } from '../../dtos/balance-response-dto';
import { RepaymentResponseDto } from '../../dtos/repayment-response-dto';
import { RepaymentRequestDto } from '../../dtos/repayment-request-dto';
import { CommonModule } from '@angular/common';
import {
  MatSnackBar,
  MatSnackBarConfig,
  MatSnackBarModule,
} from '@angular/material/snack-bar';
import { JudgementService } from '../../services/judgement.service';
import { JudgementResponseDto } from '../../dtos/judgement-response-dto';
import { RouterModule } from '@angular/router';

const snackbarConfig: MatSnackBarConfig = {
  duration: 3000,
  horizontalPosition: 'center',
  verticalPosition: 'top',
};

@Component({
  selector: 'app-repayment',
  templateUrl: './repayment.component.html',
  styleUrls: ['./repayment.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatSnackBarModule,
    RouterModule,
  ],
})
export class RepaymentComponent implements OnInit {
  application?: ApplicationResponseDto;
  balance?: BalanceResponseDto;
  judgement?: JudgementResponseDto;
  repayments?: Array<RepaymentResponseDto>;
  repaymentForm: FormGroup = new FormGroup({});

  constructor(
    private applicationService: ApplicationService,
    private balanceService: BalanceService,
    private repaymentService: RepaymentService,
    private judgementService: JudgementService,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.addToggleFunctionality();
    this.initializeRepaymentForm();
    this.getApplication();
  }

  initializeRepaymentForm(): void {
    this.repaymentForm = this.formBuilder.group({
      repaymentAmount: [null, [Validators.required, Validators.min(0.01)]],
    });
  }

  getApplication(): void {
    console.log('Fetching application...');
    this.applicationService.getApplicationByEmail().subscribe({
      next: (res) => {
        console.log('Application fetched:', res);
        this.application = res.data;
        if (this.application && this.application.applicationId) {
          const applicationId = this.application.applicationId;
          this.getBalance(applicationId);
          this.getRepayments(applicationId);
          this.getJudgement(applicationId);
        } else {
          console.log('Application data does not contain an applicationId.');
        }
      },
      error: (error) => {
        console.log('Error fetching application:', error);
      },
    });
  }

  getJudgement(applicationId: number) {
    console.log('Fetching judgement...');
    this.judgementService.getJudgementOfApplication(applicationId).subscribe({
      next: (res) => {
        console.log('Judgement fetched:', res);
        this.judgement = res.data;
      },
      error: (error) => {
        console.log('Error fetching judgement:', error);
      },
    });
  }

  getBalance(applicationId: number): void {
    console.log('Fetching balance...');
    this.balanceService.get(applicationId).subscribe({
      next: (res) => {
        console.log('Balance fetched:', res);
        this.balance = res.data;
      },
      error: (error) => {
        console.log('Error fetching balance:', error);
      },
    });
  }

  getRepayments(applicationId: number): void {
    console.log('Fetching repayments...');
    this.repaymentService.getRepayments(applicationId).subscribe({
      next: (res) => {
        console.log('Repayments fetched:', res);
        this.repayments = res.data;
      },
      error: (error) => {
        console.log('Error fetching repayments:', error);
      },
    });
  }

  getRepaymentErrorMessage(controlName: string): string {
    const control = this.repaymentForm.get(controlName);

    if (control?.errors) {
      if (control.errors['required']) {
        return `${
          controlName.charAt(0).toUpperCase() + controlName.slice(1)
        } cannot be null or empty.`;
      }
      if (control.errors['min']) {
        switch (controlName) {
          case 'repaymentAmount':
            return 'Repayment Amount must be at least 1.';
          default:
            return 'Invalid format.';
        }
      }
    }

    return '';
  }

  onSubmitRepayment(): void {
    console.log('clicked');
    if (this.repaymentForm.valid && this.application?.applicationId) {
      const repaymentRequest: RepaymentRequestDto = {
        repaymentAmount: this.repaymentForm.value.repaymentAmount,
      };

      this.repaymentService
        .createRepayment(this.application.applicationId, repaymentRequest)
        .subscribe({
          next: (res) => {
            console.log('Repayment submitted:', res);
            this.snackBar.open(
              'Repayment submitted successfully!',
              'Close',
              snackbarConfig
            );
            this.repaymentForm.reset();
            this.balance!.balance = res.data?.balance;
            this.getRepayments(this.application!.applicationId!);
            this.repayments?.push(res.data!);
          },
          error: (error) => {
            console.error('Error submitting repayment:', error);
            this.snackBar.open(
              'Error submitting repayment. Please try again.',
              'Close',
              snackbarConfig
            );
          },
        });
    } else {
      this.repaymentForm.markAllAsTouched();
      this.snackBar.open(
        'Please enter a valid repayment amount.',
        'Close',
        snackbarConfig
      );
    }
  }

  addToggleFunctionality(): void {
    const faqQuestions = document.querySelectorAll('.faq-question');
    faqQuestions.forEach((question) => {
      question.addEventListener('click', () => {
        const answer = question.nextElementSibling as HTMLElement;
        if (answer) {
          if (answer.classList.contains('open')) {
            answer.classList.remove('open');
          } else {
            document.querySelectorAll('.faq-answer').forEach((el) => {
              el.classList.remove('open');
            });
            answer.classList.add('open');
          }
        }
      });
    });
  }
}
