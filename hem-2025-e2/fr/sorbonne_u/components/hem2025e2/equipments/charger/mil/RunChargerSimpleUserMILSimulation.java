package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.PluginCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.PlugoutCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.StartCharging;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.StopCharging;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SetTargetPower;

import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.ArchitectureI;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

/**
 * The class <code>RunChargerSimpleUserMILSimulation</code> runs a unit
 * simulation of the charger with a simple user model.
 */
public class RunChargerSimpleUserMILSimulation
{
    public static void main(String[] args)
    {
        Time.setPrintPrecision(4);
        Duration.setPrintPrecision(4);

        try {
            // -------------------------------
            // Atomic model descriptors
            // -------------------------------
            Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors =
                    new HashMap<>();

            // Charger electricity model (HIOA)
            atomicModelDescriptors.put(
                    ChargerElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            ChargerElectricityModel.class,
                            ChargerElectricityModel.URI,
                            TimeUnit.HOURS,
                            null));

            // Charger simple user model (atomic)
            atomicModelDescriptors.put(
                    ChargerSimpleUserModel.URI,
                    AtomicModelDescriptor.create(
                            ChargerSimpleUserModel.class,
                            ChargerSimpleUserModel.URI,
                            TimeUnit.HOURS,
                            null));

            // -------------------------------
            // Coupled model descriptors
            // -------------------------------
            Map<String, CoupledModelDescriptor> coupledModelDescriptors =
                    new HashMap<>();

            // Submodels of ChargerCoupledModel
            Set<String> submodels = new HashSet<>();
            submodels.add(ChargerElectricityModel.URI);
            submodels.add(ChargerSimpleUserModel.URI);

            // -------------------------------
            // Event connections
            // -------------------------------
            Map<EventSource, EventSink[]> connections = new HashMap<>();

            connections.put(
                    new EventSource(ChargerSimpleUserModel.URI, PluginCharger.class),
                    new EventSink[] {
                        new EventSink(ChargerElectricityModel.URI, PluginCharger.class)
                    });

            connections.put(
                    new EventSource(ChargerSimpleUserModel.URI, PlugoutCharger.class),
                    new EventSink[] {
                        new EventSink(ChargerElectricityModel.URI, PlugoutCharger.class)
                    });

            connections.put(
                    new EventSource(ChargerSimpleUserModel.URI, StartCharging.class),
                    new EventSink[] {
                        new EventSink(ChargerElectricityModel.URI, StartCharging.class)
                    });

            connections.put(
                    new EventSource(ChargerSimpleUserModel.URI, StopCharging.class),
                    new EventSink[] {
                        new EventSink(ChargerElectricityModel.URI, StopCharging.class)
                    });

            connections.put(
                    new EventSource(ChargerSimpleUserModel.URI, SetTargetPower.class),
                    new EventSink[] {
                        new EventSink(ChargerElectricityModel.URI, SetTargetPower.class)
                    });

            // Coupled model descriptor
            coupledModelDescriptors.put(
                    ChargerCoupledModel.URI,
                    new CoupledModelDescriptor(
                            ChargerCoupledModel.class,
                            ChargerCoupledModel.URI,
                            submodels,
                            null,
                            null,
                            connections,
                            null));

            // -------------------------------
            // Create architecture
            // -------------------------------
            ArchitectureI architecture =
                    new Architecture(
                            ChargerCoupledModel.URI,
                            atomicModelDescriptors,
                            coupledModelDescriptors,
                            TimeUnit.HOURS);

            // -------------------------------
            // Construct simulator
            // -------------------------------
            SimulatorI se = architecture.constructSimulator();

            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;

            // -------------------------------
            // Run simulation
            // -------------------------------
            se.doStandAloneSimulation(0.0, 24.0);
            SimulationReportI sr = se.getSimulatedModel().getFinalReport();
            System.out.println(sr);

            System.exit(0);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
