package fr.sorbonne_u.components.hem2025e1.equipments.ac;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVMUnitTest extends AbstractCVM {

    public CVMUnitTest() throws Exception {
    }

    @Override
    public void deploy() throws Exception {
        AbstractComponent.createComponent(
            AC.class.getCanonicalName(),
            new Object[]{}); 

        AbstractComponent.createComponent(
            ACUnitTester.class.getCanonicalName(),
            new Object[]{}
        );

        super.deploy();
    }

    public static void main(String[] args) {
        try {
            CVMUnitTest cvm = new CVMUnitTest();
            cvm.startStandardLifeCycle(10000L);
            Thread.sleep(500L); 
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}