import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FilterPipe } from '../filter-pipe';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-counsel',
  templateUrl: './admin-counsel.component.html',
  styleUrls: ['./admin-counsel.component.scss'],
  imports: [CommonModule, FormsModule, FilterPipe, RouterModule],
})
export class AdminCounselComponent {
  searchQuery: string = '';
  tempId: number = 100;

  counsels = Array.from({ length: 10 }).map((_, i) => ({
    counselId: this.tempId++,
    firstname: `Customer ${i + 1}`,
    lastname: `Last ${i + 1}`,
    email: `customer${i + 1}@email.com`,
    createdAt: new Date(),
    communicationStatus: Math.random() > 0.5 ? 'Pending' : 'Completed',
  }));
}
