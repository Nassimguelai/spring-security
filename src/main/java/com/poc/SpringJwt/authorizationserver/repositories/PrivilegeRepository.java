package com.poc.SpringJwt.authorizationserver.repositories;

import com.poc.SpringJwt.authorizationserver.entities.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {

    Privilege findByName(String name);

    Privilege save(Privilege privilege);
}