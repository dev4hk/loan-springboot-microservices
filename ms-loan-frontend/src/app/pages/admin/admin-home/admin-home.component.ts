import { Component, OnInit } from '@angular/core';
import { DonutChartComponent } from '../../../components/donut-chart/donut-chart.component';
import { DonutChartInterface } from '../../../components/donut-chart/donut-chart-interface';
import { RouterModule } from '@angular/router';
import { ApplicationService } from '../../../services/application.service';
import { CounselService } from '../../../services/counsel.service';
import { CommunicationStatus } from '../../../dtos/communitation-status';

@Component({
  selector: 'app-admin-home',
  imports: [DonutChartComponent, RouterModule],
  templateUrl: './admin-home.component.html',
  styleUrl: './admin-home.component.scss',
})
export class AdminHomeComponent implements OnInit {
  applicationStats?: Record<CommunicationStatus, number>;
  counselStats?: Record<CommunicationStatus, number>;
  applicationChart: DonutChartInterface = { title: '', data: [] };
  counselChart: DonutChartInterface = { title: '', data: [] };
  APPLICATION_CHART_TITLE = 'Application Statistics';
  COUNSEL_CHART_TITLE = 'Counsel Statistics';

  constructor(
    private applicationService: ApplicationService,
    private counselService: CounselService
  ) {}

  ngOnInit(): void {
    this.getApplicationStats();
    this.getCounselStats();
  }

  getApplicationStats() {
    this.applicationService.getStats().subscribe({
      next: (res) => {
        this.applicationStats = res.data;

        if (
          this.applicationStats &&
          Object.keys(this.applicationStats).length > 0
        ) {
          this.applicationChart = this.transformToDonutChartData(
            this.APPLICATION_CHART_TITLE,
            this.applicationStats
          );
        } else {
          this.applicationChart = {
            title: this.APPLICATION_CHART_TITLE,
            data: [],
          };
        }
      },
      error: (res) => console.log(res.error),
    });
  }

  getCounselStats() {
    this.counselService.getStats().subscribe({
      next: (res) => {
        this.counselStats = res.data;

        if (this.counselStats && Object.keys(this.counselStats).length > 0) {
          this.counselChart = this.transformToDonutChartData(
            this.COUNSEL_CHART_TITLE,
            this.counselStats
          );
        } else {
          this.counselChart = { title: this.COUNSEL_CHART_TITLE, data: [] };
        }
      },
      error: (res) => console.log(res.error),
    });
  }

  transformToDonutChartData(
    title: string,
    map: Record<CommunicationStatus, number>
  ): DonutChartInterface {
    return {
      title: title,
      data: Object.entries(map).map(([key, value]) => ({
        label: key,
        number: value,
      })),
    };
  }
}
