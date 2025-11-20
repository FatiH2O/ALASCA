package Connectors;

import java.util.StringJoiner;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class ConnectorGenerator {

	private static String resolveReturnType(AdapterParser.MethodDef m) {
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

	public static Class<?> makeConnectorClassJavassist(AdapterParser.ControlAdapterDescriptor desc)
			throws Exception {

		ClassPool pool = ClassPool.getDefault();
		
		

		// Nom de classe basé sur l’UID
		String className = "fr.sorbonne_u.generated.connectors." + desc.uid + "Connector";
		
		// Vérifier si la classe a déjà été chargée
	    try {
	        return Class.forName(className);
	    } catch (ClassNotFoundException e) {
	        // La classe n'existe pas → ok pour la créer
	    }
		CtClass cc = pool.makeClass(className);

		// Superclasse fixée une fois pour toutes (commune à tous les connecteurs)
		CtClass superClass = pool.get("fr.sorbonne_u.components.connectors.AbstractConnector");
		cc.setSuperclass(superClass);

		// Interface générique des connecteurs modulables
		CtClass ci = pool.get("fr.sorbonne_u.components.hem2025.bases.AdjustableCI");
		cc.addInterface(ci);

		// Interface "offered" déclarée dans le XML
		CtClass offered = pool.get(desc.offered);
		cc.addInterface(offered);

		// Variables d’instance
		for (AdapterParser.InstanceVar v : desc.instanceVars) {
			String fieldSrc = v.modifiers + " " + v.type + " " + v.name;
			if (v.staticInit != null && !v.staticInit.isEmpty()) {
				fieldSrc += " = " + v.staticInit;
			}
			fieldSrc += ";";
			CtField field = CtField.make(fieldSrc, cc);
			cc.addField(field);
		}

		// Méthodes
		for (AdapterParser.MethodDef m : desc.methods) {
			StringBuilder src = new StringBuilder();

			// Modificateurs et signature
			src.append(m.modifiers != null ? m.modifiers : "public").append(" ");
			String returnType = resolveReturnType(m);
			src.append(returnType).append(" ");
			src.append(m.name).append("(");

			// Paramètres
			StringJoiner paramsDecl = new StringJoiner(", ");
			for (AdapterParser.ParameterDef p : m.parameters) {
				String type = (p.type != null && !p.type.isEmpty()) ? p.type : "int";
				paramsDecl.add(type + " " + p.name);
			}
			src.append(paramsDecl.toString()).append(")");

			// Exceptions
			if (!m.exceptions.isEmpty()) {
				src.append(" throws ").append(String.join(",", m.exceptions));
			}

			// Corps avec substitution si equipmentRef présent
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