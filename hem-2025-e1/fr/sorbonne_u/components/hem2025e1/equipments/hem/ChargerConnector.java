package fr.sorbonne_u.components.hem2025e1.equipments.hem;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2025.bases.AdjustableCI;
import fr.sorbonne_u.components.hem2025e1.equipments.charger.ChargerControlCI;
import fr.sorbonne_u.components.hem2025e1.equipments.charger.ChargerUserCI;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;

public class ChargerConnector extends AbstractConnector implements AdjustableCI {

    private static final int MAX_MODE = 3;

    private int currentMode = MAX_MODE;
    private boolean suspended = false;

    private final double[] modeConsumptions = {
            0.0,
            20.0,
            40.0,
            65.0
    };

    private ChargerUserCI user() {
        return (ChargerUserCI) this.offering;
    }

    private ChargerControlCI ctrl() {
        return (ChargerControlCI) this.offering;
    }

    // ====================================================
    //   AdjustableCI
    // ====================================================

    @Override
    public int maxMode() throws Exception {
        return MAX_MODE;
    }

    @Override
    public int currentMode() throws Exception {
        return currentMode;
    }

    @Override
    public boolean upMode() throws Exception {
        if (suspended) return false;
        if (currentMode >= MAX_MODE) return false;

        currentMode++;
        applyMode();
        return true;
    }

    @Override
    public boolean downMode() throws Exception {
        if (suspended) return false;
        if (currentMode <= 0) return false;

        currentMode--;
        applyMode();
        return true;
    }

    @Override
    public boolean setMode(int modeIndex) throws Exception {
        if (suspended) return false;
        if (modeIndex < 0 || modeIndex > MAX_MODE) return false;

        currentMode = modeIndex;
        applyMode();
        return true;
    }

    @Override
    public double getModeConsumption(int modeIndex) throws Exception {
        return modeConsumptions[modeIndex];
    }

    @Override
    public boolean suspended() throws Exception {
        return suspended;
    }

    @Override
    public boolean suspend() throws Exception {
        if (suspended) return false;

        ctrl().setTargetPower(new Measure<>(0.0, MeasurementUnit.WATTS));
        suspended = true;
        return true;
    }

    @Override
    public boolean resume() throws Exception {
        if (!suspended) return false;

        suspended = false;
        applyMode();
        return true;
    }

    @Override
    public double emergency() throws Exception {
        return 0.5;
    }

    // ====================================================
    // TRADUCTION MODE → ACTION PHYSIQUE SUR LE CHARGEUR
    // ====================================================
    private void applyMode() throws Exception {

        switch (currentMode) {
            case 0:
                ctrl().setTargetPower(new Measure<>(0.0, MeasurementUnit.WATTS));
                break;

            case 1:
                ctrl().setTargetPower(new Measure<>(20.0, MeasurementUnit.WATTS));
                break;

            case 2:
                ctrl().setTargetPower(new Measure<>(40.0, MeasurementUnit.WATTS));
                break;

            case 3:
                ctrl().setTargetPower(new Measure<>(65.0, MeasurementUnit.WATTS));
                break;
        }
    }
}
