package fr.sorbonne_u.components.hem2025e1.equipments.charger;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ChargerControlJava4CI 
extends     OfferedCI, RequiredCI 
{
    public double getMaxChargingPowerJava4() throws Exception;

    public void setTargetPowerJava4(double watts) throws Exception;
    
    public double getCurrentPowerJava4() throws Exception;
    
}