import { mapToUserMedication } from "../mapper/MedicationMapper";
import { DateFilterEnum } from "../model/DateFilterEnum";
import { UserMedication } from "../model/medication/UserMedicationResponse";
import { stringToServerDate } from "../utils/DateUtils";
import axiosInstance from "./axiosConfig";

const API_URL = "/user-medications";

export const getUserMedications = async (): Promise<UserMedication[]> => {
  try {
    const response = await axiosInstance.get(`${API_URL}`);
    return response.data.map((item: any) => mapToUserMedication(item));
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "Failed to fetch user-medications"
    );
  }
};

export const getUserMedicationsByDateFilter = async (
  filter: DateFilterEnum
): Promise<UserMedication[]> => {
  try {
    const response = await axiosInstance.get(`${API_URL}?dateFilter=${filter}`);
    return response.data.map((item: any) => mapToUserMedication(item));
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "Failed to fetch user-medications"
    );
  }
};

export const getUserMedicationsByTextFilter = async (
  text: string
): Promise<UserMedication[]> => {
  try {
    const response = await axiosInstance.get(`${API_URL}?textFilter=${text}`);
    return response.data.map((item: any) => mapToUserMedication(item));
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "Failed to fetch user-medications"
    );
  }
};

export const getUserMedicationsByUid = async (
  uid: String
): Promise<UserMedication[]> => {
  try {
    const response = await axiosInstance.get(`/public${API_URL}?uid=${uid}`);
    return response.data.map((item: any) => mapToUserMedication(item));
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "Failed to fetch user-medications"
    );
  }
};

export const addUserMedication = async (formData: {
  medicationId: string;
  medicationDoseId: string;
  startDate: string;
  endDate: string;
  instructions: string;
}): Promise<void> => {
  try {
    // Extract and reformat startDate and endDate
    const { startDate, endDate, ...rest } = formData;
    const request = {
      ...rest,
      startDate: stringToServerDate(startDate),
      endDate: stringToServerDate(endDate),
    };
    await axiosInstance.post(`${API_URL}`, request);
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "Failed to fetch user-medications"
    );
  }
};

export const deleteUserMedicationById = async (
  userMedicationId: String
): Promise<void> => {
  try {
    await axiosInstance.delete(`${API_URL}/${userMedicationId}`);
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(
      error.response?.data?.message || "Failed to delete user-medication"
    );
  }
};
