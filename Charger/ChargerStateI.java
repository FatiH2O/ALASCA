package Charger;

import physical_data.fr.sorbonne_u.alasca.physical_data.SignalData;

public interface ChargerStateI {
    public SignalData<Double> getChargeLevel() throws Exception;
}