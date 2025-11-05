package fr.sorbonne_u.components.hem2025e1.equipments.smartCharger;

public interface ChargerUserI {
    public boolean isPluggedIn() throws Exception;
    public void plugin() throws Exception;
    public void plugout() throws Exception;
}