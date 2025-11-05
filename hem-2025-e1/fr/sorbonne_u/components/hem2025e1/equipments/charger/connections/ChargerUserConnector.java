package fr.sorbonne_u.components.hem2025e1.equipments.charger.connections;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2025e1.equipments.smartCharger.ChargerUserCI;

public class ChargerUserConnector
extends     AbstractConnector
implements  ChargerUserCI
{
    @Override
    public boolean isPluggedIn() throws Exception {
        return ((ChargerUserCI)this.offering).isPluggedIn();
    }

    @Override
    public void plugin() throws Exception {
        ((ChargerUserCI)this.offering).plugin();
    }

    @Override
    public void plugout() throws Exception {
        ((ChargerUserCI)this.offering).plugout();
    }
}