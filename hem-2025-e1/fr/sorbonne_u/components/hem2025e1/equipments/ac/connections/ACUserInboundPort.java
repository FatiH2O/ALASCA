package fr.sorbonne_u.components.hem2025e1.equipments.ac.connections;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.SignalData;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACExternalControlI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACUserCI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACUserI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

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
        this.getOwner().handleRequest(o -> { ((ACUserI)o).switchOn(); return null; });
    }

    @Override
    public void switchOff() throws Exception {
        this.getOwner().handleRequest(o -> { ((ACUserI)o).switchOff(); return null; });
    }

    @Override
    public void setTargetTemperature(Measure<Double> target) throws Exception {
        this.getOwner().handleRequest(o -> { ((ACUserI)o).setTargetTemperature(target); return null; });
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return this.getOwner().handleRequest(o -> ((ACUserI)o).getTargetTemperature());
    }

    @Override
    public fr.sorbonne_u.alasca.physical_data.SignalData<Double> getCurrentTemperature() throws Exception {
        return this.getOwner().handleRequest(o -> ((ACUserI)o).getCurrentTemperature());
    }
    @Override
	public SignalData<Double>	getCurrentPowerLevel() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((ACExternalControlI)o).getCurrentPowerLevel());
	}
    @Override
	public void			setCurrentPowerLevel(Measure<Double> powerLevel)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> { ((ACExternalControlI)o).
								setCurrentPowerLevel(powerLevel);
						return null;
				});
	}
    @Override
	public Measure<Double>	getMaxPowerLevel() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((ACExternalControlI)o).getMaxPowerLevel());
	}

}