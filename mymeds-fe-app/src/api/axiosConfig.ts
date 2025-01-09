import axios from "axios";

export const axiosInstance = axios.create({
  baseURL: process.env.REACT_APP_API_SERVICE_URL,
  timeout: 5000,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

// Add a response interceptor
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Handle unauthorized errors
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
