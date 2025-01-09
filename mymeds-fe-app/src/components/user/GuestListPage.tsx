import FormContainer from "../container/FormContainer";
import { Guest } from "../../model/user/Guest";
import classes from "./GuestListPage.module.css";
import GuestItem from "./GuestItem";
import { useEffect, useState } from "react";
import { getAllGuests } from "../../api/guestApi";
import AddIcon from "@mui/icons-material/Add";
import { useNavigate } from "react-router-dom";

const GuestListPage: React.FC<{}> = () => {
  const [list, setList] = useState<Guest[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const navigate = useNavigate();

  useEffect(() => {
    setIsLoading(true);
    const fetchGuests = async (): Promise<void> => {
      try {
        const res = await getAllGuests();
        setList(res);
      } catch (err: any) {
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchGuests();
  }, []);

  const handleItemClick = () => {
    navigate("detail");
  };

  return (
    <FormContainer activeLink="/profile" isLoading={isLoading}>
      {list.length > 0 ? (
        <ul className={classes.list}>
          {list.length < 4 && (
            <li
              key={9999}
              className={classes.item}
              onClick={() => handleItemClick()}
            >
              <div className={classes.iconContainer}>
                <AddIcon className={classes.addIcon} />
              </div>
              <div className={classes.textContainer}>
                <p>Pridať kontakt</p>
              </div>
            </li>
          )}
          {list.map((guest, index) => (
            <GuestItem key={index} item={guest} index={index} />
          ))}
        </ul>
      ) : (
        <div className={classes.emptyListContainer}>
          <p className={classes.emptyList}>
            Zatiaľ neboli pridané žiadne kontaktné osoby
          </p>
        </div>
      )}
    </FormContainer>
  );
};

export default GuestListPage;
