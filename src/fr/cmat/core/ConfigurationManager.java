package fr.cmat.core;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.cmat.data.AppItem;
import fr.cmat.data.Configuration;

public class ConfigurationManager {
	private static final Logger LOGGER = Logger.getLogger(ConfigurationManager.class);

	private static final String XML_FILE = "launcher.xml";

	private static final String XML_LAUNCHER = "launcher";
	private static final String XML_CONFIGURATION = "configuration";
	private static final String XML_START_TIME = "start_time";
	private static final String XML_INTERVAL_TIME = "interval_time";
	private static final String XML_APP_ITEMS = "app_items";
	private static final String XML_APP_ITEM = "app_item";
	private static final String XML_APP_ITEM_NAME = "name";
	private static final String XML_APP_ITEM_PATH = "path";
	private static final String XML_APP_ITEM_ARGS = "args";
	private static final String XML_APP_ITEM_POSITION = "position";

	public static void read(String filename) {
		Configuration configuration = Configuration.getInstance();

		synchronized (configuration) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
//				Document document = builder.parse(new File(XML_FILE));
				Document document = builder.parse(new File(filename));

				NodeList list = document.getElementsByTagName(XML_CONFIGURATION);
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
						configuration.setStartTimeInMillis(Long.parseLong(element.getElementsByTagName(XML_START_TIME).item(0).getTextContent()));
						configuration.setIntervalTimeInMillis(Long.parseLong(element.getElementsByTagName(XML_INTERVAL_TIME).item(0).getTextContent()));
					}
				}

				list = document.getElementsByTagName(XML_APP_ITEM);
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						AppItem appItem = new AppItem();
						Element element = (Element) node;
						appItem.setName(element.getAttribute(XML_APP_ITEM_NAME));
						appItem.setPath(element.getAttribute(XML_APP_ITEM_PATH));
						appItem.setArgs(element.getAttribute(XML_APP_ITEM_ARGS));
						appItem.setPosition(Integer.parseInt(element.getAttribute(XML_APP_ITEM_POSITION)));

						configuration.getAppItems().add(appItem);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Impossible de lire la configuration : ", e);
			}
		}
	}

	public static void write() {
		Configuration configuration = Configuration.getInstance();

		synchronized (configuration) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();

				Document doc = builder.newDocument();
				Element rootElement = doc.createElement(XML_LAUNCHER);
				doc.appendChild(rootElement);

				Element config = doc.createElement(XML_CONFIGURATION);
				rootElement.appendChild(config);

				Element startTime = doc.createElement(XML_START_TIME);
				startTime.appendChild(doc.createTextNode("" + configuration.getStartTimeInMillis()));
				config.appendChild(startTime);

				Element intervalTime = doc.createElement(XML_INTERVAL_TIME);
				intervalTime.appendChild(doc.createTextNode("" + configuration.getIntervalTimeInMillis()));
				config.appendChild(intervalTime);

				Element appItems = doc.createElement(XML_APP_ITEMS);
				config.appendChild(appItems);

				for (AppItem appItem : configuration.getAppItems()) {
					Element item = doc.createElement(XML_APP_ITEM);
					item.setAttribute(XML_APP_ITEM_NAME, "" + appItem.getName());
					item.setAttribute(XML_APP_ITEM_PATH, "" + appItem.getPath());
					item.setAttribute(XML_APP_ITEM_ARGS, "" + appItem.getArgs());
					item.setAttribute(XML_APP_ITEM_POSITION, "" + appItem.getPosition());
					appItems.appendChild(item);
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(XML_FILE));

				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.transform(source, result);
			} catch (Exception e) {
				LOGGER.error("Impossible de sauvegarder la configuration : ", e);
			}
		}
	}
}
