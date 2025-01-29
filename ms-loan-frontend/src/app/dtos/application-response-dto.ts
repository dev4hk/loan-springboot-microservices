import { CounselResponseDto } from './counsel-response-dto';
import { FileResponseDto } from './file-response-dto';

export interface ApplicationResponseDto {
  applicationId?: number;
  firstname?: string;
  lastname?: string;
  cellPhone?: string;
  email?: string;
  hopeAmount?: number | string;
  approvalAmount?: number | string;
  appliedAt?: string;
  contractedAt?: string;
  communicationStatus?: string;
  createdAt?: string;
  createdBy?: string;
  updatedAt?: string;
  updatedBy?: string;
  counselInfo?: CounselResponseDto;
  fileInfo?: Array<FileResponseDto>;
}
