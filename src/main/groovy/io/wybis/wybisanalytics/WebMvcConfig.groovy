package io.wybis.wybisanalytics

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

import java.text.DecimalFormat

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

//	@Override
//	void addInterceptors(InterceptorRegistry registry) {
//		SecurityInterceptor si = new SecurityInterceptor()
//		si.init()
//		registry.addInterceptor(si).addPathPatterns("/**")
//	}

	@Override
	void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("bootstrap/index-b.html")
	}

    @Bean
    Properties javaMailProperties() {
        Properties props = new Properties()

        props.setProperty("mail.smtp.timeout", "3000")
        props.setProperty("mail.smtp.connectiontimeout", "3000")
        props.setProperty("mail.smtp.auth", "true")
        props.setProperty("mail.smtp.starttls.enable", "true")

        return props
    }

    @Bean
    DateTimeFormatter dateTimeFormatterAndParser() {
        return DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss")
    }

    @Bean
    DateTimeFormatter dateFormatterAndParser() {
        return DateTimeFormat.forPattern("dd-MM-yyyy")
    }

    @Bean
    DateTimeFormatter timeFormatterAndParser() {
        return DateTimeFormat.forPattern("HH:mm:ss")
    }

    @Bean
    DecimalFormat amountFormatterAndParser() {
        DecimalFormat df = new DecimalFormat()
        df.setMaximumFractionDigits(2)
        return df
    }

    @Bean
    public ObjectMapper jsonObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
        return objectMapper
    }

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate()
        return restTemplate
    }

    @Bean
    SimpleTemplateEngine simpleTemplateEngine() {
        SimpleTemplateEngine ste = new SimpleTemplateEngine()
        return ste
    }

//    @Bean
//    Template displayTemplate() throws Exception {
//        Template tmpl = null
//
//        ClassPathResource cpr = new ClassPathResource("templates/display.html")
//        SimpleTemplateEngine ste = this.simpleTemplateEngine()
//        tmpl = ste.createTemplate(cpr.getFile())
//
//        return tmpl
//    }

}
