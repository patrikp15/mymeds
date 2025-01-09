import classes from "./FormLabelInput.module.css";
import { SelectOption } from "./FormSelectInput";

const FormLabelSelectInput: React.FC<{
  id: string;
  label: string;
  values: SelectOption[];
  disabled?: boolean;
  required?: boolean;
  value: string;
  defaultValue?: string;
  onChange: (value: string) => void;
}> = ({
  id,
  label,
  values,
  disabled = false,
  required = false,
  value,
  defaultValue = "",
  onChange,
}) => {
  const handleInputChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    onChange(e.target.value);
  };

  return (
    <div className={classes.container}>
      <label className={classes.label} htmlFor={id}>
        {label}
      </label>
      <div className={classes.inputContainer}>
        <select
          className={classes.input}
          id={id}
          name={id}
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
    </div>
  );
};

export default FormLabelSelectInput;
