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

  createdAt?: string;

  createdBy?: string;

  updatedAt?: string;

  updatedBy?: string;
}
