package be.loganfarci.financial.service.api;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@TestConfiguration
public class ApplicationTestConfiguration {

    final int MESSAGE_SOURCE_CACHE_SECONDS = 10;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/errors");
        messageSource.setCacheSeconds(MESSAGE_SOURCE_CACHE_SECONDS);
        return messageSource;
    }

}
