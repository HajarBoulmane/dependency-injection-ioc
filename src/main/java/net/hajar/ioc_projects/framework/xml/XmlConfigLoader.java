package net.hajar.ioc_projects.framework.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import java.io.InputStream;

public class XmlConfigLoader {

    public BeanConfig load(String fileName) {
        try {
            JAXBContext context = JAXBContext.newInstance(BeanConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(fileName);

            return (BeanConfig) unmarshaller.unmarshal(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}