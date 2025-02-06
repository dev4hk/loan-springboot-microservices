import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ResponseDTO } from '../dtos/response-dto';
import { FileResponseDto } from '../dtos/file-response-dto';

@Injectable({
  providedIn: 'root',
})
export class FileStorageService {
  private BASE_URL = 'http://localhost:8072/loan/file-storage/api';
  constructor(private http: HttpClient) {}

  uploadFile(applicationId: number, file: File) {
    const formData = new FormData();
    formData.set('file', file);
    return this.http.post<ResponseDTO<void>>(
      `${this.BASE_URL}/${applicationId}`,
      formData
    );
  }

  getFilesInfo(applicationId: number) {
    return this.http.get<ResponseDTO<Array<FileResponseDto>>>(
      `${this.BASE_URL}/${applicationId}/info`
    );
  }

  getFile(applicationId: number, fileName: string) {
    console.log(applicationId);
    const params = new HttpParams().set('fileName', fileName);
    return this.http.get(`${this.BASE_URL}/${applicationId}`, {
      responseType: 'blob',
      params: params,
    });
  }

  deleteFile(applicationId: number, fileName: string) {
    const params = new HttpParams().set('fileName', fileName);
    return this.http.delete<ResponseDTO<void>>(
      `${this.BASE_URL}/${applicationId}/file`,
      {
        params: params,
      }
    );
  }
}
