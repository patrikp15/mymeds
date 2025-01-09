import classes from "./YesNoWindowPopup.module.css";

const YesNoWindowPopup: React.FC<{
  text: React.ReactNode;
  style?: string;
  onYes: () => void;
  onNo: () => void;
}> = ({ text, style = "", onYes, onNo }) => {
  return (
    <div className={classes.container}>
      <div className={classes.textContainer}>{text}</div>
      <div className={classes.btnContainer}>
        <button className={classes.btn} onClick={() => onYes()}>
          Áno
        </button>
        <button className={classes.btn} onClick={() => onNo()}>
          Nie
        </button>
      </div>
    </div>
  );
};

export default YesNoWindowPopup;
