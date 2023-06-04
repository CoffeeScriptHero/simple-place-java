package com.kozarenko.service;

import com.kozarenko.model.additional.keys.FollowingPk;
import com.kozarenko.model.base.User;
import com.kozarenko.repository.FollowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowingService {

  private final FollowingRepository followingRepository;

  public boolean isCurrUserFollowing(User currentUser, User user) {
    return followingRepository.existsById(new FollowingPk(currentUser.getId(), user.getId()));
  }
}
