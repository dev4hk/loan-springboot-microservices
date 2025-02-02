import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApplicationService } from '../../../services/application.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApplicationResponseDto } from '../../../dtos/application-response-dto';
import { KeycloakService } from '../../../utils/keycloak/keycloak.service';
import { FileStorageService } from '../../../services/file-storage.service';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { FileResponseDto } from '../../../dtos/file-response-dto';
import { JudgementRequestDto } from '../../../dtos/judgement-request-dto';
import { JudgementService } from '../../../services/judgement.service';
import { JudgementResponseDto } from '../../../dtos/judgement-response-dto';
import { EntryResponseDto } from '../../../dtos/entry-response-dto';
import { EntryRequestDto } from '../../../dtos/entry-request-dto';
import { EntryService } from '../../../services/entry.service';
import { CommunicationStatus } from '../../../dtos/communitation-status';
import { RepaymentResponseDto } from '../../../dtos/repayment-response-dto';
import { BalanceResponseDto } from '../../../dtos/balance-response-dto';
import { BalanceService } from '../../../services/balance.service';
import { RepaymentService } from '../../../services/repayment.service';

const snackbarConfig: MatSnackBarConfig = {
  duration: 3000,
  horizontalPosition: 'center',
  verticalPosition: 'top',
};

@Component({
  selector: 'app-admin-application-detail',
  templateUrl: './admin-application-detail.component.html',
  styleUrls: ['./admin-application-detail.component.scss'],
  imports: [CommonModule, FormsModule],
})
export class AdminApplicationDetailComponent implements OnInit {
  applicationId!: string;
  application?: ApplicationResponseDto;
  judgeFirstname?: string;
  judgeLastname?: string;
  approvalAmount?: number;
  filesInfo?: Array<FileResponseDto>;
  judgement?: JudgementResponseDto;
  entry?: EntryResponseDto;
  entryAmount?: number;
  repayments?: Array<RepaymentResponseDto>;
  balance?: BalanceResponseDto;

  constructor(
    private route: ActivatedRoute,
    private applicationService: ApplicationService,
    private balanceService: BalanceService,
    private keyclockService: KeycloakService,
    private fileStorageService: FileStorageService,
    private judgementService: JudgementService,
    private entryService: EntryService,
    private repaymentService: RepaymentService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.applicationId = this.route.snapshot.paramMap.get('applicationId')!;
    this.getApplication();
    this.judgeFirstname = this.keyclockService.firstName;
    this.judgeLastname = this.keyclockService.lastName;
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
    if (!this.approvalAmount) {
      console.error('Approval amount is undefined');
      this.snackBar.open(
        'Approval amount is missing. Cannot perform action.',
        'close',
        snackbarConfig
      );
      return;
    }
    const judgementRequest: JudgementRequestDto = {
      applicationId: +this.applicationId,
      approvalAmount: +this.approvalAmount,
      firstname: this.keyclockService.firstName,
      lastname: this.keyclockService.lastName,
    };
    this.judgementService.create(judgementRequest).subscribe({
      next: (res) => {
        this.judgement = res.data;
        this.snackBar.open('Approval amount created.', 'Close', snackbarConfig);
      },
      error: (res) => {
        console.error('Create judgement failed.');
        this.snackBar.open(
          'Create judgement failed. Please try again.',
          'Close',
          snackbarConfig
        );
      },
      complete: () => {
        this.approvalAmount = undefined;
      },
    });
  }

  updateJudgement() {
    if (this.approvalAmount === this.judgement?.approvalAmount) {
      console.error('Approval amount cannot be the same');
      this.snackBar.open(
        'Approval amount cannot be the same as current approval amount.',
        'close',
        snackbarConfig
      );
      return;
    }
    if (!this.approvalAmount) {
      console.error('Approval amount is undefined');
      this.snackBar.open(
        'Approval amount is missing. Cannot perform action.',
        'close',
        snackbarConfig
      );
      return;
    }

    const judgementRequest: JudgementRequestDto = {
      applicationId: +this.applicationId,
      approvalAmount: +this.approvalAmount,
      firstname: this.keyclockService.firstName,
      lastname: this.keyclockService.lastName,
    };
    if (this.judgement?.judgementId) {
      this.judgementService
        .update(+this.judgement?.judgementId, judgementRequest)
        .subscribe({
          next: (res) => {
            this.judgement = res.data;
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
          complete: () => {
            this.approvalAmount = undefined;
          },
        });
    }
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

    if (!this.entryAmount) {
      this.snackBar.open(
        'Payout amount is missing. Cannot perform action.',
        'Close',
        snackbarConfig
      );
      return;
    }

    const request: EntryRequestDto = {
      entryAmount: this.entryAmount,
    };

    if (!this.entry) {
      this.entryService.createEntry(+this.applicationId, request).subscribe({
        next: (res) => {
          this.entry = res.data;
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
        complete: () => {
          this.entryAmount = undefined;
        },
      });
    } else {
      const entryId = this.entry.entryId;
      this.entryService.updateEntry(entryId, request).subscribe({
        next: (res) => {
          if (res.data) {
            this.entry!.entryAmount = res.data.afterEntryAmount!;
            this.entry!.updatedAt = res.data.updatedAt!;
            this.entry!.updatedBy = res.data.updatedBy!;
          }
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
        complete: () => (this.entryAmount = undefined),
      });
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
