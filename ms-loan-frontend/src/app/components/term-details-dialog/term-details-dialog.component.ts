import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatDialogModule,
} from '@angular/material/dialog';

@Component({
  selector: 'app-term-details-dialog',
  imports: [CommonModule, MatDialogModule, MatButtonModule],
  templateUrl: './term-details-dialog.component.html',
  styleUrl: './term-details-dialog.component.scss',
})
export class TermDetailsDialogComponent {
  constructor(
    private dialogRef: MatDialogRef<TermDetailsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { name: string; termsDetail: string }
  ) {}

  closeDialog() {
    this.dialogRef.close();
  }
}
