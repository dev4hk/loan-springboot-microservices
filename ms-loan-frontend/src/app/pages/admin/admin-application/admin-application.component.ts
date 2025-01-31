import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FilterPipe } from '../filter-pipe';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-application',
  templateUrl: './admin-application.component.html',
  styleUrls: ['./admin-application.component.scss'],
  imports: [CommonModule, FormsModule, FilterPipe, RouterModule],
})
export class AdminApplicationComponent {
  searchQuery: string = '';
  tempId: number = 1;

  applications = Array.from({ length: 10 }).map((_, i) => ({
    applicationId: this.tempId++,
    firstname: `Customer ${i + 1}`,
    lastname: `Last ${i + 1}`,
    cellPhone: `1234567890`,
    email: `customer${i + 1}@email.com`,
    hopeAmount: Math.floor(Math.random() * 10000) + 5000,
    approvalAmount:
      Math.random() > 0.5 ? Math.floor(Math.random() * 8000) + 2000 : null,
    appliedAt: new Date(),
    communicationStatus: Math.random() > 0.5 ? 'Pending' : 'Approved',
  }));
}
