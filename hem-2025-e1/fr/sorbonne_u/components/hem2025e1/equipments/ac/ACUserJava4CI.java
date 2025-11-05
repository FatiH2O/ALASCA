package fr.sorbonne_u.components.hem2025e1.equipments.ac;

public interface ACUserJava4CI extends ACUserCI {
    /**
     * @see fr.sorbonne_u.components.hem2025e1.equipments.ac.ACUserI#setTargetTemperature(fr.sorbonne_u.alasca.physical_data.Measure)
     */
    public void			setTargetTemperatureJava4(double target) throws Exception;

    /**
     * @see fr.sorbonne_u.components.hem2025e1.equipments.ac.ACTemperatureI#getTargetTemperature()
     */
    public double		getTargetTemperatureJava4() throws Exception;

    /**
     * @see fr.sorbonne_u.components.hem2025e1.equipments.ac.ACTemperatureI#getCurrentTemperature()
     */
    public double		getCurrentTemperatureJava4() throws Exception;
}