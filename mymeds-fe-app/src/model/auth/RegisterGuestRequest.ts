import { RelationshipType } from "./RelationshipType";

export interface RegisterGuestRequest {
  firstName: string;

  middleName?: string;

  lastName: string;

  email: string;

  mobileNumber: string;

  relationshipType: RelationshipType;
}
