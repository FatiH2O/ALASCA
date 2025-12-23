package Wifi;


import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface BoxWifiExternalControlCI
extends OfferedCI,
        BoxWifiExternalControlI,BoxWifiImplementationI
{ 
        	/**
             * Retourne la puissance maximale consommée par la box en mode FULL_ON.
             * (Par exemple : 12 W)
             */
            public double getMaxPowerLevel() throws Exception;

            /**
             * Définit la puissance actuelle selon le mode choisi (OFF, BOX_ONLY, FULL_ON)
             */
            public void setCurrentPowerLevel(double powerLevel) throws Exception;

            /**
             * Retourne la puissance actuelle de la box.
             */
            public double getCurrentPowerLevel() throws Exception;
           

            }
