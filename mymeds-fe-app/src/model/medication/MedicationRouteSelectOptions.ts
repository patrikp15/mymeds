import { SelectOption } from "../../components/ui/FormSelectInput";
import { MedicationRoute } from "./MedicationRoute";

export const medicationRouteTypes: SelectOption[] = [
  {
    key: "1",
    value: MedicationRoute.ORAL,
    text: "Perorálne",
  },
  { key: "2", value: MedicationRoute.INJECTION, text: "Injekčne" },
  { key: "3", value: MedicationRoute.INTRAVENOUS, text: "Injekčne (do žily)" },
  {
    key: "4",
    value: MedicationRoute.SUBCUTANEOUS,
    text: "Injekčne (pod kožu)",
  },
  {
    key: "5",
    value: MedicationRoute.INTRAMUSCULAR,
    text: "Injekčne (do svalu)",
  },
  {
    key: "6",
    value: MedicationRoute.TOPICAL,
    text: "Topicky (na kožu)",
  },
  {
    key: "7",
    value: MedicationRoute.TRANSDERMAL,
    text: "Transdermálne (náplasť)",
  },
  {
    key: "8",
    value: MedicationRoute.INHALATION,
    text: "Inhalácia (do plúc)",
  },
  {
    key: "9",
    value: MedicationRoute.RECTAL,
    text: "Rektálne (do konečníka)",
  },
  {
    key: "10",
    value: MedicationRoute.VAGINAL,
    text: "Vaginálne (do vagíny)",
  },
  {
    key: "11",
    value: MedicationRoute.BUCCAL,
    text: "Bukálne (do líca)",
  },
  {
    key: "12",
    value: MedicationRoute.SUBLINGUAL,
    text: "Bukálne (pod jayzk)",
  },
  {
    key: "13",
    value: MedicationRoute.OPHTHALMIC,
    text: "Očné (do oka)",
  },
  {
    key: "14",
    value: MedicationRoute.OTIC,
    text: "Ušné (do ucha)",
  },
  {
    key: "15",
    value: MedicationRoute.NASAL,
    text: "Nosové (do nosa)",
  },
  {
    key: "16",
    value: MedicationRoute.OTHER,
    text: "Iné",
  },
];
