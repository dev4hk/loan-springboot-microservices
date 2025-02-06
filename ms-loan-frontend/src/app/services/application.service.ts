import { Injectable } from '@angular/core';
import { ApplicationRequestDto } from '../dtos/application-request-dto';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ResponseDTO } from '../dtos/response-dto';
import { KeycloakService } from '../utils/keycloak/keycloak.service';
import { ApplicationResponseDto } from '../dtos/application-response-dto';
import { Page } from '../dtos/page-dto';
import { AcceptTermsRequestDto } from '../dtos/accept-terms-request-dto';
import { CommunicationStatus } from '../dtos/communitation-status';

@Injectable({
  providedIn: 'root',
})
export class ApplicationService {
  private BASE_URL = 'http://localhost:8072/loan/application/api';
  constructor(
    private http: HttpClient,
    private keycloakService: KeycloakService
  ) {}

  createApplication(
    applicationRequestDto: ApplicationRequestDto,
    acceptTermsRequestDto: AcceptTermsRequestDto
  ) {
    return this.http.post<ResponseDTO<ApplicationResponseDto>>(this.BASE_URL, {
      applicationRequestDto,
      acceptTermsRequestDto,
    });
  }

  getApplicationByEmail() {
    const params = new HttpParams().set('email', this.keycloakService.email);
    return this.http.get<ResponseDTO<ApplicationResponseDto>>(
      `${this.BASE_URL}/email`,
      {
        params: params,
      }
    );
  }

  getApplicationById(applicationId: number) {
    return this.http.get<ResponseDTO<ApplicationResponseDto>>(
      `${this.BASE_URL}/${applicationId}`
    );
  }

  getApplications(page: number, size: number) {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<ResponseDTO<Page<ApplicationResponseDto>>>(
      `${this.BASE_URL}`,
      { params: params }
    );
  }

  contract(applicationId: number) {
    return this.http.put<ResponseDTO<ApplicationResponseDto>>(
      `${this.BASE_URL}/${applicationId}/contract`,
      {}
    );
  }

  getStats() {
    return this.http.get<ResponseDTO<Record<CommunicationStatus, number>>>(
      `${this.BASE_URL}/stats`
    );
  }
}
