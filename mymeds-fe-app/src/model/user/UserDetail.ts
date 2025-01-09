import { UserStatus } from "./UserStatus";

export interface UserDetail {
  firstName: string;

  middleName: string;

  lastName: string;

  email: string;

  mobileNumber: string;

  dateOfBirth: string;

  userStatus: UserStatus;
}
