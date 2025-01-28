import { ResultObject } from './result-object';

/**
 * Schema to hold response information
 */
export interface Response {
  /**
   * Schema to hold data information
   */
  data?: {};
  result?: ResultObject;
}
