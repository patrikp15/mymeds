import { AuthTokenStep } from "../model/auth/AuthTokenStep";
import { LoginRequest } from "../model/auth/LoginRequest";
import { RegisterUserRequest } from "../model/auth/RegisterUserRequest";
import { RelationshipType } from "../model/auth/RelationshipType";
import { UserRole } from "../model/auth/UserRole";
import { stringToServerDate } from "../utils/DateUtils";
import axiosInstance from "./axiosConfig";

const API_URL = "/auth";

export const login = async (formData: {
  email: string;
  password: string;
  userRole: string;
}) => {
  try {
    const loginRequest: LoginRequest = {
      userName: formData.email,
      password: formData.password,
      userRole: UserRole[formData.userRole as keyof typeof UserRole],
    };
    const response = await axiosInstance.post(`${API_URL}/login`, loginRequest);
    return response.data;
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(error.response?.data?.message || "Login failed");
  }
};

export const logout = async () => {
  try {
    await axiosInstance.post(`${API_URL}/logout`);
    window.location.href = "/login";
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(error.response?.data?.message || "Logout failed");
  }
};

export const register = async (
  userFormData: {
    firstname: string;
    middlename?: string;
    lastname: string;
    password: string;
    passwordre: string;
    email: string;
    mobile: string;
    date: string;
  },
  guestFormData: {
    guestFirstName: string;
    guestMiddleName?: string;
    guestLastName: string;
    guestEmail: string;
    guestMobile: string;
    relationshipType: string;
  }
) => {
  try {
    const registerRequest: RegisterUserRequest = {
      email: userFormData.email,
      password: userFormData.password,
      passwordRep: userFormData.passwordre,
      firstName: userFormData.firstname,
      middleName: userFormData.middlename,
      lastName: userFormData.lastname,
      mobileNumber: userFormData.mobile,
      dateOfBirth: stringToServerDate(userFormData.date),
      guestRequest: {
        firstName: guestFormData.guestFirstName,
        middleName: guestFormData.guestMiddleName,
        lastName: guestFormData.guestLastName,
        email: guestFormData.guestEmail,
        mobileNumber: guestFormData.guestMobile,
        relationshipType:
          RelationshipType[
            guestFormData.relationshipType as keyof typeof RelationshipType
          ],
      },
    };

    const response = await axiosInstance.post(
      `${API_URL}/register`,
      registerRequest
    );
    return response.data;
  } catch (error: any) {
    // Handle API errors
    throw new Error(error.response?.data?.message || "Registration failed");
  }
};

export const updateUser = async (formData: {
  email: string;
  mobileNumber: string;
}) => {
  try {
    const response = await axiosInstance.put(`${API_URL}/users`, formData);
    return response.data;
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(error.response?.data?.message || "User update failed");
  }
};

export const resetPassword = async (formData: {
  tokenStep: AuthTokenStep;
  email: string;
}): Promise<void> => {
  try {
    await axiosInstance.post(`${API_URL}/password/reset`, formData);
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "User password reset failed"
    );
  }
};

export const changePassword = async (formData: {
  tokenStep: AuthTokenStep;
  token: string;
  password: string;
  passwordRe: string;
}): Promise<void> => {
  try {
    await axiosInstance.post(`${API_URL}/password/reset`, formData);
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "User password reset failed"
    );
  }
};

export const verifyUser = async (): Promise<void> => {
  try {
    await axiosInstance.get(`${API_URL}/verify`);
    await logout();
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "User verification failed"
    );
  }
};

export const verifyGuest = async (guestId: string): Promise<void> => {
  try {
    await axiosInstance.get(`${API_URL}/${guestId}/verify`);
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "Guest verification failed"
    );
  }
};

export const registerGuest = async (
  token: string,
  formData: {
    password: string;
    passwordRe: string;
  }
): Promise<void> => {
  try {
    await axiosInstance.post(
      `${API_URL}/guests/register?token=${token}`,
      formData
    );
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "Guest registration failed"
    );
  }
};
