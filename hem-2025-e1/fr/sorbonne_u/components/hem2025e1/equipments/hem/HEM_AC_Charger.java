package fr.sorbonne_u.components.hem2025e1.equipments.hem;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.BCMRuntimeException;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.hem2025.bases.AdjustableCI;
import fr.sorbonne_u.components.hem2025.bases.RegistrationCI;
import fr.sorbonne_u.components.hem2025.tests_utils.TestsStatistics;
import fr.sorbonne_u.components.hem2025e1.CVMIntegrationTest;
import fr.sorbonne_u.components.hem2025e1.equipments.meter.ElectricMeter;
import fr.sorbonne_u.components.hem2025e1.equipments.meter.ElectricMeterCI;
import fr.sorbonne_u.components.hem2025e1.equipments.meter.connections.ElectricMeterConnector;
import fr.sorbonne_u.components.hem2025e1.equipments.meter.connections.ElectricMeterOutboundPort;
import fr.sorbonne_u.exceptions.ImplementationInvariantException;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.InvariantException;
import fr.sorbonne_u.exceptions.PreconditionException;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServer;
import fr.sorbonne_u.utils.aclocks.ClocksServerCI;
import fr.sorbonne_u.utils.aclocks.ClocksServerConnector;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@RequiredInterfaces(required = {AdjustableCI.class, ElectricMeterCI.class, ClocksServerCI.class})
public class HEM_AC_Charger extends AbstractComponent implements RegistrationCI {
    
    public static boolean VERBOSE = false;
    public static int X_RELATIVE_POSITION = 0;
    public static int Y_RELATIVE_POSITION = 0;

    protected ElectricMeterOutboundPort meterop;
    protected boolean isPreFirstStep;
    protected AdjustableOutboundPort acop;
    protected AdjustableOutboundPort chargerop;

    protected boolean performTest;
    protected AcceleratedClock ac;
    protected TestsStatistics statistics;

    protected static boolean implementationInvariants(HEM_AC_Charger hem) {
        assert hem != null : new PreconditionException("hem != null");
        return true;
    }

    protected static boolean invariants(HEM_AC_Charger hem) {
        assert hem != null : new PreconditionException("hem != null");
        boolean ret = true;
        ret &= AssertionChecking.checkImplementationInvariant(X_RELATIVE_POSITION >= 0, HEM_AC_Charger.class, hem, "X_RELATIVE_POSITION >= 0");
        ret &= AssertionChecking.checkImplementationInvariant(Y_RELATIVE_POSITION >= 0, HEM_AC_Charger.class, hem, "Y_RELATIVE_POSITION >= 0");
        return ret;
    }

    protected HEM_AC_Charger() {
        this(true);
    }

    protected HEM_AC_Charger(boolean performTest) {
        super(1, 1);
        this.performTest = performTest;
        this.isPreFirstStep = true;
        this.statistics = new TestsStatistics();

        if (VERBOSE) {
            this.tracer.get().setTitle("Home Energy Manager (AC + Charger)");
            this.tracer.get().setRelativePosition(X_RELATIVE_POSITION, Y_RELATIVE_POSITION);
            this.toggleTracing();
        }

        assert HEM_AC_Charger.implementationInvariants(this) : new ImplementationInvariantException("HEM_AC_Charger.implementationInvariants(this)");
        assert HEM_AC_Charger.invariants(this) : new InvariantException("HEM_AC_Charger.invariants(this)");
    }

    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();

        try {
            this.meterop = new ElectricMeterOutboundPort(this);
            this.meterop.publishPort();
            this.doPortConnection(
                    this.meterop.getPortURI(),
                    ElectricMeter.ELECTRIC_METER_INBOUND_PORT_URI,
                    ElectricMeterConnector.class.getCanonicalName());

            if (this.isPreFirstStep) {
                this.acop = new AdjustableOutboundPort(this);
                this.acop.publishPort();
                this.doPortConnection(
                        this.acop.getPortURI(),
                        "AC-EXTERNAL-JAVA4-IBP-URI",
                        ACConnector.class.getCanonicalName());

                this.chargerop = new AdjustableOutboundPort(this);
                this.chargerop.publishPort();
                this.doPortConnection(
                        this.chargerop.getPortURI(),
                        "SMARTCHARGER-CONTROL-INBOUND-URI",
                        ChargerConnector.class.getCanonicalName());
            }
        } catch (Exception e) {
            throw new ComponentStartException(e);
        }
    }

    @Override
    public synchronized void execute() throws Exception {

        this.logMessage("HEM starts without clock.\n");

        if (this.performTest) {
            this.logMessage("Electric meter tests start.");
            this.testMeter();
            this.logMessage("Electric meter tests end.");

            if (this.isPreFirstStep) {
                this.logMessage("AC tests start.");
                this.testAC();

                this.logMessage("Charger tests start.");
                this.testCharger();
            }
        }
    }


    @Override
    public synchronized void finalise() throws Exception {
        this.doPortDisconnection(this.meterop.getPortURI());
        if (this.isPreFirstStep) {
            this.doPortDisconnection(this.acop.getPortURI());
            this.doPortDisconnection(this.chargerop.getPortURI());
        }
        super.finalise();
    }

    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            this.meterop.unpublishPort();
            if (this.isPreFirstStep) {
                this.acop.unpublishPort();
                this.chargerop.unpublishPort();
            }
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

    protected void testMeter() throws Exception {
        this.statistics.updateStatistics();
    }

    protected void testAC() throws Exception {
        this.logMessage("Air Conditioner tests start.");
        this.statistics = new TestsStatistics();
        try {
            this.logMessage("Feature: adjustable appliance mode management");
            this.logMessage("  Scenario: getting the max mode index");
            this.logMessage("    When I call maxMode()");
            final int maxMode = acop.maxMode();
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: getting the current mode index");
            this.logMessage("    When I call currentMode()");
            int result = acop.currentMode();
            if (result != maxMode) {
                this.logMessage("      but was: " + result);
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: going down one mode index");
            result = acop.currentMode();
            if (result != maxMode) {
                this.logMessage("      but was: " + result);
                this.statistics.failedCondition();
            }
            this.logMessage("    When I call downMode()");
            boolean bResult = acop.downMode();
            if (!bResult) {
                this.logMessage("      but was: " + bResult);
                this.statistics.incorrectResult();
            }
            result = acop.currentMode();
            if (result != maxMode - 1) {
                this.logMessage("      but was: " + result);
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: going up one mode index");
            result = acop.currentMode();
            if (result != maxMode - 1) {
                this.logMessage("      but was: " + result);
                this.statistics.failedCondition();
            }
            this.logMessage("    When I call upMode()");
            bResult = acop.upMode();
            if (!bResult) {
                this.logMessage("      but was: " + bResult);
                this.statistics.incorrectResult();
            }
            result = acop.currentMode();
            if (result != maxMode) {
                this.logMessage("      but was: " + result);
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: setting the mode index");
            int index = 1;
            this.logMessage("    And the mode index 1 is legitimate");
            if (index > maxMode) {
                this.logMessage("      but was not!");
                this.statistics.failedCondition();
            }
            this.logMessage("    When I call setMode(1)");
            bResult = acop.setMode(1);
            if (!bResult) {
                this.logMessage("      but was: " + bResult);
                this.statistics.incorrectResult();
            }
            result = acop.currentMode();
            if (result != 1) {
                this.logMessage("      but was: " + result);
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("Feature: Getting the power consumption given a mode");
            this.logMessage("  Scenario: getting the power consumption of the maximum mode");
            double dResult = acop.getModeConsumption(maxMode);
            this.statistics.updateStatistics();

            this.logMessage("Feature: suspending and resuming");
            this.logMessage("  Scenario: checking if suspended when not");
            bResult = acop.suspended();
            if (bResult) {
                this.logMessage("      but it was!");
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: suspending");
            bResult = acop.suspended();
            if (bResult) {
                this.logMessage("      but it was!");
                this.statistics.failedCondition();
            }
            this.logMessage("    When I call suspend()");
            bResult = acop.suspend();
            if (!bResult) {
                this.logMessage("      but was: " + bResult);
                this.statistics.incorrectResult();
            }
            bResult = acop.suspended();
            if (!bResult) {
                this.logMessage("      but it was not!");
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: checking the emergency");
            bResult = acop.suspended();
            if (!bResult) {
                this.logMessage("      but it was not!");
                this.statistics.failedCondition();
            }
            this.logMessage("    When I call emergency()");
            dResult = acop.emergency();
            if (dResult < 0.0 || dResult > 1.0) {
                this.logMessage("      but was: " + dResult);
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: resuming");
            bResult = acop.suspended();
            if (!bResult) {
                this.logMessage("      but it was not!");
                this.statistics.failedCondition();
            }
            this.logMessage("    When I call resume()");
            bResult = acop.resume();
            if (!bResult) {
                this.logMessage("      but was: " + bResult);
                this.statistics.incorrectResult();
            }
            bResult = acop.suspended();
            if (bResult) {
                this.logMessage("      but it was!");
                this.statistics.incorrectResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.statistics.updateStatistics();
        this.statistics.statisticsReport(this);
        this.logMessage("Air Conditioner tests end.");
    }

    protected void testCharger() throws Exception {
        this.logMessage("Smart Charger tests start.");
        this.statistics = new TestsStatistics();
        try {
            this.logMessage("Feature: adjustable appliance mode management");
            this.logMessage("  Scenario: getting the max mode index");
            this.logMessage("    When I call maxMode()");
            final int maxMode = chargerop.maxMode();
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: getting the current mode index");
            this.logMessage("    When I call currentMode()");
            int result = chargerop.currentMode();
            if (result != maxMode) {
                this.logMessage("      but was: " + result);
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: going down one mode index");
            result = chargerop.currentMode();
            if (result != maxMode) {
                this.logMessage("      but was: " + result);
                this.statistics.failedCondition();
            }
            this.logMessage("    When I call downMode()");
            boolean bResult = chargerop.downMode();
            if (!bResult) {
                this.logMessage("      but was: " + bResult);
                this.statistics.incorrectResult();
            }
            result = chargerop.currentMode();
            if (result != maxMode - 1) {
                this.logMessage("      but was: " + result);
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: going up one mode index");
            result = chargerop.currentMode();
            if (result != maxMode - 1) {
                this.logMessage("      but was: " + result);
                this.statistics.failedCondition();
            }
            this.logMessage("    When I call upMode()");
            bResult = chargerop.upMode();
            if (!bResult) {
                this.logMessage("      but was: " + bResult);
                this.statistics.incorrectResult();
            }
            result = chargerop.currentMode();
            if (result != maxMode) {
                this.logMessage("      but was: " + result);
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: setting the mode index");
            int index = 1;
            this.logMessage("    And the mode index 1 is legitimate");
            if (index > maxMode) {
                this.logMessage("      but was not!");
                this.statistics.failedCondition();
            }
            this.logMessage("    When I call setMode(1)");
            bResult = chargerop.setMode(1);
            if (!bResult) {
                this.logMessage("      but was: " + bResult);
                this.statistics.incorrectResult();
            }
            result = chargerop.currentMode();
            if (result != 1) {
                this.logMessage("      but was: " + result);
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("Feature: Getting the power consumption given a mode");
            this.logMessage("  Scenario: getting the power consumption of mode 1");
            double dResult = chargerop.getModeConsumption(1);
            this.statistics.updateStatistics();

            this.logMessage("Feature: suspending and resuming");
            this.logMessage("  Scenario: checking if suspended when not");
            bResult = chargerop.suspended();
            if (bResult) {
                this.logMessage("      but it was!");
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: suspending");
            bResult = chargerop.suspended();
            if (bResult) {
                this.logMessage("      but it was!");
                this.statistics.failedCondition();
            }
            this.logMessage("    When I call suspend()");
            bResult = chargerop.suspend();
            if (!bResult) {
                this.logMessage("      but was: " + bResult);
                this.statistics.incorrectResult();
            }
            bResult = chargerop.suspended();
            if (!bResult) {
                this.logMessage("      but it was not!");
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: checking the emergency");
            bResult = chargerop.suspended();
            if (!bResult) {
                this.logMessage("      but it was not!");
                this.statistics.failedCondition();
            }
            this.logMessage("    When I call emergency()");
            dResult = chargerop.emergency();
            if (dResult < 0.0 || dResult > 1.0) {
                this.logMessage("      but was: " + dResult);
                this.statistics.incorrectResult();
            }
            this.statistics.updateStatistics();

            this.logMessage("  Scenario: resuming");
            bResult = chargerop.suspended();
            if (!bResult) {
                this.logMessage("      but it was not!");
                this.statistics.failedCondition();
            }
            this.logMessage("    When I call resume()");
            bResult = chargerop.resume();
            if (!bResult) {
                this.logMessage("      but was: " + bResult);
                this.statistics.incorrectResult();
            }
            bResult = chargerop.suspended();
            if (bResult) {
                this.logMessage("      but it was!");
                this.statistics.incorrectResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.statistics.statisticsReport(this);
        this.logMessage("Smart Charger tests end.");
    }

    protected void scheduleTestAC() {
        Instant acTestStart = this.ac.getStartInstant().plusSeconds(5);
        this.traceMessage("HEM schedules the AC test.\n");
        long delay = this.ac.nanoDelayUntilInstant(acTestStart);

        this.scheduleTaskOnComponent(
                new AbstractComponent.AbstractTask() {
                    @Override
                    public void run() {
                        try {
                            testAC();
                        } catch (Exception e) {
                            throw new BCMRuntimeException(e);
                        }
                    }
                }, delay, TimeUnit.NANOSECONDS);
    }

    protected void scheduleTestCharger() {
        Instant chargerTestStart = this.ac.getStartInstant().plusSeconds(15);
        this.traceMessage("HEM schedules the charger test.\n");
        long delay = this.ac.nanoDelayUntilInstant(chargerTestStart);

        this.scheduleTaskOnComponent(
                new AbstractComponent.AbstractTask() {
                    @Override
                    public void run() {
                        try {
                            testCharger();
                        } catch (Exception e) {
                            throw new BCMRuntimeException(e);
                        }
                    }
                }, delay, TimeUnit.NANOSECONDS);
    }

    @Override
    public boolean registered(String uid) throws Exception {
        return false;
    }

    @Override
    public boolean register(String uid, String controlPortURI, String xmlControlAdapter) throws Exception {
        return false;
    }

    @Override
    public void unregister(String uid) throws Exception {
    }
}