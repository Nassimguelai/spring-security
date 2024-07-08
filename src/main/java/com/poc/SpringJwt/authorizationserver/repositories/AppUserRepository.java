package com.poc.SpringJwt.authorizationserver.repositories;

import com.poc.SpringJwt.authorizationserver.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    AppUser findByUsername(String username);

    AppUser save(AppUser user);

}
