package softuni.exam.config;

import org.springframework.stereotype.Component;
import softuni.exam.config.XmlParser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class XmlParserImpl implements XmlParser {
    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromFile(String filePath, Class<T> tClass) throws JAXBException, FileNotFoundException {
        JAXBContext jaxbContext = JAXBContext.newInstance(tClass);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new FileReader(filePath));
    }
//    public <T> T fromFile(String filePath, Class<T> tClass) throws JAXBException, FileNotFoundException {
//        try {
//            JAXBContext jaxbContext = JAXBContext.newInstance(tClass);
//            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//
//            return (T) unmarshaller.unmarshal(new File(filePath));
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//
//        return null;
    }


