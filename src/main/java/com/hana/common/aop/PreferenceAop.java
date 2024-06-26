package com.hana.common.aop;


import java.time.LocalDate;

import com.hana.api.depositsProduct.entity.DepositsProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class PreferenceAop {

    private final RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.hana.common.aop.Preference)")
    private void cut() {
    }


    @Around("cut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDate today = LocalDate.now();
        Long productId = (Long) joinPoint.getArgs()[0];
        log.info("{} : 예적금 상품 아이디 : {} 가 조회되었습니다.", today.toString(), productId);
        DepositsProduct selectedProduct = (DepositsProduct) joinPoint.proceed();
        log.info("조회된 상품명 : {}", selectedProduct.getFinPrdtNm());
        updatePreference(selectedProduct.getFinPrdtNm(), today);
        return selectedProduct;
    }

    private void updatePreference(String productName, LocalDate today) {
        if(!redisTemplate.opsForHash().hasKey(productName, today.toString())) {
            redisTemplate.opsForHash().put(productName, today.toString(), String.valueOf(1));
            return;
        }

        int viewCount = Integer.parseInt((String) redisTemplate.opsForHash().get(productName, today.toString()));
        viewCount = viewCount + 1;
        redisTemplate.opsForHash().put(productName, today.toString(),String.valueOf(viewCount));
    }
}
