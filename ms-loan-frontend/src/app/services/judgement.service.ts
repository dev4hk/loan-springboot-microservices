import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JudgementRequestDto } from '../dtos/judgement-request-dto';
import { ResponseDTO } from '../dtos/response-dto';
import { JudgementResponseDto } from '../dtos/judgement-response-dto';
import { GrantAmountDto } from '../dtos/grant-amount-dto';
import { CommunicationStatus } from '../dtos/communitation-status';

@Injectable({
  providedIn: 'root',
})
export class JudgementService {
  private BASE_URL = 'http://localhost:8072/loan/judgement/api';
  constructor(private http: HttpClient) {}

  create(request: JudgementRequestDto) {
    console.log(request);
    return this.http.post<ResponseDTO<JudgementResponseDto>>(
      `${this.BASE_URL}`,
      request
    );
  }

  update(judgementId: number, request: JudgementRequestDto) {
    return this.http.put<ResponseDTO<JudgementResponseDto>>(
      `${this.BASE_URL}/${judgementId}`,
      request
    );
  }

  getJudgementOfApplication(applicationId: number) {
    return this.http.get<ResponseDTO<JudgementResponseDto>>(
      `${this.BASE_URL}/applications/${applicationId}`
    );
  }

  grant(judgementId: number) {
    return this.http.patch<ResponseDTO<GrantAmountDto>>(
      `${this.BASE_URL}/${judgementId}/grants`,
      {}
    );
  }

  getStats() {
    return this.http.get<ResponseDTO<Record<CommunicationStatus, number>>>(
      `${this.BASE_URL}/stats`
    );
  }
}
