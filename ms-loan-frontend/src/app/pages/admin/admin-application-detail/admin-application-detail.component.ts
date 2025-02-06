import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApplicationService } from '../../../services/application.service';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ApplicationResponseDto } from '../../../dtos/application-response-dto';
import { KeycloakService } from '../../../utils/keycloak/keycloak.service';
import { FileStorageService } from '../../../services/file-storage.service';
import {
  MatSnackBar,
  MatSnackBarConfig,
  MatSnackBarModule,
} from '@angular/material/snack-bar';
import { FileResponseDto } from '../../../dtos/file-response-dto';
import { JudgementService } from '../../../services/judgement.service';
import { JudgementResponseDto } from '../../../dtos/judgement-response-dto';
import { EntryResponseDto } from '../../../dtos/entry-response-dto';
import { EntryService } from '../../../services/entry.service';
import { CommunicationStatus } from '../../../dtos/communitation-status';
import { RepaymentResponseDto } from '../../../dtos/repayment-response-dto';
import { BalanceResponseDto } from '../../../dtos/balance-response-dto';
import { BalanceService } from '../../../services/balance.service';
import { RepaymentService } from '../../../services/repayment.service';
import { OrdinalPipe } from '../../../utils/pipe/ordinal.pipe';

const snackbarConfig: MatSnackBarConfig = {
  duration: 3000,
  horizontalPosition: 'center',
  verticalPosition: 'top',
};

@Component({
  selector: 'app-admin-application-detail',
  templateUrl: './admin-application-detail.component.html',
  styleUrls: ['./admin-application-detail.component.scss'],
  imports: [CommonModule, ReactiveFormsModule, MatSnackBarModule, OrdinalPipe],
})
export class AdminApplicationDetailComponent implements OnInit {
  applicationId!: string;
  application?: ApplicationResponseDto;
  filesInfo?: Array<FileResponseDto>;
  judgement?: JudgementResponseDto;
  entry?: EntryResponseDto;
  repayments?: Array<RepaymentResponseDto>;
  balance?: BalanceResponseDto;
  judgementForm: FormGroup = new FormGroup({});
  entryForm: FormGroup = new FormGroup({});

  constructor(
    private route: ActivatedRoute,
    private applicationService: ApplicationService,
    private balanceService: BalanceService,
    private keycloakService: KeycloakService,
    private fileStorageService: FileStorageService,
    private judgementService: JudgementService,
    private entryService: EntryService,
    private repaymentService: RepaymentService,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.applicationId = this.route.snapshot.paramMap.get('applicationId')!;
    this.getApplication();
    this.initializeForms();
  }

  initializeForms() {
    this.judgementForm = this.formBuilder.group({
      applicationId: [this.applicationId],
      firstname: [this.keycloakService.firstName],
      lastname: [this.keycloakService.lastName],
      approvalAmount: ['', [Validators.required, Validators.min(1)]],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      payDay: [
        '',
        [Validators.required, Validators.min(1), Validators.max(31)],
      ],
      interest: ['', [Validators.required, Validators.min(0)]],
    });

    this.entryForm = this.formBuilder.group({
      entryAmount: ['', [Validators.required, Validators.min(1)]],
    });
  }

  getEntryErrorMessage(controlName: string): string {
    const control = this.judgementForm.get(controlName);

    if (control?.errors) {
      if (control.errors['required']) {
        return `${
          controlName.charAt(0).toUpperCase() + controlName.slice(1)
        } cannot be null or empty.`;
      }
      if (control.errors['min']) {
        switch (controlName) {
          case 'entryAmount':
            return 'Payout Amount must be at least 1.';
          default:
            return 'Invalid format.';
        }
      }
    }

    return '';
  }

  getJudgementErrorMessage(controlName: string): string {
    const control = this.judgementForm.get(controlName);

    if (control?.errors) {
      if (control.errors['required']) {
        return `${
          controlName.charAt(0).toUpperCase() + controlName.slice(1)
        } cannot be null or empty.`;
      }
      if (control.errors['min']) {
        switch (controlName) {
          case 'approvalAmount':
            return 'Approval Amount must be at least 1.';
          case 'payDay':
            return 'Pay Day must be at least 1.';
          case 'interest':
            return 'Interest must be at least 0.';
          default:
            return 'Invalid format.';
        }
      }
      if (control.errors['max']) {
        switch (controlName) {
          case 'payDay':
            return 'PayDay must be at most 31.';
          default:
            return 'Invalid format.';
        }
      }
    }

    return '';
  }

  getApplication() {
    this.applicationService.getApplicationById(+this.applicationId).subscribe({
      next: (res) => {
        console.log('Application fetched:', res);
        this.application = res.data;

        if (this.application?.applicationId) {
          this.getJudgement();
          this.getFilesInfo();
          if (this.application.contractedAt) {
            this.getEntry();
            this.getBalance(this.application.applicationId);
            this.getRepayments(this.application.applicationId);
          }
        }
      },
      error: (res) => console.log(res.error),
    });
  }

  getFilesInfo() {
    if (!this.application?.applicationId) {
      console.error('Application ID is undefined. Cannot upload file.');
      this.snackBar.open(
        'Application ID is missing. Cannot upload file.',
        'Close',
        { ...snackbarConfig }
      );
      return;
    }

    this.fileStorageService
      .getFilesInfo(this.application.applicationId)
      .subscribe({
        next: (res) => {
          this.filesInfo = res.data;
        },
        error: (res) => console.log(res.error),
      });
  }

  downloadFile(fileName?: string) {
    if (!this.checkApplicationAndFile(fileName)) {
      return;
    }

    const applicationId = this.application?.applicationId;

    if (applicationId === undefined) {
      console.error('Application ID is missing. Cannot download file.');
      return;
    }

    this.fileStorageService.getFile(applicationId, fileName!).subscribe({
      next: (blob) => {
        const downloadUrl = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = downloadUrl;
        a.download = fileName!;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
      },
      error: (error) => {
        console.error('File download failed:', error);
        this.snackBar.open(
          'File download failed. Please try again.',
          'Close',
          snackbarConfig
        );
      },
    });
  }

  submitJudgement() {
    if (this.judgementForm.valid) {
      const request = this.getJudgementRequestDto();
      this.judgementService.create(request).subscribe({
        next: (res) => {
          this.judgement = res.data;
          if (this.judgement) {
            this.judgement.communicationStatus =
              CommunicationStatus.JUDGEMENT_CREATED;
          }
          this.judgementForm.patchValue({
            approvalAmount: '',
            startDate: '',
            endDate: '',
            payDay: '',
            interest: '',
          });
          this.judgementForm.markAsPristine();
          this.judgementForm.markAsUntouched();

          this.snackBar.open(
            'Approval amount created.',
            'Close',
            snackbarConfig
          );
        },
        error: (res) => {
          console.error('Create judgement failed.');
          this.snackBar.open(
            'Create judgement failed. Please try again.',
            'Close',
            snackbarConfig
          );
        },
      });
    } else {
      this.judgementForm.markAllAsTouched();
    }
  }

  updateJudgement() {
    if (this.judgementForm.valid) {
      const request = this.getJudgementRequestDto();
      this.judgementService
        .update(+this.judgement!.judgementId!, request)
        .subscribe({
          next: (res) => {
            this.judgement = res.data;
            if (this.judgement) {
              this.judgement.communicationStatus =
                CommunicationStatus.JUDGEMENT_UPDATED;
            }
            this.judgementForm.patchValue({
              approvalAmount: '',
              startDate: '',
              endDate: '',
              payDay: '',
              interest: '',
            });
            this.judgementForm.markAsPristine();
            this.judgementForm.markAsUntouched();

            this.snackBar.open(
              'Approval amount created.',
              'Close',
              snackbarConfig
            );
            this.application!.communicationStatus =
              CommunicationStatus.APPLICATION_GRANT_UPDATED;
          },
          error: (res) => {
            console.error('Create judgement failed.');
            this.snackBar.open(
              'Create judgement failed. Please try again.',
              'Close',
              snackbarConfig
            );
          },
        });
    } else {
      this.judgementForm.markAllAsTouched();
    }
  }

  private getJudgementRequestDto() {
    return {
      applicationId: Number(this.applicationId),
      approvalAmount: parseFloat(
        this.judgementForm.value.approvalAmount.toFixed(2)
      ),
      firstname: String(this.judgementForm.value.firstname),
      lastname: String(this.judgementForm.value.lastname),
      startDate: new Date(this.judgementForm.value.startDate).toISOString(),
      endDate: new Date(this.judgementForm.value.endDate).toISOString(),
      payDay: Number(this.judgementForm.value.payDay),
      interest: parseFloat(this.judgementForm.value.interest.toFixed(2)),
    };
  }

  getJudgement() {
    const applicationId = this.application?.applicationId;

    if (!applicationId) {
      console.error('Application ID is missing. Cannot download file.');
      return;
    }
    this.judgementService.getJudgementOfApplication(applicationId).subscribe({
      next: (res) => {
        this.judgement = res.data;
      },
      error: (res) => {
        console.log(res);
      },
    });
  }

  grant() {
    const judgementId = this.judgement?.judgementId;

    if (!judgementId) {
      console.error('Judgement ID is missing. Cannot download file.');
      return;
    }

    this.judgementService.grant(judgementId).subscribe({
      next: (res) => {
        this.application!.approvalAmount = res.data?.approvalAmount;
        this.application!.communicationStatus =
          CommunicationStatus.APPLICATION_GRANT_UPDATED;
        this.snackBar.open(
          'Approval amount has been granted.',
          'Close',
          snackbarConfig
        );
        if (this.judgement) {
          this.judgement.communicationStatus =
            CommunicationStatus.JUDGEMENT_COMPLETE;
        }
      },
      error: (res) => {
        this.snackBar.open(
          'Failed to grant approval amount.',
          'Close',
          snackbarConfig
        );
      },
    });
  }

  getEntry() {
    const applicationId = this.application?.applicationId;

    if (!applicationId) {
      console.error('Application ID is missing. Cannot download file.');
      return;
    }

    if (!this.application?.contractedAt) {
      return;
    }

    this.entryService.getEntry(applicationId).subscribe({
      next: (res) => (this.entry = res.data),
      error: (res) => console.log(res.error),
    });
  }

  private checkApplicationAndFile(fileName?: string): boolean {
    if (!this.application?.applicationId) {
      console.error('Application ID is undefined.');
      this.snackBar.open(
        'Application ID is missing. Cannot perform action.',
        'Close',
        snackbarConfig
      );
      return false;
    }

    if (!fileName) {
      console.error('Filename is undefined.');
      this.snackBar.open(
        'Filename is missing. Cannot perform action.',
        'Close',
        snackbarConfig
      );
      return false;
    }

    return true;
  }

  createUpdatePayout() {
    if (!this.application?.applicationId) {
      this.snackBar.open(
        'Application ID is missing. Cannot perform action.',
        'Close',
        snackbarConfig
      );
      return;
    }

    if (this.entryForm.valid) {
      console.log('called');
      if (!this.entry) {
        this.entryService
          .createEntry(+this.applicationId, this.entryForm.value)
          .subscribe({
            next: (res) => {
              this.getBalance(+this.applicationId);
              this.entry = res.data;
              if (this.entry) {
                this.entry.communicationStatus =
                  CommunicationStatus.ENTRY_CREATED;
              }
              this.entryForm.reset();
              this.snackBar.open(
                'Payout has been created.',
                'Close',
                snackbarConfig
              );
            },
            error: (res) => {
              this.snackBar.open(
                'Failed to create payout.',
                'Close',
                snackbarConfig
              );
            },
          });
      } else {
        const entryId = this.entry.entryId;
        this.entryService.updateEntry(entryId, this.entryForm.value).subscribe({
          next: (res) => {
            if (res.data) {
              this.entry!.entryAmount = res.data.afterEntryAmount!;
              this.entry!.updatedAt = res.data.updatedAt!;
              this.entry!.updatedBy = res.data.updatedBy!;
            }
            if (this.entry) {
              this.entry.communicationStatus =
                CommunicationStatus.ENTRY_UPDATED;
            }
            this.entryForm.reset();
            this.snackBar.open(
              'Payout has been updated.',
              'Close',
              snackbarConfig
            );
          },
          error: (res) => {
            this.snackBar.open(
              'Failed to update payout.',
              'Close',
              snackbarConfig
            );
          },
        });
      }
    } else {
      this.entryForm.markAllAsTouched();
    }
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

  deleteEntry() {
    if (!this.entry?.entryId) {
      this.snackBar.open(
        'Payout ID is missing. Cannot perform action.',
        'Close',
        snackbarConfig
      );
      return;
    }

    this.entryService.deleteEntry(this.entry.entryId).subscribe({
      next: (res) => {
        this.snackBar.open('Payout has been deleted.', 'Close', snackbarConfig);
        this.entry = undefined;
      },
      error: (res) => {
        this.snackBar.open('Failed to delete payout.', 'Close', snackbarConfig);
      },
    });
  }
}
