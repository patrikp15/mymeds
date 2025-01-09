import { mapToMedicationSelectPopup } from "../mapper/MedicationMapper";
import { MedicationSelectPopup } from "../model/medication/MedicationSelectPopup";
import axiosInstance from "./axiosConfig";

const API_URL = "/medications";

export const getMedicationSelectPopup =
  async (): Promise<MedicationSelectPopup> => {
    try {
      const response = await axiosInstance.get(`${API_URL}`);
      return mapToMedicationSelectPopup(response.data);
    } catch (error: any) {
      // Handle API errors
      console.log(error);
      throw new Error(
        error.response?.data?.message || "Failed to fetch medications"
      );
    }
  };
