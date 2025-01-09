import { Medication } from "../model/medication/Medication";
import { MedicationResponse } from "../model/medication/MedicationResponse";
import { MedicationRoute } from "../model/medication/MedicationRoute";
import { MedicationSelectPopup } from "../model/medication/MedicationSelectPopup";
import { UserMedication } from "../model/medication/UserMedicationResponse";
import { SelectPopupItem } from "../model/ui/SelectPopupItem";
import { stringToAppDate } from "../utils/DateUtils";
import { medicationRouteToString, stringToEnum } from "../utils/EnumConverter";

export const mapToMedication = (data: any): Medication => {
  const medication: Medication = {
    medicationId: data.medicationId || "",
    name: data.name || "",
    description: data.description || "",
    route: data.route || "",
    dose: data.dose || "",
    frequency: data.frequency || "",
    doseDescription: data.doseDescription || "",
  };
  return medication;
};

export const mapToUserMedication = (data: any): UserMedication => {
  const userMedication: UserMedication = {
    userMedicationId: data.userMedicationId,
    startDate: stringToAppDate(data.startDate) || "",
    endDate: stringToAppDate(data.endDate) || "",
    instructions: data.instructions || "",
    medication: mapToMedication(data.medication),
  };
  return userMedication;
};

export const mapToMedicationSelectPopup = (
  data: MedicationResponse[]
): MedicationSelectPopup => {
  let medications: SelectPopupItem[] = [];
  let dosesMap: Map<string, SelectPopupItem[]> = new Map();

  // Map medications to SelectPopupItem[]
  medications = data.map((value: MedicationResponse) => {
    return {
      id: value.medicationId || "",
      value: `${value.name || ""} | ${
        medicationRouteToString(stringToEnum(value.route, MedicationRoute)) ||
        ""
      }`,
    };
  });

  // Map medication doses to a Map where the key is the medicationId
  dosesMap = new Map(
    data.map((value: MedicationResponse) => {
      const doses =
        value.medicationDoses?.map((dose) => ({
          id: dose.medicationDoseId || "",
          value: `${dose.dose || ""} | ${dose.frequency || ""}`,
        })) || [];
      return [value.medicationId || "", doses];
    })
  );

  return { medications, dosesMap };
};
