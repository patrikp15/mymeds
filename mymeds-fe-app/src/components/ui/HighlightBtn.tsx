import { Link } from "react-router-dom";
import classes from "./HighlightBtn.module.css";
import general from "../Components.module.css";
import React from "react";

const HighlightBtn: React.FC<{ linkTo: string; text: string }> = ({
  linkTo,
  text,
}) => {
  return (
    <Link className={`${general.btn} ${classes.highlightBtn}`} to={linkTo}>
      {text}
    </Link>
  );
};

export default HighlightBtn;
