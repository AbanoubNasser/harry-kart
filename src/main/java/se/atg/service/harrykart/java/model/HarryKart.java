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
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "harryKart")
public class HarryKart implements Serializable {

    @JacksonXmlProperty(localName = "numberOfLoops")
    int numberOfLoops;

    @JacksonXmlElementWrapper(localName = "startList")
    @JacksonXmlProperty(localName = "participant")
    List<Participant> startList;

    @JacksonXmlElementWrapper(localName = "powerUps")
    @JacksonXmlProperty(localName = "loop")
    List<Loop> powerUps;
}
