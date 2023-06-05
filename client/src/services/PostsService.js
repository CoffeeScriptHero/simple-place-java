import axiosIns from "../axiosInstance";

export const getPosts = async (from, step) => {
  const response = axiosIns.get("/api/posts", {
    params: {
      p: from,
      n: step,
    },
  });

  return response;
};

export const getPost = async (id) => {
  const response = axiosIns.get(`/api/posts/${id}`);

  return response;
};

export const createComment = async (postId, text) => {
  const response = await axiosIns.post(`/api/posts/${postId}/comments`, {
    text,
  });

  return response;
};

export const updatePostLikes = async (id) => {
  const response = await axiosIns.post(`/api/posts/${id}/likes`);

  return response;
};

export const updateCommentLikes = async (id) => {
  const response = await axiosIns.post(`/api/posts/${id}/comments/likes`);

  return response;
};

export const removeComment = async (id) => {
  const response = await axiosIns.delete(`/api/posts/comments/${id}`);

  return response;
};

export const createPost = async (data) => {
  const response = await axiosIns.post(`/api/posts`, data);

  return response;
};

export const deletePost = async (postId) => {
  const response = await axiosIns.delete(`/api/posts/${postId}`);

  return response;
};

export const likeHandler = (
  setIsFilled,
  isFilled,
  setLikesArr,
  likes,
  postId,
  mainUserId
) => {
  setIsFilled((prevState) => !prevState);
  if (!isFilled) {
    likes.push(mainUserId);
  } else {
    const userIndex = likes.indexOf(mainUserId);
    likes.splice(userIndex, userIndex + 1);
  }
  setLikesArr(likes);
  updatePostLikes(postId);
};
