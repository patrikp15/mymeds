import classes from "./FormMessage.module.css";
import FailIcon from "@mui/icons-material/PriorityHigh";
import SuccessIcon from "@mui/icons-material/Check";

const FormMessage: React.FC<{
  message: string;
  isSuccess: boolean;
}> = ({ message, isSuccess }) => {
  return (
    <>
      {message !== "" ? (
        <div
          className={`${classes.container} ${
            isSuccess ? classes.success : classes.fail
          }`}
        >
          {isSuccess ? (
            <SuccessIcon className={classes.icon} />
          ) : (
            <FailIcon className={classes.icon} />
          )}
          <p>{message}</p>
        </div>
      ) : (
        ""
      )}
    </>
  );
};

export default FormMessage;
