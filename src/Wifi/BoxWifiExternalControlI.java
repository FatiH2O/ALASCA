package Wifi;


public interface BoxWifiExternalControlI
{
    /**
     * Retourne la puissance maximale consommée par la box en mode FULL_ON.
     * (Par exemple : 12 W)
     */
    public double getMaxPowerLevel() throws Exception;

    /**
     * Définit la puissance actuelle selon le mode choisi (OFF, WIFI_ONLY, BOX_ONLY, FULL_ON)
     */
    public void setCurrentPowerLevel(double powerLevel) throws Exception;

    /**
     * Retourne la puissance actuelle de la box.
     */
    public double getCurrentPowerLevel() throws Exception;
  

}