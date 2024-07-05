package com.hana.api.user.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.hana.api.user.dto.CustomerContactDto;
import com.hana.api.user.dto.request.SignUpRequest;
import com.hana.api.user.dto.response.MyInfoResponse;
import com.hana.api.user.entity.User;
import com.hana.api.user.repository.CustomerRepository;
import com.hana.common.config.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final CustomerRepository customerRepository;

    @Transactional
    public UUID save(SignUpRequest signUpRequest) {
        try {
            User saved = customerRepository.save(signUpRequest.toEntity());
            return saved.getUserCode();
        } catch (DataIntegrityViolationException e) {
            throw new BaseException(BaseResponseStatus.DUPLICATE_CUSTOMER);
        }
    }
    @Transactional
    public User save(User user) {
        try {
            return customerRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new BaseException(BaseResponseStatus.DUPLICATE_CUSTOMER);
        }
    }




    public MyInfoResponse findByUserCode(UUID userCode) {
        return new MyInfoResponse((User) customerRepository.findByUserCode(userCode).orElseThrow(() ->
                new BaseException(BaseResponseStatus.INVALID_USER_JWT)));
    }



    public Optional<User> findUser(UUID userCode) {
        return customerRepository.findByUserCode(userCode);
    }

    public User findbyUser(UUID userCode) {
        return customerRepository.findByUserCode(userCode).orElseThrow(() ->
                new BaseException(BaseResponseStatus.INVALID_USER_JWT));
    }

    public List<CustomerContactDto> getCustomerContact() {
        return customerRepository.findCustomerContact();
    }
}
