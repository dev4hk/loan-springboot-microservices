import { ResultObject } from './result-object';

/**
 * Schema to hold response information
 */
export interface ResponseDTO<T> {
  /**
   * Schema to hold data information
   */
  data?: T;
  result?: ResultObject;
}
