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
import { CounselResponseDto } from '../../dtos/counsel-response-dto';
import { CounselService } from '../../services/counsel.service';
import { CommonModule } from '@angular/common';
import { BalanceResponseDto } from '../../dtos/balance-response-dto';
import { JudgementResponseDto } from '../../dtos/judgement-response-dto';
import { BalanceService } from '../../services/balance.service';
import { JudgementService } from '../../services/judgement.service';
import { KeycloakService } from '../../utils/keycloak/keycloak.service';

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
  balance?: BalanceResponseDto;
  judgement?: JudgementResponseDto;
  slides: Array<ImageSlideInterface> = homeImageSlides;

  constructor(
    private applicationService: ApplicationService,
    private counselService: CounselService,
    private repaymentService: RepaymentService,
    private balanceService: BalanceService,
    private judgementService: JudgementService,
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
          this.applicationId = this.application.applicationId;
          if (this.applicationId) {
            this.getBalance();
            this.getJudgement();
          }
        }
      },
      error: (res) => {
        console.log(res.error);
      },
    });
  }

  getBalance() {
    if (this.applicationId) {
      this.balanceService.get(this.applicationId).subscribe({
        next: (res) => (this.balance = res.data),
        error: (res) => {
          console.log(res.error);
        },
        complete: () => {
          this.getRepayments();
        },
      });
    }
  }

  getRepayments() {
    if (this.applicationId) {
      this.repaymentService.getRepayments(this.applicationId).subscribe({
        next: (res) => {
          this.repayments = res.data;
        },
        error: (res) => console.log(res.error),
        complete: () => this.createChartData(),
      });
    }
  }

  getCounsel() {
    this.counselService.getCounselByEmail().subscribe({
      next: (res) => (this.counsel = res.data),
      error: (res) => console.log(res.error),
    });
  }

  getJudgement() {
    if (this.applicationId) {
      this.judgementService
        .getJudgementOfApplication(this.applicationId)
        .subscribe({
          next: (res) => (this.judgement = res.data),
          error: (res) => console.log(res.error),
        });
    }
  }

  get fullName() {
    return this.keycloakService.fullName;
  }

  createChartData() {
    if (this.repayments && this.repayments.length > 0) {
      const totalRepaymentAmount = this.repayments!.reduce(
        (sum, repayment) => sum + (repayment.repaymentAmount ?? 0),
        0
      );
      this.dataChart = {
        title: `${this.keycloakService.fullName}'s Current Balance`,
        data: [
          { label: 'Balance', number: this.balance?.balance ?? 0 },
          { label: 'Repayment', number: totalRepaymentAmount },
        ],
      };
    } else if (this.balance) {
      this.dataChart = {
        title: `${this.keycloakService.fullName}'s Current Balance`,
        data: [
          { label: 'Balance', number: this.balance.balance ?? 0 },
          { label: 'Repayment', number: 0 },
        ],
      };
    }
  }
}
