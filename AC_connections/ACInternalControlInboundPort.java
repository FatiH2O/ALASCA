package AC_connections;

import AC.ACInternalControlCI;
import AC.ACInternalControlI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import physical_data.fr.sorbonne_u.alasca.physical_data.Measure;
import physical_data.fr.sorbonne_u.alasca.physical_data.SignalData;

public class ACInternalControlInboundPort extends AbstractInboundPort implements ACInternalControlCI {
    private static final long serialVersionUID = 1L;

    public ACInternalControlInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ACInternalControlCI.class, owner);
    }

    @Override
    public boolean isCooling() throws Exception {
        return ((ACInternalControlI)this.owner).isCooling();
    }

    @Override
    public void startCooling() throws Exception {
    	((ACInternalControlI)this.owner).startCooling();
    }

    @Override
    public void stopCooling() throws Exception {
    	((ACInternalControlI)this.owner).stopCooling();
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return ((ACInternalControlI)this.owner).getTargetTemperature();
    }

    @Override
    public SignalData<Double> getCurrentTemperature() throws Exception {
        return ((ACInternalControlI)this.owner).getCurrentTemperature();
    }
}