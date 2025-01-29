import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CounselRequestDto } from '../dtos/counsel-request-dto';
import { Response } from '../dtos/response';

@Injectable({
  providedIn: 'root',
})
export class CounselService {
  private BASE_URL = 'http://localhost:8072/loan/counsel/api';
  constructor(private http: HttpClient) {}

  createCounsel(request: CounselRequestDto) {
    return this.http.post<Response>(this.BASE_URL, request);
  }
}
