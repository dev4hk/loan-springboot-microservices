import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApplicationService } from '../../../services/application.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApplicationResponseDto } from '../../../dtos/application-response-dto';
import { KeycloakService } from '../../../utils/keycloak/keycloak.service';

@Component({
  selector: 'app-admin-application-detail',
  templateUrl: './admin-application-detail.component.html',
  styleUrls: ['./admin-application-detail.component.scss'],
  imports: [CommonModule, FormsModule],
})
export class AdminApplicationDetailComponent implements OnInit {
  applicationId!: string;
  application?: ApplicationResponseDto;
  judgeFirstname?: string;
  judgeLastname?: string;
  approvalAmount?: number;

  constructor(
    private route: ActivatedRoute,
    private applicationService: ApplicationService,
    private keyclockService: KeycloakService
  ) {}

  ngOnInit(): void {
    this.applicationId = this.route.snapshot.paramMap.get('applicationId')!;
    this.fetchApplicationDetails();
    this.judgeFirstname = this.keyclockService.firstName;
    this.judgeLastname = this.keyclockService.lastName;
  }

  fetchApplicationDetails() {
    this.applicationService.getApplicationById(+this.applicationId).subscribe({
      next: (res) => (this.application = res.data),
      error: (res) => console.log(res.error),
    });
  }

  submitJudgment() {}
}
