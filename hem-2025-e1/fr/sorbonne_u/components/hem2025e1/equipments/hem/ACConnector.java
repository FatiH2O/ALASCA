package fr.sorbonne_u.components.hem2025e1.equipments.hem;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2025.bases.AdjustableCI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACExternalControlJava4CI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACUserCI;
import fr.sorbonne_u.exceptions.PreconditionException;
import fr.sorbonne_u.exceptions.PostconditionException;

public class ACConnector extends AbstractConnector implements AdjustableCI {

    // -----------------------------
    // Constants
    // -----------------------------
    public static final int MAX_MODE = 4;

    // -----------------------------
    // Variables
    // -----------------------------
    protected int currentMode;
    protected boolean isSuspended;

    // -----------------------------
    // Constructor
    // -----------------------------
    public ACConnector() {
        super();
        this.currentMode = MAX_MODE;
        this.isSuspended = false;
    }

    // -----------------------------
    // Helpers to access interfaces
    // -----------------------------
    protected ACExternalControlJava4CI ctrl() {
        return (ACExternalControlJava4CI) this.offering;
    }

    protected ACUserCI user() {
        return (ACUserCI) this.offering;
    }

    // -----------------------------
    // INTERNAL LOGIC
    // -----------------------------

    /**
     * Compute power level from mode.
     */
    protected double computePowerLevel(int mode) throws Exception {
        assert mode >= 0 && mode <= MAX_MODE :
                new PreconditionException("mode >= 0 && mode <= MAX_MODE");

        double max = ctrl().getMaxPowerLevelJava4();  // e.g., 2000W
        return (mode == 0) ? 0.0 : (mode * max / MAX_MODE);
    }

    /**
     * Apply power level to AC.
     */
    protected void setNewPowerLevel(double level) throws Exception {
        assert level >= 0.0 :
                new PreconditionException("level >= 0.0");

        double max = ctrl().getMaxPowerLevelJava4();
        if (level > max) level = max;

        ctrl().setCurrentPowerLevelJava4(level);
    }

    /**
     * Compute and apply mode power.
     */
    protected void computeAndSetNewPowerLevel(int mode) throws Exception {
        double level = computePowerLevel(mode);
        setNewPowerLevel(level);
    }

    // -----------------------------
    // AdjustableCI
    // -----------------------------

    @Override
    public int maxMode() throws Exception {
        return MAX_MODE;
    }

    @Override
    public boolean upMode() throws Exception {
        assert !this.suspended() :
                new PreconditionException("!suspended()");
        assert this.currentMode() < MAX_MODE :
                new PreconditionException("currentMode < MAX_MODE");

        try {
            computeAndSetNewPowerLevel(currentMode + 1);
            currentMode++;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean downMode() throws Exception {
        assert !this.suspended() :
                new PreconditionException("!suspended()");
        assert this.currentMode() > 0 :
                new PreconditionException("currentMode > 0");

        try {
            computeAndSetNewPowerLevel(currentMode - 1);
            currentMode--;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean setMode(int modeIndex) throws Exception {
        assert !this.suspended() :
                new PreconditionException("!suspended()");
        assert modeIndex >= 0 && modeIndex <= MAX_MODE :
                new PreconditionException("modeIndex in range");

        try {
            computeAndSetNewPowerLevel(modeIndex);
            currentMode = modeIndex;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public int currentMode() throws Exception {
    	if (this.suspended()) {
			return 0;
		} else {
			return this.currentMode;
		}
    }

    @Override
    public double getModeConsumption(int modeIndex) throws Exception {
        assert modeIndex >= 0 && modeIndex <= MAX_MODE :
                new PreconditionException("modeIndex in range");

        return computePowerLevel(modeIndex);
    }

    @Override
    public boolean suspended() throws Exception {
        return isSuspended;
    }

    @Override
    public boolean suspend() throws Exception {
        assert !this.suspended() :
                new PreconditionException("!suspended()");

        try {
            ctrl().setCurrentPowerLevelJava4(0.0);
            isSuspended = true;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean resume() throws Exception {
        assert this.suspended() :
                new PreconditionException("suspended()");

        try {
            computeAndSetNewPowerLevel(currentMode);
            isSuspended = false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public double emergency() throws Exception {
        assert this.suspended() :
                new PreconditionException("suspended()");

        double currentTemp = ctrl().getCurrentTemperatureJava4();
        double targetTemp = ctrl().getTargetTemperatureJava4();

        double delta = Math.abs(targetTemp - currentTemp);

        double ret;

        if (currentTemp > 35.0 || delta >= 10.0) {
            ret = 1.0;
        } else {
            ret = delta / 10.0;
        }

        assert ret >= 0.0 && ret <= 1.0 :
                new PostconditionException("ret in [0,1]");

        return ret;
    }
}
