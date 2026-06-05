package com.ecommerce.auth_service.service;


import com.ecommerce.auth_service.dto.AuthResponse;
import com.ecommerce.auth_service.dto.LoginRequest;
import com.ecommerce.auth_service.dto.RegisterRequest;
import com.ecommerce.auth_service.dto.RegisterResponse;
import com.ecommerce.auth_service.entity.Role;
import com.ecommerce.auth_service.entity.User;
import com.ecommerce.auth_service.exception.EmailAlreadyExistsException;
import com.ecommerce.auth_service.exception.InvalidCredentialsException;
import com.ecommerce.auth_service.repository.UserRepository;
import com.ecommerce.auth_service.security.CustomUserDetails;
import com.ecommerce.auth_service.util.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public RegisterResponse register(RegisterRequest request){
        log.info("Register request received for email={}", request.email());

        if(userRepository.countByEmail(request.email()) > 0){

            log.warn("Registered failed. Email already exists={}",
                    request.email());

            throw new EmailAlreadyExistsException(
                    "Email already exists"
            );
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(
                        passwordEncoder.encode(
                                request.password()
                        )
                )
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        log.info("User registered successfully. userID={}",
                user.getId());

        return new RegisterResponse("Registration successful");
    }

    public AuthResponse login(LoginRequest request){

        log.info(
                "Login attemp for email={}",
                request.email()
        );

        try{
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.email(),
                                    request.password()
                            )
                    );

            CustomUserDetails userDetails =
                    (CustomUserDetails) authentication.getPrincipal();

            String role = userDetails
                    .getAuthorities()
                    .iterator()
                    .next()
                    .getAuthority();

            String token = jwtService.generateToken(
                    userDetails.getUsername(),
                    role
            );

            log.info(
                    "Login successful for email={}",
                    request.email()
            );

            return new AuthResponse(
                    token,
                    userDetails.getUsername(),
                    userDetails
                            .getAuthorities()
                            .iterator()
                            .next()
                            .getAuthority()
            );
        } catch (AuthenticationException ex){
            log.warn(
                    "Invalid credentials for email={}",
                    request.email()
            );

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }
    }



}
