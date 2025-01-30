import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CounselRequestDto } from '../dtos/counsel-request-dto';
import { ResponseDTO } from '../dtos/response';
import { CounselResponseDto } from '../dtos/counsel-response-dto';
import { KeycloakService } from '../utils/keycloak/keycloak.service';

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

  getCounselByEmail() {
    const params = new HttpParams().set('email', this.keycloakService.email);
    return this.http.get<ResponseDTO<CounselResponseDto>>(
      `${this.BASE_URL}/email`,
      {
        params: params,
      }
    );
  }
}
