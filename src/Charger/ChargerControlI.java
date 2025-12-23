package Charger;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.SignalData;

public interface ChargerControlI {
    boolean isCharging() throws Exception;
    void startCharging() throws Exception;
    void stopCharging() throws Exception;
    Measure<Double> getMaxChargingPower() throws Exception;
    SignalData<Double> getChargeLevel() throws Exception;

    // fonctionnalit� advanced 
    Measure<Double> getCurrentPower() throws Exception;
    void setTargetPower(Measure<Double> powerLevel) throws Exception; 
}