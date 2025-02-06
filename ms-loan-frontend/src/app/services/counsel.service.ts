import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CounselRequestDto } from '../dtos/counsel-request-dto';
import { ResponseDTO } from '../dtos/response-dto';
import { CounselResponseDto } from '../dtos/counsel-response-dto';
import { KeycloakService } from '../utils/keycloak/keycloak.service';
import { Page } from '../dtos/page-dto';
import { CommunicationStatus } from '../dtos/communitation-status';

@Injectable({
  providedIn: 'root',
})
export class CounselService {
  private BASE_URL = 'http://localhost:8072/loan/counsel/api';
  constructor(
    private http: HttpClient,
    private keycloakService: KeycloakService
  ) {}

  createCounsel(request: CounselRequestDto) {
    return this.http.post<ResponseDTO<CounselResponseDto>>(
      this.BASE_URL,
      request
    );
  }

  updateCounsel(counselId: number, request: CounselRequestDto) {
    return this.http.put<ResponseDTO<CounselResponseDto>>(
      `${this.BASE_URL}/${counselId}}`,
      request
    );
  }

  getCounselById(counselId: number) {
    return this.http.get<ResponseDTO<CounselResponseDto>>(
      `${this.BASE_URL}/${counselId}`
    );
  }

  getCounselByEmail() {
    const params = new HttpParams().set('email', this.keycloakService.email);
    return this.http.get<ResponseDTO<CounselResponseDto>>(
      `${this.BASE_URL}/email`,
      {
        params: params,
      }
    );
  }

  getCounsels(page: number, size: number) {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<ResponseDTO<Page<CounselResponseDto>>>(
      `${this.BASE_URL}`,
      { params: params }
    );
  }

  getStats() {
    return this.http.get<ResponseDTO<Record<CommunicationStatus, number>>>(
      `${this.BASE_URL}/stats`
    );
  }

  complete(counselId: number) {
    return this.http.patch<ResponseDTO<void>>(
      `${this.BASE_URL}/${counselId}`,
      {}
    );
  }
}
