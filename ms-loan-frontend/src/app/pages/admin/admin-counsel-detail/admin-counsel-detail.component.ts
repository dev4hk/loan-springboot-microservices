import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CounselService } from '../../../services/counsel.service';
import { CommonModule } from '@angular/common';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-admin-counsel-detail',
  templateUrl: './admin-counsel-detail.component.html',
  styleUrls: ['./admin-counsel-detail.component.scss'],
  imports: [CommonModule, MatSnackBarModule],
})
export class AdminCounselDetailComponent {
  counselId!: number;
  counsel: any;
  showMemo: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private counselService: CounselService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.counselId = Number(this.route.snapshot.paramMap.get('counselId'));
    this.fetchCounselDetails();
  }

  fetchCounselDetails() {
    this.counselService.getCounselById(this.counselId).subscribe({
      next: (res) => {
        console.log(res);
        this.counsel = res.data;
      },
      error: (res) => console.log(res.error),
    });
  }

  getCounsel(): Promise<void> {
    return new Promise((resolve) => {
      this.counselService.getCounselById(this.counselId).subscribe({
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

  toggleMemo() {
    this.showMemo = !this.showMemo;
  }
}
