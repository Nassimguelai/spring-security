package com.poc.SpringJwt.authorizationserver.configurations;

import com.poc.SpringJwt.authorizationserver.entities.*;
import com.poc.SpringJwt.authorizationserver.repositories.AppUserRepository;
import com.poc.SpringJwt.authorizationserver.repositories.PrivilegeRepository;
import com.poc.SpringJwt.authorizationserver.repositories.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(AppUserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (!alreadySetup) {
            Privilege readPrivilege = savePrivilegeIfNotExists(PrivilegeType.READ);
            Privilege writePrivilege = savePrivilegeIfNotExists(PrivilegeType.WRITE);
            Privilege deletePrivilege = savePrivilegeIfNotExists(PrivilegeType.DELETE);

            List<Privilege> adminPrivilege = Arrays.asList(readPrivilege, writePrivilege, deletePrivilege);
            List<Privilege> editorPrivilege = Arrays.asList(readPrivilege, writePrivilege);
            List<Privilege> userPrivilege = Arrays.asList(readPrivilege);

            Role adminRole = saveRoleIfNotExists(RoleType.ADMIN, adminPrivilege);
            Role editorRole = saveRoleIfNotExists(RoleType.EDITOR, editorPrivilege);
            Role userRole = saveRoleIfNotExists(RoleType.USER, userPrivilege);

            String adminPassword = passwordEncoder.encode("test");
            String editorPassword = "{noop}" + NoOpPasswordEncoder.getInstance().encode("test");
            String userPassword ="{sha256}" + new StandardPasswordEncoder().encode("test");

            saveUserIfNotExists("admin", adminPassword, List.of(adminRole));
            saveUserIfNotExists("editor", editorPassword, List.of(editorRole));
            saveUserIfNotExists("user", userPassword, List.of(userRole));

            alreadySetup = true;
        }
    }

    private void saveUserIfNotExists(String username, String password, Collection<Role> roles) {
        AppUser user = userRepository.findByUsername(username);
        if ( user == null) {
            user = new AppUser(username, password, roles);
            userRepository.save(user);
        }
    }

    private Privilege savePrivilegeIfNotExists(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if(privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    private Role saveRoleIfNotExists(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if(role == null) {
            role = new Role(name, privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
