import { CommunicationStatus } from './communitation-status';

export interface EntryResponseDto {
  entryId: number;
  applicationId: number;
  entryAmount: number;
  communicationStatus: CommunicationStatus;
  createdAt: string;
  createdBy: string;
  updatedAt: string;
  updatedBy: string;
}
