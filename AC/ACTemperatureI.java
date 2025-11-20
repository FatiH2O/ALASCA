package AC;

import physical_data.fr.sorbonne_u.alasca.physical_data.Measure;
import physical_data.fr.sorbonne_u.alasca.physical_data.SignalData;

public interface ACTemperatureI {
    public Measure<Double> getTargetTemperature() throws Exception;
    public SignalData<Double> getCurrentTemperature() throws Exception;
}