package Charger_connections;

import Charger.ChargerControlJava4CI;
import fr.sorbonne_u.components.ComponentI;
import physical_data.fr.sorbonne_u.alasca.physical_data.Measure;
import physical_data.fr.sorbonne_u.alasca.physical_data.MeasurementUnit;

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