package fr.sorbonne_u.components.hem2025e1.equipments.ac.connections;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.alasca.physical_data.SignalData;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACUserJava4CI;
import fr.sorbonne_u.components.hem2025e1.equipments.heater.Heater;
import fr.sorbonne_u.components.hem2025e1.equipments.heater.HeaterUserJava4CI;
import fr.sorbonne_u.components.hem2025e1.equipments.heater.connections.HeaterUserInboundPort;

public class ACUserJava4InboundPort
extends		ACUserInboundPort
implements	ACUserJava4CI

{
    private static final long serialVersionUID = 1L;

    public ACUserJava4InboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, owner);
    }

    
    @Override
    public void setTargetTemperatureJava4(double target) throws Exception {
        this.setTargetTemperature(new Measure<>(target, MeasurementUnit.CELSIUS));
    }

    @Override
    public double getTargetTemperatureJava4() throws Exception {
        return this.getTargetTemperature().getData();
    }

    @Override
    public double getCurrentTemperatureJava4() throws Exception {
        return this.getCurrentTemperature().getMeasure().getData();
    }
    @Override
	public double		getCurrentPowerLevelJava4() throws Exception
	{
		return this.getCurrentPowerLevel().getMeasure().getData();
	}
    @Override
	public void			setCurrentPowerLevelJava4(double powerLevel)
	throws Exception
	{
		this.setCurrentPowerLevel(
				new Measure<Double>(powerLevel, Heater.POWER_UNIT));
	}
    @Override
	public double		getMaxPowerLevelJava4() throws Exception
	{
		return this.getMaxPowerLevel().getData();
	}

}