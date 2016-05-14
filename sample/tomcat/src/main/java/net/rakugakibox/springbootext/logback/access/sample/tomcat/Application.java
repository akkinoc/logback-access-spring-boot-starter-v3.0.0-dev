package net.rakugakibox.springbootext.logback.access.sample.tomcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The sample application for Tomcat.
 */
@SpringBootApplication
public class Application {

    /**
     * The entry point of application.
     *
     * @param args unused.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
