package com.example.jsonprocessingex9.util.parsers;
import javax.xml.bind.JAXBException;

public interface Parser {
    <T> T fromJSon(String json, Class<T> clazz);

    String toJson(Object entity);

    <T> T fromXML(String xml, Class<T> clazz) throws JAXBException;

    <T> String toXML(T entity) throws JAXBException;
}