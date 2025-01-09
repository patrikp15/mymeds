import classes from "./ResetBtn.module.css";

const ResetBtn: React.FC<{
  name: string;
  style?: string;
  onClick: () => void;
}> = ({ name, style = "", onClick }) => {
  return (
    <button
      type="reset"
      className={`${classes.resetBtn} ${style}`}
      onClick={onClick}
    >
      {name}
    </button>
  );
};

export default ResetBtn;
