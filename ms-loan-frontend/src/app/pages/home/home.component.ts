import { Component, OnInit } from '@angular/core';
import { DonutChartComponent } from '../../components/donut-chart/donut-chart.component';
import { DonutChartInterface } from '../../components/donut-chart/donut-chart-interface';
import { RepaymentService } from '../../services/repayment.service';
import { RepaymentResponseDto } from '../../dtos/repayment-response-dto';
import { ApplicationService } from '../../services/application.service';
import { ApplicationResponseDto } from '../../dtos/application-response-dto';
import { ImageSliderComponent } from '../../components/image-slider/image-slider.component';
import { homeImageSlides } from '../../components/image-slider/home-image-slides';
import { ImageSlideInterface } from '../../components/image-slider/image-slide-interface';
import { RouterModule } from '@angular/router';
import { KeycloakService } from '../../utils/keycloak/keycloak.service';
import { CounselResponseDto } from '../../dtos/counsel-response-dto';
import { CounselService } from '../../services/counsel.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [
    DonutChartComponent,
    ImageSliderComponent,
    RouterModule,
    CommonModule,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  repayments?: Array<RepaymentResponseDto>;
  application?: ApplicationResponseDto;
  applicationId?: number;
  counsel?: CounselResponseDto;
  dataChart?: DonutChartInterface;
  slides: Array<ImageSlideInterface> = homeImageSlides;

  constructor(
    private applicationService: ApplicationService,
    private counselService: CounselService,
    private repaymentService: RepaymentService,
    private keycloakService: KeycloakService
  ) {}

  ngOnInit(): void {
    this.getApplicationByEmail();
    if (!this.application) {
      this.getCounsel();
    }
  }

  getApplicationByEmail() {
    this.applicationService.getApplicationByEmail().subscribe({
      next: (res) => {
        this.application = res.data;
        if (this.application?.applicationId) {
          this.applicationId = this.applicationId;
          this.getRepayments(this.applicationId!);
        }
      },
      error: (res) => {
        console.log(res.error);
      },
    });
  }

  getRepayments(applicationId: number) {
    this.repaymentService.getRepayments(applicationId).subscribe({
      next: (res) => {
        this.repayments = res.data;
        this.createChartData();
      },
      error: (res) => console.log(res.error),
    });
  }

  getCounsel() {
    this.counselService.getCounselByEmail().subscribe({
      next: (res) => (this.counsel = res.data),
      error: (res) => console.log(res.error),
    });
  }

  createChartData() {
    const balance = this.repayments![this.repayments!.length - 1].balance;
    const totalRepaymentAmount = this.repayments!.reduce(
      (sum, repayment) => sum + (repayment.repaymentAmount || 0),
      0
    );
    this.dataChart = {
      title: 'Current Balance vs. Repayment Amount',
      data: [
        { label: 'Balance', number: balance ?? 0 },
        { label: 'Repayment Amount', number: totalRepaymentAmount },
      ],
    };
  }
}
