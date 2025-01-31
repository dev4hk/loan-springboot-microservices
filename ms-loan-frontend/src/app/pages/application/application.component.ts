import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ApplicationService } from '../../services/application.service';
import { CommonModule } from '@angular/common';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ApplicationResponseDto } from '../../dtos/application-response-dto';
import { KeycloakService } from '../../utils/keycloak/keycloak.service';
import { RouterModule } from '@angular/router';
import { ImageSlideInterface } from '../../components/image-slider/image-slide-interface';
import { ImageSliderComponent } from '../../components/image-slider/image-slider.component';
import { loanProcessSlides } from '../../components/image-slider/loan-process-slides';
import { FileStorageService } from '../../services/file-storage.service';
import { FileResponseDto } from '../../dtos/file-response-dto';

const snackbarConfig: MatSnackBarConfig = {
  duration: 3000,
  horizontalPosition: 'center',
  verticalPosition: 'top',
};

@Component({
  selector: 'app-application',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    MatSnackBarModule,
    RouterModule,
    ImageSliderComponent,
  ],
  templateUrl: './application.component.html',
  styleUrl: './application.component.scss',
})
export class ApplicationComponent implements OnInit {
  form: FormGroup = new FormGroup({});
  application?: ApplicationResponseDto;
  slides: Array<ImageSlideInterface> = loanProcessSlides;
  selectedFile?: File;
  filesInfo?: Array<FileResponseDto>;

  constructor(
    private formBuilder: FormBuilder,
    private applicationService: ApplicationService,
    private snackBar: MatSnackBar,
    private keycloakService: KeycloakService,
    private fileStorageService: FileStorageService
  ) {}

  ngOnInit(): void {
    this.addToggleFunctionality();
    this.form = this.formBuilder.group({
      firstname: [
        this.keycloakService.firstName,
        [Validators.required, Validators.pattern('^[a-zA-Z]+$')],
      ],
      lastname: [
        this.keycloakService.lastName,
        [Validators.required, Validators.pattern('^[a-zA-Z]+$')],
      ],
      cellPhone: [
        this.application?.cellPhone,
        [Validators.required, Validators.pattern('^[0-9]{10}$')],
      ],
      email: [
        this.keycloakService.email,
        [
          Validators.required,
          Validators.email,
          Validators.pattern('^[a-zA-Z0-9._]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$'),
        ],
      ],
      hopeAmount: [
        this.application?.hopeAmount,
        [Validators.required, Validators.pattern('^\\d+(\\.\\d{1,2})?$')],
      ],
    });
    this.getApplication();
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

  getErrorMessage(controlName: string): string {
    const control = this.form.get(controlName);

    if (control?.errors) {
      if (control.errors['required']) {
        return 'This field is required.';
      }
      if (control.errors['pattern']) {
        return 'Invalid format.';
      }
      if (control.errors['email']) {
        return 'Invalid email format.';
      }
    }

    return '';
  }

  onSubmit() {
    if (this.form.valid) {
      this.applicationService.createApplication(this.form.value).subscribe({
        next: (res) => {
          this.form.reset();
          this.snackBar.open('Application submitted successfully!', 'Close', {
            ...snackbarConfig,
          });
          this.application = res.data;
        },
        error: (res) => {
          this.snackBar.open(
            'Application submission failed. Please try again.',
            'Close',
            {
              ...snackbarConfig,
            }
          );
        },
      });
    } else {
      this.form.markAllAsTouched();
    }
  }

  getApplication() {
    console.log('Fetching application...');
    this.applicationService.getApplicationByEmail().subscribe({
      next: (res) => {
        console.log('Application fetched:', res);
        this.application = res.data;

        if (this.application?.applicationId) {
          this.getFilesInfo();
        }
      },
      error: (error) => {
        console.error('Error fetching application:', error);
      },
    });
  }

  onFileSelected(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
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
        next: (res) => (this.filesInfo = res.data),
        error: (res) => console.log(res.error),
      });
  }

  uploadFile() {
    if (!this.application?.applicationId) {
      console.error('Application ID is undefined. Cannot upload file.');
      this.snackBar.open(
        'Application ID is missing. Cannot upload file.',
        'Close',
        { ...snackbarConfig }
      );
      return;
    }

    if (!this.selectedFile) {
      console.error('No file selected.');
      this.snackBar.open('Please select a file before uploading.', 'Close', {
        ...snackbarConfig,
      });
      return;
    }

    this.fileStorageService
      .uploadFile(this.application.applicationId, this.selectedFile)
      .subscribe({
        next: () => {
          this.getFilesInfo();
          this.snackBar.open('File uploaded successfully!', 'Close', {
            ...snackbarConfig,
          });
        },
        error: (error) => {
          console.error('File upload failed:', error);
          this.snackBar.open('File upload failed. Please try again.', 'Close', {
            ...snackbarConfig,
          });
        },
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

  deleteFile(fileName?: string) {
    if (!this.checkApplicationAndFile(fileName)) {
      return;
    }

    const applicationId = this.application?.applicationId;

    if (applicationId === undefined) {
      console.error('Application ID is missing. Cannot delete file.');
      this.snackBar.open(
        'Application ID is missing. Cannot delete file.',
        'Close',
        snackbarConfig
      );
      return;
    }

    this.fileStorageService.deleteFile(applicationId, fileName!).subscribe({
      next: (res) => {
        this.filesInfo = this.filesInfo?.filter(
          (file) => file.name !== fileName
        );
        this.snackBar.open(
          'File deleted successfully!',
          'Close',
          snackbarConfig
        );
      },
      error: (error) => {
        console.error('File deletion failed:', error);
        this.snackBar.open('File deletion failed. Please try again.', 'Close', {
          ...snackbarConfig,
        });
      },
    });
  }
}
