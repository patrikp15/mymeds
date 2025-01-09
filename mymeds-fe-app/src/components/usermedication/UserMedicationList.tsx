import { UserMedication } from "../../model/medication/UserMedicationResponse";
import classes from "./UserMedicationList.module.css";
import UserMedicationListItem from "./UserMedicationListItem";

const MedicationList: React.FC<{
  list: UserMedication[];
  isPublic?: boolean;
  onRefresh?: () => void;
}> = ({ list, isPublic = false, onRefresh = () => {} }) => {
  return (
    <>
      {list.length > 0 ? (
        <ul className={classes.userMedicationList}>
          {list.map((userMedication, index) => (
            <UserMedicationListItem
              key={index}
              item={userMedication}
              index={index}
              isPublic={isPublic}
              onRefresh={() => onRefresh()}
            />
          ))}
        </ul>
      ) : (
        <div className={classes.emptyListContainer}>
          <p className={classes.emptyList}>
            Zatiaľ nemáte pridané žiadne lieky.
          </p>
        </div>
      )}
    </>
  );
};

export default MedicationList;
