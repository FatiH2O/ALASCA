package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil;

import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.hem2025.tests_utils.AbstractTestScenarioBasedAtomicModel;
import fr.sorbonne_u.components.hem2025.tests_utils.TestScenario;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SwitchOffCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SwitchOnCharger;
import fr.sorbonne_u.devs_simulation.exceptions.MissingRunParameterException;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.AtomicSimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import java.util.Map;

@ModelExternalEvents(exported = {SwitchOnCharger.class, SwitchOffCharger.class})
public class ChargerUnitTesterModel extends AbstractTestScenarioBasedAtomicModel
{
    private static final long serialVersionUID = 1L;

    public static final boolean VERBOSE = true;
    public static final boolean DEBUG = false;

    public static final String URI = "charger-unit-tester-model";
    public static final String TEST_SCENARIO_RP_NAME = "TEST_SCENARIO";

    public ChargerUnitTesterModel(String uri, TimeUnit simulatedTimeUnit,
                                  AtomicSimulatorI simulationEngine)
    {
        super(uri, simulatedTimeUnit, simulationEngine);

        this.getSimulationEngine().setLogger(new StandardLogger());

        assert ChargerUnitTesterModel.implementationInvariants(this) :
                new NeoSim4JavaException(
                        "Implementation Invariants violation: ChargerUnitTesterModel.implementationInvariants(this)");
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

        assert simParams != null : new MissingRunParameterException("simParams != null");
        assert simParams.containsKey(testScenarioName) : new MissingRunParameterException(testScenarioName);

        this.setTestScenario((TestScenario) simParams.get(testScenarioName));
    }
}
