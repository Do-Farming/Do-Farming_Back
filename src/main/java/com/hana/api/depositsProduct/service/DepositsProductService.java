package com.hana.api.depositsProduct.service;

import com.hana.api.depositsProduct.dto.PreferenceInfo;
import com.hana.api.depositsProduct.dto.response.DepositsProductResponse;
import com.hana.api.depositsProduct.dto.response.GetListDepositsProductResponse;
import com.hana.api.depositsProduct.entity.DepositsProduct;
import com.hana.api.depositsProduct.repository.DepositsProductRepository;
import com.hana.api.depositsProduct.util.DepositsProductUtil;
import com.hana.common.aop.Preference;
import com.hana.common.config.*;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Transactional
    public void saveSavingProduct() {
        List<DepositsProductResponse> savingList = depositsProductUtil.getSavingList();
        depositsProductRepository.saveAll(savingList
                .stream()
                .map(DepositsProductResponse::toSavingEntity)
                .toList());
    }

    @Transactional
    public void saveDepositProduct() {
        List<DepositsProductResponse> depositList = depositsProductUtil.getDepositList();
        depositsProductRepository.saveAll(depositList
                .stream()
                .map(DepositsProductResponse::toDepositEntity)
                .toList());
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
    public List<GetListDepositsProductResponse> getList() {
        List<DepositsProduct> depositsProductList = depositsProductRepository.findAll();
        if (depositsProductList.isEmpty()) {
            throw new BaseException(BaseResponseStatus.EMPTY_DEPOSITS_PRODUCT);
        }
        return depositsProductList
                .stream()
                .map(GetListDepositsProductResponse::fromEntity)
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