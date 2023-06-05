import { CommentsWrapper, ShowAll } from "./Comments-styles";
import Comment from "../Comment/Comment";
import { useDispatch, useSelector } from "react-redux";
import { usersModalOperations } from "../../store/usersModal";
import { userSelectors } from "../../store/user";

const Comments = ({
  showAll,
  comments,
  setShowModal,
  setComments = null,
  postId = null,
  userId = null,
  postModalHandler = null,
}) => {
  const dispatch = useDispatch();
  const user = useSelector(userSelectors.getUser());

  const modalHandler = (commentId) => {
    dispatch(usersModalOperations.setNewModalType("Likes"));
    dispatch(usersModalOperations.getCommentLikes(commentId));
    setShowModal(true);
  };

  const commentsList = comments.map((c) => (
    <Comment
      key={c.id}
      commentId={c.id}
      text={c.text}
      liked={c.likes.includes(user.id)}
      likes={c.likes}
      username={c.author.username}
      profileImg={c.author.profileImgUrl}
      setComments={setComments}
      mainUserId={user.id}
      commentUserId={c.author.id}
      postId={postId}
      userId={userId}
      comments={comments}
      modalHandler={modalHandler}
    />
  ));

  return (
    <CommentsWrapper>
      {comments.length >= 1 && !showAll && (
        <ShowAll onClick={postModalHandler}>
          Show all comments ({comments.length})
        </ShowAll>
      )}
      {comments.length >= 1 && showAll && commentsList}
    </CommentsWrapper>
  );
};

export default Comments;
