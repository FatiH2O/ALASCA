package fr.sorbonne_u.components.hem2025e1.equipments.ac.connections;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACExternalControlCI;

public class ACExternalControlConnector extends AbstractConnector implements ACExternalControlCI {
    @Override
    public Measure<Double> getMaxPowerLevel() throws Exception {
        return ((ACExternalControlCI)this.offering).getMaxPowerLevel();
    }

    @Override
    public void setCurrentPowerLevel(Measure<Double> powerLevel) throws Exception {
        ((ACExternalControlCI)this.offering).setCurrentPowerLevel(powerLevel);
    }
    
    @Override
    public Measure<Double> getCurrentPowerLevel() throws Exception {
        return ((ACExternalControlCI)this.offering).getCurrentPowerLevel();
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return ((ACExternalControlCI)this.offering).getTargetTemperature();
    }

    @Override
    public fr.sorbonne_u.alasca.physical_data.SignalData<Double> getCurrentTemperature() throws Exception {
        return ((ACExternalControlCI)this.offering).getCurrentTemperature();
    }
}