package com.hana.api.signSaving.service;

import com.hana.api.account.dto.MakeTransactionDto;
import com.hana.api.account.dto.SavingAccountCreateDto;
import com.hana.api.account.entity.Account;
import com.hana.api.account.service.AccountService;
import com.hana.api.depositsProduct.entity.DepositsProduct;
import com.hana.api.depositsProduct.service.DepositsProductService;
import com.hana.api.signSaving.dto.request.DofarmingJoinRequestDto;
import com.hana.api.signSaving.dto.request.SavingJoinRequestDto;
import com.hana.api.signSaving.dto.response.AccountInfoResponse;
import com.hana.api.signSaving.dto.response.SavingJoinResponseDto;
import com.hana.api.signSaving.entity.SignSaving;
import com.hana.api.signSaving.repository.SignSavingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class SignSavingService {
    private final DepositsProductService depositsProductService;
    private final AccountService accountService;
    private final SignSavingRepository signSavingRepository;


    @Transactional
    public SavingJoinResponseDto joinSavingAccount(UUID userCode, SavingJoinRequestDto savingJoinRequestDto) {

        // 가입할 상품 가져오기
        DepositsProduct product = depositsProductService.getDetail(savingJoinRequestDto.getSavingProductId());

        SavingAccountCreateDto savingAccountCreateDto = new SavingAccountCreateDto(product.getFinPrdtNm(),
                savingJoinRequestDto.getAccountPassword(), 0L);
        Account savingAccount = accountService.createAccount(userCode, savingAccountCreateDto);
        log.info("예금 계좌 생성 : {}", savingAccount.getAccountNumber());
        Account withdrawAccount = accountService.getAccount(savingJoinRequestDto.getWithdrawAccountId());

        /* TO-DO
         * 가입 요청 Dto에서 예치금액만큼 출금계좌에서 차감하기 (이체 로직 사용)
         */
        log.info("거래 시작");
        accountService.makeTransaction(MakeTransactionDto.builder()
                .accountId(withdrawAccount.getId())
                .password(withdrawAccount.getPassword())
                .amount(savingJoinRequestDto.getDepositAmount())
                .recipientAccountNumber(savingAccount.getAccountNumber())
                .recipientBank("하나")
                .senderRemarks(savingAccount.getName())
                .recipientRemarks(withdrawAccount.getName()).build());

        SignSaving signSaving = SignSaving.builder()
                .account(savingAccount)
                .depositsProduct(product)
                .contractYears(savingJoinRequestDto.getContractYears())
                .snsNotice(savingJoinRequestDto.getSnsNotice())
                .interestRate(savingJoinRequestDto.getInterestRate())
                .build();

        return new SavingJoinResponseDto(signSavingRepository.save(signSaving));
    }

    @Transactional
    public void joinDofarmingAccount(UUID userCode, DofarmingJoinRequestDto dofarmingJoinRequestDto) {

        // 가입할 상품 가져오기
        DepositsProduct product = depositsProductService.getDetail(dofarmingJoinRequestDto.getDofarmingProductId());

        SavingAccountCreateDto savingAccountCreateDto = new SavingAccountCreateDto(product.getFinPrdtNm(),
                dofarmingJoinRequestDto.getAccountPassword(), 0L);
        Account savingAccount = accountService.createAccount(userCode, savingAccountCreateDto);
        log.info("도파밍 계좌 생성 : {}", savingAccount.getAccountNumber());
        Account withdrawAccount = accountService.getAccount(dofarmingJoinRequestDto.getWithdrawAccountId());

        /* TO-DO
         * 가입 요청 Dto에서 예치금액만큼 출금계좌에서 차감하기 (이체 로직 사용)
         */
        log.info("거래 시작");
        accountService.makeTransaction(MakeTransactionDto.builder()
                .accountId(withdrawAccount.getId())
                .password(withdrawAccount.getPassword())
                .amount(1000000L)
                .recipientAccountNumber(savingAccount.getAccountNumber())
                .recipientBank("하나")
                .senderRemarks(savingAccount.getName())
                .recipientRemarks(withdrawAccount.getName()).build());

    }

    public AccountInfoResponse getAccountInfo(Long accountId) {
        SignSaving signSaving = signSavingRepository.findByAccountId(accountId);

        return AccountInfoResponse.todto(signSaving);
    }

}
