package com.poc.SpringJwt.authorizationserver.repositories;


import com.poc.SpringJwt.authorizationserver.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String name);

    Role save(Role role);

}
