import { CommunicationStatus } from './communitation-status';

export interface RepaymentResponseDto {
  repaymentId?: number;
  applicationId?: number;
  repaymentAmount?: number;
  balance?: number;
  communicationStatus?: CommunicationStatus;
  createdAt?: string;
  createdBy?: string;
}
