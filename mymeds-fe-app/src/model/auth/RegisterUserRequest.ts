import { RegisterGuestRequest } from "./RegisterGuestRequest";

export interface RegisterUserRequest {
  email: string;

  password: string;

  passwordRep: string;

  firstName: string;

  middleName?: string;

  lastName: string;

  mobileNumber: string;

  dateOfBirth: string;

  guestRequest: RegisterGuestRequest;
}
