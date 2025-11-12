package fr.sorbonne_u.components.hem2025e1.equipments.charger.connections;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.SignalData;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2025e1.equipments.charger.ChargerControlCI;

public class ChargerControlConnector extends AbstractConnector implements ChargerControlCI {

    @Override
    public boolean isCharging() throws Exception {
        return ((ChargerControlCI) this.offering).isCharging();
    }

    @Override
    public void startCharging() throws Exception {
        ((ChargerControlCI) this.offering).startCharging();
    }

    @Override
    public void stopCharging() throws Exception {
        ((ChargerControlCI) this.offering).stopCharging();
    }

    @Override
    public Measure<Double> getMaxChargingPower() throws Exception {
        return ((ChargerControlCI) this.offering).getMaxChargingPower();
    }

    @Override
    public SignalData<Double> getChargeLevel() throws Exception {
        return ((ChargerControlCI) this.offering).getChargeLevel();
    }

    //  fonctionnalité 2
    @Override
    public Measure<Double> getCurrentPower() throws Exception {
        return ((ChargerControlCI) this.offering).getCurrentPower();
    }

    @Override
    public void setTargetPower(Measure<Double> watts) throws Exception {
        ((ChargerControlCI) this.offering).setTargetPower(watts);
    }
}
