import { Link } from "react-router-dom";
import AuthContainer from "../container/AuthContainer";
import classes from "./WelcomePage.module.css";
import HighlightBtn from "../ui/HighlightBtn";
import React from "react";

const WelcomePage: React.FC<{}> = () => {
  return (
    <AuthContainer>
      <div className={classes.welcomeContainer}>
        <div className={classes.textContainer}>
          <h1 className={classes.welcomeHeader}>MojeLieky.com</h1>
          <h2 className={classes.welcomeText}>
            <span>Vaša digitálna lekárnička.</span>
            <span>Jednoduché, bezpečné, spoľahlivé.</span>
          </h2>
        </div>
        <div className={classes.btnContainer}>
          <HighlightBtn linkTo="/signup" text="Chcem sa pridať" />
          <Link to="/login" className={classes.signInBtn}>
            Už máte konto ? <span>Prihlásiť sa</span>
          </Link>
        </div>
      </div>
    </AuthContainer>
  );
};

export default WelcomePage;
