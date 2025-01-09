import { Medication, testMedications } from "./Medication";

export interface UserMedication {
  userMedicationId: string;

  startDate: string;

  endDate: string;

  instructions: string;

  medication?: Medication;
}

export const testUserMedications: UserMedication[] = [
  {
    userMedicationId: "1111-1111-1111-1111",
    startDate: "2024-01-01",
    endDate: "2024-02-01",
    instructions:
      "Užívajte podľa potreby na zníženie horúčky alebo zmiernenie bolesti.",
    medication: testMedications[0], // Paracetamol
  },
  {
    userMedicationId: "1111-1111-1111-1111",
    startDate: "2024-01-15",
    endDate: "2024-03-15",
    instructions: "Užívajte pri bolesti alebo zápale.",
    medication: testMedications[1], // Ibuprofen
  },
  {
    userMedicationId: "1111-1111-1111-1111",
    startDate: "2024-02-01",
    endDate: "2024-02-14",
    instructions: "Užívajte dvakrát denne na liečbu infekcie.",
    medication: testMedications[2], // Amoxicillin
  },
  {
    userMedicationId: "1111-1111-1111-1111",
    startDate: "2024-03-01",
    endDate: "2024-06-01",
    instructions:
      "Užívajte s jedlom každý deň na kontrolu hladiny cukru v krvi.",
    medication: testMedications[3], // Metformin
  },
  {
    userMedicationId: "1111-1111-1111-1111",
    startDate: "2024-04-01",
    endDate: "2024-05-01",
    instructions: "Užívajte denne na zmiernenie alergických prejavov.",
    medication: testMedications[4], // Cetirizine
  },
  {
    userMedicationId: "1111-1111-1111-1111",
    startDate: "2024-01-01",
    endDate: "2024-02-01",
    instructions:
      "Užívajte podľa potreby na zníženie horúčky alebo zmiernenie bolesti.",
    medication: testMedications[0], // Paracetamol
  },
  {
    userMedicationId: "1111-1111-1111-1111",
    startDate: "2024-01-15",
    endDate: "2024-03-15",
    instructions: "Užívajte pri bolesti alebo zápale.",
    medication: testMedications[1], // Ibuprofen
  },
  {
    userMedicationId: "1111-1111-1111-1111",
    startDate: "2024-02-01",
    endDate: "2024-02-14",
    instructions: "Užívajte dvakrát denne na liečbu infekcie.",
    medication: testMedications[2], // Amoxicillin
  },
  {
    userMedicationId: "1111-1111-1111-1111",
    startDate: "2024-03-01",
    endDate: "2024-06-01",
    instructions:
      "Užívajte s jedlom každý deň na kontrolu hladiny cukru v krvi.",
    medication: testMedications[3], // Metformin
  },
  {
    userMedicationId: "1111-1111-1111-1111",
    startDate: "2024-04-01",
    endDate: "2024-05-01",
    instructions: "Užívajte denne na zmiernenie alergických prejavov.",
    medication: testMedications[4], // Cetirizine
  },
];
