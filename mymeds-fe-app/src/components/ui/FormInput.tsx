import React from "react";
import classes from "./FormInput.module.css";

const FormInput: React.FC<{
  id: string;
  inputStyle?: string;
  type: string;
  placeholder: string;
  value?: string;
  disabled?: boolean;
  required?: boolean;
  onChange: React.ChangeEventHandler<HTMLInputElement>;
}> = ({
  inputStyle = classes.input,
  value = "",
  required = false,
  disabled = false,
  ...props
}) => {
  return (
    <input
      id={props.id}
      name={props.id}
      className={inputStyle}
      type={props.type}
      placeholder={props.placeholder}
      value={value}
      disabled={disabled}
      required={required}
      onChange={props.onChange}
    />
  );
};

export default FormInput;
