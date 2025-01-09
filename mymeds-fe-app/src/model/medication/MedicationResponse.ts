import { MedicationDose } from "./MedicationDose";
import { MedicationRoute } from "./MedicationRoute";

export interface MedicationResponse {
  medicationId: string;

  name: string;

  description: string;

  route: MedicationRoute;

  medicationDoses?: MedicationDose[];
}
