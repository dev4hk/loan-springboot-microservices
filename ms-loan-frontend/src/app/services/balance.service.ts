import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ResponseDTO } from '../dtos/response-dto';
import { BalanceResponseDto } from '../dtos/balance-response-dto';

@Injectable({
  providedIn: 'root',
})
export class BalanceService {
  private BASE_URL = 'http://localhost:8072/loan/balance/api';
  constructor(private http: HttpClient) {}

  get(applicationId: number) {
    const url = `${this.BASE_URL}/${applicationId}`;
    return this.http.get<ResponseDTO<BalanceResponseDto>>(url);
  }
}
