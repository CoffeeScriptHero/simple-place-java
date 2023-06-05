import React from "react";
import {
  PostPreview,
  PostInfo,
  Wrapper,
  Stat,
  InfoText,
} from "./UserPost-styles";
import Icon from "../Icon/Icon";
import { useNavigate } from "react-router-dom";
import { postModalOperations } from "../../store/postModal";
import { getPost } from "../../services/PostsService";

const UserPost = ({ id, img, likes, comments, dispatch }) => {
  const navigate = useNavigate();

  const postModalHandler = () => {
    getPost(id).then((res) => {
      dispatch(
        postModalOperations.setPostInfo({
          username: res.data.author.username,
          profileImg: res.data.author.profileImgUrl,
          imageUrl: res.data.imageUrl,
          likes: likes,
          userId: res.data.author.id,
          postId: id,
          comments: res.data.comments,
          description: res.data.description,
        })
      );
      navigate(`p/${id}`);
    });
  };
  return (
    <Wrapper>
      <PostPreview img={img} onClick={postModalHandler}></PostPreview>
      <Stat>
        <PostInfo>
          <Icon type="like" stroke={"white"} color={"white"}></Icon>
          <InfoText>{likes.length}</InfoText>
        </PostInfo>
        <PostInfo>
          <Icon type="previewcomment" stroke={"white"} color={"white"}></Icon>
          <InfoText>{comments.length}</InfoText>
        </PostInfo>
      </Stat>
    </Wrapper>
  );
};

export default UserPost;
