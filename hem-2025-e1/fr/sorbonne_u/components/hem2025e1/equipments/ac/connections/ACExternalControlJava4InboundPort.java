package fr.sorbonne_u.components.hem2025e1.equipments.ac.connections;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACExternalControlJava4CI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACExternalControlI; // Pour la délégation


public class ACExternalControlJava4InboundPort
extends     ACExternalControlInboundPort 
implements  ACExternalControlJava4CI
{
    private static final long serialVersionUID = 1L;
    
    public ACExternalControlJava4InboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, owner);
    }
    

    @Override
    public double getMaxPowerLevelJava4() throws Exception {
        return this.getMaxPowerLevel().getData(); 
    }

    @Override
    public void setCurrentPowerLevelJava4(double powerLevel) throws Exception {
        this.setCurrentPowerLevel(new Measure<>(powerLevel, MeasurementUnit.WATTS));
    }
    
    @Override
    public double getCurrentPowerLevelJava4() throws Exception {
        return this.getCurrentPowerLevel().getMeasure().getData();
    }

    @Override
    public double getTargetTemperatureJava4() throws Exception {
        return this.getTargetTemperature().getData();
    }

    @Override
    public double getCurrentTemperatureJava4() throws Exception {
        return this.getCurrentTemperature().getMeasure().getData();
    }
}