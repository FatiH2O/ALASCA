package AC;

public interface ACExternalControlJava4CI extends ACExternalControlCI {
    /**
     * @see fr.sorbonne_u.components.hem2025e1.equipments.ac.ACExternalControlI#getMaxPowerLevel()
     */
    public double		getMaxPowerLevelJava4() throws Exception;

    /**
     * @see fr.sorbonne_u.components.hem2025e1.equipments.ac.ACExternalControlI#setCurrentPowerLevel(fr.sorbonne_u.alasca.physical_data.Measure)
     */
    public void			setCurrentPowerLevelJava4(double powerLevel) throws Exception;

    /**
     * @see fr.sorbonne_u.components.hem2025e1.equipments.ac.ACExternalControlI#getCurrentPowerLevel()
     */
    public double		getCurrentPowerLevelJava4() throws Exception;

    /**
     * @see fr.sorbonne_u.components.hem2025e1.equipments.ac.ACTemperatureI#getTargetTemperature()
     */
    public double		getTargetTemperatureJava4() throws Exception;

    /**
     * @see fr.sorbonne_u.components.hem2025e1.equipments.ac.ACTemperatureI#getCurrentTemperature()
     */
    public double		getCurrentTemperatureJava4() throws Exception;
}