package fr.sorbonne_u.components.hem2025e1.equipments.ac.connections;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACInternalControlCI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACInternalControlI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class ACInternalControlInboundPort extends AbstractInboundPort implements ACInternalControlCI {
    private static final long serialVersionUID = 1L;

    public ACInternalControlInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ACInternalControlCI.class, owner);
    }

    @Override
    public boolean isCooling() throws Exception {
        return this.getOwner().handleRequest(o -> ((ACInternalControlI)o).isCooling());
    }

    @Override
    public void startCooling() throws Exception {
        this.getOwner().handleRequest(o -> { ((ACInternalControlI)o).startCooling(); return null; });
    }

    @Override
    public void stopCooling() throws Exception {
        this.getOwner().handleRequest(o -> { ((ACInternalControlI)o).stopCooling(); return null; });
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return this.getOwner().handleRequest(o -> ((ACInternalControlI)o).getTargetTemperature());
    }

    @Override
    public fr.sorbonne_u.alasca.physical_data.SignalData<Double> getCurrentTemperature() throws Exception {
        return this.getOwner().handleRequest(o -> ((ACInternalControlI)o).getCurrentTemperature());
    }
}