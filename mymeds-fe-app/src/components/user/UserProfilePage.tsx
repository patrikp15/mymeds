import { Form, Link } from "react-router-dom";
import FormContainer from "../container/FormContainer";
import classes from "./UserProfilePage.module.css";
import SubmitBtn from "../ui/SubmitBtn";
import FormLabelInput from "../ui/FormLabelInput";
import { useEffect, useState } from "react";
import FormLabelDateInput from "../ui/FormLabelDateInput";
import ForwardIcon from "@mui/icons-material/ChevronRight";
import FormActionBtn from "../ui/FormActionBtn";
import { convertDate, DEFAULT_APP_DATE_FORMAT } from "../../utils/DateUtils";
import { UserStatus } from "../../model/user/UserStatus";
import { UserDetail } from "../../model/user/UserDetail";
import {
  userStatusToClassColor,
  userStatusToString,
} from "../../utils/EnumConverter";
import YesNoWindowPopup from "../ui/YesNoWindowPopup";
import general from "../Components.module.css";
import { getUserDetail } from "../../api/userApi";
import { updateUser, verifyUser } from "../../api/authApi";
import {
  checkAndReplaceEmail,
  checkAndReplaceMobileNumber,
  hasFormChanged,
  isFormValid,
} from "../../utils/FormUtils";
import FormMessage from "../ui/FormMessage";
import {
  FILL_ALL_FIELDS,
  SAVE_SUCCESSFULL,
  SAVE_UNSUCCESSFULL,
} from "../../model/ui/FormConstants";

const UserProfilePage: React.FC<{}> = () => {
  const defaultFormData: UserDetail = {
    firstName: "",
    middleName: "",
    lastName: "",
    email: "",
    mobileNumber: "",
    dateOfBirth: "",
    userStatus: UserStatus.NOT_VERIFIED,
  };
  const [formData, setFormData] = useState<UserDetail>(defaultFormData);
  const [initialFormData, setInitialFormData] =
    useState<UserDetail>(defaultFormData);
  const [isVerifiedPopupOpen, setIsVerifiedPopupOpen] =
    useState<boolean>(false);
  const [isDeactivatePopupOpen, setIsDeactivatePopupOpen] =
    useState<boolean>(false);
  const [formMessage, setFormMessage] = useState<string>("");
  const [isSuccess, setIsSuccess] = useState<boolean>(true);
  const [isSaveDiabled, setIsSaveDisabled] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  useEffect(() => {
    setIsLoading(true);
    const fetchUserDetail = async () => {
      try {
        const res = await getUserDetail();
        setFormData((prevData) => ({
          ...prevData,
          ...res,
        }));
        setInitialFormData((prevData) => ({
          ...prevData,
          ...res,
        }));
      } catch (error) {
        console.error("Failed to fetch user details:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchUserDetail();
  }, []);

  useEffect(() => {
    const isFormChanged = hasFormChanged(formData, initialFormData);
    setIsSaveDisabled(!isFormChanged);
  }, [formData, initialFormData]);

  const handleFormInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;
    if (id === "mobileNumber") {
      setFormData((prevData) => ({
        ...prevData,
        [id]: checkAndReplaceMobileNumber(value),
      }));
      return;
    }
    if (id === "email") {
      setFormData((prevData) => ({
        ...prevData,
        [id]: checkAndReplaceEmail(value),
      }));
      return;
    }
    setFormData((prevData) => ({
      ...prevData,
      [id]: value,
    }));
    setFormMessage("");
    setIsSuccess(true);
  };

  const handleClear = (id: string) => {
    setFormData((prevData) => ({
      ...prevData,
      [id]: "",
    }));
  };

  const handleDateChange = (date: string) => {
    setFormData((prevData) => ({
      ...prevData,
      dateOfBirth: date,
    }));
    setFormMessage("");
    setIsSuccess(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      const formValid = isFormValid({
        emailField: formData.email,
        mobileField: formData.mobileNumber,
      });
      if (!formValid) {
        setFormMessage(FILL_ALL_FIELDS);
        setIsSuccess(false);
        return;
      }
      await updateUser({
        email: formData.email,
        mobileNumber: formData.mobileNumber,
      });
      setFormMessage(SAVE_SUCCESSFULL);
      setIsSuccess(true);
    } catch (error) {
      setFormMessage(SAVE_UNSUCCESSFULL);
      setIsSuccess(false);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleVerifyUser = async (): Promise<void> => {
    try {
      await verifyUser();
    } catch (error) {
      console.error(error);
    } finally {
      setIsVerifiedPopupOpen(false);
    }
  };

  return (
    <FormContainer activeLink="/profile" isLoading={isLoading}>
      <Form className={classes.form} onSubmit={handleSubmit}>
        <div className={classes.formRow2SpaceBetween}>
          <h3 className={classes.formHeader}>Môj profil</h3>
          <FormMessage message={formMessage} isSuccess={isSuccess} />
        </div>
        <div className={classes.formRow}>
          <div
            className={`${classes.statusContainer} ${
              classes.row
            } ${userStatusToClassColor(formData.userStatus)}`}
          >
            <p className={classes.text}>
              {userStatusToString(formData.userStatus)}
            </p>
          </div>
        </div>
        <div className={classes.formRow3}>
          <FormLabelInput
            id="firstName"
            label="Prvé meno"
            value={formData.firstName}
            disabled={true}
            onChange={() => {}}
          />
          <FormLabelInput
            id="midleName"
            label="Stredné meno"
            value={formData.middleName}
            disabled={true}
            onChange={() => {}}
          />
          <FormLabelInput
            id="lastName"
            label="Priezvisko"
            value={formData.lastName}
            disabled={true}
            onChange={() => {}}
          />
        </div>
        <div className={classes.formRow2}>
          <FormLabelInput
            id="email"
            label="Email*"
            value={formData.email}
            onChange={handleFormInputChange}
            onClear={() => handleClear("email")}
          />
          <FormLabelInput
            id="mobileNumber"
            label="Mobilné číslo*"
            value={formData.mobileNumber}
            onChange={handleFormInputChange}
            onClear={() => handleClear("mobileNumber")}
          />
        </div>
        <div className={classes.formRow2}>
          <FormLabelDateInput
            label="Dátum narodenia"
            id="dateOfBirth"
            value={
              formData.dateOfBirth
                ? convertDate(formData.dateOfBirth, DEFAULT_APP_DATE_FORMAT)
                : ""
            }
            onChange={(date) => handleDateChange(date)}
            onClear={() => handleClear("dateOfBirth")}
            disabled={true}
          />
          <div className={classes.guestsBtnContainer}>
            <Link className={classes.guestsBtn} to="/guests">
              Spravovať kontaktné osoby
            </Link>
            <ForwardIcon className={classes.guestsIcon} aria-hidden="true" />
          </div>
        </div>
        <div className={classes.formRow2SpaceBetween}>
          <SubmitBtn
            name="Uložiť"
            isDisabled={isSaveDiabled}
            isSubmitting={isSubmitting}
          />
          <FormActionBtn
            name1="Verifiovať"
            name2="Deaktivovať"
            onClick1={() => {
              setIsDeactivatePopupOpen(false);
              setIsVerifiedPopupOpen(true);
            }}
            onClick2={() => {
              setIsVerifiedPopupOpen(false);
              setIsDeactivatePopupOpen(true);
            }}
            show1={formData.userStatus === UserStatus.NOT_VERIFIED}
            disabled1={isDeactivatePopupOpen}
            disabled2={isVerifiedPopupOpen}
          />
        </div>
      </Form>

      {(isVerifiedPopupOpen || isDeactivatePopupOpen) && (
        <div className={general.overlay}></div>
      )}

      {isVerifiedPopupOpen && (
        <YesNoWindowPopup
          text={
            <p>
              Na Vašu emailovú adresu <span>{formData.email}</span> sa odošle
              verifikačný email.
              <br />
              Chcete pokračovať ?
            </p>
          }
          onYes={() => handleVerifyUser()}
          onNo={() => setIsVerifiedPopupOpen(false)}
        />
      )}
      {isDeactivatePopupOpen && (
        <YesNoWindowPopup
          text={
            <p>
              Chystáte sa deaktivovať Váš účet.
              <br />
              Chcete pokračovať ?
            </p>
          }
          onYes={() => {}}
          onNo={() => setIsDeactivatePopupOpen(false)}
        />
      )}
    </FormContainer>
  );
};

export default UserProfilePage;
