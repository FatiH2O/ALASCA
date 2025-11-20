package AC;

import physical_data.fr.sorbonne_u.alasca.physical_data.Measure;
import physical_data.fr.sorbonne_u.alasca.physical_data.SignalData;

public interface ACExternalControlI extends ACTemperatureI {
    public Measure<Double> getMaxPowerLevel() throws Exception;
    public void setCurrentPowerLevel(Measure<Double> powerLevel) throws Exception;
    public SignalData<Double> getCurrentPowerLevel() throws Exception;
}