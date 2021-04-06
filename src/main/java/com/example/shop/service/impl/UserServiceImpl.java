package com.example.shop.service.impl;

import com.example.shop.model.entity.User;
import com.example.shop.model.service.userServiceModels.UserProfileServiceModel;
import com.example.shop.model.service.userServiceModels.UserRegisterServiceModel;
import com.example.shop.repository.UserRepository;
import com.example.shop.security.DemoUserDetailsService;
import com.example.shop.service.RoleService;
import com.example.shop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final DemoUserDetailsService demoUserDetailsService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, PasswordEncoder passwordEncoder, DemoUserDetailsService demoUserDetailsService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.demoUserDetailsService = demoUserDetailsService;
    }

    @PostConstruct
    private void initializeAdmin() {

        if (this.userRepository.count() == 0) {
            User user = new User();
            user.setUsername("valo");
            user.setPassword(passwordEncoder.encode("qweqw1"));
            user.setEmail("valentin@gmail.com");
            user.setRoles(roleService.findAllRoles());

            this.userRepository.saveAndFlush(user);
        }
    }

    @Override
    public UserRegisterServiceModel registerAndLoginUser(UserRegisterServiceModel serviceModel) {
        User user = this.modelMapper.map(serviceModel, User.class);
        user.setPassword(passwordEncoder.encode(serviceModel.getPassword()));
        user.setRoles(this.roleService.findRoleByRole("USER"));
        this.userRepository.saveAndFlush(user);

        UserDetails principal = demoUserDetailsService.loadUserByUsername(user.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                user.getPassword(),
                principal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return serviceModel;
    }

    @Override
    public boolean checkUsernameIfExists(String username) {
        return this.userRepository.findUserByUsername(username).isPresent();
    }

    @Override
    public boolean checkEmailIfExists(String email) {
        return this.userRepository.findUserByEmail(email).isPresent();
    }

    @Override
    public List<String> getUsernames() {
        List<String> allUsernames = this.userRepository.findAllUsernames();
        return allUsernames;
    }

    @Override
    public void changeRole(String username, String role) {

        Optional<User> user = this.userRepository.findUserByUsername(username);

        if (role.equals("ADMIN")) {
            user.get().setRoles(this.roleService.findAllRolesForAdmin());
        } else {
            user.get().setRoles(this.roleService.findRoleByRole("USER"));
        }
        this.userRepository.saveAndFlush(user.get());
    }

    @Override
    public UserProfileServiceModel getUserByUsername(String name) {

        Optional<User> user = this.userRepository.findUserByUsername(name);

        return this.modelMapper.map(user.get(), UserProfileServiceModel.class);
    }

    @Async
    @Override
    public void deleteProfileById(String id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public User getUserEntityByUsername(String name) {
        return this.userRepository.findUserByUsername(name).get();
    }


}
