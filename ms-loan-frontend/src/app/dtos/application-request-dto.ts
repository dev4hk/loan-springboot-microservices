/**
 * Schema to hold application information
 */
export interface ApplicationRequestDto {
  /**
   * Cell phone
   */
  cellPhone: string;

  /**
   * Email
   */
  email: string;

  /**
   * First name
   */
  firstname: string;

  /**
   * Hope amount
   */
  hopeAmount: number;

  /**
   * Last name
   */
  lastname: string;
}
