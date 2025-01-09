import classes from "./MainContainer.module.css";
import React, { ReactNode } from "react";

const MainContainer: React.FC<{
  children: ReactNode;
}> = ({ children }) => {
  return (
    <div className={classes.container}>
      <div className={classes.contentContainer}>{children}</div>
    </div>
  );
};

export default MainContainer;
