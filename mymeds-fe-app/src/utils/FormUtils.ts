export const isFormValid = (
  fields: Record<string, string>,
  excludedFields: string[] = []
): boolean =>
  Object.entries(fields).every(
    ([key, value]) => excludedFields.includes(key) || value.trim() !== ""
  );

export const hasFormChanged = <T extends Object>(
  currentFormData: T,
  initialFormData: T
): boolean => {
  return Object.keys(currentFormData).some((key) => {
    const currentValue = currentFormData[key as keyof T];
    const initialValue = initialFormData[key as keyof T];
    return currentValue !== initialValue;
  });
};

export const checkAndReplaceMobileNumber = (value: string): string => {
  // Allow only digits and a single '+' at the beginning
  if (value.startsWith("+")) {
    value = "+" + value.slice(1).replace(/[^0-9]/g, ""); // Ensure only digits after "+"
  } else {
    value = value.replace(/[^0-9]/g, ""); // Only digits if there's no "+" at the start
  }

  // Limit the total length to a maximum of 11 characters (1 for "+" and 10 for digits)
  if (value.length > 13) {
    value = value.slice(0, 13);
  }
  return value;
};

export const checkAndReplaceEmail = (value: string): string => {
  return value.replace(/[^a-zA-Z0-9@.]/g, "");
};
