package fr.sorbonne_u.components.hem2025e1.equipments.charger;

import fr.sorbonne_u.alasca.physical_data.SignalData;

public interface ChargerStateI {
    public SignalData<Double> getChargeLevel() throws Exception;
}