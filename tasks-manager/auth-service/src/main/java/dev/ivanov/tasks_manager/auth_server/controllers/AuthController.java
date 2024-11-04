package dev.ivanov.tasks_manager.auth_server.controllers;

import dev.ivanov.tasks_manager.auth_server.dto.*;
import dev.ivanov.tasks_manager.auth_server.exceptions.AccountNotFoundException;
import dev.ivanov.tasks_manager.auth_server.exceptions.AuthorizationException;
import dev.ivanov.tasks_manager.auth_server.services.AccountService;
import dev.ivanov.tasks_manager.auth_server.services.AuthService;
import dev.ivanov.tasks_manager.auth_server.validators.ChangePasswordDtoValidator;
import dev.ivanov.tasks_manager.auth_server.validators.RefreshDtoValidator;
import dev.ivanov.tasks_manager.auth_server.validators.SignUpDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SignUpDtoValidator signUpDtoValidator;

    @Autowired
    private ChangePasswordDtoValidator changePasswordDtoValidator;

    @Autowired
    private RefreshDtoValidator refreshDtoValidator;

    @PostMapping("/refresh/{accountId}")
    public ResponseEntity<?> refresh(@RequestBody RefreshDto refreshDto,
                                     @PathVariable String accountId) {
        var errors = new BeanPropertyBindingResult(refreshDto, "refreshDto");
        refreshDtoValidator.validate(refreshDto, errors);
        if (errors.hasErrors())
            return ResponseEntity.badRequest().body(errors.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());

        var access = authService.refresh(accountId);
        return ResponseEntity.ok(
                TokenDto.builder()
                        .access(access)
                        .build()
        );
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInDto signInDto) {
        try {
            var tokenDto = authService.signIn(signInDto);
            return ResponseEntity.ok(tokenDto);
        } catch (AuthorizationException | AuthenticationException | AccountNotFoundException e) {
            return ResponseEntity.badRequest().body("authentication error");
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto) {
        var errors = new BeanPropertyBindingResult(signUpDto, "signUpDto");
        signUpDtoValidator.validate(signUpDto, errors);
        if (errors.hasErrors())
            return ResponseEntity.badRequest().body(
                    errors.getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        accountService.createAccount(signUpDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password/{accountId}")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto,
                                            @PathVariable String accountId) {
        var errors = new BeanPropertyBindingResult(changePasswordDto, "changePasswordDto");
        changePasswordDtoValidator.validate(changePasswordDto, errors);
        if (errors.hasErrors())
            return ResponseEntity.badRequest().body(errors.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        try {

            accountService.changePassword(accountId, changePasswordDto);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-account/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }
}
