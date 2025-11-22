package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil;

// Copyright Jacques Malenfant, Sorbonne Universite.
// (Licence header omitted for brevity; keep it if you want identical header)

import java.util.concurrent.TimeUnit;
import java.util.Map;

import fr.sorbonne_u.components.hem2025.tests_utils.AbstractTestScenarioBasedAtomicModel;
import fr.sorbonne_u.components.hem2025.tests_utils.TestScenario;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.PluginCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.PlugoutCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.StartCharging;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.StopCharging;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SetTargetPower;

import fr.sorbonne_u.devs_simulation.exceptions.MissingRunParameterException;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.AtomicSimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;


@ModelExternalEvents(exported = {
        PluginCharger.class,
        PlugoutCharger.class,
        StartCharging.class,
        StopCharging.class,
        SetTargetPower.class
})
public class ChargerUnitTesterModel
extends AbstractTestScenarioBasedAtomicModel
{
    private static final long serialVersionUID = 1L;

    /** when true, leaves a trace of the execution of the model. */
    public static final boolean VERBOSE = true;
    /** when true, leaves a debugging trace. */
    public static final boolean DEBUG = false;

    /** single model URI. */
    public static final String URI = "charger-unit-tester-model";

    /** name of the run parameter for the test scenario to be executed. */
    public static final String TEST_SCENARIO_RP_NAME = "TEST_SCENARIO";

    /**
     * create an atomic model with the given URI to be run by the given
     * simulator using the given time unit for its clock.
     *
     * @param uri                unique identifier of the model.
     * @param simulatedTimeUnit  time unit used for the simulation clock.
     * @param simulationEngine   simulation engine enacting the model.
     */
    public ChargerUnitTesterModel(
        String uri,
        TimeUnit simulatedTimeUnit,
        AtomicSimulatorI simulationEngine
        )
    {
        super(uri, simulatedTimeUnit, simulationEngine);

        this.getSimulationEngine().setLogger(new StandardLogger());

        // Invariant checking
        assert ChargerUnitTesterModel.implementationInvariants(this) :
                new NeoSim4JavaException(
                        "Implementation Invariants violation: "
                        + "ChargerUnitTesterModel.implementationInvariants(this)");
        assert ChargerUnitTesterModel.invariants(this) :
                new NeoSim4JavaException(
                        "Invariants violation: ChargerUnitTesterModel.invariants(this)");
    }

    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams)
            throws MissingRunParameterException
    {
        String testScenarioName = ModelI.createRunParameterName(this.getURI(),
                                                             TEST_SCENARIO_RP_NAME);

        // Preconditions checking
        assert simParams != null :
                new MissingRunParameterException("simParams != null");
        assert simParams.containsKey(testScenarioName) :
                new MissingRunParameterException(testScenarioName);

        this.setTestScenario((TestScenario) simParams.get(testScenarioName));
    }
}
