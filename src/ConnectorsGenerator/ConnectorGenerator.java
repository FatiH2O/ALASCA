package ConnectorsGenerator;

import java.util.StringJoiner;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class ConnectorGenerator {

	private static String resolveReturnType(Parser.MethodDef m) {
		if (m.returnType != null && !m.returnType.isEmpty()) {
			return m.returnType;
		}
		
		switch (m.name) {
		case "getModeConsumption":
		case "emergency":
			return "double";
		case "currentMode":
		case "maxMode":
			return "int";
		case "suspended":
		case "upMode":
		case "downMode":
		case "setMode":
		case "suspend":
		case "resume":
			return "boolean";
		default:
			return "void";
		}
	}

	public static Class<?> makeConnectorClassJavassist(Parser.ControlAdapterDescriptor desc)
			throws Exception {

		ClassPool pool = ClassPool.getDefault();
		
		

	
		String className = "fr.sorbonne_u.generated.connectors." + desc.uid + "Connector";
		
	
	    try {
	        return Class.forName(className);
	    } catch (ClassNotFoundException e) {
	        // La classe n'existe pas 
	    }
		CtClass cc = pool.makeClass(className);


		CtClass superClass = pool.get("fr.sorbonne_u.components.connectors.AbstractConnector");
		cc.setSuperclass(superClass);

	
		CtClass ci = pool.get("fr.sorbonne_u.components.hem2025.bases.AdjustableCI");
		cc.addInterface(ci);

		// Interface "offered" déclarée dans le XML
		CtClass offered = pool.get(desc.offered);
		cc.addInterface(offered);

		// Variables d’instance
		for (Parser.InstanceVar v : desc.instanceVars) {
			String fieldSrc = v.modifiers + " " + v.type + " " + v.name;
			if (v.staticInit != null && !v.staticInit.isEmpty()) {
				fieldSrc += " = " + v.staticInit;
			}
			fieldSrc += ";";
			CtField field = CtField.make(fieldSrc, cc);
			cc.addField(field);
		}

		// Méthodes
		for (Parser.MethodDef m : desc.methods) {
			StringBuilder src = new StringBuilder();

			// Modificateurs et signature
			src.append(m.modifiers != null ? m.modifiers : "public").append(" ");
			String returnType = resolveReturnType(m);
			src.append(returnType).append(" ");
			src.append(m.name).append("(");

			// Paramètres
			StringJoiner paramsDecl = new StringJoiner(", ");
			for (Parser.ParameterDef p : m.parameters) {
				String type = (p.type != null && !p.type.isEmpty()) ? p.type : "int";
				paramsDecl.add(type + " " + p.name);
			}
			src.append(paramsDecl.toString()).append(")");

			// Exceptions
			if (!m.exceptions.isEmpty()) {
				src.append(" throws ").append(String.join(",", m.exceptions));
			}

			// Corps avec substitution
			String body = m.body;
			if (m.equipmentRef != null && !m.equipmentRef.isEmpty()) {
				body = body.replace(m.equipmentRef + ".", "((" + desc.offered + ")this.offering).");
			}

			src.append(" { ").append(body).append(" }");

			
            
			// Création de la méthode Javassist
			CtMethod method = CtNewMethod.make(src.toString(), cc);
			cc.addMethod(method);
			
		}
		
		
		// Générer la classe
		Class<?> clazz = cc.toClass();
		cc.detach();
		return clazz;
	}
}