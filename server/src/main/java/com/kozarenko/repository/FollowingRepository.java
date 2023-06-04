package com.kozarenko.repository;

import com.kozarenko.model.additional.Following;
import com.kozarenko.model.additional.keys.FollowingPk;
import com.kozarenko.model.base.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowingRepository extends JpaRepository<Following, FollowingPk> {

  @Query(value = "SELECT u.* FROM users u " +
          "LEFT JOIN followings f ON u.id = f.followed_id AND f.follower_id = ?1 " +
          "WHERE f.followed_id IS NULL AND u.id <> ?1 " +
          "LIMIT 14", nativeQuery = true)
  List<User> findNonFollowedUsers(String id);

  @Query("SELECT f.followerUser FROM Following f WHERE f.followedUser.id = ?1")
  List<User> findFollowersByFollowedId(String id);

  @Query("SELECT f.followedUser FROM Following f WHERE f.followerUser.id = ?1")
  List<User> findUserFollowingsById(String id);
}
