package Charger;

public interface ChargerUserI {
    public boolean isPluggedIn() throws Exception;
    public void plugin() throws Exception;
    public void plugout() throws Exception;
}