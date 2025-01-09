import { SelectOption } from "../../components/ui/FormSelectInput";
import { RelationshipType } from "./RelationshipType";

export const relationshipTypes: SelectOption[] = [
  {
    key: "1",
    value: RelationshipType.FAMILY_MEMBER,
    text: "Rodinný príslušník",
  },
  { key: "2", value: RelationshipType.FRIEND, text: "Kamarát" },
  { key: "3", value: RelationshipType.CAREGIVER, text: "Opatrovateľ/ka" },
  { key: "4", value: RelationshipType.OTHER, text: "Iné" },
];
