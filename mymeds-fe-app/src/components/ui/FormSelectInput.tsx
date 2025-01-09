import React from "react";
import classes from "./FormSelectInput.module.css";

export interface SelectOption {
  key: string;
  value: string;
  text: string;
}

const FormSelectInput: React.FC<{
  name: string;
  values: SelectOption[];
  disabled?: boolean;
  required?: boolean;
  value?: string;
  defaultValue: string;
  onChange: (value: string) => void;
}> = ({
  name,
  values,
  disabled = false,
  required = false,
  value = "",
  onChange,
  defaultValue,
}) => {
  const handleInputChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    onChange(e.target.value);
  };

  return (
    <div>
      <select
        style={value === "" ? { color: "#999" } : { color: "#f5f5f5" }}
        className={classes.input}
        id={name}
        name={name}
        required={required}
        disabled={disabled}
        value={value}
        onChange={handleInputChange}
      >
        <option value="">{defaultValue}</option>
        {values.map((value) => (
          <option key={value.key} value={value.value}>
            {value.text}
          </option>
        ))}
      </select>
    </div>
  );
};

export default FormSelectInput;
