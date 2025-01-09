import { UserRole } from "./UserRole";

export class LoginRequest {
  userName!: string;

  password!: string;

  userRole!: UserRole;
}
