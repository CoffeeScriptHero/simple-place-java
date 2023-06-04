package com.kozarenko.service;

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

  public boolean isCurrUserFollowing(User currentUser, User user) {
    return followingRepository.existsById(new FollowingPk(currentUser.getId(), user.getId()));
  }
}
