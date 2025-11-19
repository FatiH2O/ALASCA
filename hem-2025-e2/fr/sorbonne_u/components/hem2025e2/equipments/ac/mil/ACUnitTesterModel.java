package fr.sorbonne_u.components.hem2025e2.equipments.ac.mil;

import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.hem2025.tests_utils.AbstractTestScenarioBasedAtomicModel;
import fr.sorbonne_u.components.hem2025.tests_utils.TestScenario;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SetPowerAC;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SwitchOffAC;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SwitchOnAC;
import fr.sorbonne_u.devs_simulation.exceptions.MissingRunParameterException;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.AtomicSimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import java.util.Map;

@ModelExternalEvents(exported = {SwitchOnAC.class,
                                 SwitchOffAC.class,
                                 SetPowerAC.class})
public class ACUnitTesterModel extends AbstractTestScenarioBasedAtomicModel
{
    private static final long serialVersionUID = 1L;

    public static final boolean VERBOSE = true;
    public static final boolean DEBUG = false;

    public static final String URI = "ac-unit-tester-model";
    public static final String TEST_SCENARIO_RP_NAME = "TEST_SCENARIO";

    public ACUnitTesterModel(String uri, TimeUnit simulatedTimeUnit,
                             AtomicSimulatorI simulationEngine)
    {
        super(uri, simulatedTimeUnit, simulationEngine);

        this.getSimulationEngine().setLogger(new StandardLogger());

        assert ACUnitTesterModel.implementationInvariants(this) :
                new NeoSim4JavaException(
                        "Implementation Invariants violation: ACUnitTesterModel.implementationInvariants(this)");
        assert ACUnitTesterModel.invariants(this) :
                new NeoSim4JavaException(
                        "Invariants violation: ACUnitTesterModel.invariants(this)");
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
