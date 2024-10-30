package dev.ivanov.tasks_manager.auth_server.services;

import dev.ivanov.tasks_manager.auth_server.dto.ChangePasswordDto;
import dev.ivanov.tasks_manager.auth_server.dto.SignUpDto;
import dev.ivanov.tasks_manager.auth_server.entities.postgres.Account;
import dev.ivanov.tasks_manager.auth_server.entities.postgres.Role;
import dev.ivanov.tasks_manager.auth_server.entities.redis.AccountCache;
import dev.ivanov.tasks_manager.auth_server.exceptions.AccountNotFoundException;
import dev.ivanov.tasks_manager.auth_server.exceptions.InternalServerException;
import dev.ivanov.tasks_manager.auth_server.producers.AccountCreatedEventsProducer;
import dev.ivanov.tasks_manager.auth_server.producers.AccountDeletedEventsProducer;
import dev.ivanov.tasks_manager.auth_server.repositories.postgres.AccountRepository;
import dev.ivanov.tasks_manager.auth_server.repositories.redis.AccountCacheRepository;
import dev.ivanov.tasks_manager.auth_server.security.JwtUtils;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountService {

    public static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountCacheRepository accountCacheRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private AccountCreatedEventsProducer accountCreatedEventsProducer;
    private AccountDeletedEventsProducer accountDeletedEventsProducer;


    @Autowired
    public void setAccountCreatedEventsProducer(@Lazy AccountCreatedEventsProducer accountCreatedEventsProducer) {
        this.accountCreatedEventsProducer = accountCreatedEventsProducer;
    }

    @Autowired
    public void setAccountDeletedEventsProducer(@Lazy AccountDeletedEventsProducer accountDeletedEventsProducer) {
        this.accountDeletedEventsProducer = accountDeletedEventsProducer;
    }

    @Value("${app.gateway.host}")
    private String gatewayHost;

    @Transactional
    public Account createAccount(SignUpDto signUpDto) {
        var encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

        var idEntity = restTemplate.getForEntity(gatewayHost + "/api/uuid", String.class);
        if (idEntity.getStatusCode().isError())
            throw new InternalServerException();
        var id = idEntity.getBody();
        var accountCache = AccountCache.builder()
                .id(id)
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .roles(signUpDto.getRoles())
                .build();
        var savedAccountCache = accountCacheRepository.save(accountCache);
        var account = Account.from(savedAccountCache);
        accountCreatedEventsProducer.send(account);
        return account;
    }

    @Transactional
    public void deleteAccount(String id) {
        accountDeletedEventsProducer.send(id);
    }

    @Transactional
    public void changePassword(ChangePasswordDto changePasswordDto) {
        var claims = jwtUtils.verify(changePasswordDto.getRefreshToken());
        var id = claims.get("id").asString();
        var accountOptional = accountRepository.findById(id);
        var account = accountOptional
                .orElseThrow(() -> new AccountNotFoundException("account with id " + id + " not found"));
        var newPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());
        account.setPassword(newPassword);
        accountRepository.save(account);
    }

    @Transactional
    public void rollbackDeletion(String id) {
        LOGGER.info("rollback deletion account {}", id);
    }

    @Transactional
    public void commitDeletion(String id) {
        accountRepository.deleteById(id);
    }

    @Transactional
    public void rollbackCreation(String id) {
        LOGGER.info("rollback creation account {}", id);
        accountCacheRepository.deleteById(id);
    }

    @Transactional
    public void commitCreation(String id) {
        var accountCache = accountCacheRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("account with id "+ id + " not found"));
        var account = Account.from(accountCache);
        accountRepository.save(account);
    }


}
