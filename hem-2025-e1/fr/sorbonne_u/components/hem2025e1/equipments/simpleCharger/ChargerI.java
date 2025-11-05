package fr.sorbonne_u.components.hem2025e1.equipments.simpleCharger;

public interface ChargerI {
    public enum State {
        ON,     
        OFF     
    }

    public State getState() throws Exception;
    public void turnOn() throws Exception;
    public void turnOff() throws Exception;
}