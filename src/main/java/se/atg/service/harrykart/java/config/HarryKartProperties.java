package se.atg.service.harrykart.java.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "harry-kart")
@Getter
@Setter
public class HarryKartProperties {


    private String schemaFileName;

    private Integer trackLength;

    private Integer topNParticipant;

}
