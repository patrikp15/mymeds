import classes from "./FormActionBtn.module.css";

const FormActionBtn: React.FC<{
  name1: string;
  name2: string;
  style1?: string;
  style2?: string;
  show1?: boolean;
  show2?: boolean;
  onClick1: () => void;
  onClick2: () => void;
  disabled1?: boolean;
  disabled2?: boolean;
}> = ({
  name1,
  name2,
  style1 = "",
  style2 = "",
  show1 = true,
  show2 = true,
  onClick1,
  onClick2,
  disabled1 = false,
  disabled2 = false,
}) => {
  return (
    <div className={classes.container}>
      {show1 && (
        <button
          className={`${classes.btn} ${classes.firstBtn} ${style1}`}
          onClick={() => onClick1()}
          disabled={disabled1}
          type="button"
        >
          {name1}
        </button>
      )}
      {show2 && (
        <button
          className={`${classes.btn} ${classes.secondBtn} ${style2}`}
          onClick={() => onClick2()}
          disabled={disabled2}
          type="button"
        >
          {name2}
        </button>
      )}
    </div>
  );
};

export default FormActionBtn;
