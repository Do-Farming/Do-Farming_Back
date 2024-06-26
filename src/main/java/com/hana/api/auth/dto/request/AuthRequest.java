package com.hana.api.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthRequest {

    @Getter
    @Setter
    public static class UserSignupRequest {
        private String name; // only for User
        private String tele; // only for User
        private String socialNumber; // only for User
    }

    @Getter
    @Setter
    public static class UserAuthRequest {
        private String randomKey;
    }

    @Getter
    @Setter
    public static class ConsultantSignupRequest {
        private String loginId;
        private String password;
        private String role; // only for Consultant
    }

    @Getter
    @Setter
    public static class ConsultantAuthRequest {
        private String loginId;  // 사용자 이름 또는 ID
        private String password;  // 비밀번호

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken("CONSULTANT_" + loginId, password);
        }
    }

    @Getter
    @Setter
    public static class Reissue {
        @NotEmpty(message = "accessToken 을 입력해주세요.")
        private String accessToken;

        @NotEmpty(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;
    }

    @Getter
    @Setter
    public static class Logout {
        @NotEmpty(message = "잘못된 요청입니다.")
        private String accessToken;

        @NotEmpty(message = "잘못된 요청입니다.")
        private String refreshToken;
    }
}
