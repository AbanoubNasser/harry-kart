package se.atg.service.harrykart.java.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "loop")
public class Loop implements Serializable {
    @JacksonXmlProperty(isAttribute = true)
    int number;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "lane")
    List<Lane> lanes;
}
