import React, { useEffect, useState } from "react";
import { Section, EndMessage } from "./Posts-styles";
import Post from "../Post/Post";
import { getPosts } from "../../services/PostsService";
import UsersModal from "../UsersModal/UsersModal";
import { userSelectors } from "../../store/user";
import { useSelector } from "react-redux";
import InfiniteScroll from "react-infinite-scroll-component";
import Loader from "../Loader/Loader";

const Posts = () => {
  const user = useSelector(userSelectors.getUser());
  const [posts, setPosts] = useState([]);
  const [from, setFrom] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [hasMore, setHasMore] = useState(true);

  const fetchPosts = () => {
    if (from === 0) return;
    getPosts(from, 3).then((res) => {
      setPosts((prevState) => [...prevState, ...res.data]);
      setFrom((prevState) => prevState + 1);
      setHasMore(res.data.length < 3 ? false : true);
    });
  };

  const postsList = posts.map((p) => (
    <Post
      key={p.id}
      postId={p.id}
      img={p.imageUrl}
      userImg={p.author.profileImgUrl}
      userId={p.author.id}
      username={p.author.username}
      mainUserId={user.id}
      likes={p.likes}
      liked={p.likes.includes(user.id)}
      desc={p.description}
      postComments={p.comments}
      setShowModal={setShowModal}
      setPosts={setPosts}
    />
  ));

  useEffect(() => {
    if (!user.user) return;
    if (posts.length) {
      getPosts(0, 3).then((res) => {
        setHasMore(res.data.length > 0 ? true : false);
        setPosts([...res.data]);
        setFrom(1);
      });
    } else {
      getPosts(from, 3).then((res) => {
        setPosts((prevState) => [...prevState, ...res.data]);
        setFrom((prevState) => prevState + 1);
        setHasMore(res.data.length > 0 ? true : false);
      });
    }
  }, [user]);

  return (
    <Section>
      <InfiniteScroll
        dataLength={posts.length}
        next={fetchPosts}
        hasMore={hasMore}
        loader={<Loader postsLoader={true} />}
        endMessage={<EndMessage>Yay! You have seen it all</EndMessage>}
      >
        {postsList}
        {showModal && <UsersModal setShowModal={setShowModal} />}
      </InfiniteScroll>
    </Section>
  );
};

export default Posts;
