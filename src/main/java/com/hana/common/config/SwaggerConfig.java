package com.hana.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer Token");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", securityScheme))
                .addSecurityItem(securityRequirement);
    }

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/**"}; // 해당 path인경우에만 스웨거에 추가되도록 설정

        return GroupedOpenApi
                .builder()
                .group("Pro API v1")
                .pathsToMatch(paths)
                .addOpenApiCustomizer(
                        openApi -> openApi.setInfo(
                                new Info()
                                        .title("Dofarming Api") // API 제목
                                        .description("갓생 경쟁을 통해 도파민을 파밍해라 Do! farming") // API 설명
                                        .version("1.0.0") // API 버전
                        )
                )
                .build();
    }
}
