import { Component, OnInit } from '@angular/core';
import { TermsResponseDto } from '../../../dtos/terms-response-dto';
import { TermsService } from '../../../services/terms.service';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

const snackbarConfig: MatSnackBarConfig = {
  duration: 3000,
  horizontalPosition: 'center',
  verticalPosition: 'top',
};

@Component({
  selector: 'app-admin-terms',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './admin-terms.component.html',
  styleUrl: './admin-terms.component.scss',
})
export class AdminTermsComponent implements OnInit {
  terms?: Array<TermsResponseDto>;
  form: FormGroup = new FormGroup({});
  showForm: boolean = false;
  selectedTermId?: number;

  constructor(
    private termsService: TermsService,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.getAllTerms();
    this.form = this.formBuilder.group({
      name: [
        '',
        [
          Validators.required,
          Validators.maxLength(255),
          Validators.minLength(1),
        ],
      ],
      termsDetail: [
        '',
        [
          Validators.required,
          Validators.maxLength(10000),
          Validators.minLength(1),
        ],
      ],
    });
  }

  getAllTerms() {
    this.termsService.getAll().subscribe({
      next: (res) => {
        console.log('Fetched all terms', res.data);
        this.terms = res.data;
      },
      error: (res) => console.log(res.error),
    });
  }

  onSubmit() {
    if (this.form.valid) {
      this.termsService.create(this.form.value).subscribe({
        next: (res) => {
          this.form.reset();
          this.snackBar.open('Term created successfully!', 'Close', {
            ...snackbarConfig,
          });
          this.terms?.push(res.data!);
          this.showForm = false;
        },
        error: (res) => {
          this.snackBar.open(
            'Term creation failed. Please try again.',
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

  getErrorMessage(controlName: string): string {
    const control = this.form.get(controlName);

    if (control?.errors) {
      if (control.errors['required']) {
        return 'This field is required.';
      }
      if (control.errors['maxlength']) {
        return `Maximum length is ${control.errors['maxlength'].requiredLength} characters.`;
      }
      if (control.errors['minlength']) {
        return `Minimum length is ${control.errors['minlength'].requiredLength} character(s).`;
      }
    }

    return '';
  }

  editTerm(term: TermsResponseDto) {
    this.selectedTermId = term.termsId;
    this.form.patchValue({
      name: term.name,
      termsDetail: term.termsDetail,
    });
    this.showForm = true;
  }

  deleteTerm(termId: number) {
    if (confirm('Are you sure you want to delete this term?')) {
      this.termsService.delete(termId).subscribe({
        next: () => {
          this.snackBar.open(
            'Term deleted successfully!',
            'Close',
            snackbarConfig
          );
          this.terms = this.terms?.filter((term) => term.termsId !== termId);
        },
        error: () => {
          this.snackBar.open(
            'Term deletion failed. Please try again.',
            'Close',
            snackbarConfig
          );
        },
      });
    }
  }

  toggleShowForm() {
    this.showForm = !this.showForm;
    if (!this.showForm) {
      this.selectedTermId = undefined;
      this.form.reset();
    }
  }
}
