package se.atg.service.harrykart.java.utils;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import se.atg.service.harrykart.java.exception.ServiceError;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Objects;

@Service
public class XmlUtility {

    private final XmlMapper xmlMapper;

    public XmlUtility() {
        this.xmlMapper = new XmlMapper();
    }

    public void validate(String xmlContent, String schemaFileName){
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(new File(getSchemaFilePath(schemaFileName)));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlContent)));
        } catch (SAXException | IOException e) {
           throw ServiceError.INVALID_XML_CONTENT.buildException(e.getLocalizedMessage());
        }
    }

    public <T> T parseXml(String xmlContent, Class<?> target){
        T obj = null;
        try {
            obj = (T) xmlMapper.readValue(xmlContent, target);

        } catch (IOException e) {
            throw ServiceError.INVALID_XML_MAPPING.buildException();
        }
        return obj;
    }
    private String getSchemaFilePath(String schemaFileName) {
        URL resource = getClass().getClassLoader().getResource(schemaFileName);
        Objects.requireNonNull(resource);
        return resource.getFile();
    }
}
