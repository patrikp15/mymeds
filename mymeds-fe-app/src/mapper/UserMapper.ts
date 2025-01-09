import { UserRole } from "../model/auth/UserRole";
import { AuthUserDetail } from "../model/user/AuthUserDetail";
import { UserDetail } from "../model/user/UserDetail";
import { UserStatus } from "../model/user/UserStatus";
import { stringToAppDate } from "../utils/DateUtils";
import { stringToEnum } from "../utils/EnumConverter";

export const mapToUserDetail = (data: any): UserDetail => {
  const userDetail: UserDetail = {
    firstName: data.firstName,
    middleName: data.middleName,
    lastName: data.lastName,
    email: data.email,
    mobileNumber: data.mobileNumber,
    dateOfBirth: stringToAppDate(data.dateOfBirth),
    userStatus: stringToEnum(data.userStatus, UserStatus),
  };

  return userDetail;
};

export const mapToAuthUserDetail = (data: any): AuthUserDetail => {
  const authUserDetail: AuthUserDetail = {
    email: data.email,
    userStatus: stringToEnum(data.userStatus, UserStatus),
    userRole: stringToEnum(data.userRole, UserRole),
  };

  return authUserDetail;
};
