package net.hajar.ioc_projects.framework.xml;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class BeanDefinition {
    @XmlAttribute
    private String id;

    @XmlAttribute(name = "className")
    private String className;

    @XmlElement(name = "property")
    private List<PropertyDefinition> properties;

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public List<PropertyDefinition> getProperties() {
        return properties;
    }
}
