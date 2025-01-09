import React from "react";
import classes from "./FormDateInput.module.css";

const FormDateInput: React.FC<{
  name: string;
  disabled?: boolean;
  value?: string;
  onChange: (value: string) => void;
}> = ({ name, disabled = false, value = "", onChange }) => {
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let input = e.target.value;

    // Remove all non-numeric characters except '.'
    input = input.replace(/[^0-9.]/g, "");

    // Split the input into parts (day, month, year)
    const parts = input.split(".");

    // Add leading zero to day and month if missing
    if (
      input.length === 2 &&
      input.charAt(1) === "." &&
      parts[0]?.length === 1
    ) {
      parts[0] = `0${parts[0]}`;
    }
    if (
      input.length === 5 &&
      input.charAt(4) === "." &&
      parts[1]?.length === 1
    ) {
      parts[1] = `0${parts[1]}`;
    }

    // Rejoin parts while limiting length
    input = parts.join(".").slice(0, 10);

    // Add dots automatically for the user
    if (input.length === 3 && input.charAt(2) !== ".") {
      input = `${input.slice(0, 2)}.${input.slice(2)}`;
    }
    if (input.length === 6 && input.charAt(5) !== ".") {
      input = `${input.slice(0, 5)}.${input.slice(5)}`;
    }

    onChange(input); // Notify parent
  };

  return (
    <div className={classes.container}>
      <input
        id={name}
        name={name}
        type="text"
        className={classes.input}
        value={value}
        onChange={handleInputChange}
        disabled={disabled}
        placeholder="Dátum narodenia*"
        maxLength={10}
      />
    </div>
  );
};

export default FormDateInput;
