package elements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "way")
public class Way {
	
	@XmlElement(name = "nd")
	private List<Nd> nds = new ArrayList<Nd>();
	
	public void setNodes(List<Nd> nds) {
		this.nds = nds;
	}
	
	public List<Nd> getNds() {
		return nds;
	}

}
