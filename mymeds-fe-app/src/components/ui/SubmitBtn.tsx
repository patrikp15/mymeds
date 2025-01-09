import classes from "./SubmitBtn.module.css";

const SubmitBtn: React.FC<{
  name: string;
  isVisible?: boolean;
  isDisabled?: boolean;
  isSubmitting?: boolean;
}> = ({ name, isVisible = true, isDisabled = false, isSubmitting = false }) => {
  return (
    <button
      type="submit"
      className={`${isVisible ? classes.submitBtn : classes.hidden} ${
        isDisabled ? classes.disabled : classes.active
      }`}
      disabled={isDisabled}
    >
      {isSubmitting ? <span className={classes.loader}></span> : name}
    </button>
  );
};

export default SubmitBtn;
