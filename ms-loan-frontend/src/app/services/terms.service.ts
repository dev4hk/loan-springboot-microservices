import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ResponseDTO } from '../dtos/response-dto';
import { TermsResponseDto } from '../dtos/terms-response-dto';

@Injectable({
  providedIn: 'root',
})
export class TermsService {
  private BASE_URL = 'http://localhost:8072/loan/terms/api';
  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<ResponseDTO<Array<TermsResponseDto>>>(this.BASE_URL);
  }
}
