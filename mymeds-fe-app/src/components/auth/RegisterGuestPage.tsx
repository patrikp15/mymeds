import { Form, useNavigate, useSearchParams } from "react-router-dom";
import AuthContainer from "../container/AuthContainer";
import classes from "./RegisterGuestPage.module.css";
import FormInput from "../ui/FormInput";
import { useEffect, useState } from "react";
import { registerGuest } from "../../api/authApi";
import { getGuestDetail } from "../../api/guestApi";
import { AuthUserDetail } from "../../model/user/AuthUserDetail";
import { UserRole } from "../../model/auth/UserRole";
import { UserStatus } from "../../model/user/UserStatus";
import FormMessage from "../ui/FormMessage";
import SubmitBtn from "../ui/SubmitBtn";

const RegisterGuestPage: React.FC<{}> = () => {
  const [guestDetail, setGuestDetail] = useState<AuthUserDetail>({
    email: "",
    userRole: UserRole.GUEST,
    userStatus: UserStatus.NOT_VERIFIED,
  });
  const [formData, setFormData] = useState({
    password: "",
    passwordRe: "",
  });
  const [token, setToken] = useState<string>("");
  const [formMessage, setFormMessage] = useState<string>("");
  const [isSuccess, setIsSuccess] = useState<boolean>(true);
  const [warning, setWarning] = useState<string>("");
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchGuestDetail = async (): Promise<void> => {
      const token = searchParams.get("token");
      if (token) {
        setToken(token);
        try {
          const guest = await getGuestDetail(token);
          setGuestDetail(guest);
        } catch (error: any) {
          setWarning(
            "Token je neplatný. Vyžiadajte si nový a skúste to znovu."
          );
        }
      }
    };
    fetchGuestDetail();
  }, [searchParams]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [id]: value,
    }));
    setFormMessage("");
    setIsSuccess(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    setIsSubmitting(true);
    e.preventDefault();
    try {
      await registerGuest(token, formData);
      navigate("/login");
    } catch (error) {
      setFormMessage("Registrácia zlyhala.");
      setIsSuccess(false);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <AuthContainer>
      <div className={classes.container}>
        <FormMessage message={formMessage} isSuccess={isSuccess} />
        {warning === "" ? (
          <h3 className={classes.header}>
            Registrujete používateľa {guestDetail.email}
          </h3>
        ) : (
          <p className={classes.warning}>{warning}</p>
        )}
        <Form className={classes.form} onSubmit={handleSubmit}>
          <FormInput
            id="password"
            type="password"
            placeholder="Zadajte heslo"
            required={true}
            onChange={handleInputChange}
            value={formData.password}
            disabled={warning !== ""}
          />
          <FormInput
            id="passwordRe"
            type="password"
            placeholder="Zopakujte heslo"
            required={true}
            onChange={handleInputChange}
            value={formData.passwordRe}
            disabled={warning !== ""}
          />
          <SubmitBtn
            name="Registrovať sa"
            isDisabled={warning !== ""}
            isSubmitting={isSubmitting}
          />
        </Form>
      </div>
    </AuthContainer>
  );
};

export default RegisterGuestPage;
