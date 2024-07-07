package com.hana.api.account.service;


import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.hana.api.account.dto.*;
import com.hana.api.account.entity.Account;
import com.hana.api.account.repository.AccountRepository;
import com.hana.api.depositsProduct.repository.DepositsProductRepository;
import com.hana.api.history.dto.TransferHistoryDto;
import com.hana.api.history.service.HistoryService;
import com.hana.api.user.entity.User;
import com.hana.api.user.repository.UserRepository;
import com.hana.common.config.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final UserRepository customerRepository;
    private final HistoryService historyService;
    private final DepositsProductRepository depositsProductRepository;

    private final int LOW_BOUND = 10_000_000;
    private final int MAX_BOUND = 90_000_000;

    @Transactional
    public Account createAccount(UUID userCode, AccountCreateDto accountCreateDto) {
        log.info(String.valueOf(accountCreateDto.getBalance()));
        Long accountNumber;
        System.out.println(userCode);
        do {
            accountNumber = generateAccountNumber(accountCreateDto.getBranchCode(), accountCreateDto.getAccountCode());
        } while (accountRepository.existsAccountByAccountNumber(accountNumber));

        User user = customerRepository.findByUserCode(userCode).get();
        Account account = accountCreateDto.toEntity(accountNumber, user);

        return accountRepository.save(account);
    }

    public List<AccountCheckResponse> myAccounts(UUID userCode) {
        List<Account> accounts = accountRepository.findAccountByCustomerId(userCode);
        return accounts.stream().map(AccountCheckResponse::new).toList();
    }

    public Boolean validateAccount(AccountValidationRequest validationRequest) {
        Account account = accountRepository.findById(validationRequest.getAccountId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SYSTEM_ERROR));

        if (!account.getPassword().equals(validationRequest.getAccountPassword())) {
            throw new BaseException(BaseResponseStatus.INVALID_ACCOUNT_PASSWORD);
        }
        return Boolean.TRUE;
    }

    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.SYSTEM_ERROR)
        );
    }

    @Transactional
    public MakeTransactionDto makeTransaction(MakeTransactionDto makeTransactionDto) {
        // 계좌 아이디 기준으로 출금 계좌 정보 조회
        Account account = accountRepository.findById(makeTransactionDto.getAccountId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_FROM_ACCOUNT));

        // 잔액 조회 - 이체 가능 여부 확인
        checkBalance(account.getBalance(), makeTransactionDto.getAmount());
        log.info("잔액 확인 OK...");
        // 비밀번호 일치 여부 확인
        if (CheckAccountPassword(account.getPassword(), makeTransactionDto.getPassword())) {
            log.info("비밀 번호 OK...");
            String recipient = null;
            // 하나 -> 하나 이체
            if (makeTransactionDto.getRecipientBank().equals("하나")) {
                Account toAccount = accountRepository.findByAccountNumber(
                                makeTransactionDto.getRecipientAccountNumber())
                        .orElseThrow(
                                () -> new BaseException(BaseResponseStatus.NO_TO_ACCOUNT)
                        );

                recipient = toAccount.getUser().getName();
                //당행 이체 처리
                hanaTransfer(account, toAccount, makeTransactionDto.getAmount());
                log.info("{} 에서 {} 계좌로 {} 를 이체합니다",account.getAccountNumber(), toAccount.getAccountNumber(), makeTransactionDto.getAmount());
                historyService.historySave(TransferHistoryDto.builder()
                        .dealClassification("입금")
                        .amount(makeTransactionDto.getAmount())
                        .recipient(recipient)
                        .recipientBank(makeTransactionDto.getRecipientBank())
                        .recipientNumber(toAccount.getAccountNumber())
                        .senderNumber(account.getAccountNumber())
                        .remainBalance(toAccount.getBalance())
                        .recipientRemarks(recipient)
                        .sender(account.getUser().getName())
                        .senderRemarks(account.getUser().getName())
                        .account(toAccount).build());
            }
            // 타행이체
            else {
                otherTransfer(account, makeTransactionDto.getAmount());
            }
            historyService.historySave(TransferHistoryDto.builder()
                    .dealClassification("출금")
                    .amount(makeTransactionDto.getAmount())
                    .recipient(recipient)
                    .recipientBank(makeTransactionDto.getRecipientBank())
                    .recipientNumber(makeTransactionDto.getRecipientAccountNumber())
                    .recipientRemarks(makeTransactionDto.getRecipientRemarks())
                    .senderRemarks(makeTransactionDto.getSenderRemarks())
                    .remainBalance(account.getBalance())
                    .sender(account.getUser().getName())
                    .senderNumber(account.getAccountNumber())
                    .account(account).build());
        }
        return makeTransactionDto;
    }

    @Transactional
    public void hanaTransfer(Account fromAccount, Account toAccount, Long amount) {
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
    }

    @Transactional
    public void otherTransfer(Account fromAccount, Long amount) {
        fromAccount.setBalance(fromAccount.getBalance() - amount);
    }

    public void checkBalance(Long balance, long amount) {
        // 잔액이 부족한 경우 -> 메서드 분리
        if (balance < amount) {
            throw new BaseException(BaseResponseStatus.INVALID_BALANCE);
        }
    }


    public boolean CheckAccountPassword(String accPw, String inputPw) {
        if (!accPw.equals(inputPw)) {
            throw new BaseException(BaseResponseStatus.WRONG_PASSWORD);
        }
        return true;
    }

    private Long generateAccountNumber(String branchCode, String accountCode) {
        StringBuilder sb = new StringBuilder(branchCode).append(accountCode).append(makeRandomNumber());
        return Long.parseLong(sb.toString());
    }

    private String makeRandomNumber() {
        Random random = new Random();
        return String.valueOf(LOW_BOUND + random.nextInt(MAX_BOUND));
    }
}