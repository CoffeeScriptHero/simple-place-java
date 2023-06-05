import axiosIns from "../axiosInstance";

export const authenticate = async (data, request) => {
  const response = await axiosIns.post(`api/authentication/${request}`, data);

  return response;
};

export const logInByToken = async () => {
  const response = await axiosIns.get("api/authentication/jwt");

  return response;
};

export const getRecommendedUsers = async () => {
  const response = await axiosIns.get("/api/users/recommended");

  return response;
};

export const searchUsers = async (input = "") => {
  const response = await axiosIns.get("/api/users/search", {
    params: {
      s: input,
    },
  });

  return response;
};

export const getUserpage = async (username) => {
  const response = await axiosIns.get(`/api/users/${username}/profile`);

  return response;
};

export const changeUsername = async (data) => {
  const response = await axiosIns.patch("/api/users/profile/username", data);

  return response;
};

export const changeProfileImg = async (data) => {
  const response = await axiosIns.post("/api/users/profile/avatar", data);

  return response;
};

export const deleteProfileImg = async () => {
  const response = await axiosIns.delete("/api/users/profile/avatar");

  return response;
};
