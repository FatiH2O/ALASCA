package fr.sorbonne_u.components.hem2025e1.equipments.ac.connections;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACInternalControlCI;

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
    public fr.sorbonne_u.alasca.physical_data.SignalData<Double> getCurrentTemperature() throws Exception {
        return ((ACInternalControlCI)this.offering).getCurrentTemperature();
    }
}