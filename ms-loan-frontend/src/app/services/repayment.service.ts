import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ResponseDTO } from '../dtos/response-dto';
import { RepaymentResponseDto } from '../dtos/repayment-response-dto';
import { RepaymentRequestDto } from '../dtos/repayment-request-dto';

@Injectable({
  providedIn: 'root',
})
export class RepaymentService {
  private BASE_URL = 'http://localhost:8072/loan/repayment/api';
  constructor(private http: HttpClient) {}

  getRepayments(applicationId: number) {
    const url = `${this.BASE_URL}/${applicationId}`;
    return this.http.get<ResponseDTO<Array<RepaymentResponseDto>>>(url);
  }

  createRepayment(
    applicationId: number,
    repaymentRequestDto: RepaymentRequestDto
  ) {
    const url = `${this.BASE_URL}/${applicationId}`;
    return this.http.post<ResponseDTO<RepaymentResponseDto>>(
      url,
      repaymentRequestDto
    );
  }
}
