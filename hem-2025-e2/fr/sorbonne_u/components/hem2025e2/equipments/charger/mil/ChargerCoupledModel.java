package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.CoupledModel;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.CoordinatorI;

public class ChargerCoupledModel extends CoupledModel {
    private static final long serialVersionUID = 1L;

    public static final String URI = ChargerCoupledModel.class.getSimpleName();

    public ChargerCoupledModel(String uri,
    		TimeUnit simulatedTimeUnit,
    		CoordinatorI simulationEngine,
    		ModelI[] submodels,
    		Map<Class<? extends EventI>,EventSink[]> imported,
    		Map<Class<? extends EventI>,ReexportedEvent> reexported,
    		Map<EventSource, EventSink[]> connections
    		) throws Exception
    	{
    		super(uri, simulatedTimeUnit, simulationEngine, submodels,
    			  imported, reexported, connections);
    	}

}
