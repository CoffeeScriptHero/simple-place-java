import axiosIns from "../../axiosInstance";
import actions from "./actions";

const setNewUser = (userInfo) => (dispatch, getState) => {
  dispatch(actions.saveUser(userInfo));
};

const followUser = (id) => async (dispatch, getState) => {
  const response = await axiosIns.post(`/api/users/${id}`);

  if (response.status === 201) {
    getState().user.following.push(id);
  }
  if (response.status === 200) {
    getState().user.following = getState().user.following.filter(
      (i) => i !== id
    );
  }

  dispatch(actions.updateModal());

  return response;
};

const deleteUser = (id) => async (dispatch, getState) => {
  const response = await axiosIns.delete(`/api/users/${id}`);

  getState().user.followers = getState().user.followers.filter((i) => i !== id);
  dispatch(actions.updateModal());

  return response;
};

const saveAddedPost = (post) => (dispatch, getState) => {
  dispatch(actions.saveAddedPost(post));
};

const saveDeletedPost = (post) => (dispatch, getState) => {
  dispatch(actions.saveDeletedPost(post));
};

const updateUsername = (username) => (dispatch, getState) => {
  dispatch(actions.updateUsername(username));
};

const updateProfilePic = (profilePic) => (dispatch, getState) => {
  dispatch(actions.updateProfilePic(profilePic));
};

const operationsObj = {
  setNewUser,
  followUser,
  deleteUser,
  saveAddedPost,
  saveDeletedPost,
  updateUsername,
  updateProfilePic,
};

export default operationsObj;
