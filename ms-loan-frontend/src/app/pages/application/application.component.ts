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

const snackbarConfig: MatSnackBarConfig = {
  duration: 3000,
  horizontalPosition: 'center',
  verticalPosition: 'top',
};

@Component({
  selector: 'app-application',
  imports: [ReactiveFormsModule, CommonModule, MatSnackBarModule, RouterModule],
  templateUrl: './application.component.html',
  styleUrl: './application.component.scss',
})
export class ApplicationComponent implements OnInit {
  form: FormGroup = new FormGroup({});
  application?: ApplicationResponseDto;

  constructor(
    private formBuilder: FormBuilder,
    private applicationService: ApplicationService,
    private snackBar: MatSnackBar,
    private keycloakService: KeycloakService
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
    console.log('Called');
    this.applicationService.getApplicationByEmail().subscribe({
      next: (res) => {
        console.log(res);
        this.application = res.data;
      },
      error: (res) => console.log(res.error),
    });
  }
}
