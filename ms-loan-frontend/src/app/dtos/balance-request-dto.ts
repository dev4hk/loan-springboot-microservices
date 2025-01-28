/**
 * Schema to hold balance information
 */
export interface BalanceRequestDto {
  /**
   * Application ID
   */
  applicationId: number;

  /**
   * Entry amount
   */
  entryAmount: number;
}
