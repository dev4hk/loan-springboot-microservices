import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FilterPipe } from '../filter-pipe';
import { RouterModule } from '@angular/router';
import { ApplicationService } from '../../../services/application.service';
import { CounselService } from '../../../services/counsel.service';

@Component({
  selector: 'app-admin-counsel',
  templateUrl: './admin-counsel.component.html',
  styleUrls: ['./admin-counsel.component.scss'],
  imports: [CommonModule, FormsModule, FilterPipe, RouterModule],
})
export class AdminCounselComponent {
  searchQuery: string = '';
  counsels: Array<any> = [];
  totalElements: number = 0;
  currentPage: number = 0;
  pageSize: number = 5;

  constructor(private counselService: CounselService) {}

  ngOnInit() {
    this.loadCounsels();
  }

  loadCounsels() {
    this.counselService.getCounsels(this.currentPage, this.pageSize).subscribe({
      next: (res) => {
        this.counsels = res?.data?.content ?? [];
        this.totalElements = res?.data?.totalElements ?? 0;
      },
      error: (res) => {
        console.error('Error loading counsels', res);
      },
    });
  }

  nextPage() {
    if ((this.currentPage + 1) * this.pageSize < this.totalElements) {
      this.currentPage++;
      this.loadCounsels();
    }
  }

  prevPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadCounsels();
    }
  }

  goToPage(page: number) {
    this.currentPage = page;
    this.loadCounsels();
  }

  get totalPages(): number {
    return Math.ceil(this.totalElements / this.pageSize);
  }
}
