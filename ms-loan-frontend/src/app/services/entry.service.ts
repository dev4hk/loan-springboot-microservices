import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EntryRequestDto } from '../dtos/entry-request-dto';
import { ResponseDTO } from '../dtos/response-dto';
import { EntryResponseDto } from '../dtos/entry-response-dto';
import { EntryUpdateResponseDto } from '../dtos/entry-update-response-dto';

@Injectable({
  providedIn: 'root',
})
export class EntryService {
  private BASE_URL = 'http://localhost:8072/loan/entry/api';
  constructor(private http: HttpClient) {}

  createEntry(applicationId: number, request: EntryRequestDto) {
    const url = `${this.BASE_URL}/${applicationId}`;
    return this.http.post<ResponseDTO<EntryResponseDto>>(url, request);
  }

  getEntry(applicationId: number) {
    const url = `${this.BASE_URL}/${applicationId}`;
    return this.http.get<ResponseDTO<EntryResponseDto>>(url);
  }

  updateEntry(entryId: number, request: EntryRequestDto) {
    const url = `${this.BASE_URL}/${entryId}`;
    return this.http.put<ResponseDTO<EntryUpdateResponseDto>>(url, request);
  }

  deleteEntry(entryId: number) {
    const url = `${this.BASE_URL}/${entryId}`;
    return this.http.delete<ResponseDTO<void>>(url);
  }
}
