package AC_connections;

import AC.ACInternalControlCI;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.SignalData;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ACInternalControlConnector extends AbstractConnector implements ACInternalControlCI {
    @Override
    public boolean isCooling() throws Exception {
        return ((ACInternalControlCI)this.offering).isCooling();
    }

    @Override
    public void startCooling() throws Exception {
        ((ACInternalControlCI)this.offering).startCooling();
    }

    @Override
    public void stopCooling() throws Exception {
        ((ACInternalControlCI)this.offering).stopCooling();
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return ((ACInternalControlCI)this.offering).getTargetTemperature();
    }

    @Override
    public SignalData<Double> getCurrentTemperature() throws Exception {
        return ((ACInternalControlCI)this.offering).getCurrentTemperature();
    }
}