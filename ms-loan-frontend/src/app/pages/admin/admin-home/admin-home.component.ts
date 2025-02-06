import { Component, OnInit } from '@angular/core';
import { DonutChartComponent } from '../../../components/donut-chart/donut-chart.component';
import { DonutChartInterface } from '../../../components/donut-chart/donut-chart-interface';
import { RouterModule } from '@angular/router';
import { ApplicationService } from '../../../services/application.service';
import { CounselService } from '../../../services/counsel.service';
import { CommunicationStatus } from '../../../dtos/communitation-status';
import { EntryService } from '../../../services/entry.service';
import { JudgementService } from '../../../services/judgement.service';
import { ApplicationResponseDto } from '../../../dtos/application-response-dto';
import { CounselResponseDto } from '../../../dtos/counsel-response-dto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-home',
  imports: [DonutChartComponent, RouterModule, CommonModule],
  templateUrl: './admin-home.component.html',
  styleUrl: './admin-home.component.scss',
})
export class AdminHomeComponent implements OnInit {
  applicationStats?: Record<CommunicationStatus, number>;
  counselStats?: Record<CommunicationStatus, number>;
  entryStats?: Record<CommunicationStatus, number>;
  judgementStats?: Record<CommunicationStatus, number>;
  applicationChart: DonutChartInterface = { title: '', data: [] };
  counselChart: DonutChartInterface = { title: '', data: [] };
  entryChart: DonutChartInterface = { title: '', data: [] };
  judgementChart: DonutChartInterface = { title: '', data: [] };
  APPLICATION_CHART_TITLE = 'Application';
  COUNSEL_CHART_TITLE = 'Counsel';
  ENTRY_CHART_TITLE = 'Entry';
  JUDGEMENT_CHART_TITLE = 'Judgement';
  applications?: Array<ApplicationResponseDto>;
  counsels?: Array<CounselResponseDto>;

  constructor(
    private applicationService: ApplicationService,
    private counselService: CounselService,
    private entryService: EntryService,
    private judgementService: JudgementService
  ) {}

  ngOnInit(): void {
    this.getApplicationStats();
    this.getCounselStats();
    this.getJudgementStats();
    this.getEntryStats();
    this.getNewApplications();
    this.getNewCounsels();
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

  getJudgementStats() {
    this.judgementService.getStats().subscribe({
      next: (res) => {
        this.judgementStats = res.data;

        if (
          this.judgementStats &&
          Object.keys(this.judgementStats).length > 0
        ) {
          this.judgementChart = this.transformToDonutChartData(
            this.JUDGEMENT_CHART_TITLE,
            this.judgementStats
          );
        } else {
          this.judgementChart = { title: this.JUDGEMENT_CHART_TITLE, data: [] };
        }
      },
      error: (res) => console.log(res.error),
    });
  }

  getEntryStats() {
    this.entryService.getStats().subscribe({
      next: (res) => {
        this.entryStats = res.data;

        if (this.entryStats && Object.keys(this.entryStats).length > 0) {
          this.entryChart = this.transformToDonutChartData(
            this.ENTRY_CHART_TITLE,
            this.entryStats
          );
        } else {
          this.entryChart = { title: this.ENTRY_CHART_TITLE, data: [] };
        }
      },
      error: (res) => console.log(res.error),
    });
  }

  getNewApplications() {
    this.applicationService.getNewApplications().subscribe({
      next: (res) => (this.applications = res.data),
      error: (res) => console.log(res.error),
    });
  }

  getNewCounsels() {
    this.counselService.getNewCounsels().subscribe({
      next: (res) => (this.counsels = res.data),
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
