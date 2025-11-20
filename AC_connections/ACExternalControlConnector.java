package AC_connections;

import AC.ACExternalControlCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import physical_data.fr.sorbonne_u.alasca.physical_data.Measure;
import physical_data.fr.sorbonne_u.alasca.physical_data.SignalData;

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
    public SignalData<Double> getCurrentPowerLevel() throws Exception {
        return ((ACExternalControlCI)this.offering).getCurrentPowerLevel();
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return ((ACExternalControlCI)this.offering).getTargetTemperature();
    }

    @Override
    public SignalData<Double> getCurrentTemperature() throws Exception {
        return ((ACExternalControlCI)this.offering).getCurrentTemperature();
    }
}