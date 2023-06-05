import axiosIns from "../../axiosInstance";
import actions from "./actions";

const getFollowers = (username) => async (dispatch, getState) => {
  await axiosIns
    .get(`/api/users/${username}/followers`)
    .then((res) => {
      dispatch(actions.setUsers(res.data));
    })
    .catch((e) => console.log(e));
};

const getFollowing = (username) => async (dispatch, getState) => {
  await axiosIns
    .get(`/api/users/${username}/following`)
    .then((res) => {
      dispatch(actions.setUsers(res.data));
    })
    .catch((e) => console.log(e));
};

const getLiked = (id) => async (dispatch, getState) => {
  const response = await axiosIns.get(`/api/posts/${id}/likes`);

  dispatch(actions.setUsers(response.data));
};

const getCommentLikes = (id) => async (dispatch, getState) => {
  const response = await axiosIns.get(`/api/posts/${id}/comments/likes`);

  dispatch(actions.setUsers(response.data));
};

const deleteUser = (username) => (dispatch, getState) => {
  const updatedUsers = getState().usersModal.users.filter(
    (u) => u.username !== username
  );
  getState().usersModal.users = updatedUsers;
  dispatch(actions.setUsers(updatedUsers));
};

const setNewModalType = (type) => (dispatch, getState) => {
  dispatch(actions.setModalType(type));
};

const operationsObj = {
  setNewModalType,
  getFollowers,
  getCommentLikes,
  getFollowing,
  getLiked,
  deleteUser,
};

export default operationsObj;
