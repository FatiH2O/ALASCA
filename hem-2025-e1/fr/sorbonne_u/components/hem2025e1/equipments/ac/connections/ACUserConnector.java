package fr.sorbonne_u.components.hem2025e1.equipments.ac.connections;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACUserCI;

public class ACUserConnector extends AbstractConnector implements ACUserCI {
    @Override
    public boolean isOn() throws Exception {
        return ((ACUserCI)this.offering).isOn();
    }

    @Override
    public void switchOn() throws Exception {
        ((ACUserCI)this.offering).switchOn();
    }

    @Override
    public void switchOff() throws Exception {
        ((ACUserCI)this.offering).switchOff();
    }

    @Override
    public void setTargetTemperature(Measure<Double> target) throws Exception {
        ((ACUserCI)this.offering).setTargetTemperature(target);
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return ((ACUserCI)this.offering).getTargetTemperature();
    }

    @Override
    public fr.sorbonne_u.alasca.physical_data.SignalData<Double> getCurrentTemperature() throws Exception {
        return ((ACUserCI)this.offering).getCurrentTemperature();
    }
}