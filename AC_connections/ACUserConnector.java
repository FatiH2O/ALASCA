package AC_connections;

import AC.ACUserCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import physical_data.fr.sorbonne_u.alasca.physical_data.Measure;
import physical_data.fr.sorbonne_u.alasca.physical_data.SignalData;

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
	public void			setCurrentPowerLevel(Measure<Double> powerLevel)
	throws Exception
	{
		((ACUserCI)this.offering).setCurrentPowerLevel(powerLevel);
	}
    @Override
	public SignalData<Double>	getCurrentPowerLevel() throws Exception
	{
		return ((ACUserCI)this.offering).getCurrentPowerLevel();
	}
    @Override
	public Measure<Double>	getMaxPowerLevel() throws Exception
	{
		return ((ACUserCI)this.offering).getMaxPowerLevel();
	}
    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return ((ACUserCI)this.offering).getTargetTemperature();
    }

    @Override
    public SignalData<Double> getCurrentTemperature() throws Exception {
        return ((ACUserCI)this.offering).getCurrentTemperature();
    }
}