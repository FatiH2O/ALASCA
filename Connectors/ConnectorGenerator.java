package Connectors;


import java.util.Arrays;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class ConnectorGenerator {
	  public static void main(String[] args) throws Exception {

	        String connectorName = "Tumbledryer";
	        String interfaceName = "fr.sorbonne_u.components.hem2025e1.equipement.Wifi.BoxWifiUserCI";

	        List<String> methods = Arrays.asList("turnOn", "turnOff", "activateWifi", "deactivateWifi");

	        ClassPool pool = ClassPool.getDefault();
	        CtClass newClass = pool.makeClass("fr.sorbonne_u.generated." + connectorName);

	        CtClass abstractConnector = pool.get("fr.sorbonne_u.components.connectors.AbstractConnector");
	        newClass.setSuperclass(abstractConnector);

	        CtClass interfaceClass = pool.get(interfaceName);
	        newClass.addInterface(interfaceClass);

	        
	        for (String methodName : methods) {
	            CtMethod method = CtNewMethod.make(
	                "public void " + methodName + "() throws Exception {" +
	                "((" + interfaceName + ")this.offering)." + methodName + "();" +
	                "}", newClass);
	            newClass.addMethod(method);
	        }
            
	        CtMethod methde1 =CtNewMethod.make(
	                "public BoxWifiMode getMode() throws Exception {" +
	                "((" + interfaceName + ")this.offering).getMode();" +
	                "}", newClass);
	            newClass.addMethod(methde1);
	            
	         CtMethod methde2 =CtNewMethod.make(
		                "public boolean isOn() throws Exception {" +
		                "((" + interfaceName + ")this.offering).isOn();" +
		                "}", newClass);
		            newClass.addMethod(methde2);
	        
	        newClass.writeFile("target/generated-classes/");
	        System.out.println("✅ Connecteur " + connectorName + " généré avec succès !");
	    }
	}

