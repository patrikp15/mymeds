import { RelationshipType } from "../model/auth/RelationshipType";
import { UserStatus } from "../model/user/UserStatus";
import classes from "../components/Components.module.css";
import { MedicationRoute } from "../model/medication/MedicationRoute";

export function stringToEnum<T>(value: string, enumType: T): T[keyof T] {
  const enumValue = enumType[value as keyof T];
  if (enumValue === undefined) {
    throw new Error(`Invalid string value: ${value}`);
  }
  return enumValue;
}

export const userStatusToString = (value: UserStatus): string => {
  if (value === UserStatus.ACTIVE) {
    return "Aktívny";
  } else if (value === UserStatus.NOT_ACTIVE) {
    return "Neaktívny";
  }
  return "Neverifikovaný";
};

export const relationshipTypeToString = (value: RelationshipType): string => {
  if (value === RelationshipType.CAREGIVER) {
    return "Opatrovateľ/ka";
  } else if (value === RelationshipType.FAMILY_MEMBER) {
    return "Rodinný člen";
  } else if (value === RelationshipType.FRIEND) {
    return "Známy";
  }
  return "Iný vzťah";
};

export const userStatusToClassColor = (value: UserStatus): string => {
  switch (value) {
    case "ACTIVE":
      return classes.active;
    case "NOT_ACTIVE":
      return classes.notActive;
    case "NOT_VERIFIED":
      return classes.notVerified;
    default:
      return "";
  }
};

export function medicationRouteToString(route: MedicationRoute): string {
  const routeTranslations: { [key in MedicationRoute]: string } = {
    [MedicationRoute.ORAL]: "Ústne",
    [MedicationRoute.INJECTION]: "Injekcia",
    [MedicationRoute.INTRAVENOUS]: "Intravenózne",
    [MedicationRoute.SUBCUTANEOUS]: "Subkutánne",
    [MedicationRoute.INTRAMUSCULAR]: "Intramuskulárne",
    [MedicationRoute.TOPICAL]: "Topické",
    [MedicationRoute.TRANSDERMAL]: "Transdermálne",
    [MedicationRoute.INHALATION]: "Inhalácia",
    [MedicationRoute.RECTAL]: "Rektálne",
    [MedicationRoute.VAGINAL]: "Vaginálne",
    [MedicationRoute.BUCCAL]: "Bukálne",
    [MedicationRoute.SUBLINGUAL]: "Sublingválne",
    [MedicationRoute.OPHTHALMIC]: "Oftalmické",
    [MedicationRoute.OTIC]: "Oticé",
    [MedicationRoute.NASAL]: "Nosové",
    [MedicationRoute.OTHER]: "Iné",
  };

  return routeTranslations[route] || route;
}
