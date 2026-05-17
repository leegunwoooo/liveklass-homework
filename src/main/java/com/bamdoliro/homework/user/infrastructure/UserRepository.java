package com.bamdoliro.homework.user.infrastructure;

import com.bamdoliro.homework.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
