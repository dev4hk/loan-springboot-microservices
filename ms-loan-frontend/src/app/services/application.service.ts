import { Injectable } from '@angular/core';
import { ApplicationRequestDto } from '../dtos/application-request-dto';
import { HttpClient } from '@angular/common/http';
import { Response } from '../dtos/response';

@Injectable({
  providedIn: 'root',
})
export class ApplicationService {

  private BASE_URL = 'http://localhost:8072/loan/application/api';
  constructor(private http: HttpClient) {}

  createApplication(request: ApplicationRequestDto) {
    return this.http.post<Response>(this.BASE_URL, request);
  }
}
