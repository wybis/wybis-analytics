package io.wybis.wybisanalytics

import io.wybis.wybisanalytics.service.AccessDataService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource
import org.springframework.integration.annotation.IntegrationComponentScan
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.dsl.core.Pollers
import org.springframework.integration.dsl.file.Files
import org.springframework.integration.file.FileReadingMessageSource
import org.springframework.integration.file.splitter.FileSplitter
import org.springframework.integration.scheduling.PollerMetadata

@ImportResource("classpath:applicationContext-int.xml")
@IntegrationComponentScan
@EnableIntegration
@Configuration
class IntegrationConfig {

    @Bean
    DirectChannel accessDataLogLineInChannel() {
        return new DirectChannel();
    }

    @Bean
    IntegrationFlow accessDataFlow(AccessDataService accessDataService) {
        return IntegrationFlows.from('accessDataLogFile')
                .split(new FileSplitter())
                .channel(accessDataLogLineInChannel())
                .filter(accessDataService, 'hasAccessData')
                .transform(accessDataService, 'createFrom')
                .handle(accessDataService, 'save')
                //.handle(accessDataService, 'handle')
                .get();
    }
}
