import { Link, NavLink } from "react-router-dom";
import MainContainer from "./MainContainer";
import classes from "./MenuContainer.module.css";
import React, { ReactNode, useState } from "react";
import MedicationIcon from "@mui/icons-material/MedicationRounded";
import AddIcon from "@mui/icons-material/AddCircle";
import MenuIcon from "@mui/icons-material/Menu";
import CloseIcon from "@mui/icons-material/Close";
import PersonIcon from "@mui/icons-material/Person";
import ShareIcon from "@mui/icons-material/Share";
import LogoutIcon from "@mui/icons-material/Logout";
import { logout } from "../../api/authApi";
import LoadingPage from "../ui/LoadingPage";

const MenuContainer: React.FC<{
  active: string;
  children: ReactNode;
  isLoading?: boolean;
}> = ({ children, active, isLoading = false }) => {
  const [isMenuOpen, setIsMenuOpen] = useState<boolean>(true);
  const [activeLink, setActiveLink] = useState<string>(active);

  const handleLinkClick = (link: string) => {
    setActiveLink(link);
  };

  const handleLogout = () => {
    logout();
  };

  return (
    <MainContainer>
      <header className={classes.headerContainer}>
        <div className={classes.logoContainer}>
          {!isMenuOpen && (
            <MenuIcon
              className={classes.menuIcon}
              onClick={() => setIsMenuOpen(true)}
            />
          )}
          {isMenuOpen && (
            <CloseIcon
              className={classes.menuIcon}
              onClick={() => setIsMenuOpen(false)}
            />
          )}
          <h1 className={classes.logoHeader}>MojeLieky.com</h1>
        </div>
        <div className={classes.logoutContainer}>
          <div className={classes.addBtnContainer}>
            <AddIcon className={classes.addIcon} aria-hidden="true" />
            <Link
              className={classes.addBtn}
              to="/add-user-medications"
              onClick={() => {
                setActiveLink("");
              }}
            >
              Pridať nový liek
            </Link>
          </div>
          <LogoutIcon
            className={classes.menuIcon}
            onClick={() => handleLogout()}
          />
        </div>
      </header>
      <div
        className={isMenuOpen ? classes.containerOpen : classes.containerClosed}
      >
        <aside
          className={
            isMenuOpen ? classes.menuContainer : classes.menuContainerHide
          }
        >
          <nav className={classes.nav} aria-label="Main navigation">
            <ul className={classes.navList}>
              <li
                className={
                  activeLink === "/user-medications" ? classes.activeLink : ""
                }
              >
                <MedicationIcon
                  className={classes.navIcon}
                  aria-hidden="true"
                />
                <NavLink
                  to="/user-medications"
                  className={classes.link}
                  onClick={() => handleLinkClick("/user-medications")}
                >
                  Moje lieky
                </NavLink>
              </li>
              <li
                className={activeLink === "/profile" ? classes.activeLink : ""}
              >
                <PersonIcon className={classes.navIcon} aria-hidden="true" />
                <NavLink
                  to="/profile"
                  className={classes.link}
                  onClick={() => handleLinkClick("/profile")}
                >
                  Môj profil
                </NavLink>
              </li>
              <li className={activeLink === "/share" ? classes.activeLink : ""}>
                <ShareIcon className={classes.navIcon} aria-hidden="true" />
                <NavLink
                  to="/share"
                  className={classes.link}
                  onClick={() => handleLinkClick("/share")}
                >
                  Zdieľanie
                </NavLink>
              </li>
            </ul>
          </nav>
        </aside>
        {isLoading ? (
          <LoadingPage />
        ) : (
          <div className={classes.contentContainer}>{children}</div>
        )}
      </div>
    </MainContainer>
  );
};

export default MenuContainer;
