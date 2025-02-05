import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { TermsResponseDto } from '../../dtos/terms-response-dto';

@Component({
  selector: 'app-terms-agreement-dialog',
  imports: [
    CommonModule,
    MatDialogModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
  ],
  templateUrl: './terms-agreement-dialog.component.html',
  styleUrl: './terms-agreement-dialog.component.scss',
})
export class TermsAgreementDialogComponent implements OnInit {
  termsForm: FormGroup = new FormGroup({});

  constructor(
    private dialogRef: MatDialogRef<TermsAgreementDialogComponent>,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: { terms: TermsResponseDto[] }
  ) {}

  ngOnInit(): void {
    let controls: { [key: string]: FormControl } = {};
    this.data.terms.forEach((term, i) => {
      const controlName = term.termsId?.toString() || `term_${i}`;
      controls[controlName] = new FormControl(false, Validators.requiredTrue);
    });

    this.termsForm = this.formBuilder.group(controls);
  }

  closeDialog() {
    this.dialogRef.close();
  }

  submitAgreement() {
    const agreedTermsIds = Object.keys(this.termsForm.value)
      .filter((key) => this.termsForm.value[key])
      .map((key) => parseInt(key.replace('term_', '')));

    this.dialogRef.close(agreedTermsIds);
  }
}
