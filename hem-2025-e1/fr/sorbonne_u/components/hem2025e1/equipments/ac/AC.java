package fr.sorbonne_u.components.hem2025e1.equipments.ac;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.alasca.physical_data.SignalData;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.connections.ACExternalControlInboundPort;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.connections.ACInternalControlInboundPort;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.connections.ACUserInboundPort;

@OfferedInterfaces(offered = {ACUserCI.class, ACInternalControlCI.class, ACExternalControlCI.class})
public class AC
extends     AbstractComponent
implements  ACUserI, ACInternalControlI, ACExternalControlI 
{
    
    public enum ACState {
        ON,         // Allumé
        COOLING,    // En train de refroidir
        OFF         // Éteint
    }

    
    public static final String REFLECTION_INBOUND_PORT_URI = "AC-RIP-URI";
    public static final String USER_INBOUND_PORT_URI = "AC-USER-IBP-URI";
    public static final String INTERNAL_CONTROL_INBOUND_PORT_URI = "AC-INTERNAL-IBP-URI";
    public static final String EXTERNAL_CONTROL_INBOUND_PORT_URI = "AC-EXTERNAL-IBP-URI";

    protected ACUserInboundPort acUserInboundPort;
    protected ACInternalControlInboundPort acInternalControlInboundPort;
    protected ACExternalControlInboundPort acExternalControlInboundPort;

    protected ACState currentState;
    protected Measure<Double> targetTemperature;
    protected Measure<Double> currentPowerLevel;

    public static final Measure<Double> MAX_POWER_LEVEL = new Measure<>(2000.0, MeasurementUnit.WATTS);
    public static final Measure<Double> STANDARD_TARGET_TEMPERATURE = new Measure<>(21.0, MeasurementUnit.CELSIUS);
    public static final SignalData<Double> FAKE_CURRENT_TEMPERATURE = new SignalData<>(new Measure<>(25.0, MeasurementUnit.CELSIUS));

    protected AC() throws Exception {
        this(USER_INBOUND_PORT_URI, INTERNAL_CONTROL_INBOUND_PORT_URI, EXTERNAL_CONTROL_INBOUND_PORT_URI);
    }

    protected AC(
        String userInboundPortURI,
        String internalControlInboundPortURI,
        String externalControlInboundPortURI
    ) throws Exception {
        this(REFLECTION_INBOUND_PORT_URI, userInboundPortURI, internalControlInboundPortURI, externalControlInboundPortURI);
    }

    protected AC(
        String reflectionInboundPortURI,
        String userInboundPortURI,
        String internalControlInboundPortURI,
        String externalControlInboundPortURI
    ) throws Exception {
        super(reflectionInboundPortURI, 3, 0);
        this.initialise(userInboundPortURI, internalControlInboundPortURI, externalControlInboundPortURI);
    }

    protected void initialise(
        String userInboundPortURI,
        String internalControlInboundPortURI,
        String externalControlInboundPortURI
    ) throws Exception {
        this.currentState = ACState.OFF;
        this.targetTemperature = STANDARD_TARGET_TEMPERATURE;
        this.currentPowerLevel = new Measure<>(0.0, MeasurementUnit.WATTS);

        this.acUserInboundPort = new ACUserInboundPort(userInboundPortURI, this);
        this.acUserInboundPort.publishPort();

        this.acInternalControlInboundPort = new ACInternalControlInboundPort(internalControlInboundPortURI, this);
        this.acInternalControlInboundPort.publishPort();

        this.acExternalControlInboundPort = new ACExternalControlInboundPort(externalControlInboundPortURI, this);
        this.acExternalControlInboundPort.publishPort();
        
        this.getTracer().setTitle("Composant Climatiseur (AC)");
        this.getTracer().setRelativePosition(3, 0);
        this.toggleTracing();
    }


    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            this.acUserInboundPort.unpublishPort();
            this.acInternalControlInboundPort.unpublishPort();
            this.acExternalControlInboundPort.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

   
    @Override
    public boolean isOn() throws Exception {
        return this.currentState != ACState.OFF;
    }

    @Override
    public void switchOn() throws Exception {
        if (!this.isOn()) {
            this.currentState = ACState.ON;
        }
    }

    @Override
    public void switchOff() throws Exception {
        if (this.isOn()) {
            this.currentState = ACState.OFF;
        }
    }

    @Override
    public void setTargetTemperature(Measure<Double> target) throws Exception {
        this.targetTemperature = target;
    }

    @Override
    public boolean isCooling() throws Exception {
        return this.currentState == ACState.COOLING;
    }

    @Override
    public void startCooling() throws Exception {
        if (this.isOn() && !this.isCooling()) {
            this.currentState = ACState.COOLING;
        }
    }

    @Override
    public void stopCooling() throws Exception {
        if (this.isCooling()) {
            this.currentState = ACState.ON;
        }
    }

    @Override
    public Measure<Double> getMaxPowerLevel() throws Exception {
        return MAX_POWER_LEVEL;
    }

    @Override
    public void setCurrentPowerLevel(Measure<Double> powerLevel) throws Exception {
        this.currentPowerLevel = powerLevel;
    }

    @Override
    public Measure<Double> getCurrentPowerLevel() throws Exception {
        return this.currentPowerLevel;
    }

    @Override
    public Measure<Double> getTargetTemperature() throws Exception {
        return this.targetTemperature;
    }

    @Override
    public SignalData<Double> getCurrentTemperature() throws Exception {
        return FAKE_CURRENT_TEMPERATURE;
    }
}