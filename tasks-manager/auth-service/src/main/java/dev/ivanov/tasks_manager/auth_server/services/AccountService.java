package dev.ivanov.tasks_manager.auth_server.services;

import dev.ivanov.tasks_manager.auth_server.dto.ChangePasswordDto;
import dev.ivanov.tasks_manager.auth_server.dto.SignUpDto;
import dev.ivanov.tasks_manager.auth_server.entities.postgres.Account;
import dev.ivanov.tasks_manager.auth_server.entities.postgres.Role;
import dev.ivanov.tasks_manager.auth_server.entities.redis.AccountCache;
import dev.ivanov.tasks_manager.auth_server.entities.redis.Token;
import dev.ivanov.tasks_manager.auth_server.entities.redis.UsernameCache;
import dev.ivanov.tasks_manager.auth_server.exceptions.AccountNotFoundException;
import dev.ivanov.tasks_manager.auth_server.exceptions.InternalServerException;
import dev.ivanov.tasks_manager.auth_server.producers.AccountCreatedEventsProducer;
import dev.ivanov.tasks_manager.auth_server.producers.AccountDeletedEventsProducer;
import dev.ivanov.tasks_manager.auth_server.repositories.postgres.AccountRepository;
import dev.ivanov.tasks_manager.auth_server.repositories.postgres.RoleRepository;
import dev.ivanov.tasks_manager.auth_server.repositories.redis.AccountCacheRepository;
import dev.ivanov.tasks_manager.auth_server.repositories.redis.BlacklistTokenRepository;
import dev.ivanov.tasks_manager.auth_server.repositories.redis.UsernameCacheRepository;
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

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsernameCacheRepository usernameCacheRepository;

    @Autowired
    private BlacklistTokenRepository blacklistTokenRepository;

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
    public void createAccount(SignUpDto signUpDto) {
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
        accountCacheRepository.save(accountCache);
        usernameCacheRepository.save(UsernameCache.builder()
                .id(signUpDto.getUsername())
                .accountId(id)
                .build());
        accountCreatedEventsProducer.send(id);
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
        blacklistTokenRepository.save(Token.builder()
                .id(changePasswordDto.getRefreshToken())
                .token(changePasswordDto.getRefreshToken())
                .build());
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
        accountCacheRepository.deleteById(id);
        usernameCacheRepository.deleteById(accountCache.getUsername());
        var account = Account.from(accountCache);
        var savedAccount = accountRepository.save(account);
        var roles = accountCache.getRoles().stream().map(r -> roleRepository.getReferenceById(r)).toList();
        savedAccount.setRoles(roles);
        accountRepository.save(savedAccount);
    }


}