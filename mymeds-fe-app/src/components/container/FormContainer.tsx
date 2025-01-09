import MenuContainer from "./MenuContainer";
import classes from "./FormContainer.module.css";
import { ReactNode } from "react";

const FormContainer: React.FC<{
  children: ReactNode;
  activeLink?: string;
  isLoading?: boolean;
}> = ({ children, activeLink = "", isLoading = false }) => {
  return (
    <MenuContainer active={activeLink} isLoading={isLoading}>
      <div className={classes.mainContainer}>{children}</div>
    </MenuContainer>
  );
};

export default FormContainer;
