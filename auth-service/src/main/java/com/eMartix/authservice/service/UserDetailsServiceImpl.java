package com.eMartix.authservice.service;

import com.eMartix.authservice.model.RolePermission;
import com.eMartix.authservice.model.User;
import com.eMartix.authservice.repository.RolePermissionRepository;
import com.eMartix.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;

//    UserDetailsServiceImpl(UserRepository userRepository, RolePermissionRepository rolePermissionRepository) {
//        this.userRepository = userRepository;
//        this.rolePermissionRepository = rolePermissionRepository;
//    }

    @Primary
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return  userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail)));

    }


    private Collection<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add role-based authorities
        authorities.addAll(user.getUserRoles().stream()
                .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getRoleName()))
                .collect(Collectors.toList()));

        // Add permission-based authorities
        List<RolePermission> rolePermissions = rolePermissionRepository.findByUserId(user.getId());
        authorities.addAll(rolePermissions.stream()
                .map(rp -> new SimpleGrantedAuthority(rp.getPermission().getPermissionName()))
                .collect(Collectors.toList()));

        return authorities;
    }
}
