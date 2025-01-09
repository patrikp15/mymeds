import { UserMedication } from "../../model/medication/UserMedicationResponse";
import classes from "./UserMedicationListItem.module.css";
import MedicationIcon from "@mui/icons-material/MedicationRounded";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import InfoIcon from "@mui/icons-material/Info";
import { deleteUserMedicationById } from "../../api/userMedicationsApi";
import general from "../Components.module.css";
import { useState } from "react";
import YesNoWindowPopup from "../ui/YesNoWindowPopup";

const UserMedicationListItem: React.FC<{
  item: UserMedication;
  index: number;
  isPublic?: boolean;
  onRefresh?: () => void;
}> = ({ item, index, isPublic = false, onRefresh = () => {} }) => {
  const [isDeletePopupOpen, setIsDeletePopupOpen] = useState<boolean>(false);

  const handleDelete = async (): Promise<void> => {
    try {
      await deleteUserMedicationById(item.userMedicationId);
      onRefresh();
    } catch (error: any) {
      console.error(error);
    } finally {
      setIsDeletePopupOpen(false);
    }
  };

  return (
    <>
      <li key={index} className={classes.medicationItem}>
        <div className={classes.medicationContent}>
          <div className={classes.mainIconColumn}>
            <MedicationIcon className={classes.mainIcon} />
          </div>

          <div className={classes.column}>
            <h2 className={classes.medicationName}>
              {item?.medication?.name ?? ""}
            </h2>
            <div className={classes.medicationDetail}>
              <p>
                <span>Dávka: </span>
                {item?.medication?.dose ?? ""}
              </p>
              <p>
                <span>Ako často: </span>
                {item?.medication?.frequency ?? ""}
              </p>
            </div>
          </div>

          <div className={classes.column}>
            <div className={classes.medicationDetail}>
              <p>
                <span>Začiatok užívania: </span>
                {item.startDate}
              </p>
              <p>
                <span>Koniec užívania: </span>
                {item.endDate}
              </p>
              <p>
                <span>Inštrukcie: </span>
                {item?.instructions ?? ""}
              </p>
            </div>
          </div>

          <div
            className={`${
              isPublic ? classes.columnInRowHidden : classes.columnInRow
            }`}
          >
            <InfoIcon
              className={`${classes.medicationIcon} ${classes.iconInfo}`}
            />
            <EditIcon
              className={`${classes.medicationIcon} ${classes.iconEdit}`}
            />
            <DeleteIcon
              className={`${classes.medicationIcon} ${classes.iconDelete}`}
              onClick={() => setIsDeletePopupOpen(true)}
            />
          </div>
        </div>
      </li>
      {isDeletePopupOpen && <div className={general.overlay}></div>}
      {isDeletePopupOpen && (
        <YesNoWindowPopup
          text={
            <p>
              Chystáte sa vymazať Váš liek.
              <br />
              Chcete pokračovať ?
            </p>
          }
          onYes={() => handleDelete()}
          onNo={() => setIsDeletePopupOpen(false)}
        />
      )}{" "}
    </>
  );
};

export default UserMedicationListItem;
