import React, { useEffect, useState } from "react";
import {
  RecommendedWrapper,
  Inscription,
  UsersWrapper,
  InfoWrapper,
} from "./Recommended-styles";
import { getRecommendedUsers } from "../../services/UserService.js";
import Recommendation from "../Recommendation/Recommendation";

const Recommended = () => {
  const [users, setUsers] = useState([]);
  const emojisArray = [
    "☕",
    "🌈",
    "🤴🏻",
    "💪",
    "🐳",
    "🌸",
    "🌌",
    "🎨",
    "🏄🏻",
    "🃏",
    "🎵",
  ];

  useEffect(() => {
    getRecommendedUsers().then((res) => {
      setUsers(res.data);
    });
  }, []);

  const usersList = users.map((u) => (
    <Recommendation
      key={u.id}
      id={u.id}
      profileImg={u.profileImgUrl}
      username={u.username}
    />
  ));

  return (
    <RecommendedWrapper>
      <Inscription>
        Recommended for you{" "}
        {emojisArray[Math.floor(Math.random() * emojisArray.length)]}
      </Inscription>
      {users.length > 0 && <UsersWrapper>{usersList}</UsersWrapper>}
      {users.length === 0 && (
        <InfoWrapper>
          <Inscription>
            WOW! <br /> There are no people you don't subscribe to!
          </Inscription>
        </InfoWrapper>
      )}
    </RecommendedWrapper>
  );
};

export default Recommended;
