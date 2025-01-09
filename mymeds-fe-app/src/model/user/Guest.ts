import { RelationshipType } from "../auth/RelationshipType";
import { UserRole } from "../auth/UserRole";
import { UserStatus } from "./UserStatus";

export interface Guest {
  guestId: string;

  firstName: string;

  middleName: string;

  lastName: string;

  email: string;

  mobileNumber: string;

  relationshipType: RelationshipType;

  userStatus: UserStatus;

  userRole?: UserRole;
}

export const testGuests: Guest[] = [
  {
    guestId: "1",
    firstName: "John",
    middleName: "David",
    lastName: "Smith",
    email: "john.smith@example.com",
    mobileNumber: "+1234567890",
    relationshipType: RelationshipType.FAMILY_MEMBER,
    userStatus: UserStatus.ACTIVE,
  },
  {
    guestId: "2",
    firstName: "Emily",
    middleName: "",
    lastName: "Johnson",
    email: "emily.johnson@example.com",
    mobileNumber: "+0987654321",
    relationshipType: RelationshipType.FRIEND,
    userStatus: UserStatus.NOT_VERIFIED,
  },
  {
    guestId: "3",
    firstName: "Michael",
    middleName: "James",
    lastName: "Brown",
    email: "michael.brown@example.com",
    mobileNumber: "+1122334455",
    relationshipType: RelationshipType.CAREGIVER,
    userStatus: UserStatus.ACTIVE,
  },
];
