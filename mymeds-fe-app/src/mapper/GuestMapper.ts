import { RelationshipType } from "../model/auth/RelationshipType";
import { Guest } from "../model/user/Guest";
import { UserStatus } from "../model/user/UserStatus";
import { stringToEnum } from "../utils/EnumConverter";

export const mapToGuest = (data: any): Guest => {
  const guest: Guest = {
    guestId: data.guestId,
    firstName: data.firstName,
    middleName: data.middleName,
    lastName: data.lastName,
    email: data.email,
    mobileNumber: data.mobileNumber,
    userStatus: stringToEnum(data.userStatus, UserStatus),
    relationshipType: stringToEnum(data.relationshipType, RelationshipType),
  };

  return guest;
};
