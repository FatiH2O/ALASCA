package fr.sorbonne_u.components.hem2025e1.equipments.charger.connections;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.hem2025e1.equipments.charger.ChargerControlJava4CI;
import fr.sorbonne_u.components.hem2025e1.equipments.charger.connections.ChargerControlInboundPort; // Port de base

public class ChargerControlJava4InboundPort
extends     ChargerControlInboundPort
implements  ChargerControlJava4CI 
{
    private static final long serialVersionUID = 1L;

    public ChargerControlJava4InboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, owner);
    }
    

    @Override
    public double getMaxChargingPowerJava4() throws Exception {
        return this.getMaxChargingPower().getData(); 
    }

    @Override
    public void setTargetPowerJava4(double watts) throws Exception {
         this.setTargetPower(new Measure<>(watts, MeasurementUnit.WATTS));
    }
    
    @Override
    public double getCurrentPowerJava4() throws Exception {
        return this.getCurrentPower().getData();
    }
}