package com.kozarenko.repository;

import com.kozarenko.model.additional.Following;
import com.kozarenko.model.additional.keys.FollowingPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Following, FollowingPk> {
}
