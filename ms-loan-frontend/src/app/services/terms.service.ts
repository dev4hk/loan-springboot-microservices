import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ResponseDTO } from '../dtos/response-dto';
import { TermsResponseDto } from '../dtos/terms-response-dto';
import { TermsRequestDto } from '../dtos/terms-request-dto';

@Injectable({
  providedIn: 'root',
})
export class TermsService {
  private BASE_URL = 'http://localhost:8072/loan/terms/api';
  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<ResponseDTO<Array<TermsResponseDto>>>(this.BASE_URL);
  }

  create(request: TermsRequestDto) {
    return this.http.post<ResponseDTO<TermsResponseDto>>(
      this.BASE_URL,
      request
    );
  }

  delete(termsId: number) {
    return this.http.delete<ResponseDTO<void>>(`${this.BASE_URL}/${termsId}`);
  }
}
