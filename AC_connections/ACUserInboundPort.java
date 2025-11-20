package AC_connections;

import AC.ACUserCI;
import AC.ACUserI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import physical_data.fr.sorbonne_u.alasca.physical_data.Measure;
import physical_data.fr.sorbonne_u.alasca.physical_data.SignalData;

public class ACUserInboundPort extends AbstractInboundPort implements ACUserCI {
    private static final long serialVersionUID = 1L;

    public ACUserInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ACUserCI.class, owner);
    }

    @Override
    public boolean isOn() throws Exception {
        return this.getOwner().handleRequest(o -> ((ACUserI)o).isOn());
    }

    @Override
    public void switchOn() throws Exception {
    	((ACUserI)this.owner).switchOn(); 
    }

    @Override
    public void switchOff() throws Exception {
        this.getOwner().handleRequest(o -> { ((ACUserI)o).switchOff(); return null; });
    }

    @Override
    public void setTargetTemperature(Measure<Double> target) throws Exception {
    	((ACUserI)this.owner).setTargetTemperature(target); 
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return ((ACUserI)this.owner).getTargetTemperature();
    }

    @Override
    public SignalData<Double> getCurrentTemperature() throws Exception {
        return ((ACUserI)this.owner).getCurrentTemperature();
    }
    @Override
	public SignalData<Double>	getCurrentPowerLevel() throws Exception
	{
		return ((ACUserI)this.owner).getCurrentPowerLevel();
	}
    @Override
	public void			setCurrentPowerLevel(Measure<Double> powerLevel)
	throws Exception
	{
    	((ACUserI)this.owner).setCurrentPowerLevel(powerLevel);
						
	}
    @Override
	public Measure<Double>	getMaxPowerLevel() throws Exception
	{
		return ((ACUserI)this.owner).getMaxPowerLevel();
	}

}