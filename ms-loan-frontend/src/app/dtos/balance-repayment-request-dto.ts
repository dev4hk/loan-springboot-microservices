/**
 * Schema to hold balance repayment information
 */
export interface BalanceRepaymentRequestDto {
  /**
   * Repayment amount
   */
  repaymentAmount: number;

  /**
   * Schema to hold type of repayment
   */
  type: 'ADD' | 'REMOVE';
}
