/**
 * Schema to hold counsel information
 */
export interface CounselRequestDto {
  /**
   * address
   */
  address1: string;

  /**
   * address2
   */
  address2?: string;

  /**
   * Cell phone
   */
  cellPhone: string;

  /**
   * city
   */
  city: string;

  /**
   * Email
   */
  email: string;

  /**
   * First name
   */
  firstname: string;

  /**
   * Last name
   */
  lastname: string;

  /**
   * memo
   */
  memo: string;

  /**
   * state
   */
  state: string;

  /**
   * zip code
   */
  zipCode: string;
}
