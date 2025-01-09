import { useEffect, useState } from "react";
import FormContainer from "../container/FormContainer";
import classes from "./SharePage.module.css";
import { getPDF, getQRCode } from "../../api/shareApi";
import { PDFFormat } from "../../model/share/PDFFormat";
import { Form } from "react-router-dom";
import SubmitBtn from "../ui/SubmitBtn";
import FormMessage from "../ui/FormMessage";

const SharePage: React.FC<{}> = () => {
  const [imageData, setImageData] = useState("");
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [isSubmittingA4, setIsSubmittingA4] = useState<boolean>(false);
  const [isSubmittingMobile, setIsSubmittingMobile] = useState<boolean>(false);
  const [formMessage, setFormMessage] = useState<string>("");
  const [isSuccess, setIsSuccess] = useState<boolean>(true);

  useEffect(() => {
    setIsLoading(true);
    const fetchQRCode = async () => {
      try {
        const res = await getQRCode();
        setImageData("data:image/png;base64," + res.qrCodeBase64);
      } catch (err: any) {
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    };
    fetchQRCode();
  }, []);

  const handlePdf = async (format: PDFFormat): Promise<void> => {
    setIsSuccess(true);
    setFormMessage("");
    setIsSubmittingA4(format === PDFFormat.A4);
    setIsSubmittingMobile(format === PDFFormat.MOBILE);
    try {
      const pdfBlob = await getPDF(format);
      if (pdfBlob) {
        const url = window.URL.createObjectURL(pdfBlob);
        const link = document.createElement("a");
        link.href = url;
        link.download = "mojelieky.pdf";
        link.click();
        window.URL.revokeObjectURL(url);
      }
    } catch (error: any) {
      setIsSuccess(false);
      setFormMessage("PDF súbor sa nepodarilo získať");
    } finally {
      setIsSubmittingA4(false);
      setIsSubmittingMobile(false);
    }
  };

  return (
    <FormContainer activeLink="/share" isLoading={isLoading}>
      <div className={classes.container}>
        <figure className={classes.imageContainer}>
          <img src={imageData} alt="QR Code" />
        </figure>
        <aside className={classes.textContainer}>
          <FormMessage message={formMessage} isSuccess={isSuccess} />
          <p>
            <span>Pozor!</span>
            <span>
              Všetci, ktorým tento QR kód poskytnete budú mať prístup k Vašému
              zoznamu liekov.
            </span>
          </p>
          <div className={classes.btnContainer}>
            <Form
              className={classes.form}
              onSubmit={() => handlePdf(PDFFormat.A4)}
            >
              <SubmitBtn
                name="Vytlačiť A4 verziu"
                isSubmitting={isSubmittingA4}
              />
            </Form>
            <Form
              className={classes.form}
              onSubmit={() => handlePdf(PDFFormat.MOBILE)}
            >
              <SubmitBtn
                name="Vytlačiť mobilnú verziu"
                isSubmitting={isSubmittingMobile}
              />
            </Form>
          </div>
        </aside>
      </div>
    </FormContainer>
  );
};

export default SharePage;
