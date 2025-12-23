package Charger_connections;
import Charger.ChargerControlCI;
import fr.sorbonne_u.alasca.physical_data.Measure;

import fr.sorbonne_u.alasca.physical_data.SignalData;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class ChargerControlOutboundPort extends AbstractOutboundPort implements ChargerControlCI {
    private static final long serialVersionUID = 1L;

    public ChargerControlOutboundPort(ComponentI owner) throws Exception {
        super(ChargerControlCI.class, owner);
    }

    public ChargerControlOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ChargerControlCI.class, owner);
    }

    @Override
    public boolean isCharging() throws Exception {
        return ((ChargerControlCI) this.getConnector()).isCharging();
    }

    @Override
    public void startCharging() throws Exception {
        ((ChargerControlCI) this.getConnector()).startCharging();
    }

    @Override
    public void stopCharging() throws Exception {
        ((ChargerControlCI) this.getConnector()).stopCharging();
    }

    @Override
    public Measure<Double> getMaxChargingPower() throws Exception {
        return ((ChargerControlCI) this.getConnector()).getMaxChargingPower();
    }

    @Override
    public SignalData<Double> getChargeLevel() throws Exception {
        return ((ChargerControlCI) this.getConnector()).getChargeLevel();
    }

    // fonctions 2
    @Override
    public Measure<Double> getCurrentPower() throws Exception {
        return ((ChargerControlCI) this.getConnector()).getCurrentPower();
    }

    @Override
    public void setTargetPower(Measure<Double> watts) throws Exception {
        ((ChargerControlCI) this.getConnector()).setTargetPower(watts);
    }
}
