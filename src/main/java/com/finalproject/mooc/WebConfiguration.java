package com.finalproject.mooc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//@EnableAsync
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
                                "PUT /user/profile<br>" +
                                "PUT /user/password<br>" +
                                "GET /user<br>" +

                                "<br><b>ADMIN:</b><br>" +
                                "POST /course<br>" +
                                "POST /subject<br>" +
                                "PUT /course<br>" +
                                "PUT /subject<br>" +

                                "<br><b>ALL PERMIT : </b> <br>" +
                                "POST /auth/signup<br>" +
                                "POST /auth/signin<br>" +
                                "GET /forget-password/generate-token<br>" +
                                "GET /forget-password/reset<br>" +
                                "GET /course-detail<br>" +
                                "GET /course<br>" +
                                "<br><br>Kunjungi beberapa URL di bawah ini untuk portofolio/proyek saya lainnya:" +
                                "<ul>" +
                                "<li>[Portofolio Website](https://hq.achmodez.tech)</li>" +
                                "<li>[Linkedin Profile](https://www.linkedin.com/in/habbatul/)</li>" +
                                "<ul>")
                        .version("1.0.0"))
                .components(new Components().addSecuritySchemes("Authorization", securityScheme))
                .addSecurityItem(securityRequirement);
    }


//    @Bean(name = "asyncTaskExecutor")
//    public TaskExecutor asyncTaskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(3);
//        executor.setMaxPoolSize(5);
//        executor.setQueueCapacity(10);
//        executor.setThreadNamePrefix("HanThread-");
//        executor.initialize();
//        return executor;
//    }
}
