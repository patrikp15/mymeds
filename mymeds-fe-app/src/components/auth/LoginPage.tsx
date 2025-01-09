import { Form, Link, useNavigate } from "react-router-dom";
import AuthContainer from "../container/AuthContainer";
import classes from "./LoginPage.module.css";
import FormInput from "../ui/FormInput";
import { useState } from "react";
import { login } from "../../api/authApi";
import { checkAndReplaceEmail } from "../../utils/FormUtils";
import SubmitBtn from "../ui/SubmitBtn";

const LoginPage: React.FC<{}> = () => {
  const [formData, setFormData] = useState({
    password: "",
    email: "",
    userRole: "BASIC",
  });
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  const navigate = useNavigate();

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
    setError("");
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      await login(formData);
      navigate("/user-medications");
    } catch (error) {
      setError("Nesprávne meno alebo heslo");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <AuthContainer>
      <div className={classes.loginContainer}>
        <h2 className={classes.loginHeader}>Vítajte späť !</h2>
        <p className={classes.errorMsg}>{error}</p>
        <Form className={classes.form} onSubmit={handleSubmit}>
          <FormInput
            id="email"
            type="email"
            placeholder="Zadajte email"
            required={true}
            onChange={handleInputChange}
            value={formData.email}
          />
          <FormInput
            id="password"
            type="password"
            placeholder="Zadajte heslo"
            required={true}
            onChange={handleInputChange}
            value={formData.password}
          />
          <SubmitBtn name="Prihlásiť sa" isSubmitting={isSubmitting} />
        </Form>
        <Link to="/signup" className={classes.signUpBtn}>
          Ešte nemáte konto ? <span>Registrovať sa</span>
        </Link>
        <Link to="/forgotten-password" className={classes.forgottenPassword}>
          Zabudol som heslo
        </Link>
      </div>
    </AuthContainer>
  );
};

export default LoginPage;
