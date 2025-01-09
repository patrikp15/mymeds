import classes from "./FormLabelDateInput.module.css";
import ClearIcon from "@mui/icons-material/Close";

const FormLabelDateInput: React.FC<{
  label: string;
  id: string;
  placeholder?: string;
  disabled?: boolean;
  value: string;
  inputStyle?: string;
  onChange: (value: string) => void;
  onClear?: () => void;
}> = ({
  disabled = false,
  placeholder = "",
  inputStyle = "",
  onClear = () => {},
  ...props
}) => {
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

    props.onChange(input); // Notify parent
  };

  return (
    <div className={classes.container}>
      <label className={classes.label} htmlFor={props.id}>
        {props.label}
      </label>
      <div className={classes.inputContainer}>
        <input
          id={props.id}
          name={props.id}
          type="text"
          className={`${classes.input} ${inputStyle}`}
          value={props.value}
          onChange={handleInputChange}
          disabled={disabled}
          placeholder={placeholder}
          maxLength={10}
          autoComplete="off"
        />
        <ClearIcon
          className={
            props.value !== "" && !disabled
              ? classes.clearIcon
              : classes.clearIconHidden
          }
          onClick={() => onClear()}
        />
      </div>
    </div>
  );
};

export default FormLabelDateInput;
