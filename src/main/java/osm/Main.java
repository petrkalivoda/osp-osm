package osm;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.geotools.referencing.GeodeticCalculator;

import elements.FilteredWorld;
import elements.Nd;
import elements.Node;
import elements.Way;

public class Main {
	
	public static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1000);
	public static final String CONFIG_FILE = "config.properties";
	
	public static final String OSMOSIS_PATH = "osmosis-path";
	public static final String OSMOSIS_COMMAND = "osmosis-command";
	public static final String OSMOSIS_TMPFILE = "osmosis-tempfile";
	
	public static void main(String[] args) throws Exception {
		
		if(args == null || args.length != 2) {
			System.err.println("USAGE: osm <input.pbf> <filter>");
			System.exit(1);
		}
		
		final String FILENAME = args[0];
		final String FILTER = args[1];
		
		
		InputStream is = Main.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
		Properties prop = new Properties();
		prop.load(is);
		
		System.out.println("[1/2] Running osmosis.");
		String path = prop.getProperty(OSMOSIS_PATH);
		String command = prop.getProperty(OSMOSIS_COMMAND);
		String tmpfile = prop.getProperty(OSMOSIS_TMPFILE);
		
		String program = String.format("%s %s", path, String.format(command, FILENAME, FILTER, tmpfile));
		Process p = Runtime.getRuntime().exec(program);
		p.waitFor();		
		
		System.out.println("[2/2] Calculating length.");
		
		File input = new File(tmpfile);
		JAXBContext context = JAXBContext.newInstance(FilteredWorld.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		FilteredWorld world = (FilteredWorld) unmarshaller.unmarshal(input);

		Map<Long, Node> nodeMap = new HashMap<Long, Node>();
		for (Iterator<Node> it = world.getNodes().iterator(); it.hasNext();) {
			Node n = it.next();
			nodeMap.put(n.getId(), n);
			it.remove();
		}

		BigDecimal total = BigDecimal.ZERO;
		for (Way w : world.getWays()) {
			Iterator<Nd> it = w.getNds().iterator();
			Node start = nodeMap.get(it.next().getRef());

			while (it.hasNext()) {
				Node end = nodeMap.get(it.next().getRef());				
				GeodeticCalculator calc = new GeodeticCalculator();
				calc.setStartingGeographicPoint(start.getLon(), start.getLat());
				calc.setDestinationGeographicPoint(end.getLon(), end.getLat());
				total = total.add(BigDecimal.valueOf(calc.getOrthodromicDistance()).divide(ONE_THOUSAND));
				start = end;
			}
		}

		System.out.printf("Total length of ways tagged \"%s\" is %.5f KM.\n", FILTER, total.setScale(5, RoundingMode.HALF_UP).doubleValue());
		Files.delete(input.toPath());
	}
}
