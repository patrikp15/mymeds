import { Form, Link, useNavigate, useSearchParams } from "react-router-dom";
import AuthContainer from "../container/AuthContainer";
import classes from "./ForgotPasswordPage.module.css";
import general from "../Components.module.css";
import FormInput from "../ui/FormInput";
import { useEffect, useState } from "react";
import { checkAndReplaceEmail } from "../../utils/FormUtils";
import FormMessage from "../ui/FormMessage";
import { VERIFICATION_EMAIL_SENT } from "../../model/ui/FormConstants";
import { changePassword, resetPassword } from "../../api/authApi";
import { AuthTokenStep } from "../../model/auth/AuthTokenStep";
import SubmitBtn from "../ui/SubmitBtn";

const ForgotPasswordPage: React.FC<{}> = () => {
  const [formData, setFormData] = useState({
    tokenStep: AuthTokenStep.NOTIFICATION_STEP,
    password: "",
    passwordRe: "",
    token: "",
    email: "",
  });
  const [step, setStep] = useState<number>(1);
  const [formMessage, setFormMessage] = useState<string>("");
  const [isSuccess, setIsSuccess] = useState<boolean>(true);
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    const emailToken = searchParams.get("token");
    if (emailToken) {
      setStep(2);
      setFormData((prevData) => ({
        ...prevData,
        tokenStep: AuthTokenStep.VERIFICATION_STEP,
        token: emailToken,
      }));
    }
  }, [searchParams]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;
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

  const handleResetPassword = async (e: React.FormEvent) => {
    setIsSubmitting(true);
    e.preventDefault();
    try {
      setFormData((prevData) => ({
        ...prevData,
        tokenStep: AuthTokenStep.NOTIFICATION_STEP,
      }));
      await resetPassword(formData);
      setIsSuccess(true);
      setFormMessage(VERIFICATION_EMAIL_SENT);
    } catch (error) {
      setFormMessage("Nepodarilo sa zaslať verifikačný email");
      setIsSuccess(false);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleChangePassword = async (e: React.FormEvent) => {
    setIsSubmitting(true);
    e.preventDefault();
    try {
      setFormData((prevData) => ({
        ...prevData,
        tokenStep: AuthTokenStep.VERIFICATION_STEP,
      }));
      await changePassword(formData);
      navigate("/login");
    } catch (error) {
      setFormMessage("Nepodarilo sa zmeniť heslo. Skúste to znovu.");
      setIsSuccess(false);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <AuthContainer>
      <div className={classes.container}>
        <FormMessage message={formMessage} isSuccess={isSuccess} />
        <h2 className={classes.header}>Obnovenie zabudnutého hesla</h2>
        {step === 1 && (
          <Form className={classes.form} onSubmit={handleResetPassword}>
            <FormInput
              id="email"
              type="email"
              placeholder="Zadajte email"
              required={true}
              onChange={handleInputChange}
              value={formData.email}
            />
            <SubmitBtn
              name="Zaslať verifikačný email"
              isSubmitting={isSubmitting}
            />
          </Form>
        )}
        {step === 2 && (
          <Form className={classes.form} onSubmit={handleChangePassword}>
            <FormInput
              id="password"
              type="password"
              placeholder="Zadajte nové heslo"
              required={true}
              onChange={handleInputChange}
              value={formData.password}
            />
            <FormInput
              id="passwordRe"
              type="password"
              placeholder="Zopakujte nové heslo"
              required={true}
              onChange={handleInputChange}
              value={formData.passwordRe}
            />
            <SubmitBtn name="Vytvoriť nové heslo" isSubmitting={isSubmitting} />
          </Form>
        )}
        <Link to="/login" className={classes.loginBtn}>
          Chcete sa len prihlásiť? <span>Kliknite sem</span>
        </Link>
      </div>
    </AuthContainer>
  );
};

export default ForgotPasswordPage;
