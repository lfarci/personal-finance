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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankAccountService {

    public static final String NOT_FOUND = "account.not_found";
    public static final String IBAN_ALREADY_EXIST = "account.iban_conflict";
    public static final String SAVE_ID_MISMATCH = "account.save_id_mismatch";
    public static final String REQUIRED_BALANCE = "account.required_balance";
    public static final String SAVE_INTERNAL_OWNER_NAME = "account.save_internal_with_owner_name";
    public static final String SAVE_EXTERNAL_OWNER_NAME = "account.save_external_without_owner_name";
    public static final String INTERNAL_FLAG_UPDATE = "account.update_cannot_update_internal_flag";
    public static final String UPDATE_INTERNAL_OWNER_NAME = "account.update_internal_with_owner_name";
    public static final String UPDATE_EXTERNAL_OWNER_NAME = "account.update_external_without_owner_name";
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

    public Page<BankAccountDto> findByUserId(Long userId, Pageable pageable) {
        UserDto user = userService.findById(userId);
        return findAllFor(user, pageable);
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

    private Page<BankAccountDto> findAllFor(UserDto user, Pageable pageable) {
        return repository.findByUserIdAndInternalIsTrue(user.getId(), pageable).map(mapper::toRest);
    }

    private void requireValidBankAccountForSaving(Long userId, BankAccountDto bankAccount) {
        if (!userId.equals(bankAccount.getUserId())) {
            throw userIdMismatch(userId, bankAccount.getId());
        }
        if (existsByIban(bankAccount.getIban())) {
            throw conflict(bankAccount.getIban());
        }
        if (bankAccount.isInternal() && bankAccount.hasOwnerName()) {
            throw unprocessable(SAVE_INTERNAL_OWNER_NAME);
        }
        if (bankAccount.isExternal() && !bankAccount.hasOwnerName()) {
            throw unprocessable(SAVE_EXTERNAL_OWNER_NAME);
        }
    }

    public BankAccountDto saveForUserId(Long userId, BankAccountDto bankAccount) {
        UserEntity user = userService.findEntityById(userId);
        BankAccountEntity entity = mapper.fromRest(bankAccount, user);
        requireValidBankAccountForSaving(userId, bankAccount);
        if (!entity.hasBalance()) {
            entity.setBalance(DEFAULT_BALANCE);
        }
        return mapper.toRest(repository.save(entity));
    }

    private boolean hasNewIban(BankAccountDto existing, BankAccountDto updated) {
        return existing.getIban() != null && !existing.getIban().equals(updated.getIban());
    }

    private boolean hasTheInternalFlagBeenUpdated(BankAccountDto existing, BankAccountDto updated) {
        return existing.isInternal() != null && !existing.isInternal().equals(updated.isInternal());
    }


    private void requireValidBankAccountForUpdating(Long userId, BankAccountDto existing, BankAccountDto updated) {
        if (updated.getBalance() == null) {
            throw balanceRequired();
        }
        if (!userId.equals(updated.getUserId())) {
            throw userIdMismatch(userId, updated.getId());
        }
        if (hasNewIban(existing, updated) && existsByIban(updated.getIban())) {
            throw conflict(updated.getIban());
        }
        if (hasTheInternalFlagBeenUpdated(existing, updated)) {
            throw unprocessable(INTERNAL_FLAG_UPDATE);
        }
        if (existing.isInternal() && updated.hasOwnerName()) {
            throw unprocessable(UPDATE_INTERNAL_OWNER_NAME);
        }
        if (existing.isExternal() && !updated.hasOwnerName()) {
            throw unprocessable(UPDATE_EXTERNAL_OWNER_NAME);
        }
    }

    public void updateByIdAndUserUd(Long userId, Long bankAccountId, BankAccountDto updated) {
        UserEntity user = userService.findEntityById(userId);
        BankAccountDto existing = findByIdAndUserId(userId, bankAccountId);
        requireValidBankAccountForUpdating(userId, existing, updated);
        existing.setName(updated.getName());
        existing.setIban(updated.getIban());
        existing.setBalance(updated.getBalance());
        if (existing.isExternal()) {
            existing.setOwnerName(updated.getOwnerName());
        }
        repository.save(mapper.fromRest(existing, user));
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

    public static class UnprocessableEntity extends RuntimeException {
        public UnprocessableEntity(String message) {
            super(message);
        }
    }

    private BankAccountService.NotFound notFound(Long bankAccountId) {
        return new BankAccountService.NotFound(messages.getMessage(NOT_FOUND, new Long[]{ bankAccountId }));
    }

    private BankAccountService.Conflict conflict(String iban) {
        return new Conflict(messages.getMessage(IBAN_ALREADY_EXIST, new String[] { iban }));
    }

    private BankAccountService.InvalidArgument balanceRequired() {
        return new BankAccountService.InvalidArgument(messages.getMessage(REQUIRED_BALANCE));
    }

    private BankAccountService.InvalidArgument userIdMismatch(Long bodyId, Long URLQueryParamId) {
        return new BankAccountService.InvalidArgument(messages.getMessage(SAVE_ID_MISMATCH, new Long[]{ bodyId, URLQueryParamId }));
    }

    private BankAccountService.UnprocessableEntity unprocessable(String messageKey) {
        return new BankAccountService.UnprocessableEntity(messages.getMessage(messageKey));
    }

    private boolean existsByIban(String iban) {
        return iban != null && repository.existsByIban(iban);
    }

}
