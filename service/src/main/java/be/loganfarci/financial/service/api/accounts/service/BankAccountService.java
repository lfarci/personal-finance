package be.loganfarci.financial.service.api.accounts.service;

import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.accounts.persistence.BankAccountEntity;
import be.loganfarci.financial.service.api.accounts.persistence.BankAccountRepository;
import be.loganfarci.financial.service.api.errors.exceptions.ResourceConflict;
import be.loganfarci.financial.service.api.errors.exceptions.ResourceNotFound;
import be.loganfarci.financial.service.api.users.model.UserDto;
import be.loganfarci.financial.service.api.users.persistence.UserEntity;
import be.loganfarci.financial.service.api.users.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankAccountService {

    public static final String NOT_FOUND_MESSAGE_CODE = "account.not_found";
    public static final String IBAN_ALREADY_EXIST_MESSAGE_CODE = "account.iban_conflict";
    public static final String SAVE_ID_MISMATCH_MESSAGE_CODE = "account.save_id_mismatch";
    public static final Double DEFAULT_BALANCE = 0.0;

    private final UserService userService;
    private final BankAccountRepository repository;
    private final BankAccountMapper mapper;
    private final MessageSourceAccessor messages;

    public BankAccountService(
            UserService userService,
            BankAccountRepository repository,
            BankAccountMapper mapper,
            MessageSource messages
    ) {
        this.userService = userService;
        this.repository = repository;
        this.mapper = mapper;
        this.messages = new MessageSourceAccessor(messages);
    }

    public boolean existsByUserIdAndBankAccountId(Long userId, Long bankAccountId) {
        return repository.existsByIdAndUserId(bankAccountId, userId);
    }

    public List<BankAccountDto> findByUserId(Long userId) {
        UserDto user = userService.findById(userId);
        return findAllFor(user);
    }

    public BankAccountDto findByIdAndUserId(Long userId, Long bankAccountId) {
        UserDto user = userService.findById(userId);
        return findById(user, bankAccountId);
    }

    public BankAccountDto findById(UserDto user, Long bankAccountId) {
        Optional<BankAccountDto> bankAccount = attemptToFindById(user, bankAccountId);
        if (bankAccount.isPresent()) {
            return bankAccount.get();
        } else {
            throw notFound(bankAccountId);
        }
    }

    public void deleteByIdAndUserId(Long userId, Long bankAccountId) {
        UserDto user = userService.findById(userId);
        Optional<BankAccountDto> bankAccount = attemptToFindById(user, bankAccountId);
        if (bankAccount.isPresent()) {
            repository.deleteById(bankAccountId);
        } else {
            throw notFound(bankAccountId);
        }
    }

    private Optional<BankAccountDto> attemptToFindById(UserDto user, Long bankAccountId) {
        return findAllFor(user)
                .stream()
                .filter(b -> b.getId().equals(bankAccountId))
                .findFirst();
    }

    private List<BankAccountDto> findAllFor(UserDto user) {
        return repository.findByUserId(user.getId())
                .stream()
                .map(mapper::toRest)
                .collect(Collectors.toList());
    }

    private BankAccountService.NotFound notFound(Long bankAccountId) {
        return new BankAccountService.NotFound(messages.getMessage(NOT_FOUND_MESSAGE_CODE, new Long[]{ bankAccountId }));
    }


    private BankAccountService.Conflict conflict(String iban) {
        return new Conflict(messages.getMessage(IBAN_ALREADY_EXIST_MESSAGE_CODE, new String[] { iban }));
    }


    private UserService.InvalidArgument userIdMismatch(Long bodyId, Long URLQueryParamId) {
        return new UserService.InvalidArgument(messages.getMessage(SAVE_ID_MISMATCH_MESSAGE_CODE, new Long[]{ bodyId, URLQueryParamId }));
    }

    private boolean existsByIban(String iban) {
        return iban != null && repository.existsByIban(iban);
    }

    public BankAccountDto saveForUserId(Long userId, BankAccountDto bankAccount) {
        UserEntity user = userService.findEntityById(userId);
        BankAccountEntity entity = mapper.fromRest(bankAccount, user);
        if (!userId.equals(bankAccount.getUserId())) {
            throw userIdMismatch(userId, bankAccount.getId());
        }
        if (existsByIban(bankAccount.getIban())) {
            throw conflict(bankAccount.getIban());
        }
        if (!entity.hasBalance()) {
            entity.setBalance(DEFAULT_BALANCE);
        }
        return mapper.toRest(repository.save(entity));
    }

    public static class NotFound extends ResourceNotFound {
        public NotFound(String message) {
            super(message);
        }
    }

    public static class Conflict extends ResourceConflict {
        public Conflict(String message) {
            super(message);
        }
    }

    public static class InvalidArgument extends RuntimeException {
        public InvalidArgument(String message) {
            super(message);
        }
    }

}
