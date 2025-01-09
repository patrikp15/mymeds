import { UserRole } from "./UserRole";
import { UserStatus } from "./UserStatus";

export interface AuthUserDetail {
  email: string;
  userStatus: UserStatus;
  userRole: UserRole;
}
