import { useNavigate } from "react-router-dom";
import { Guest } from "../../model/user/Guest";
import classes from "./GuestItem.module.css";
import {
  relationshipTypeToString,
  userStatusToClassColor,
  userStatusToString,
} from "../../utils/EnumConverter";

const GuestItem: React.FC<{
  item: Guest;
  index: number;
}> = ({ item, index }) => {
  const nameComposer = (contact: Guest): string => {
    const { firstName, middleName, lastName } = contact;
    return middleName
      ? `${firstName} ${middleName} ${lastName}`
      : `${firstName} ${lastName}`;
  };

  const navigate = useNavigate();

  const handleItemClick = () => {
    navigate("detail", { state: { item } });
  };

  return (
    <li key={index} className={classes.item} onClick={() => handleItemClick()}>
      <div
        className={`${classes.statusContainer} ${
          classes.row
        } ${userStatusToClassColor(item.userStatus)}`}
      >
        <p className={classes.text}>{userStatusToString(item.userStatus)}</p>
      </div>
      <div className={classes.row}>
        <p className={classes.text}>{nameComposer(item)}</p>
      </div>
      <div className={classes.row}>
        <p className={classes.text}>{item.email}</p>
      </div>
      <div className={classes.row}>
        <p className={classes.text}>{item.mobileNumber}</p>
      </div>
      <div className={`${classes.typeContainer} ${classes.row}`}>
        <p className={classes.text}>
          {relationshipTypeToString(item.relationshipType)}
        </p>
      </div>
    </li>
  );
};

export default GuestItem;
