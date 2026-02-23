package framework.xml;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class BeansList {
    @XmlElement(name = "bean")
    private List<BeanModel> beans;

    public List<BeanModel> getBeans() { return beans; }
    public void setBeans(List<BeanModel> beans) { this.beans = beans; }
}