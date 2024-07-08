package com.hana.api.depositsProduct.service;

import com.hana.api.depositsProduct.dto.CheckingRequest;
import com.hana.api.depositsProduct.dto.PreferenceInfo;
import com.hana.api.depositsProduct.dto.response.DepositProductsResponse;
import com.hana.api.depositsProduct.dto.response.DepositsProductResponse;
import com.hana.api.depositsProduct.dto.response.GetListDepositsProductResponse;
import com.hana.api.depositsProduct.entity.DepositsProduct;
import com.hana.api.depositsProduct.entity.DepositsType;
import com.hana.api.depositsProduct.repository.DepositsProductRepository;
import com.hana.api.depositsProduct.util.DepositsProductUtil;
import com.hana.common.aop.Preference;
import com.hana.common.config.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepositsProductService {

    private final DepositsProductRepository depositsProductRepository;
    private final DepositsProductUtil depositsProductUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    // 적금
    @Transactional
    public void saveSavingProduct() {
        List<DepositsProductResponse> savingList = depositsProductUtil.getSavingList();
        depositsProductRepository.saveAll(savingList
                .stream()
                .map(DepositsProductResponse::toSavingEntity)
                .toList());
    }

    // 예금
    @Transactional
    public void saveDepositProduct() {
        List<DepositsProductResponse> depositList = depositsProductUtil.getDepositList();
        depositsProductRepository.saveAll(depositList
                .stream()
                .map(DepositsProductResponse::toDepositEntity)
                .toList());
    }

    // 입출금
    @Transactional
    public DepositsProduct saveCheckingProduct(CheckingRequest.CheckingCreateReq request) {
        DepositsProduct depositsProduct = DepositsProduct.builder()
                .type(request.getType())
                .finPrdtCd(request.getFinPrdtCd())
                .dclsMonth(request.getDclsMonth())
                .finCoNo(request.getFinCoNo())
                .korCoNm(request.getKorCoNm())
                .finPrdtNm(request.getFinPrdtNm())
                .joinWay(request.getJoinWay())
                .mtrtInt(request.getMtrtInt())
                .spclCnd(request.getSpclCnd())
                .joinDeny(request.getJoinDeny())
                .joinMember(request.getJoinMember())
                .etcNote(request.getEtcNote())
                .maxLimit(request.getMaxLimit())
                .dclsStrtDay(request.getDclsStrtDay())
                .dclsEndDay(request.getDclsEndDay())
                .finCoSubmDay(request.getFinCoSubmDay())
                .build();

        depositsProductRepository.save(depositsProduct);

        return depositsProduct;
    }

    public List<DepositsProduct> getCheckingProduct() {
        List<DepositsProduct> checkingProductList = depositsProductRepository.findCheckingProduct();
        return checkingProductList;
    }

    @Preference
    public DepositsProduct getDetail(Long id) {
        Optional<DepositsProduct> depositsProductResponse = depositsProductRepository.findById(id);
        if (depositsProductResponse.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_DEPOSIT_PRODUCT_ID);
        }

        return depositsProductResponse.get();
    }

    @Transactional
    public Map<DepositsType, List<DepositProductsResponse>> getList() {
        List<DepositsProduct> depositsProductList = depositsProductRepository.findAll();
        if (depositsProductList.isEmpty()) {
            throw new BaseException(BaseResponseStatus.EMPTY_DEPOSITS_PRODUCT);
        }
        return depositsProductList.stream()
                .map(DepositProductsResponse::fromEntity)
                .collect(Collectors.groupingBy(DepositProductsResponse::getType));
    }

    @Transactional
    public List<DepositProductsResponse> getListByType(String type) {
        List<DepositsProduct> depositsProductList = depositsProductRepository.findByType(type);
        if (depositsProductList.isEmpty()) {
            throw new BaseException(BaseResponseStatus.EMPTY_DEPOSITS_PRODUCT);
        }
        return depositsProductList
                .stream()
                .map(DepositProductsResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<GetListDepositsProductResponse> searchList(String searchword) {
        List<DepositsProduct> depositsProductList = depositsProductRepository.findByserchwordLike(searchword);
        if (depositsProductList.isEmpty()) {
            throw new BaseException(BaseResponseStatus.EMPTY_DEPOSITS_PRODUCT);
        }
        return depositsProductList
                .stream()
                .map(GetListDepositsProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Map<Integer, PreferenceInfo> getTopProduct() {
        Map<Integer, PreferenceInfo> topProduct = new HashMap<>();
        LocalDate today = LocalDate.now();

        PriorityQueue<PreferenceInfo> queue = new PriorityQueue<>();
        depositsProductRepository.findAll().forEach(depositsProduct -> {
            String productName = depositsProduct.getFinPrdtNm();
            if (Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(productName, today.toString()))) {
                queue.add(new PreferenceInfo(productName, Integer.parseInt(
                        (String) Objects.requireNonNull(
                                redisTemplate.opsForHash().get(productName, today.toString())))));
            }
        });

        int cnt = 1;
        while (!queue.isEmpty() && cnt <= 3) {
            topProduct.put(cnt++, queue.poll());
        }

        return topProduct;
    }

    public Map<String, Integer> getViewCountPerDay(String productName) {
        Map<String, Integer> viewCountPerDay = new HashMap<>();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(productName);
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            viewCountPerDay.put(entry.getKey().toString(), Integer.parseInt(entry.getValue().toString()));
        }
        return viewCountPerDay;
    }
}