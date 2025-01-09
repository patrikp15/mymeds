import { PDFFormat } from "../model/share/PDFFormat";
import { QRCodeResponse } from "../model/share/QRCodeResponse";
import axiosInstance from "./axiosConfig";

const API_URL = "/share";

export const getQRCode = async (): Promise<QRCodeResponse> => {
  try {
    const response = await axiosInstance.get(`${API_URL}/qr`);
    return {
      qrCodeBase64: response.data.qrCodeBase64,
    };
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(error.response?.data?.message || "Failed to get QR Code");
  }
};

export const getPDF = async (
  format: PDFFormat,
  text?: string
): Promise<Blob> => {
  try {
    const response = await axiosInstance.get(
      `${API_URL}/pdf?format=${format}${text ? "&text=" + text : ""}`,
      { responseType: "blob" }
    );
    return response.data;
  } catch (error: any) {
    // Handle API errors
    console.log(error);
    throw new Error(error.response?.data?.message || "Failed to get PDF");
  }
};
