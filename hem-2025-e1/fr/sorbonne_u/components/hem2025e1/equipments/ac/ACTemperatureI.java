package fr.sorbonne_u.components.hem2025e1.equipments.ac;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.SignalData;

public interface ACTemperatureI {
    public Measure<Double> getTargetTemperature() throws Exception;
    public SignalData<Double> getCurrentTemperature() throws Exception;
}