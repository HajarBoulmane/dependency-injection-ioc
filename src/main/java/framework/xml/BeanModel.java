package framework.xml;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class BeanModel {
    @XmlAttribute
    private String id;
    @XmlAttribute(name = "class")
    private String className;

    @XmlElement(name = "property")
    private List<PropertyModel> properties;

    public String getId() { return id; }
    public String getClassName() { return className; }
    public List<PropertyModel> getProperties() { return properties; }

    // This is now a static inner class
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PropertyModel {
        @XmlAttribute
        private String name;
        @XmlAttribute
        private String ref;

        public String getName() { return name; }
        public String getRef() { return ref; }
    }
}