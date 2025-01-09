import { RouterProvider, createBrowserRouter } from "react-router-dom";
import WelcomePage from "./components/auth/WelcomePage";
import LoginPage from "./components/auth/LoginPage";
import SignUpPage from "./components/auth/SignUpPage";
import UserMedicationPage from "./components/usermedication/UserMedicationPage";
import AddUserMedicationPage from "./components/usermedication/AddUserMedicationPage";
import UserProfilePage from "./components/user/UserProfilePage";
import GuestListPage from "./components/user/GuestListPage";
import GuestDetailPage from "./components/user/GuestDetailPage";
import SharePage from "./components/share/SharePage";
import UserMedicationPublicPage from "./components/usermedication/UserMedicationPublicPage";
import ForgotPasswordPage from "./components/auth/ForgotPasswordPage";
import RegisterGuestPage from "./components/auth/RegisterGuestPage";

const router = createBrowserRouter([
  {
    path: "/",
    element: <WelcomePage />,
    id: "root",
  },
  {
    path: "/login",
    element: <LoginPage />,
    id: "login",
  },
  {
    path: "/signup",
    element: <SignUpPage />,
    id: "signup",
  },
  {
    path: "/forgotten-password",
    element: <ForgotPasswordPage />,
    id: "forgotten_password",
  },
  {
    path: "/guest/register",
    element: <RegisterGuestPage />,
    id: "register_guest",
  },
  {
    path: "/user-medications",
    element: <UserMedicationPage />,
    id: "user_medications",
  },
  {
    path: "/user-medications/public",
    element: <UserMedicationPublicPage />,
    id: "user_medications_public",
  },
  {
    path: "/add-user-medications",
    element: <AddUserMedicationPage />,
    id: "add_user_medication",
  },
  {
    path: "/profile",
    element: <UserProfilePage />,
    id: "user_profile",
  },
  {
    path: "/guests",
    element: <GuestListPage />,
    id: "guests",
  },
  {
    path: "/guests/detail",
    element: <GuestDetailPage />,
    id: "guest_detail",
  },
  {
    path: "/share",
    element: <SharePage />,
    id: "share",
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
