import { Form, Link, useNavigate } from "react-router-dom";
import AuthContainer from "../container/AuthContainer";
import classes from "./SignUpPage.module.css";
import general from "../Components.module.css";
import FormInput from "../ui/FormInput";
import FormDateInput from "../ui/FormDateInput";
import { relationshipTypes } from "../../model/auth/RelationshipTypeSelectOption";
import FormSelectInput from "../ui/FormSelectInput";
import { useState } from "react";
import { register } from "../../api/authApi";
import {
  relationshipTypeToString,
  stringToEnum,
} from "../../utils/EnumConverter";
import { RelationshipType } from "../../model/auth/RelationshipType";
import FormMessage from "../ui/FormMessage";
import {
  FILL_ALL_FIELDS,
  PASSWORDS_NOT_MATCH,
} from "../../model/ui/FormConstants";
import {
  checkAndReplaceEmail,
  checkAndReplaceMobileNumber,
  isFormValid,
} from "../../utils/FormUtils";
import SubmitBtn from "../ui/SubmitBtn";

const SignUpPage: React.FC<{}> = () => {
  const [userFormData, setUserFormData] = useState({
    firstname: "",
    middlename: "",
    lastname: "",
    password: "",
    passwordre: "",
    email: "",
    mobile: "",
    date: "",
  });
  const [guestFormData, setGuestFormData] = useState({
    guestFirstName: "",
    guestMiddleName: "",
    guestLastName: "",
    guestEmail: "",
    guestMobile: "",
    relationshipType: "",
  });
  const [activePhase, setActivePhase] = useState<number>(1);
  const phases: number[] = [1, 2, 3];
  const [formMessage, setFormMessage] = useState<string>("");
  const [isSuccess, setIsSuccess] = useState<boolean>(true);
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  const nextPhase = () => {
    if (activePhase < phases.length) {
      if (activePhase === 1) {
        const isUserFormValid = isFormValid(userFormData, ["middlename"]);

        if (!isUserFormValid) {
          setFormMessage(FILL_ALL_FIELDS);
          setIsSuccess(false);
          return;
        }
        if (userFormData.password !== userFormData.passwordre) {
          setFormMessage(PASSWORDS_NOT_MATCH);
          setIsSuccess(false);
          return;
        }
      }
      if (activePhase === 2) {
        const isGuestFormValid = isFormValid(guestFormData, [
          "guestMiddleName",
        ]);

        if (!isGuestFormValid) {
          setFormMessage(FILL_ALL_FIELDS);
          setIsSuccess(false);
          return;
        }
      }

      setActivePhase((prev) => prev + 1);
      setFormMessage("");
      setIsSuccess(true);
    }
  };

  const previousPhase = () => {
    if (activePhase > 1) {
      setActivePhase((prev) => prev - 1);
    }
    setFormMessage("");
    setIsSuccess(true);
  };

  const navigate = useNavigate();

  const handleUserFormInputChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { id, value } = e.target;
    if (id === "mobile" || id === "guestMobile") {
      setUserFormData((prevData) => ({
        ...prevData,
        [id]: checkAndReplaceMobileNumber(value),
      }));
      return;
    }
    if (id === "email" || id === "guestEmail") {
      setUserFormData((prevData) => ({
        ...prevData,
        [id]: checkAndReplaceEmail(value),
      }));
      return;
    }
    setUserFormData((prevData) => ({
      ...prevData,
      [id]: value,
    }));
  };

  const handleGuestFormInputChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { id, value } = e.target;
    setGuestFormData((prevData) => ({
      ...prevData,
      [id]: value,
    }));
  };

  const handleSelectChange = (name: string, value: string) => {
    setGuestFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleDateChange = (date: string) => {
    setUserFormData((prevData) => ({
      ...prevData,
      date: date,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      await register(userFormData, guestFormData);
      navigate("/user-medications");
    } catch (error) {
      setFormMessage("Počas registrácie sa vyskytla chyba");
      setIsSuccess(false);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <AuthContainer isSignup={true}>
      <div className={classes.signupContainer}>
        <div className={classes.headerContainer}>
          <h2 className={classes.signupHeader}>Vytvorte si profil</h2>
          <FormMessage message={formMessage} isSuccess={isSuccess} />
        </div>
        <nav className={classes.phaseContainer} aria-label="Registration steps">
          {[1, 2, 3].map((phase) => (
            <button
              key={phase}
              className={`${classes.phaseIndicator} ${
                phase === activePhase ? classes.activePhase : ""
              }`}
            >
              {phase}
            </button>
          ))}
        </nav>
        <Form className={classes.form} onSubmit={handleSubmit}>
          {activePhase === 1 && (
            <div className={classes.formContainer}>
              <div className={classes.formRow}>
                <h3 className={classes.formHeader}>Užívateľ</h3>
              </div>
              <div className={classes.formRow}>
                <FormInput
                  id="firstname"
                  type="text"
                  placeholder="Meno*"
                  required={true}
                  onChange={handleUserFormInputChange}
                  value={userFormData.firstname}
                />
                <FormInput
                  id="middlename"
                  type="text"
                  placeholder="Stredné meno"
                  onChange={handleUserFormInputChange}
                  value={userFormData.middlename}
                />
                <FormInput
                  id="lastname"
                  type="text"
                  placeholder="Priezvisko*"
                  required={true}
                  onChange={handleUserFormInputChange}
                  value={userFormData.lastname}
                />
              </div>
              <div className={classes.formRow}>
                <FormInput
                  id="password"
                  type="password"
                  placeholder="Zadajte heslo*"
                  required={true}
                  onChange={handleUserFormInputChange}
                  value={userFormData.password}
                />
                <FormInput
                  id="passwordre"
                  type="password"
                  placeholder="Zopakujte heslo*"
                  required={true}
                  onChange={handleUserFormInputChange}
                  value={userFormData.passwordre}
                />
              </div>
              <div className={classes.formRow}>
                <FormInput
                  id="email"
                  type="email"
                  placeholder="Email*"
                  required={true}
                  onChange={handleUserFormInputChange}
                  value={userFormData.email}
                />
                <FormInput
                  id="mobile"
                  type="text"
                  placeholder="Mobilné číslo*"
                  required={true}
                  onChange={handleUserFormInputChange}
                  value={userFormData.mobile}
                />
              </div>
              <div className={classes.formRow}>
                <FormDateInput
                  name="date"
                  onChange={(date) => {
                    handleDateChange(date);
                  }}
                  value={userFormData.date}
                />
              </div>
            </div>
          )}
          {activePhase === 2 && (
            <div className={classes.formContainer}>
              {/* Guest */}
              <div className={classes.formRow}>
                <h3 className={classes.formHeader}>Kontaktná osoba</h3>
              </div>
              <div className={classes.formRow}>
                <FormInput
                  id="guestFirstName"
                  type="text"
                  placeholder="Meno*"
                  required={true}
                  onChange={handleGuestFormInputChange}
                  value={guestFormData.guestFirstName}
                />
                <FormInput
                  id="guestMiddleName"
                  type="text"
                  placeholder="Stredné meno"
                  onChange={handleGuestFormInputChange}
                  value={guestFormData.guestMiddleName}
                />
                <FormInput
                  id="guestLastName"
                  type="text"
                  placeholder="Priezvisko*"
                  required={true}
                  onChange={handleGuestFormInputChange}
                  value={guestFormData.guestLastName}
                />
              </div>
              <div className={classes.formRow}>
                <FormInput
                  id="guestEmail"
                  type="email"
                  placeholder="Email*"
                  required={true}
                  onChange={handleGuestFormInputChange}
                  value={guestFormData.guestEmail}
                />
                <FormInput
                  id="guestMobile"
                  type="text"
                  placeholder="Mobilné číslo*"
                  required={true}
                  onChange={handleGuestFormInputChange}
                  value={guestFormData.guestMobile}
                />
                <FormSelectInput
                  name="relationshipType"
                  values={relationshipTypes}
                  onChange={(value) =>
                    handleSelectChange("relationshipType", value)
                  }
                  defaultValue="Vzťah k užívateľovi*"
                  value={guestFormData.relationshipType}
                />
              </div>
            </div>
          )}
          {activePhase === 3 && (
            <div className={classes.formContainer}>
              <div className={classes.summaryContainer}>
                <h3 className={classes.summaryHeader}>Užívateľ</h3>
                <div className={classes.summaryRow}>
                  <div>
                    <label className={classes.summaryLabel}>Prvé meno</label>
                    <p className={classes.summaryValue}>
                      {userFormData.firstname}
                    </p>
                  </div>
                  <div>
                    <label className={classes.summaryLabel}>Stredné meno</label>
                    <p className={classes.summaryValue}>
                      {userFormData.middlename}
                    </p>
                  </div>
                  <div>
                    <label className={classes.summaryLabel}>Priezvisko</label>
                    <p className={classes.summaryValue}>
                      {userFormData.lastname}
                    </p>
                  </div>
                </div>
                <div className={classes.summaryRow}>
                  <div>
                    <label className={classes.summaryLabel}>Email</label>
                    <p className={classes.summaryValue}>{userFormData.email}</p>
                  </div>
                  <div>
                    <label className={classes.summaryLabel}>Mobil</label>
                    <p className={classes.summaryValue}>
                      {userFormData.mobile}
                    </p>
                  </div>
                  <div>
                    <label className={classes.summaryLabel}>
                      Dátum narodenia
                    </label>
                    <p className={classes.summaryValue}>{userFormData.date}</p>
                  </div>
                </div>
                <h3 className={classes.summaryHeader}>Kontaktná osoba</h3>
                <div className={classes.summaryRow}>
                  <div>
                    <label className={classes.summaryLabel}>Prvé meno</label>
                    <p className={classes.summaryValue}>
                      {guestFormData.guestFirstName}
                    </p>
                  </div>
                  <div>
                    <label className={classes.summaryLabel}>Stredné meno</label>
                    <p className={classes.summaryValue}>
                      {guestFormData.guestMiddleName}
                    </p>
                  </div>
                  <div>
                    <label className={classes.summaryLabel}>Priezvisko</label>
                    <p className={classes.summaryValue}>
                      {guestFormData.guestLastName}
                    </p>
                  </div>
                </div>
                <div className={classes.summaryRow}>
                  <div>
                    <label className={classes.summaryLabel}>Email</label>
                    <p className={classes.summaryValue}>
                      {guestFormData.guestEmail}
                    </p>
                  </div>
                  <div>
                    <label className={classes.summaryLabel}>Mobil</label>
                    <p className={classes.summaryValue}>
                      {guestFormData.guestMobile}
                    </p>
                  </div>
                  <div>
                    <label className={classes.summaryLabel}>
                      Vzťah k užívateľovi
                    </label>
                    <p className={classes.summaryValue}>
                      {guestFormData.relationshipType !== ""
                        ? relationshipTypeToString(
                            stringToEnum(
                              guestFormData.relationshipType,
                              RelationshipType
                            )
                          )
                        : ""}
                    </p>
                  </div>
                </div>
              </div>
              <SubmitBtn name="Registrovať" isSubmitting={isSubmitting} />
            </div>
          )}
        </Form>
        <div className={classes.phaseBtnContainer}>
          <button
            className={`${classes.phaseBtn} ${
              activePhase === 1 ? classes.phaseBtnDisabled : ""
            }`}
            onClick={previousPhase}
            disabled={activePhase === 1}
          >
            Predošlý krok
          </button>
          <button
            className={`${classes.phaseBtn} ${
              activePhase === phases.length ? classes.phaseBtnDisabled : ""
            }`}
            onClick={nextPhase}
            disabled={activePhase === phases.length}
          >
            Ďalší krok
          </button>
        </div>
        <Link to="/login" className={classes.loginBtn}>
          Už máte konto ? <span>Prihlásiť sa</span>
        </Link>
      </div>
    </AuthContainer>
  );
};

export default SignUpPage;
