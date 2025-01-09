import classes from "./LoadingPage.module.css";

const LoadingPage: React.FC<{}> = () => {
  return (
    <div className={classes.container}>
      <div className={classes.loader}></div>
    </div>
  );
};

export default LoadingPage;
