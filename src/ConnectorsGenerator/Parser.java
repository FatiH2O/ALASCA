package ConnectorsGenerator;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Parser {

	// ---------- Structures de données ----------
	public static class InstanceVar {
		public String modifiers;
		public String type;
		public String name;
		public String staticInit;
	}

	public static class ParameterDef {
		public String type;
		public String name;
	}

	public static class MethodDef {
		public String name;
		public String returnType;
		public String modifiers;
		public List<ParameterDef> parameters = new ArrayList<>();
		public List<String> exceptions = new ArrayList<>();
		public String body;
		public String equipmentRef;
	}

	public static class ControlAdapterDescriptor {
		public String uid;
		public String offered;
		public List<String> required = new ArrayList<>();
		public Map<String, String> consumption = new HashMap<>();
		public List<InstanceVar> instanceVars = new ArrayList<>();
		public List<MethodDef> methods = new ArrayList<>();
	}

	public static ControlAdapterDescriptor parse(File xmlFile) throws Exception {
		ControlAdapterDescriptor desc = new ControlAdapterDescriptor();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xmlFile);
		doc.getDocumentElement().normalize();

		Element root = doc.getDocumentElement();
		desc.uid = root.getAttribute("uid");
		desc.offered = root.getAttribute("offered");

		NodeList cons = root.getElementsByTagName("consumption");
		if (cons.getLength() > 0) {
			Element e = (Element) cons.item(0);
			desc.consumption.put("nominal", e.getAttribute("nominal"));
			desc.consumption.put("min", e.getAttribute("min"));
			desc.consumption.put("max", e.getAttribute("max"));
		}

		NodeList reqs = root.getElementsByTagName("required");
		for (int i = 0; i < reqs.getLength(); i++) {
			desc.required.add(reqs.item(i).getTextContent().trim());
		}

		NodeList vars = root.getElementsByTagName("instance-var");
		for (int i = 0; i < vars.getLength(); i++) {
			Element e = (Element) vars.item(i);
			InstanceVar v = new InstanceVar();
			v.modifiers = e.getAttribute("modifiers");
			v.type = e.getAttribute("type");
			v.name = e.getAttribute("name");
			v.staticInit = e.getAttribute("static-init");
			desc.instanceVars.add(v);
		}

		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				String tag = e.getTagName();

				if (tag.equals("consumption") || tag.equals("required") || tag.equals("instance-var"))
					continue;

				MethodDef m = new MethodDef();
				m.name = e.hasAttribute("name") && !e.getAttribute("name").isEmpty() ? e.getAttribute("name") : tag;
				m.returnType = e.hasAttribute("type") ? e.getAttribute("type") : null;
				m.modifiers = e.hasAttribute("modifiers") ? e.getAttribute("modifiers") : null;

				NodeList params = e.getElementsByTagName("parameter");
				for (int j = 0; j < params.getLength(); j++) {
					Element p = (Element) params.item(j);
					ParameterDef pd = new ParameterDef();
					pd.type = p.hasAttribute("type") ? p.getAttribute("type") : null;
					pd.name = p.hasAttribute("name") ? p.getAttribute("name") : p.getTextContent().trim();
					m.parameters.add(pd);
				}

				NodeList thr = e.getElementsByTagName("thrown");
				for (int j = 0; j < thr.getLength(); j++) {
					m.exceptions.add(thr.item(j).getTextContent().trim());
				}

				NodeList bodies = e.getElementsByTagName("body");
				if (bodies.getLength() > 0) {
					Element bodyElt = (Element) bodies.item(0);
					m.equipmentRef = bodyElt.getAttribute("equipmentRef");
					m.body = bodyElt.getTextContent().trim();
				}

				desc.methods.add(m);
			}
		}

		return desc;
	}
}
