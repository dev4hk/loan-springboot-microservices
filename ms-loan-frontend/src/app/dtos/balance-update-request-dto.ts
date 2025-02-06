/**
 * Schema to hold balance update information
 */
export interface BalanceUpdateRequestDto {
  /**
   * After entry amount
   */
  afterEntryAmount: number;

  /**
   * Application ID
   */
  applicationId: number;

  /**
   * Before entry amount
   */
  beforeEntryAmount: number;
}
