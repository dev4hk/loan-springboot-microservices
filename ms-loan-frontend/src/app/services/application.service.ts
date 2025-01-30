import { Injectable } from '@angular/core';
import { ApplicationRequestDto } from '../dtos/application-request-dto';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ResponseDTO } from '../dtos/response';
import { KeycloakService } from '../utils/keycloak/keycloak.service';
import { ApplicationResponseDto } from '../dtos/application-response-dto';

@Injectable({
  providedIn: 'root',
})
export class ApplicationService {
  private BASE_URL = 'http://localhost:8072/loan/application/api';
  constructor(
    private http: HttpClient,
    private keycloakService: KeycloakService
  ) {}

  createApplication(request: ApplicationRequestDto) {
    return this.http.post<ResponseDTO<ApplicationResponseDto>>(
      this.BASE_URL,
      request
    );
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
}
