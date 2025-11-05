package fr.sorbonne_u.components.hem2025e1.equipments.ac.connections;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACExternalControlCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class ACExternalControlOutboundPort extends AbstractOutboundPort implements ACExternalControlCI {
    private static final long serialVersionUID = 1L;

    public ACExternalControlOutboundPort(ComponentI owner) throws Exception {
        super(ACExternalControlCI.class, owner);
    }

    @Override
    public Measure<Double> getMaxPowerLevel() throws Exception {
        return ((ACExternalControlCI)this.getConnector()).getMaxPowerLevel();
    }

    @Override
    public void setCurrentPowerLevel(Measure<Double> powerLevel) throws Exception {
        ((ACExternalControlCI)this.getConnector()).setCurrentPowerLevel(powerLevel);
    }
    
    @Override
    public Measure<Double> getCurrentPowerLevel() throws Exception {
        return ((ACExternalControlCI)this.getConnector()).getCurrentPowerLevel();
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return ((ACExternalControlCI)this.getConnector()).getTargetTemperature();
    }

    @Override
    public fr.sorbonne_u.alasca.physical_data.SignalData<Double> getCurrentTemperature() throws Exception {
        return ((ACExternalControlCI)this.getConnector()).getCurrentTemperature();
    }
}