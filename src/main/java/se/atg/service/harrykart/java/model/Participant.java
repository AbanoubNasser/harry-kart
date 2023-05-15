package se.atg.service.harrykart.java.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "participant")
public class Participant implements Serializable {
    @JacksonXmlProperty(localName = "lane")
    Integer lane;

    @JacksonXmlProperty(localName = "name")
    String name;

    @JacksonXmlProperty(localName = "baseSpeed")
    Integer baseSpeed;
}
