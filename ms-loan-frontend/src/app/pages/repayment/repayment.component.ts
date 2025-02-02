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

const snackbarConfig: MatSnackBarConfig = {
  duration: 3000,
  horizontalPosition: 'center',
  verticalPosition: 'top',
};

@Component({
  selector: 'app-repayment',
  templateUrl: './repayment.component.html',
  styleUrls: ['./repayment.component.scss'],
  imports: [CommonModule, FormsModule, ReactiveFormsModule, MatSnackBarModule],
})
export class RepaymentComponent implements OnInit {
  application?: ApplicationResponseDto;
  balance?: BalanceResponseDto;
  repayments?: Array<RepaymentResponseDto>;
  repaymentForm: FormGroup = new FormGroup({});

  constructor(
    private applicationService: ApplicationService,
    private balanceService: BalanceService,
    private repaymentService: RepaymentService,
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
        } else {
          console.error('Application data does not contain an applicationId.');
          this.snackBar.open(
            'Application data is missing.',
            'Close',
            snackbarConfig
          );
        }
      },
      error: (error) => {
        console.error('Error fetching application:', error);
        this.snackBar.open(
          'Error fetching application.',
          'Close',
          snackbarConfig
        );
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
        console.error('Error fetching balance:', error);
        this.snackBar.open('Error fetching balance.', 'Close', snackbarConfig);
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
        console.error('Error fetching repayments:', error);
        this.snackBar.open(
          'Error fetching repayment history.',
          'Close',
          snackbarConfig
        );
      },
    });
  }

  onSubmitRepayment(): void {
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
            this.getBalance(this.application!.applicationId!);
            this.getRepayments(this.application!.applicationId!);
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
