/**
 * Schema to hold judgement information
 */
export interface JudgementRequestDto {
  /**
   * Application ID
   */
  applicationId: number;

  /**
   * Approval amount
   */
  approvalAmount: number;

  /**
   * First name
   */
  firstname: string;

  /**
   * Last name
   */
  lastname: string;
}
