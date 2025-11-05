package fr.sorbonne_u.components.hem2025e1.equipments.ac.connections;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACUserCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class ACUserOutboundPort extends AbstractOutboundPort implements ACUserCI {
    private static final long serialVersionUID = 1L;

    public ACUserOutboundPort(ComponentI owner) throws Exception {
        super(ACUserCI.class, owner);
    }
    
    public ACUserOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ACUserCI.class, owner);
    }

    @Override
    public boolean isOn() throws Exception {
        return ((ACUserCI)this.getConnector()).isOn();
    }

    @Override
    public void switchOn() throws Exception {
        ((ACUserCI)this.getConnector()).switchOn();
    }

    @Override
    public void switchOff() throws Exception {
        ((ACUserCI)this.getConnector()).switchOff();
    }

    @Override
    public void setTargetTemperature(Measure<Double> target) throws Exception {
        ((ACUserCI)this.getConnector()).setTargetTemperature(target);
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return ((ACUserCI)this.getConnector()).getTargetTemperature();
    }

    @Override
    public fr.sorbonne_u.alasca.physical_data.SignalData<Double> getCurrentTemperature() throws Exception {
        return ((ACUserCI)this.getConnector()).getCurrentTemperature();
    }
}