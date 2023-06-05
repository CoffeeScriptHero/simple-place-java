import React, { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { searchUsers } from "../../services/UserService";
import { userSelectors } from "../../store/user";
import {
  Wrapper,
  UsersWrapper,
  UserWrapperLink,
  SearchText,
} from "./SearchBarUsers-styles";
import UserWrapper from "../UserWrapper/UserWrapper";
import Loader from "../Loader/Loader";

const SearchBarUsers = ({ usernameChunk, usersLoaded, setUsersLoaded }) => {
  const mainUser = useSelector(userSelectors.getUser());
  const [users, setUsers] = useState([]);

  const usersList = users.map((u) => (
    <UserWrapperLink to={`/${u.username}`} key={u.username}>
      <UserWrapper
        isLink={false}
        padding="8px 16px"
        activeBackground="rgba(var(--bb2,239,239,239),1)"
        profileImg={u.profileImgUrl}
        username={u.username}
      />
    </UserWrapperLink>
  ));

  useEffect(() => {
    const containsForbiddenSymbol = (usernameChunk) => {
      const forbiddenSymbols = ["\\", "/", "[", "]"];
      for (let i = 0; i < forbiddenSymbols.length; i++) {
        if (usernameChunk.includes(forbiddenSymbols[i])) {
          return true;
        }
      }
      return false;
    }

    if (containsForbiddenSymbol(usernameChunk)) {
      setUsers([]);
      setUsersLoaded(true);
      return;
    }

    searchUsers(usernameChunk).then((res) => {
      setUsers(res.data);
      setUsersLoaded(true);
    });
  }, [usernameChunk, mainUser]);

  return (
    <Wrapper>
      {!usersLoaded && <Loader />}
      {usersLoaded && (
        <UsersWrapper showText={!users.length}>
          {!usernameChunk.length && (
            <SearchText>
              Write the username of the person you want to find
            </SearchText>
          )}
          {!!usernameChunk.length && !!users.length && usersList}
          {!!usernameChunk.length && !users.length && (
            <SearchText>There is no such user</SearchText>
          )}
        </UsersWrapper>
      )}
    </Wrapper>
  );
};

export default SearchBarUsers;
