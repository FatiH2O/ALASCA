package fr.sorbonne_u.components.hem2025e1.equipments.ac;

public interface ACUserJava4CI extends ACUserCI {
	public void			setCurrentPowerLevelJava4(double powerLevel) throws Exception;

    public void			setTargetTemperatureJava4(double target) throws Exception;
    public double		getMaxPowerLevelJava4() throws Exception;

    
    public double		getTargetTemperatureJava4() throws Exception;
	public double		getCurrentPowerLevelJava4() throws Exception;

   
    public double		getCurrentTemperatureJava4() throws Exception;
}