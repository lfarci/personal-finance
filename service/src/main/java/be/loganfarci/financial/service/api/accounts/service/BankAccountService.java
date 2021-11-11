package be.loganfarci.financial.service.api.accounts.service;

import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.accounts.persistence.BankAccountRepository;
import be.loganfarci.financial.service.api.errors.exceptions.ResourceNotFound;
import be.loganfarci.financial.service.api.users.model.UserDto;
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

    public List<BankAccountDto> findByUserId(Long userId) {
        UserDto user = userService.findById(userId);
        return findAllFor(user);
    }

    public BankAccountDto findByUserIdAndBankAccountId(Long userId, Long bankAccountId) {
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

    public Optional<BankAccountDto> attemptToFindById(UserDto user, Long bankAccountId) {
        return findAllFor(user)
                .stream()
                .filter(b -> b.getId().equals(bankAccountId))
                .findFirst();
    }

    public List<BankAccountDto> findAllFor(UserDto user) {
        return repository.findByUserId(user.getId())
                .stream()
                .map(mapper::toRest)
                .collect(Collectors.toList());
    }

    private BankAccountService.NotFound notFound(Long bankAccountId) {
        return new BankAccountService.NotFound(messages.getMessage(NOT_FOUND_MESSAGE_CODE, new Long[]{ bankAccountId }));
    }


    public static class NotFound extends ResourceNotFound {
        public NotFound(String message) {
            super(message);
        }
    }

}
