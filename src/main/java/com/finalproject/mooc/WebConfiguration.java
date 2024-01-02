package com.finalproject.mooc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Authorization");

        return new OpenAPI()
                .info(new Info()
                        .title("MOOC OPEN API")
                        .description("<b>Pastikan untuk login dengan form authorize terlebih dahulu agar Endpoint mendapatkan " +
                                "username anda </b>" +

                                "<br><br> <b>USER</b> : <br>" +
                                "POST /course-progress/start-course<br>" +
                                "POST /order<br>" +
                                "POST /order-updatePaidStatus<br>" +
                                "PUT /user/profile<br>" +
                                "PUT /user/password<br>" +
                                "PUT /course-progress/subject-done<br>" +
                                "GET /user<br>" +
                                "GET /course-progress<br>" +
                                "GET /course-progress/list<br>" +
                                "GET /order/history<br>" +

                                "<br><b>ADMIN:</b><br>" +
                                "POST /course<br>" +
                                "POST /subject<br>" +
                                "PUT /course<br>" +
                                "PUT /subject<br>" +
                                "DELETE /subject/{subjectCode}<br>" +
                                "DELETE /course/{courseCode}<br>" +
                                "GET /admin/payment-status<br>" +
                                "GET /admin/manage-course<br>" +
                                "GET /dashboard-data<br>" +

                                "<br><b>ALL PERMIT : </b> <br>" +
                                "POST /auth/signup<br>" +
                                "POST /auth/signup/admin<br>" +
                                "POST /auth/signin<br>" +
                                "POST /regenerate-otp<br>" +
                                "POST /forget-password/generate-token<br>" +
                                "PUT /verify-account<br>" +
                                "PUT /forget-password/reset<br>" +
                                "GET /course-detail<br>" +
                                "GET /course<br>")
                        .version("1.0.0"))
                .components(new Components().addSecuritySchemes("Authorization", securityScheme))
                .addSecurityItem(securityRequirement);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");  // Set the allowed origin pattern
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
