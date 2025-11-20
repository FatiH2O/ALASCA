package Charger_connections;

import Charger.ChargerControlCI;
import Charger.ChargerControlI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import physical_data.fr.sorbonne_u.alasca.physical_data.Measure;
import physical_data.fr.sorbonne_u.alasca.physical_data.SignalData;

public class ChargerControlInboundPort
extends     AbstractInboundPort
implements  ChargerControlCI
{
    private static final long serialVersionUID = 1L;

    public ChargerControlInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ChargerControlCI.class, owner);
    }

    @Override
    public boolean isCharging() throws Exception {
        return this.getOwner().handleRequest(o -> ((ChargerControlI)o).isCharging());
    }

    @Override
    public void startCharging() throws Exception {
        this.getOwner().handleRequest(o -> { ((ChargerControlI)o).startCharging(); return null; });
    }

    @Override
    public void stopCharging() throws Exception {
        this.getOwner().handleRequest(o -> { ((ChargerControlI)o).stopCharging(); return null; });
    }

    @Override
    public Measure<Double> getMaxChargingPower() throws Exception {
        return this.getOwner().handleRequest(o -> ((ChargerControlI)o).getMaxChargingPower());
    }

    @Override
    public SignalData<Double> getChargeLevel() throws Exception {
        return this.getOwner().handleRequest(o -> ((ChargerControlI)o).getChargeLevel());
    }

    // ---------- Smart Charger additions ----------

    @Override
    public Measure<Double> getCurrentPower() throws Exception {
        return this.getOwner().handleRequest(o -> ((ChargerControlI)o).getCurrentPower());
    }

    @Override
    public void setTargetPower(Measure<Double>watts) throws Exception {
        this.getOwner().handleRequest(o -> { ((ChargerControlI)o).setTargetPower(watts); return null; });
    }
}
