import { mapToUserDetail } from "../mapper/UserMapper";
import { UserDetail } from "../model/user/UserDetail";
import axiosInstance from "./axiosConfig";

const API_URL = "/users";

export const getUserDetail = async (): Promise<UserDetail> => {
  try {
    const response = await axiosInstance.get(`${API_URL}`);
    return mapToUserDetail(response.data);
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "Failed to fetch user-detail"
    );
  }
};
