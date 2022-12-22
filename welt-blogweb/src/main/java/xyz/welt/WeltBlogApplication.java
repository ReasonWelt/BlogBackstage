package xyz.welt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@MapperScan("xyz.welt.mapper")
@EnableScheduling
@EnableSwagger2
public class WeltBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeltBlogApplication.class,args);
    }
}
