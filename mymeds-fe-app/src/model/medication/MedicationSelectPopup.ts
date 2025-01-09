import { SelectPopupItem } from "../ui/SelectPopupItem";

export class MedicationSelectPopup {
  constructor() {
    this.medications = [];
    this.dosesMap = new Map<string, SelectPopupItem[]>();
  }

  medications: SelectPopupItem[];
  dosesMap: Map<string, SelectPopupItem[]>;
}
