import { CommunicationStatus } from './communitation-status';

export interface CounselResponseDto {
  counselId?: number;
  firstname?: string;
  lastname?: string;
  cellPhone?: string;
  email?: string;
  memo?: string;
  address1?: string;
  address2?: string;
  city?: string;
  state?: string;
  zipCode?: string;
  appliedAt?: string;
  communicationStatus?: CommunicationStatus;
  createdAt?: string;
  createdBy?: string;
  updatedAt?: string;
  updatedBy?: string;
}
