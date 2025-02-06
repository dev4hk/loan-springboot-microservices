import { CommunicationStatus } from './communitation-status';

export interface JudgementResponseDto {
  judgementId?: number;

  applicationId?: number;

  firstname?: string;

  lastname?: string;

  approvalAmount?: number;

  startDate?: string;

  endDate?: string;

  payDay?: number;

  monthlyPayment?: number;

  numberOfPayments?: number;

  interest?: number;

  total?: number;

  communicationStatus: CommunicationStatus;

  createdAt?: string;

  createdBy?: string;

  updatedAt?: string;

  updatedBy?: string;
}
