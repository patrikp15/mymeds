import classes from "./AuthContainer.module.css";
import React, { ReactNode } from "react";
import backgroundImage from "../../assets/images/register.png";

const AuthContainer: React.FC<{
  children: ReactNode;
  isSignup?: boolean;
}> = ({ children, isSignup = false }) => {
  return (
    <div className={classes.container}>
      <div
        className={`${
          isSignup ? classes.authContainerSignup : classes.authContainerBasic
        } ${classes.authContainer}`}
      >
        {!isSignup ? (
          <div className={classes.leftContainer}>
            <img
              src={backgroundImage}
              alt="Person surrounded by healthcare thematic things"
              className={classes.image}
            />
          </div>
        ) : (
          ""
        )}
        <div className="righContainer">{children}</div>
      </div>
    </div>
  );
};

export default AuthContainer;
