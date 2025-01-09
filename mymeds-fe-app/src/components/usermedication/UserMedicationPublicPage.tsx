import { useEffect, useState } from "react";
import { UserMedication } from "../../model/medication/UserMedicationResponse";
import UserMedicationList from "./UserMedicationList";
import classes from "./UserMedicationPage.module.css";
import { getUserMedicationsByUid } from "../../api/userMedicationsApi";
import MainContainer from "../container/MainContainer";
import LoadingPage from "../ui/LoadingPage";
import { useSearchParams } from "react-router-dom";

const UserMedicationPublicPage: React.FC<{}> = () => {
  const [userMedications, setUserMedications] = useState<UserMedication[]>([]);
  const [isContentLoading, setIsContentLoading] = useState<boolean>(false);
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const fetchUserMedications = async (): Promise<void> => {
      setIsContentLoading(true);
      try {
        const uid = searchParams.get("uid");
        if (uid) {
          const res = await getUserMedicationsByUid(uid);
          setUserMedications(res);
        } else {
          setUserMedications([]);
        }
      } catch (err: any) {
        console.error(err);
      } finally {
        setIsContentLoading(false);
      }
    };

    fetchUserMedications();
  }, [searchParams]);

  return (
    <MainContainer>
      {isContentLoading ? (
        <LoadingPage />
      ) : (
        <div className={classes.mainContainer}>
          <UserMedicationList list={userMedications} isPublic={true} />
        </div>
      )}
    </MainContainer>
  );
};

export default UserMedicationPublicPage;
