package fr.sorbonne_u.components.hem2025e1.equipments.charger;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.alasca.physical_data.SignalData;

public class ChargerImplementation
        implements ChargerControlI, ChargerUserI {

    protected enum ChargerState {
        OFF, IDLE, CHARGING, SUSPENDED
    }

    protected ChargerState currentState;
    protected SignalData<Double> currentChargeLevel;
    protected double currentPower;
    protected double targetPower;

    public static final Measure<Double> MAX_POWER =
            new Measure<>(65.0, MeasurementUnit.WATTS);

    public ChargerImplementation() {
        this.currentState = ChargerState.OFF;
        this.currentChargeLevel =
                new SignalData<>(new Measure<>(50.0, MeasurementUnit.WATTS));
        this.currentPower = 0.0;
        this.targetPower = MAX_POWER.getData();
    }


    @Override
    public boolean isCharging() throws Exception {
        return this.currentState == ChargerState.CHARGING;
    }

    @Override
    public void startCharging() throws Exception {
        if (!this.isPluggedIn())
            throw new Exception("Cannot start charging: charger is unplugged.");

        this.currentState = ChargerState.CHARGING;
        this.currentPower = this.targetPower;
        this.trace("[HEM] Démarrage de la charge à " + this.currentPower + " W\n");
    }

    @Override
    public void stopCharging() throws Exception {
        if (this.currentState == ChargerState.CHARGING) {
            this.currentState = ChargerState.SUSPENDED;
            this.currentPower = 0.0;
            this.trace("[HEM] Charge suspendue.\n");
        }
    }

    @Override
    public Measure<Double> getMaxChargingPower() throws Exception {
        return MAX_POWER;
    }

    @Override
    public SignalData<Double> getChargeLevel() throws Exception {
        return this.currentChargeLevel;
    }

    @Override
    public Measure<Double> getCurrentPower() throws Exception {
        return new Measure<>(this.currentPower, MeasurementUnit.WATTS);
    }

    @Override
    public void setTargetPower(Measure<Double> watts) throws Exception {
        this.targetPower = Math.min(watts.getData(), MAX_POWER.getData());
        this.trace("[HEM] Nouvelle puissance cible : " + this.targetPower + " W\n");
    }

    // Logique intelligente 

    public void adjustChargingPower() {
        double level = this.currentChargeLevel.getMeasure().getData();
        if (level < 20.0) this.targetPower = 65.0;
        else if (level < 80.0) this.targetPower = 40.0;
        else this.targetPower = 20.0;
        this.currentPower = this.targetPower;
    }

    public void autoChargeCycle() throws Exception {
        while (this.currentState == ChargerState.CHARGING) {
            double level = this.currentChargeLevel.getMeasure().getData();

            if (level >= 100.0) {
                this.stopCharging();
                this.trace(" Batterie pleine, charge arrêtée automatiquement.\n");
                break;
            }

            this.adjustChargingPower();
            double newLevel = Math.min(level + 2.0, 100.0);
            this.currentChargeLevel =
                    new SignalData<>(new Measure<>(newLevel, MeasurementUnit.WATTS));
            Thread.sleep(300);
        }
    }

    // Utilisateur 

    @Override
    public boolean isPluggedIn() throws Exception {
        return this.currentState != ChargerState.OFF;
    }

    @Override
    public void plugin() throws Exception {
        if (!this.isPluggedIn()) {
            this.currentState = ChargerState.IDLE;
            this.trace("[USER] Chargeur branché.\n");
        }
    }

    @Override
    public void plugout() throws Exception {
        if (this.isPluggedIn()) {
            this.currentState = ChargerState.OFF;
            this.currentPower = 0.0;
            this.trace("[USER] Chargeur débranché.\n");
        }
    }

    
    protected void trace(String msg) {
        System.out.print("[SmartCharger] " + msg);
    }
}
