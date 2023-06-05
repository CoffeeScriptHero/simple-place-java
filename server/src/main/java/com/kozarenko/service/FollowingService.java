package com.kozarenko.service;

import com.kozarenko.model.additional.Following;
import com.kozarenko.model.additional.keys.FollowingPk;
import com.kozarenko.model.base.User;
import com.kozarenko.repository.FollowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowingService {

  private final FollowingRepository followingRepository;

  public List<User> getSuggestedUsers(User user) {
    return followingRepository.findNonFollowedUsers(user.getId());
  }

  public List<User> getFollowers(String id) {
    return followingRepository.findFollowersByFollowedId(id);
  }

  public List<User> getFollowings(String id) {
    return followingRepository.findUserFollowingsById(id);
  }

  public boolean isUserFollowing(User user1, User user2) {
    return followingRepository.existsById(new FollowingPk(user1.getId(), user2.getId()));
  }

  public boolean followUnfollow(User user, User currentUser) {
    if (!isUserFollowing(currentUser, user)) {
      followingRepository.save(new Following(currentUser, user));
      return true;
    }

    followingRepository.deleteById(new FollowingPk(currentUser.getId(), user.getId()));
    return false;
  }

  public void deleteFollower(User user, User currentUser) {
    followingRepository.deleteById(new FollowingPk(user.getId(), currentUser.getId()));
  }
}
