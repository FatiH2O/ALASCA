package AC;

import fr.sorbonne_u.alasca.physical_data.Measure;

public interface ACUserI extends ACExternalControlI {
    public boolean isOn() throws Exception;
    public void switchOn() throws Exception;
    public void switchOff() throws Exception;
    public void setTargetTemperature(Measure<Double> target) throws Exception;
}