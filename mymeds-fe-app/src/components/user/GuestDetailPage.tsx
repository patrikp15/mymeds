import { Form, useLocation, useNavigate } from "react-router-dom";
import FormContainer from "../container/FormContainer";
import classes from "./GuestDetailPage.module.css";
import SubmitBtn from "../ui/SubmitBtn";
import FormLabelInput from "../ui/FormLabelInput";
import { useEffect, useMemo, useState } from "react";
import { Guest } from "../../model/user/Guest";
import BackIcon from "@mui/icons-material/ArrowBack";
import FormActionBtn from "../ui/FormActionBtn";
import {
  userStatusToClassColor,
  userStatusToString,
} from "../../utils/EnumConverter";
import YesNoWindowPopup from "../ui/YesNoWindowPopup";
import general from "../Components.module.css";
import { UserStatus } from "../../model/user/UserStatus";
import FormLabelSelectInput from "../ui/FormLabelSelectInput";
import { relationshipTypes } from "../../model/auth/RelationshipTypeSelectOption";
import { changeGuestStatus, createGuest } from "../../api/guestApi";
import {
  checkAndReplaceEmail,
  checkAndReplaceMobileNumber,
  hasFormChanged,
} from "../../utils/FormUtils";
import { verifyGuest } from "../../api/authApi";
import FormMessage from "../ui/FormMessage";

const GuestDetailPage: React.FC<{}> = () => {
  const [isDeactivatePopupOpen, setIsDeactivatePopupOpen] =
    useState<boolean>(false);
  const [isVerifiedPopupOpen, setIsVerifiedPopupOpen] =
    useState<boolean>(false);
  const location = useLocation();
  const guest = location.state?.item as Guest;

  const initialFormData = useMemo(
    () => ({
      guestId: guest?.guestId ?? "",
      firstName: guest?.firstName ?? "",
      middleName: guest?.middleName ?? "",
      lastName: guest?.lastName ?? "",
      email: guest?.email ?? "",
      mobileNumber: guest?.mobileNumber ?? "",
      relationshipType: guest?.relationshipType ?? "",
      userStatus: guest?.userStatus ?? UserStatus.NOT_VERIFIED,
    }),
    [guest]
  );

  const [formData, setFormData] = useState(initialFormData);
  const [isNew, setIsNew] = useState<boolean>(false);
  const [isSaveDiabled, setIsSaveDisabled] = useState<boolean>(false);
  const [formMessage, setFormMessage] = useState<string>("");
  const [isSuccess, setIsSuccess] = useState<boolean>(true);
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  useEffect(() => {
    if (!guest) {
      setIsNew(true);
    }
  }, [guest]);

  useEffect(() => {
    const isFormChanged = hasFormChanged(formData, initialFormData);
    setIsSaveDisabled(!isFormChanged);
  }, [formData, initialFormData]);

  const navigation = useNavigate();

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

  const handleSelectChange = (name: string, value: string) => {
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
    setFormMessage("");
    setIsSuccess(true);
  };

  const handleClear = (id: string) => {
    setFormData((prevData) => ({
      ...prevData,
      [id]: "",
    }));
    setFormMessage("");
    setIsSuccess(true);
  };

  const handleBackIcon = () => {
    navigation("/guests");
  };

  const handleSubmit = async (e: React.FormEvent) => {
    setIsSubmitting(true);
    e.preventDefault();
    try {
      await createGuest(formData);
      navigation("/guests");
    } catch (error) {
      setFormMessage("Vytvorenie kontaktnej osoby zlyhalo");
      setIsSuccess(false);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDeactivate = async (): Promise<void> => {
    try {
      await changeGuestStatus(UserStatus.NOT_ACTIVE, formData.guestId);
      navigation("/guests");
    } catch (error) {
      console.error(error);
      setFormMessage("Deaktivácia zlyhala");
      setIsSuccess(false);
    } finally {
      setIsDeactivatePopupOpen(false);
    }
  };

  const handleVerifyUser = async (): Promise<void> => {
    try {
      await verifyGuest(formData.guestId);
      setFormMessage("Verifikačný email odoslaný.");
      setIsSuccess(true);
    } catch (error) {
      console.error(error);
      setFormMessage("Odoslanie verifikačného emailu zlyhalo");
      setIsSuccess(false);
    } finally {
      setIsVerifiedPopupOpen(false);
    }
  };

  return (
    <FormContainer activeLink="/profile">
      <Form className={classes.form} onSubmit={handleSubmit}>
        <div className={classes.formRow2SpaceBetween}>
          <BackIcon
            className={classes.backIcon}
            onClick={() => handleBackIcon()}
          />
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
            label="Prvé meno*"
            value={formData.firstName}
            disabled={!isNew}
            onChange={handleFormInputChange}
            onClear={() => handleClear("firstName")}
          />
          <FormLabelInput
            id="midleName"
            label="Stredné meno"
            value={formData.middleName}
            disabled={!isNew}
            required={false}
            onChange={handleFormInputChange}
            onClear={() => handleClear("middleName")}
          />
          <FormLabelInput
            id="lastName"
            label="Priezvisko*"
            value={formData.lastName}
            disabled={!isNew}
            onChange={handleFormInputChange}
            onClear={() => handleClear("lastName")}
          />
        </div>
        <div className={classes.formRow2}>
          <FormLabelInput
            id="email"
            label="Email*"
            value={formData.email}
            disabled={!isNew}
            onChange={handleFormInputChange}
            onClear={() => handleClear("email")}
          />
          <FormLabelInput
            id="mobileNumber"
            label="Mobilné číslo*"
            disabled={!isNew}
            value={formData.mobileNumber}
            onChange={handleFormInputChange}
            onClear={() => handleClear("mobileNumber")}
          />
        </div>
        <div className={classes.formRow2}>
          <FormLabelSelectInput
            id="relationshipType"
            label="Vzťah k užívateľovi*"
            values={relationshipTypes}
            value={formData.relationshipType}
            disabled={!isNew}
            onChange={(value) => handleSelectChange("relationshipType", value)}
          />
        </div>
        <div className={classes.formRow2SpaceBetween}>
          <SubmitBtn
            name="Pridať"
            isVisible={isNew}
            isDisabled={isSaveDiabled}
            isSubmitting={isSubmitting}
          />
          <FormActionBtn
            name1="Verifiovať"
            name2="Deaktivovať"
            show1={!isNew && formData.userStatus === UserStatus.NOT_VERIFIED}
            show2={!isNew && formData.userStatus === UserStatus.ACTIVE}
            onClick1={() => {
              setIsDeactivatePopupOpen(false);
              setIsVerifiedPopupOpen(true);
            }}
            onClick2={() => {
              setIsDeactivatePopupOpen(true);
              setIsVerifiedPopupOpen(false);
            }}
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
              Na emailovú adresu <span>{formData.email}</span> sa odošle
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
              Chystáte sa deaktivovať tento kontakt.
              <br />
              Chcete pokračovať ?
            </p>
          }
          onYes={() => handleDeactivate()}
          onNo={() => setIsDeactivatePopupOpen(false)}
        />
      )}
    </FormContainer>
  );
};

export default GuestDetailPage;
