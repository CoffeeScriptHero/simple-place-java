import { Routes, Route } from "react-router-dom";
import SignUp from "../pages/SignUp/SignUp.js";
import Feed from "../pages/Feed/Feed.js";
import { useDispatch, useSelector } from "react-redux";
import { logInByToken } from "../services/UserService.js";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import User from "../pages/User/User.js";
import UsersModal from "../components/UsersModal/UsersModal.js";
import PostModal from "../components/PostModal/PostModal.js";
import { userOperations } from "../store/user/index.js";
import { userSelectors } from "../store/user";
import { TOKEN } from "../util/constants";
import Loader from "../components/Loader/Loader.js";

const AppRoutes = () => {
  const dispatch = useDispatch();
  const user = useSelector(userSelectors.getUser());
  const navigate = useNavigate();
  const token = localStorage.getItem(TOKEN);

  useEffect(() => {
    const tryToAuthenticate = async () => {
      if (token && user.user === null) {
        try {
          const response = await logInByToken();
          dispatch(
            userOperations.setNewUser({
              user: response.data.username,
              id: response.data.id,
              profileImg: response.data.profileImg,
              following: response.data.following,
              followers: response.data.followers,
            })
          );
        } catch (e) {
          localStorage.removeItem(TOKEN);
          navigate("/");
        }
      }
      if (!token) {
        navigate("/");
      }
    };

    tryToAuthenticate();
  }, [token]);

  if (token && !user.user) {
    return <Loader />;
  }

  return (
    <Routes>
      <Route path="/" element={user.user !== null ? <Feed /> : <SignUp />}>
        <Route path="p/:id" element={<PostModal />} />
      </Route>
      <Route path=":username" element={user.user !== null && <User />}>
        <Route path="followers" element={<UsersModal />} />
        <Route path="following" element={<UsersModal />} />
        <Route path="p/:id" element={<PostModal />} />
      </Route>
    </Routes>
  );
};

export default AppRoutes;
