import dayjs from "dayjs";

var customParseFormat = require("dayjs/plugin/customParseFormat");
dayjs.extend(customParseFormat);

export const DEFAULT_APP_DATE_FORMAT = "DD.MM.YYYY";
export const DEFAULT_SERVER_DATE_FORMAT = "YYYY-MM-DD";

export const convertDate = (
  value: string | Date | number,
  outputFormat: string,
  inputFormat?: string
): string => {
  // Check if the value is valid
  if (!value) {
    throw new Error("Invalid date value provided.");
  }

  // Convert the value using Day.js and return the formatted date
  let formattedDate;
  if (inputFormat) {
    formattedDate = dayjs(value, inputFormat, true).format(outputFormat);
  } else {
    formattedDate = dayjs(value).format(outputFormat);
  }

  // Handle invalid dates gracefully
  if (formattedDate === "Invalid Date") {
    throw new Error("Invalid date format or value.");
  }

  return formattedDate;
};

export const stringToServerDate = (date: string): string => {
  return convertDate(date, DEFAULT_SERVER_DATE_FORMAT, DEFAULT_APP_DATE_FORMAT);
};

export const stringToAppDate = (date: string): string => {
  return convertDate(date, DEFAULT_APP_DATE_FORMAT, DEFAULT_SERVER_DATE_FORMAT);
};
