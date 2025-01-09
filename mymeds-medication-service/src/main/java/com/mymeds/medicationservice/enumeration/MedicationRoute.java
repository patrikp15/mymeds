package com.mymeds.medicationservice.enumeration;

import java.util.Arrays;
import java.util.Optional;

public enum MedicationRoute {
  ORAL,            // perorálne (ústami)
  INJECTION,       // injekčne (pod kožu, do svalu, žily atď.)
  INTRAVENOUS,     // intravenózne (priamo do žily)
  SUBCUTANEOUS,    // subkutánne (pod kožu)
  INTRAMUSCULAR,   // intramuskulárne (do svalu)
  TOPICAL,         // topicky (na kožu)
  TRANSDERMAL,     // transdermálne (cez kožu, napr. náplasti)
  INHALATION,      // inhaláciou (do pľúc)
  RECTAL,          // rektálne (do konečníka)
  VAGINAL,         // vaginálne (do vagíny)
  BUCCAL,          // bukálne (cez líce)
  SUBLINGUAL,      // sublinguálne (pod jazyk)
  OPHTHALMIC,      // očné (do oka)
  OTIC,            // ušné (do ucha)
  NASAL,           // nosové (do nosa)
  OTHER;            // iné (rezervné pre špecifické prípady)

  public static Optional<MedicationRoute> getByName(String name) {
    return Arrays.stream(MedicationRoute.values())
        .filter(route -> route.name().equals(name))
        .findAny();
  }
}
