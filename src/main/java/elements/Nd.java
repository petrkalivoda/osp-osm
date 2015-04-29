package elements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nd", propOrder = {"ref"})
public class Nd {
	
	private @XmlAttribute Long ref;
	
	public void setRef(Long ref) {
		this.ref = ref;
	}
	
	public Long getRef() {
		return this.ref;
	}

}
