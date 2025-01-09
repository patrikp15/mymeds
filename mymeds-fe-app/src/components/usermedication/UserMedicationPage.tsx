import { useEffect, useState } from "react";
import { UserMedication } from "../../model/medication/UserMedicationResponse";
import MenuContainer from "../container/MenuContainer";
import UserMedicationList from "./UserMedicationList";
import classes from "./UserMedicationPage.module.css";
import {
  getUserMedications,
  getUserMedicationsByDateFilter,
  getUserMedicationsByTextFilter,
} from "../../api/userMedicationsApi";
import SearchBar from "../ui/SearchBar";
import { DateFilterEnum } from "../../model/DateFilterEnum";

const UserMedicationPage: React.FC<{}> = () => {
  const [userMedications, setUserMedications] = useState<UserMedication[]>([]);
  const filters: string[] = ["Všetky", "Aktuálne", "Budúce", "Minulé"];
  const [activeFilterIndex, setActiveFilterIndex] = useState<number>(1);
  const [searchValue, setSearchValue] = useState<string>("");
  const [isContentLoading, setIsContentLoading] = useState<boolean>(false);

  useEffect(() => {
    const fetch = async () => {
      fetchUserMedications();
    };

    fetch();
  }, []);

  const handleRefresh = async () => {
    fetchUserMedications();
  };

  const fetchUserMedications = async (): Promise<void> => {
    setIsContentLoading(true);
    try {
      const res = await getUserMedicationsByDateFilter(DateFilterEnum.ACTUAL);
      setUserMedications(res);
    } catch (err: any) {
      console.error(err);
    } finally {
      setIsContentLoading(false);
    }
  };

  const handleSearchInputChange = async (
    e: React.ChangeEvent<HTMLInputElement>
  ): Promise<void> => {
    const currentValue = e.target.value;
    setSearchValue(currentValue);
  };

  const handleSearch = async (): Promise<void> => {
    if (searchValue.length > 2) {
      try {
        const res = await getUserMedicationsByTextFilter(searchValue);
        setUserMedications(res);
      } catch (err: any) {
        console.error(err);
      }
    }
  };

  const handleOnClear = async (): Promise<void> => {
    setSearchValue("");
    handleDateFilter(0);
    setActiveFilterIndex(0);
  };

  const handleDateFilter = async (index: number): Promise<void> => {
    let res;
    try {
      if (index === 0) {
        res = await getUserMedications();
      } else if (index === 1) {
        res = await getUserMedicationsByDateFilter(DateFilterEnum.ACTUAL);
      } else if (index === 2) {
        res = await getUserMedicationsByDateFilter(DateFilterEnum.FUTURE);
      } else {
        res = await getUserMedicationsByDateFilter(DateFilterEnum.PAST);
      }
      setUserMedications(res);
    } catch (err: any) {
      console.error(err);
    }
  };

  return (
    <MenuContainer active="/user-medications" isLoading={isContentLoading}>
      <div className={classes.mainContainer}>
        <div className={classes.filterSearchContainer}>
          <div className={classes.filterContainer}>
            {filters.map((filter, index) => (
              <button
                key={index}
                className={`${classes.filter} ${
                  index === activeFilterIndex ? classes.activeFilter : ""
                }`}
                onClick={() => {
                  setActiveFilterIndex(index);
                  handleDateFilter(index);
                }}
              >
                {filter}
              </button>
            ))}
          </div>
          <SearchBar
            value={searchValue}
            onChange={handleSearchInputChange}
            onClear={() => handleOnClear()}
            onSearch={() => handleSearch()}
            placeholder="Hľadať v liekoch..."
          />
        </div>
        <UserMedicationList
          list={userMedications}
          onRefresh={() => handleRefresh()}
        />
      </div>
    </MenuContainer>
  );
};

export default UserMedicationPage;
