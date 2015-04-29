package elements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="osm")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilteredWorld {
	
	@XmlElement(name = "node")
	private List<Node> nodes = new ArrayList<Node>();
	
	@XmlElement(name = "way")
	private List<Way> ways = new ArrayList<Way>();
	
	public List<Node> getNodes() {
		return nodes;
	}
	
	public List<Way> getWays() {
		return ways;
	}
	
}
