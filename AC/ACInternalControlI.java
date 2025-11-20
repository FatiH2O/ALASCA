package AC;

public interface ACInternalControlI extends ACTemperatureI {
    public boolean isCooling() throws Exception;
    public void startCooling() throws Exception;
    public void stopCooling() throws Exception;
}