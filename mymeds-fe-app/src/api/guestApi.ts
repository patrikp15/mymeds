import { mapToGuest } from "../mapper/GuestMapper";
import { mapToAuthUserDetail } from "../mapper/UserMapper";
import { RelationshipType } from "../model/auth/RelationshipType";
import { AuthUserDetail } from "../model/user/AuthUserDetail";
import { Guest } from "../model/user/Guest";
import { UserStatus } from "../model/user/UserStatus";
import axiosInstance from "./axiosConfig";

const API_URL = "/guests";

export const getAllGuests = async (): Promise<Guest[]> => {
  try {
    const response = await axiosInstance.get(`${API_URL}`);
    return response.data.map((guest: any) => mapToGuest(guest));
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "Failed to fetch user-detail"
    );
  }
};

export const createGuest = async (formData: {
  firstName: string;
  middleName?: string;
  lastName: string;
  email: string;
  mobileNumber: string;
  relationshipType: RelationshipType;
}) => {
  try {
    const response = await axiosInstance.post(`/auth${API_URL}`, formData);
    return response;
  } catch (error: any) {
    // Handle API errors
    throw new Error(error.response?.data?.message || "Registration failed");
  }
};

export const changeGuestStatus = async (
  userStatus: UserStatus,
  guestId: string
): Promise<void> => {
  try {
    await axiosInstance.put(`/auth${API_URL}/${guestId}/status`, {
      userStatus: userStatus,
    });
  } catch (error: any) {
    // Handle API errors
    throw new Error(error.response?.data?.message || "Status change failed");
  }
};

export const getGuestDetail = async (
  token: string
): Promise<AuthUserDetail> => {
  try {
    const res = await axiosInstance.get(
      `/auth${API_URL}/detail?token=${token}`
    );
    return mapToAuthUserDetail(res.data);
  } catch (error: any) {
    // Handle API errors
    throw new Error(
      error.response?.data?.message || "Fetching guest detail failed"
    );
  }
};
