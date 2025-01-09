export interface Medication {
  medicationId: string;

  name: string;

  description: string;

  route: string;

  dose: string;

  frequency: string;

  doseDescription: string;
}

export const testMedications: Medication[] = [
  {
    medicationId: "1",
    name: "Paracetamol",
    description:
      "Používa sa na liečbu miernej až stredne silnej bolesti a na zníženie horúčky.",
    route: "Ústne",
    dose: "500mg",
    frequency: "Každých 6 hodín",
    doseDescription: "Užiť s vodou po jedle.",
  },
  {
    medicationId: "2",
    name: "Ibuprofén",
    description: "Nesteroidné protizápalové liečivo na bolesť a zápal.",
    route: "Ústne",
    dose: "200mg",
    frequency: "Každých 8 hodín",
    doseDescription: "Užiť s jedlom, aby sa predišlo podráždeniu žalúdka.",
  },
  {
    medicationId: "3",
    name: "Amoxicilín",
    description: "Antibiotikum používané na liečbu bakteriálnych infekcií.",
    route: "Ústne",
    dose: "500mg",
    frequency: "Každých 12 hodín",
    doseDescription: "Užiť nalačno s plným pohárom vody.",
  },
  {
    medicationId: "4",
    name: "Metformín",
    description:
      "Používa sa na kontrolu vysokej hladiny cukru v krvi pri cukrovke typu 2.",
    route: "Ústne",
    dose: "850mg",
    frequency: "Raz denne",
    doseDescription: "Užiť s jedlom, aby sa znížilo podráždenie žalúdka.",
  },
  {
    medicationId: "5",
    name: "Cetirizín",
    description: "Antihistaminikum na alergie a sennú nádchu.",
    route: "Ústne",
    dose: "10mg",
    frequency: "Raz denne",
    doseDescription: "Užiť s jedlom alebo bez jedla.",
  },
];
