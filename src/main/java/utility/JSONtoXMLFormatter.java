package utility;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Responsible for building a valid XML representation of a JSON.
 */
public class JSONtoXMLFormatter {

    /**
     * Converts JSON to XML and returns it.
     * @param json to be converted.
     * @return XML representation.
     */
    public String getXMLfromJSON(String json) {
        JSONObject jsonObject = new JSONObject(json);
        String xml = XML.toString(jsonObject, "root");

        try {
            // Convert the String representation of 'xml' to XML.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmlDoc = db.parse(new InputSource(new StringReader(xml)));

            // Add "response" attribute to the "root"-element.
            Element element = (Element) xmlDoc.getElementsByTagName("root").item(0);
            element.setAttribute("response", "False");


            // Convert XML back to String.
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(xmlDoc), new StreamResult(sw));

            return sw.toString();

        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            e.printStackTrace();
        }

        return xml;
    }
}
