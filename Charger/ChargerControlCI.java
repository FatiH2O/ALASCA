package Charger;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import physical_data.fr.sorbonne_u.alasca.physical_data.Measure;
import physical_data.fr.sorbonne_u.alasca.physical_data.SignalData;

public interface ChargerControlCI extends OfferedCI, RequiredCI {
    boolean isCharging() throws Exception;
    void startCharging() throws Exception;
    void stopCharging() throws Exception;
    Measure<Double> getMaxChargingPower() throws Exception;
    SignalData<Double> getChargeLevel() throws Exception;

    // Smart additions
    Measure<Double> getCurrentPower() throws Exception;
    void setTargetPower(Measure<Double> watts) throws Exception;
}
