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

  constructor(
    private formBuilder: FormBuilder,
    private counselService: CounselService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.addToggleFunctionality();
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
      this.counselService.createCounsel(this.form.value).subscribe({
        next: (res) => {
          console.log(res);
          this.form.reset();
          this.snackBar.open('Counsel submitted successfully!', 'Close', {
            ...snackbarConfig,
          });
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
    } else {
      this.form.markAllAsTouched();
    }
  }
}
