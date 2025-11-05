package fr.sorbonne_u.components.hem2025e1.equipments.ac;

import fr.sorbonne_u.alasca.physical_data.Measure;

public interface ACExternalControlI extends ACTemperatureI {
    public Measure<Double> getMaxPowerLevel() throws Exception;
    public void setCurrentPowerLevel(Measure<Double> powerLevel) throws Exception;
    public Measure<Double> getCurrentPowerLevel() throws Exception;
}