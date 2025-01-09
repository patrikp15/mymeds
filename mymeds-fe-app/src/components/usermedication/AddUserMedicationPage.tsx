import { useEffect, useMemo, useState } from "react";
import classes from "./AddUserMedicationPage.module.css";
import { Form, useNavigate } from "react-router-dom";
import PlusIcon from "@mui/icons-material/AddCircleOutline";
import FormContainer from "../container/FormContainer";
import FormLabelInput from "../ui/FormLabelInput";
import FormLabelDateInput from "../ui/FormLabelDateInput";
import SubmitBtn from "../ui/SubmitBtn";
import SelectPopup from "../ui/SelectPopup";
import ResetBtn from "../ui/ResetBtn";
import { SelectPopupItem } from "../../model/ui/SelectPopupItem";
import { MedicationSelectPopup } from "../../model/medication/MedicationSelectPopup";
import { getMedicationSelectPopup } from "../../api/medicationApi";
import { addUserMedication } from "../../api/userMedicationsApi";
import { hasFormChanged, isFormValid } from "../../utils/FormUtils";
import { FILL_ALL_FIELDS } from "../../model/ui/FormConstants";
import FormMessage from "../ui/FormMessage";

const AddUserMedicationPage: React.FC<{}> = () => {
  const initialFormData = useMemo(
    () => ({
      medicationId: "",
      medicationValue: "",
      medicationDoseId: "",
      medicationDoseValue: "",
      startDate: "",
      endDate: "",
      instructions: "",
    }),
    []
  );
  const [formData, setFormData] = useState(initialFormData);
  const [isMedicineOpen, setIsMedicineOpen] = useState<boolean>(false);
  const [isMedicationDoseOpen, setIsMedicationDoseOpen] =
    useState<boolean>(false);
  const [medicationSelectPopup, setMedicationSelectPopup] =
    useState<MedicationSelectPopup>(new MedicationSelectPopup());
  const [formMessage, setFormMessage] = useState<string>("");
  const [isSuccess, setIsSuccess] = useState<boolean>(true);
  const [isSaveDiabled, setIsSaveDisabled] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const navigate = useNavigate();

  useEffect(() => {
    setIsLoading(true);
    const fetchMedicatonSelectPopup = async () => {
      try {
        const res = await getMedicationSelectPopup();
        setMedicationSelectPopup(res);
      } catch (err: any) {
        //   setError(
        //     err.message || "An error occurred while fetching medications."
        //   );
        // }
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    };
    fetchMedicatonSelectPopup();
  }, []);

  useEffect(() => {
    const isFormChanged = hasFormChanged(formData, initialFormData);
    setIsSaveDisabled(!isFormChanged);
    setFormMessage("");
    setIsSuccess(true);
  }, [formData, initialFormData]);

  const handleFormInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [id]: value,
    }));
  };

  const handleClear = (id: string) => {
    setFormData((prevData) => ({
      ...prevData,
      [id]: "",
    }));
  };

  const handleDateChange = (id: string, date: string) => {
    setFormData((prevData) => ({
      ...prevData,
      [id]: date,
    }));
  };

  const handleMedicationSelect = ({ id, value }: SelectPopupItem) => {
    setFormData((prevData) => ({
      ...prevData,
      medicationId: id,
      medicationValue: value,
      medicationDoseId: "",
      medicationDoseValue: "",
    }));
  };

  const handleMedicationDoseSelect = ({ id, value }: SelectPopupItem) => {
    setFormData((prevData) => ({
      ...prevData,
      medicationDoseId: id,
      medicationDoseValue: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const formValid = isFormValid(formData);
      if (!formValid) {
        setFormMessage(FILL_ALL_FIELDS);
        setIsSuccess(false);
        return;
      }
      await addUserMedication(formData);
      navigate("/user-medications");
      setFormMessage("");
      setIsSuccess(true);
    } catch (error) {
      setFormMessage("Liek sa nepodarilo pridať.");
      setIsSuccess(false);
    }
  };

  return (
    <FormContainer isLoading={isLoading}>
      <Form className={classes.form} onSubmit={handleSubmit}>
        <div className={classes.formRow2SpaceBetween}>
          <h3 className={classes.formHeader}>Pridať nový liek</h3>
          <FormMessage message={formMessage} isSuccess={isSuccess} />
        </div>
        <div className={classes.formRow2Icon}>
          <FormLabelInput
            id="medication"
            label="Liek*"
            value={formData.medicationValue}
            disabled={true}
            onChange={() => {}}
          />
          <div
            className={classes.tableLink}
            onClick={() => setIsMedicineOpen(true)}
          >
            <PlusIcon className={classes.formIcon} />
          </div>
          {isMedicineOpen && (
            <SelectPopup
              name="Zvoľte liek*"
              onSelect={handleMedicationSelect}
              onClose={() => setIsMedicineOpen(false)}
              list={medicationSelectPopup.medications}
            />
          )}
        </div>
        {formData.medicationId !== "" && (
          <div className={classes.formRow2Icon}>
            <FormLabelInput
              id="medicationDose"
              label="Dávkovanie*"
              value={formData.medicationDoseValue}
              disabled={true}
              onChange={() => {}}
            />
            <div
              className={classes.tableLink}
              onClick={() => setIsMedicationDoseOpen(true)}
            >
              <PlusIcon className={classes.formIcon} />
            </div>
            {isMedicationDoseOpen && !isMedicineOpen && (
              <SelectPopup
                onSelect={handleMedicationDoseSelect}
                onClose={() => setIsMedicationDoseOpen(false)}
                list={
                  medicationSelectPopup.dosesMap.get(formData.medicationId) ??
                  []
                }
                name="Zvoľte dávkovanie"
              />
            )}
          </div>
        )}
        <div className={classes.formRow2}>
          <FormLabelDateInput
            label="Začiatok užívania*"
            id="startDate"
            value={formData.startDate}
            onChange={(date) => handleDateChange("startDate", date)}
            onClear={() => handleClear("startDate")}
          />
          <FormLabelDateInput
            label="Koniec užívania*"
            id="endDate"
            value={formData.endDate}
            onChange={(date) => handleDateChange("endDate", date)}
            onClear={() => handleClear("endDate")}
          />
        </div>
        <div className={classes.formRowWhole}>
          <FormLabelInput
            id="instructions"
            label="Inštrukcie*"
            value={formData.instructions}
            onChange={handleFormInputChange}
            limit={100}
            onClear={() => handleClear("instructions")}
          />
        </div>
        <div className={classes.formRow2}>
          <SubmitBtn name="Pridať" isDisabled={isSaveDiabled} />
          <ResetBtn name="Reset" onClick={() => setFormData(initialFormData)} />
        </div>
      </Form>
    </FormContainer>
  );
};

export default AddUserMedicationPage;
