package nextstep.subway.common;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Profile("test")
@Configuration
public class TestWebMvcConfig implements WebMvcConfigurer {

    public static final String PREFIX_STATIC_RESOURCES = "/resources";

    @Autowired
    private SubwayVersion subwayVersion;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(PREFIX_STATIC_RESOURCES + "/" + subwayVersion.getVersion() + "/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePrivate());


        registry.addResourceHandler(PREFIX_STATIC_RESOURCES + "/" + subwayVersion.getVersion() + "/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCacheControl(CacheControl.noCache().cachePrivate());
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter etagHeaderFilter = new ShallowEtagHeaderFilter();
        registration.setFilter(etagHeaderFilter);
        registration.addUrlPatterns(PREFIX_STATIC_RESOURCES + "/*");
        return registration;
    }
}
