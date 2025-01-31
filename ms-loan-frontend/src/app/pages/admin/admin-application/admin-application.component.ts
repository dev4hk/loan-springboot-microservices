import { Component } from '@angular/core';
import { ApplicationService } from '../../../services/application.service';
import { ApplicationResponseDto } from '../../../dtos/application-response-dto';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../filter-pipe';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-application',
  templateUrl: './admin-application.component.html',
  styleUrls: ['./admin-application.component.scss'],
  imports: [RouterModule, CommonModule, FilterPipe, FormsModule],
})
export class AdminApplicationComponent {
  searchQuery: string = '';
  applications: Array<ApplicationResponseDto> = [];
  totalElements: number = 0;
  currentPage: number = 0;
  pageSize: number = 5;

  constructor(private applicationService: ApplicationService) {}

  ngOnInit() {
    this.loadApplications();
  }

  loadApplications() {
    this.applicationService
      .getApplications(this.currentPage, this.pageSize)
      .subscribe({
        next: (res) => {
          this.applications = res?.data?.content ?? [];
          this.totalElements = res?.data?.totalElements ?? 0;
        },
        error: (res) => {
          console.error('Error loading applications', res);
          alert('Failed to load applications. Please try again later.');
        },
      });
  }

  nextPage() {
    if ((this.currentPage + 1) * this.pageSize < this.totalElements) {
      this.currentPage++;
      this.loadApplications();
    }
  }

  prevPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadApplications();
    }
  }

  goToPage(page: number) {
    this.currentPage = page;
    this.loadApplications();
  }

  get totalPages(): number {
    return Math.ceil(this.totalElements / this.pageSize);
  }
}
