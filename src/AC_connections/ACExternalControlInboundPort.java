package AC_connections;

import AC.ACExternalControlCI;

import AC.ACExternalControlI;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.SignalData;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class ACExternalControlInboundPort extends AbstractInboundPort implements ACExternalControlCI {
    private static final long serialVersionUID = 1L;

    public ACExternalControlInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ACExternalControlCI.class, owner);
    }

    @Override
    public Measure<Double> getMaxPowerLevel() throws Exception {
        return  this.getOwner().handleRequest(o -> ((ACExternalControlI)o).getMaxPowerLevel());
    }

    @Override
    public void setCurrentPowerLevel(Measure<Double> powerLevel) throws Exception {
        this.getOwner().handleRequest(o -> { ((ACExternalControlI)o).setCurrentPowerLevel(powerLevel); return null; });
    }
    
    @Override
    public SignalData<Double> getCurrentPowerLevel() throws Exception {
        return this.getOwner().handleRequest(o -> ((ACExternalControlI)o).getCurrentPowerLevel());
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return this.getOwner().handleRequest(o -> ((ACExternalControlI)o).getTargetTemperature());
    }

    @Override
    public SignalData<Double> getCurrentTemperature() throws Exception {
        return this.getOwner().handleRequest(o -> ((ACExternalControlI)o).getCurrentTemperature());
    }
}