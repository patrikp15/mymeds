import classes from "./FormLabelInput.module.css";
import ClearIcon from "@mui/icons-material/Close";

const FormLabelInput: React.FC<{
  label: string;
  id: string;
  inputStyle?: string;
  value: string;
  disabled?: boolean;
  required?: boolean;
  limit?: number;
  onChange: React.ChangeEventHandler<HTMLInputElement>;
  onClear?: () => void;
}> = ({
  disabled = false,
  required = true,
  inputStyle = "",
  limit = 100,
  value = "",
  onClear = () => {},
  ...props
}) => {
  return (
    <div className={classes.container}>
      <label className={classes.label} htmlFor={props.id}>
        {props.label}
      </label>
      <div className={classes.inputContainer}>
        <input
          id={props.id}
          name={props.id}
          className={`${classes.input} ${inputStyle}`}
          type="text"
          value={value}
          disabled={disabled}
          required={required}
          onChange={props.onChange}
          maxLength={limit}
        />
        <ClearIcon
          className={
            value !== "" && !disabled
              ? classes.clearIcon
              : classes.clearIconHidden
          }
          onClick={() => onClear()}
        />
      </div>
    </div>
  );
};

export default FormLabelInput;
