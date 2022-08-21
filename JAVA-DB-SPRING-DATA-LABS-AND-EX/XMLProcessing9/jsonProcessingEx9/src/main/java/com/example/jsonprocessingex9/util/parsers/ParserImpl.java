package com.example.jsonprocessingex9.util.parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

@Component
public class ParserImpl implements Parser {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public <T> T fromJSon(String json, Class<T> clazz) {
        return this.gson.fromJson(json, clazz);
    }

    @Override
    public String toJson(Object entity) {
        return this.gson.toJson(entity);
    }

    @Override
    public <T> T fromXML(String xml, Class<T> clazz) throws JAXBException {
        Unmarshaller unmarshaller = this.getUnmarshaller(clazz);
        StringReader reader = this.getReader(xml);

        return (T) unmarshaller.unmarshal(reader);
    }

    @Override
    public <T> String toXML(T entity) throws JAXBException {
        Marshaller marshaller = this.getMarshaller(entity.getClass());
        StringWriter stringWriter = this.getWriter();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(entity, stringWriter);

        return stringWriter.toString();
    }

    private <T> Marshaller getMarshaller(Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);

        return context.createMarshaller();
    }

    private <T> Unmarshaller getUnmarshaller(Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);

        return context.createUnmarshaller();
    }

    private StringReader getReader(String data) {
        return new StringReader(data);
    }

    private StringWriter getWriter() {
        return new StringWriter();
    }
}