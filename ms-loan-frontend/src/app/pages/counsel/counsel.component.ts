import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  MatSnackBar,
  MatSnackBarConfig,
  MatSnackBarModule,
} from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { CounselService } from '../../services/counsel.service';
import { KeycloakService } from '../../utils/keycloak/keycloak.service';
import { CounselResponseDto } from '../../dtos/counsel-response-dto';

const snackbarConfig: MatSnackBarConfig = {
  duration: 3000,
  horizontalPosition: 'center',
  verticalPosition: 'top',
};

@Component({
  selector: 'app-counsel',
  imports: [ReactiveFormsModule, CommonModule, MatSnackBarModule],
  templateUrl: './counsel.component.html',
  styleUrl: './counsel.component.scss',
})
export class CounselComponent implements OnInit {
  form: FormGroup = new FormGroup({});
  counsel?: CounselResponseDto;
  showMemo = false;
  isUpdate = false;

  constructor(
    private formBuilder: FormBuilder,
    private counselService: CounselService,
    private snackBar: MatSnackBar,
    private keycloakService: KeycloakService
  ) {}

  ngOnInit(): void {
    this.addToggleFunctionality();
    this.initializeForm();
    this.getCounsel().then(() => {
      this.updateFormWithCounselData();
    });
  }
  initializeForm() {
    this.form = this.formBuilder.group({
      firstname: ['', [Validators.required, Validators.pattern('^[a-zA-Z]+$')]],
      lastname: ['', [Validators.required, Validators.pattern('^[a-zA-Z]+$')]],
      cellPhone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      email: [
        '',
        [
          Validators.required,
          Validators.email,
          Validators.pattern('^[a-zA-Z0-9._]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$'),
        ],
      ],
      memo: ['', [Validators.required]],
      address1: ['', [Validators.required]],
      address2: [''],
      city: ['', [Validators.required]],
      state: ['', [Validators.required]],
      zipCode: ['', [Validators.required, Validators.pattern('^[0-9]{5}$')]],
    });
  }

  updateFormWithCounselData() {
    this.form.patchValue({
      firstname: this.keycloakService.firstName,
      lastname: this.keycloakService.lastName,
      cellPhone: this.counsel?.cellPhone?.toString(),
      email: this.keycloakService.email,
      memo: this.counsel?.memo,
      address1: this.counsel?.address1,
      address2: this.counsel?.address2,
      city: this.counsel?.city,
      state: this.counsel?.state,
      zipCode: this.counsel?.zipCode?.toString(),
    });
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

  toggleMemo() {
    this.showMemo = !this.showMemo;
  }

  getErrorMessage(controlName: string): string {
    const control = this.form.get(controlName);

    if (control?.errors) {
      if (control.errors['required']) {
        return `${
          controlName.charAt(0).toUpperCase() + controlName.slice(1)
        } cannot be null or empty.`;
      }
      if (control.errors['pattern']) {
        switch (controlName) {
          case 'firstname':
          case 'lastname':
            return `${
              controlName.charAt(0).toUpperCase() + controlName.slice(1)
            } must contain only letters.`;
          case 'cellPhone':
            return 'Cell phone must be 10 digits.';
          case 'email':
            return 'Invalid email format.';
          case 'zipCode':
            return 'Zip code must be 5 digits.';
          default:
            return 'Invalid format.';
        }
      }
      if (control.errors['email']) {
        return 'Invalid email format.';
      }
    }

    return '';
  }

  onSubmit() {
    if (this.form.valid) {
      if (this.isUpdate && this.counsel) {
        this.counselService
          .updateCounsel(this.counsel.counselId!, this.form.value)
          .subscribe({
            next: (res) => {
              this.form.reset();
              this.snackBar.open('Counsel updated successfully!', 'Close', {
                ...snackbarConfig,
              });
              this.counsel = res.data;
              this.toggleUpdate();
            },
          });
      } else {
        this.counselService.createCounsel(this.form.value).subscribe({
          next: (res) => {
            this.form.reset();
            this.snackBar.open('Counsel submitted successfully!', 'Close', {
              ...snackbarConfig,
            });
            this.counsel = res.data;
          },
          error: (res) => {
            console.log(res.error);
            this.snackBar.open(
              'Counsel submission failed. Please try again.',
              'Close',
              {
                ...snackbarConfig,
              }
            );
          },
        });
      }
    } else {
      this.form.markAllAsTouched();
    }
  }

  getCounsel(): Promise<void> {
    return new Promise((resolve) => {
      this.counselService.getCounselByEmail().subscribe({
        next: (res) => {
          this.counsel = res.data;
          resolve();
        },
        error: (res) => {
          console.log(res.error);
          resolve();
        },
      });
    });
  }

  toggleUpdate() {
    this.isUpdate = !this.isUpdate;
    if (this.isUpdate && this.counsel) {
      this.form.patchValue(this.counsel);
    }
  }
}
