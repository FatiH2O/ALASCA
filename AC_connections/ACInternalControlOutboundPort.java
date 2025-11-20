package AC_connections;

import AC.ACInternalControlCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import physical_data.fr.sorbonne_u.alasca.physical_data.Measure;
import physical_data.fr.sorbonne_u.alasca.physical_data.SignalData;

public class ACInternalControlOutboundPort extends AbstractOutboundPort implements ACInternalControlCI {
    private static final long serialVersionUID = 1L;

    public ACInternalControlOutboundPort(ComponentI owner) throws Exception {
        super(ACInternalControlCI.class, owner);
    }

    @Override
    public boolean isCooling() throws Exception {
        return ((ACInternalControlCI)this.getConnector()).isCooling();
    }

    @Override
    public void startCooling() throws Exception {
        ((ACInternalControlCI)this.getConnector()).startCooling();
    }

    @Override
    public void stopCooling() throws Exception {
        ((ACInternalControlCI)this.getConnector()).stopCooling();
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return ((ACInternalControlCI)this.getConnector()).getTargetTemperature();
    }

    @Override
    public SignalData<Double> getCurrentTemperature() throws Exception {
        return ((ACInternalControlCI)this.getConnector()).getCurrentTemperature();
    }
}