package fr.sorbonne_u.components.hem2025e2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.time.Instant;
import java.util.ArrayList;
import fr.sorbonne_u.components.hem2025.tests_utils.SimulationTestStep;
import fr.sorbonne_u.components.hem2025.tests_utils.TestScenario;
import fr.sorbonne_u.components.hem2025e1.equipments.batteries.Batteries;
import fr.sorbonne_u.components.hem2025e1.equipments.generator.Generator;
import fr.sorbonne_u.components.hem2025e1.equipments.solar_panel.SolarPanel;
import fr.sorbonne_u.components.hem2025e2.GlobalCoupledModel.GlobalReport;
import fr.sorbonne_u.components.hem2025e2.equipments.batteries.mil.BatteriesPowerModel;
import fr.sorbonne_u.components.hem2025e2.equipments.batteries.mil.BatteriesSimulationConfiguration;
import fr.sorbonne_u.components.hem2025e2.equipments.batteries.mil.events.BatteriesRequiredPowerChanged;
import fr.sorbonne_u.components.hem2025e2.equipments.generator.mil.GeneratorFuelModel;
import fr.sorbonne_u.components.hem2025e2.equipments.generator.mil.GeneratorGlobalTesterModel;
import fr.sorbonne_u.components.hem2025e2.equipments.generator.mil.GeneratorPowerModel;
import fr.sorbonne_u.components.hem2025e2.equipments.generator.mil.GeneratorSimulationConfiguration;
import fr.sorbonne_u.components.hem2025e2.equipments.generator.mil.events.Refill;
import fr.sorbonne_u.components.hem2025e2.equipments.generator.mil.events.GeneratorRequiredPowerChanged;
import fr.sorbonne_u.components.hem2025e2.equipments.generator.mil.events.Start;
import fr.sorbonne_u.components.hem2025e2.equipments.generator.mil.events.Stop;
import fr.sorbonne_u.components.hem2025e2.equipments.generator.mil.events.TankEmpty;
import fr.sorbonne_u.components.hem2025e2.equipments.generator.mil.events.TankNoLongerEmpty;
import fr.sorbonne_u.components.hem2025e2.equipments.hairdryer.mil.HairDryerElectricityModel;
import fr.sorbonne_u.components.hem2025e2.equipments.hairdryer.mil.HairDryerSimpleUserModel;
import fr.sorbonne_u.components.hem2025e2.equipments.hairdryer.mil.events.SetHighHairDryer;
import fr.sorbonne_u.components.hem2025e2.equipments.hairdryer.mil.events.SetLowHairDryer;
import fr.sorbonne_u.components.hem2025e2.equipments.hairdryer.mil.events.SwitchOffHairDryer;
import fr.sorbonne_u.components.hem2025e2.equipments.hairdryer.mil.events.SwitchOnHairDryer;
import fr.sorbonne_u.components.hem2025e2.equipments.heater.mil.ExternalTemperatureModel;
import fr.sorbonne_u.components.hem2025e2.equipments.heater.mil.HeaterElectricityModel;
import fr.sorbonne_u.components.hem2025e2.equipments.heater.mil.HeaterTemperatureModel;
import fr.sorbonne_u.components.hem2025e2.equipments.heater.mil.HeaterUnitTesterModel;
import fr.sorbonne_u.components.hem2025e2.equipments.heater.mil.events.DoNotHeat;
import fr.sorbonne_u.components.hem2025e2.equipments.heater.mil.events.Heat;
import fr.sorbonne_u.components.hem2025e2.equipments.heater.mil.events.SetPowerHeater;
import fr.sorbonne_u.components.hem2025e2.equipments.heater.mil.events.SwitchOffHeater;
import fr.sorbonne_u.components.hem2025e2.equipments.heater.mil.events.SwitchOnHeater;
import fr.sorbonne_u.components.hem2025e2.equipments.heater.mil.events.SetPowerHeater.PowerValue;
import fr.sorbonne_u.components.hem2025e2.equipments.meter.mil.ElectricMeterElectricityModel;
import fr.sorbonne_u.components.hem2025e2.equipments.solar_panel.mil.AstronomicalSunRiseAndSetModel;
import fr.sorbonne_u.components.hem2025e2.equipments.solar_panel.mil.DeterministicSunIntensityModel;
import fr.sorbonne_u.components.hem2025e2.equipments.solar_panel.mil.DeterministicSunRiseAndSetModel;
import fr.sorbonne_u.components.hem2025e2.equipments.solar_panel.mil.SolarPanelPowerModel;
import fr.sorbonne_u.components.hem2025e2.equipments.solar_panel.mil.SolarPanelSimulationConfigurationI;
import fr.sorbonne_u.components.hem2025e2.equipments.solar_panel.mil.StochasticSunIntensityModel;
import fr.sorbonne_u.components.hem2025e2.equipments.solar_panel.mil.SunIntensityModelI;
import fr.sorbonne_u.components.hem2025e2.equipments.solar_panel.mil.SunRiseAndSetModelI;
import fr.sorbonne_u.components.hem2025e2.equipments.solar_panel.mil.events.SunriseEvent;
import fr.sorbonne_u.components.hem2025e2.equipments.solar_panel.mil.events.SunsetEvent;
// AC and Charger imports
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.ACElectricityModel;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.ACUnitTesterModel;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.ACSimulationConfigurationI;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SwitchOnAC;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SwitchOffAC;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SetPowerAC;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.ChargerElectricityModel;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.ChargerUnitTesterModel;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.ChargerSimulationConfigurationI;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SwitchOnCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SwitchOffCharger;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.ArchitectureI;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.CoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

// -----------------------------------------------------------------------------
/**
 * A global run driver that includes AC and Charger models in the architecture.
 */
public class RunGlobalSimulationACCharger {

    public static boolean staticInvariants() {
        boolean ret = true;
        ret &= GlobalSimulationConfigurationI.staticInvariants();
        return ret;
    }

    public static void main(String[] args) {
        staticInvariants();
        Time.setPrintPrecision(4);
        Duration.setPrintPrecision(4);

        try {
            Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

            // Hair dryer
            atomicModelDescriptors.put(
                    HairDryerElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            HairDryerElectricityModel.class,
                            HairDryerElectricityModel.URI,
                            GlobalSimulationConfigurationI.TIME_UNIT,
                            null));
            atomicModelDescriptors.put(
                    HairDryerSimpleUserModel.URI,
                    AtomicModelDescriptor.create(
                            HairDryerSimpleUserModel.class,
                            HairDryerSimpleUserModel.URI,
                            GlobalSimulationConfigurationI.TIME_UNIT,
                            null));

            // Heater
            atomicModelDescriptors.put(
                    HeaterElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            HeaterElectricityModel.class,
                            HeaterElectricityModel.URI,
                            GlobalSimulationConfigurationI.TIME_UNIT,
                            null));
            atomicModelDescriptors.put(
                    HeaterTemperatureModel.URI,
                    AtomicHIOA_Descriptor.create(
                            HeaterTemperatureModel.class,
                            HeaterTemperatureModel.URI,
                            GlobalSimulationConfigurationI.TIME_UNIT,
                            null));
            atomicModelDescriptors.put(
                    ExternalTemperatureModel.URI,
                    AtomicHIOA_Descriptor.create(
                            ExternalTemperatureModel.class,
                            ExternalTemperatureModel.URI,
                            GlobalSimulationConfigurationI.TIME_UNIT,
                            null));
            atomicModelDescriptors.put(
                    HeaterUnitTesterModel.URI,
                    AtomicModelDescriptor.create(
                            HeaterUnitTesterModel.class,
                            HeaterUnitTesterModel.URI,
                            GlobalSimulationConfigurationI.TIME_UNIT,
                            null));

            // Batteries
            atomicModelDescriptors.put(
                    BatteriesPowerModel.URI,
                    AtomicHIOA_Descriptor.create(
                            BatteriesPowerModel.class,
                            BatteriesPowerModel.URI,
                            BatteriesSimulationConfiguration.TIME_UNIT,
                            null));

            // Solar panel (same as in original)
            String sunRiseAndSetURI = null;
            if (SolarPanelSimulationConfigurationI.USE_ASTRONOMICAL_MODEL) {
                sunRiseAndSetURI = AstronomicalSunRiseAndSetModel.URI;
                atomicModelDescriptors.put(
                        AstronomicalSunRiseAndSetModel.URI,
                        AtomicModelDescriptor.create(
                                AstronomicalSunRiseAndSetModel.class,
                                AstronomicalSunRiseAndSetModel.URI,
                                SolarPanelSimulationConfigurationI.TIME_UNIT,
                                null));
            } else {
                sunRiseAndSetURI = DeterministicSunRiseAndSetModel.URI;
                atomicModelDescriptors.put(
                        DeterministicSunRiseAndSetModel.URI,
                        AtomicModelDescriptor.create(
                                DeterministicSunRiseAndSetModel.class,
                                DeterministicSunRiseAndSetModel.URI,
                                SolarPanelSimulationConfigurationI.TIME_UNIT,
                                null));
            }
            String sunIntensityModelURI = null;
            if (SolarPanelSimulationConfigurationI.USE_STOCHASTIC_SUN_INTENSITY_MODEL) {
                sunIntensityModelURI = StochasticSunIntensityModel.URI;
                atomicModelDescriptors.put(
                        StochasticSunIntensityModel.URI,
                        AtomicHIOA_Descriptor.create(
                                StochasticSunIntensityModel.class,
                                StochasticSunIntensityModel.URI,
                                SolarPanelSimulationConfigurationI.TIME_UNIT,
                                null));
            } else {
                sunIntensityModelURI = DeterministicSunIntensityModel.URI;
                atomicModelDescriptors.put(
                        DeterministicSunIntensityModel.URI,
                        AtomicHIOA_Descriptor.create(
                                DeterministicSunIntensityModel.class,
                                DeterministicSunIntensityModel.URI,
                                SolarPanelSimulationConfigurationI.TIME_UNIT,
                                null));
            }
            atomicModelDescriptors.put(
                    SolarPanelPowerModel.URI,
                    AtomicHIOA_Descriptor.create(
                            SolarPanelPowerModel.class,
                            SolarPanelPowerModel.URI,
                            SolarPanelSimulationConfigurationI.TIME_UNIT,
                            null));

            // Generator
            atomicModelDescriptors.put(
                    GeneratorFuelModel.URI,
                    AtomicHIOA_Descriptor.create(
                            GeneratorFuelModel.class,
                            GeneratorFuelModel.URI,
                            GeneratorSimulationConfiguration.TIME_UNIT,
                            null));
            atomicModelDescriptors.put(
                    GeneratorPowerModel.URI,
                    AtomicHIOA_Descriptor.create(
                            GeneratorPowerModel.class,
                            GeneratorPowerModel.URI,
                            GeneratorSimulationConfiguration.TIME_UNIT,
                            null));
            atomicModelDescriptors.put(
                    GeneratorGlobalTesterModel.URI,
                    AtomicModelDescriptor.create(
                            GeneratorGlobalTesterModel.class,
                            GeneratorGlobalTesterModel.URI,
                            GeneratorSimulationConfiguration.TIME_UNIT,
                            null));

            // Electric meter
            atomicModelDescriptors.put(
                    ElectricMeterElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            ElectricMeterElectricityModel.class,
                            ElectricMeterElectricityModel.URI,
                            GlobalSimulationConfigurationI.TIME_UNIT,
                            null));

            // -----------------------------------------------------------------
            // AC and Charger models
            // -----------------------------------------------------------------
            atomicModelDescriptors.put(
                    ACElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            ACElectricityModel.class,
                            ACElectricityModel.URI,
                            ACSimulationConfigurationI.TIME_UNIT,
                            null));
            atomicModelDescriptors.put(
                    ACUnitTesterModel.URI,
                    AtomicModelDescriptor.create(
                            ACUnitTesterModel.class,
                            ACUnitTesterModel.URI,
                            ACSimulationConfigurationI.TIME_UNIT,
                            null));

            atomicModelDescriptors.put(
                    ChargerElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            ChargerElectricityModel.class,
                            ChargerElectricityModel.URI,
                            ChargerSimulationConfigurationI.TIME_UNIT,
                            null));
            atomicModelDescriptors.put(
                    ChargerUnitTesterModel.URI,
                    AtomicModelDescriptor.create(
                            ChargerUnitTesterModel.class,
                            ChargerUnitTesterModel.URI,
                            ChargerSimulationConfigurationI.TIME_UNIT,
                            null));

            // -----------------------------------------------------------------
            // Global coupled model
            // -----------------------------------------------------------------
            Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

            Set<String> submodels = new HashSet<String>();
            submodels.add(HairDryerElectricityModel.URI);
            submodels.add(HairDryerSimpleUserModel.URI);
            submodels.add(HeaterElectricityModel.URI);
            submodels.add(HeaterTemperatureModel.URI);
            submodels.add(ExternalTemperatureModel.URI);
            submodels.add(HeaterUnitTesterModel.URI);
            submodels.add(BatteriesPowerModel.URI);
            submodels.add(sunRiseAndSetURI);
            submodels.add(sunIntensityModelURI);
            submodels.add(SolarPanelPowerModel.URI);
            submodels.add(GeneratorFuelModel.URI);
            submodels.add(GeneratorPowerModel.URI);
            submodels.add(GeneratorGlobalTesterModel.URI);
            submodels.add(ElectricMeterElectricityModel.URI);
            // add AC and charger
            submodels.add(ACElectricityModel.URI);
            submodels.add(ACUnitTesterModel.URI);
            submodels.add(ChargerElectricityModel.URI);
            submodels.add(ChargerUnitTesterModel.URI);

            Map<EventSource, EventSink[]> connections = new HashMap<EventSource, EventSink[]>();

            // Hair dryer connections (copied)
            connections.put(
                    new EventSource(HairDryerSimpleUserModel.URI, SwitchOnHairDryer.class),
                    new EventSink[] {new EventSink(HairDryerElectricityModel.URI, SwitchOnHairDryer.class)});
            connections.put(
                    new EventSource(HairDryerSimpleUserModel.URI, SwitchOffHairDryer.class),
                    new EventSink[] {new EventSink(HairDryerElectricityModel.URI, SwitchOffHairDryer.class)});
            connections.put(
                    new EventSource(HairDryerSimpleUserModel.URI, SetHighHairDryer.class),
                    new EventSink[] {new EventSink(HairDryerElectricityModel.URI, SetHighHairDryer.class)});
            connections.put(
                    new EventSource(HairDryerSimpleUserModel.URI, SetLowHairDryer.class),
                    new EventSink[] {new EventSink(HairDryerElectricityModel.URI, SetLowHairDryer.class)});

            // Heater events (copied)
            connections.put(
                    new EventSource(HeaterUnitTesterModel.URI, SetPowerHeater.class),
                    new EventSink[] {new EventSink(HeaterElectricityModel.URI, SetPowerHeater.class)});
            connections.put(
                    new EventSource(HeaterUnitTesterModel.URI, SwitchOnHeater.class),
                    new EventSink[] {new EventSink(HeaterElectricityModel.URI, SwitchOnHeater.class)});
            connections.put(
                    new EventSource(HeaterUnitTesterModel.URI, SwitchOffHeater.class),
                    new EventSink[] {new EventSink(HeaterElectricityModel.URI, SwitchOffHeater.class),
                            new EventSink(HeaterTemperatureModel.URI, SwitchOffHeater.class)});
            connections.put(
                    new EventSource(HeaterUnitTesterModel.URI, Heat.class),
                    new EventSink[] {new EventSink(HeaterElectricityModel.URI, Heat.class),
                            new EventSink(HeaterTemperatureModel.URI, Heat.class)});
            connections.put(
                    new EventSource(HeaterUnitTesterModel.URI, DoNotHeat.class),
                    new EventSink[] {new EventSink(HeaterElectricityModel.URI, DoNotHeat.class),
                            new EventSink(HeaterTemperatureModel.URI, DoNotHeat.class)});

            // Batteries events (copied)
            connections.put(
                    new EventSource(ElectricMeterElectricityModel.URI, BatteriesRequiredPowerChanged.class),
                    new EventSink[] {new EventSink(BatteriesPowerModel.URI, BatteriesRequiredPowerChanged.class)});

            // Solar panel events (copied)
            connections.put(
                    new EventSource(sunRiseAndSetURI, SunriseEvent.class),
                    new EventSink[] {new EventSink(sunIntensityModelURI, SunriseEvent.class),
                            new EventSink(SolarPanelPowerModel.URI, SunriseEvent.class)});
            connections.put(
                    new EventSource(sunRiseAndSetURI, SunsetEvent.class),
                    new EventSink[] {new EventSink(sunIntensityModelURI, SunsetEvent.class),
                            new EventSink(SolarPanelPowerModel.URI, SunsetEvent.class)});

            // Generator events (copied)
            connections.put(
                    new EventSource(GeneratorGlobalTesterModel.URI, Start.class),
                    new EventSink[] {new EventSink(GeneratorFuelModel.URI, Start.class),
                            new EventSink(GeneratorPowerModel.URI, Start.class)});
            connections.put(
                    new EventSource(GeneratorGlobalTesterModel.URI, Stop.class),
                    new EventSink[] {new EventSink(GeneratorFuelModel.URI, Stop.class),
                            new EventSink(GeneratorPowerModel.URI, Stop.class)});
            connections.put(
                    new EventSource(GeneratorGlobalTesterModel.URI, Refill.class),
                    new EventSink[] {new EventSink(GeneratorFuelModel.URI, Refill.class)});
            connections.put(
                    new EventSource(ElectricMeterElectricityModel.URI, GeneratorRequiredPowerChanged.class),
                    new EventSink[] {new EventSink(GeneratorPowerModel.URI, GeneratorRequiredPowerChanged.class)});
            connections.put(
                    new EventSource(GeneratorFuelModel.URI, TankEmpty.class),
                    new EventSink[] {new EventSink(GeneratorPowerModel.URI, TankEmpty.class)});
            connections.put(
                    new EventSource(GeneratorFuelModel.URI, TankNoLongerEmpty.class),
                    new EventSink[] {new EventSink(GeneratorPowerModel.URI, TankNoLongerEmpty.class)});
            connections.put(
                    new EventSource(GeneratorPowerModel.URI, GeneratorRequiredPowerChanged.class),
                    new EventSink[] {new EventSink(GeneratorFuelModel.URI, GeneratorRequiredPowerChanged.class)});

            // -----------------------------------------------------------------
            // AC events
            // -----------------------------------------------------------------
            connections.put(
                    new EventSource(ACUnitTesterModel.URI, SetPowerAC.class),
                    new EventSink[] {new EventSink(ACElectricityModel.URI, SetPowerAC.class)});
            connections.put(
                    new EventSource(ACUnitTesterModel.URI, SwitchOnAC.class),
                    new EventSink[] {new EventSink(ACElectricityModel.URI, SwitchOnAC.class)});
            connections.put(
                    new EventSource(ACUnitTesterModel.URI, SwitchOffAC.class),
                    new EventSink[] {new EventSink(ACElectricityModel.URI, SwitchOffAC.class)});

            // -----------------------------------------------------------------
            // Charger events
            // -----------------------------------------------------------------
            connections.put(
                    new EventSource(ChargerUnitTesterModel.URI, SwitchOnCharger.class),
                    new EventSink[] {new EventSink(ChargerElectricityModel.URI, SwitchOnCharger.class)});
            connections.put(
                    new EventSource(ChargerUnitTesterModel.URI, SwitchOffCharger.class),
                    new EventSink[] {new EventSink(ChargerElectricityModel.URI, SwitchOffCharger.class)});

            // -----------------------------------------------------------------
            // Variable bindings
            // -----------------------------------------------------------------
            Map<VariableSource, VariableSink[]> bindings = new HashMap<VariableSource, VariableSink[]>();

            // Heater bindings
            bindings.put(
                    new VariableSource("externalTemperature", Double.class, ExternalTemperatureModel.URI),
                    new VariableSink[] {new VariableSink("externalTemperature", Double.class, HeaterTemperatureModel.URI)});
            bindings.put(
                    new VariableSource("currentHeatingPower", Double.class, HeaterElectricityModel.URI),
                    new VariableSink[] {new VariableSink("currentHeatingPower", Double.class, HeaterTemperatureModel.URI)});

            // Solar panel bindings
            bindings.put(
                    new VariableSource("sunIntensityCoef", Double.class, sunIntensityModelURI),
                    new VariableSink[] {new VariableSink("sunIntensityCoef", Double.class, SolarPanelPowerModel.URI)});

            // Generator bindings
            bindings.put(
                    new VariableSource("generatorOutputPower", Double.class, GeneratorPowerModel.URI),
                    new VariableSink[] {new VariableSink("generatorOutputPower", Double.class, ElectricMeterElectricityModel.URI),
                            new VariableSink("generatorOutputPower", Double.class, GeneratorFuelModel.URI)});
            bindings.put(
                    new VariableSource("generatorRequiredPower", Double.class, ElectricMeterElectricityModel.URI),
                    new VariableSink[] {new VariableSink("generatorRequiredPower", Double.class, GeneratorPowerModel.URI)});

            // Batteries <-> meter
            bindings.put(
                    new VariableSource("batteriesOutputPower", Double.class, BatteriesPowerModel.URI),
                    new VariableSink[] {new VariableSink("batteriesOutputPower", Double.class, ElectricMeterElectricityModel.URI)});
            bindings.put(
                    new VariableSource("batteriesInputPower", Double.class, BatteriesPowerModel.URI),
                    new VariableSink[] {new VariableSink("batteriesInputPower", Double.class, ElectricMeterElectricityModel.URI)});
            bindings.put(
                    new VariableSource("batteriesRequiredPower", Double.class, ElectricMeterElectricityModel.URI),
                    new VariableSink[] {new VariableSink("batteriesRequiredPower", Double.class, BatteriesPowerModel.URI)});

            // Solar panel output -> meter
            bindings.put(
                    new VariableSource("solarPanelOutputPower", Double.class, SolarPanelPowerModel.URI),
                    new VariableSink[] {new VariableSink("solarPanelOutputPower", Double.class, ElectricMeterElectricityModel.URI)});

            // Hair dryer intensity -> meter
            bindings.put(
                    new VariableSource("currentIntensity", Double.class, HairDryerElectricityModel.URI),
                    new VariableSink[] {new VariableSink("currentIntensity", Double.class, "currentHairDryerIntensity", Double.class, ElectricMeterElectricityModel.URI)});

            // Heater intensity -> meter
            bindings.put(
                    new VariableSource("currentIntensity", Double.class, HeaterElectricityModel.URI),
                    new VariableSink[] {new VariableSink("currentIntensity", Double.class, "currentHeaterIntensity", Double.class, ElectricMeterElectricityModel.URI)});

            // AC intensity -> meter
            bindings.put(
                    new VariableSource("currentIntensity", Double.class, ACElectricityModel.URI),
                    new VariableSink[] {new VariableSink("currentIntensity", Double.class, "currentACIntensity", Double.class, ElectricMeterElectricityModel.URI)});

            // Charger intensity -> meter
            bindings.put(
                    new VariableSource("currentIntensity", Double.class, ChargerElectricityModel.URI),
                    new VariableSink[] {new VariableSink("currentIntensity", Double.class, "currentChargerIntensity", Double.class, ElectricMeterElectricityModel.URI)});

            // coupled model descriptor
            coupledModelDescriptors.put(
                    GlobalCoupledModel.URI,
                    new CoupledHIOA_Descriptor(
                            GlobalCoupledModel.class,
                            GlobalCoupledModel.URI,
                            submodels,
                            null,
                            null,
                            connections,
                            null,
                            null,
                            null,
                            bindings));

            ArchitectureI architecture = new Architecture(
                    GlobalCoupledModel.URI,
                    atomicModelDescriptors,
                    coupledModelDescriptors,
                    GlobalSimulationConfigurationI.TIME_UNIT);

            SimulatorI se = architecture.constructSimulator();

            Map<String, Object> simParams = new HashMap<>();

            // hair dryer params
            simParams.put(ModelI.createRunParameterName(HairDryerElectricityModel.URI, HairDryerElectricityModel.LOW_MODE_CONSUMPTION_RPNAME), 660.0);
            simParams.put(ModelI.createRunParameterName(HairDryerElectricityModel.URI, HairDryerElectricityModel.HIGH_MODE_CONSUMPTION_RPNAME), 1320.0);
            simParams.put(ModelI.createRunParameterName(HairDryerSimpleUserModel.URI, HairDryerSimpleUserModel.MEAN_STEP_RPNAME), 0.05);
            simParams.put(ModelI.createRunParameterName(HairDryerSimpleUserModel.URI, HairDryerSimpleUserModel.MEAN_DELAY_RPNAME), 2.0);

            // solar params
            simParams.put(ModelI.createRunParameterName(sunRiseAndSetURI, SunRiseAndSetModelI.LATITUDE_RP_NAME), SolarPanelSimulationConfigurationI.LATITUDE);
            simParams.put(ModelI.createRunParameterName(sunRiseAndSetURI, SunRiseAndSetModelI.LONGITUDE_RP_NAME), SolarPanelSimulationConfigurationI.LONGITUDE);
            simParams.put(ModelI.createRunParameterName(sunRiseAndSetURI, SunRiseAndSetModelI.START_INSTANT_RP_NAME), GlobalSimulationConfigurationI.START_INSTANT);
            simParams.put(ModelI.createRunParameterName(sunRiseAndSetURI, SunRiseAndSetModelI.ZONE_ID_RP_NAME), SolarPanelSimulationConfigurationI.ZONE);

            simParams.put(ModelI.createRunParameterName(sunIntensityModelURI, SunIntensityModelI.LATITUDE_RP_NAME), SolarPanelSimulationConfigurationI.LATITUDE);
            simParams.put(ModelI.createRunParameterName(sunIntensityModelURI, SunIntensityModelI.LONGITUDE_RP_NAME), SolarPanelSimulationConfigurationI.LONGITUDE);
            simParams.put(ModelI.createRunParameterName(sunIntensityModelURI, SunIntensityModelI.START_INSTANT_RP_NAME), GlobalSimulationConfigurationI.START_INSTANT);
            simParams.put(ModelI.createRunParameterName(sunIntensityModelURI, SunIntensityModelI.ZONE_ID_RP_NAME), SolarPanelSimulationConfigurationI.ZONE);
            simParams.put(ModelI.createRunParameterName(sunIntensityModelURI, SunIntensityModelI.SLOPE_RP_NAME), SolarPanelSimulationConfigurationI.SLOPE);
            simParams.put(ModelI.createRunParameterName(sunIntensityModelURI, SunIntensityModelI.ORIENTATION_RP_NAME), SolarPanelSimulationConfigurationI.ORIENTATION);
            simParams.put(ModelI.createRunParameterName(sunIntensityModelURI, SunIntensityModelI.COMPUTATION_STEP_RP_NAME), 0.5);

            simParams.put(ModelI.createRunParameterName(SolarPanelPowerModel.URI, SolarPanelPowerModel.LATITUDE_RP_NAME), SolarPanelSimulationConfigurationI.LATITUDE);
            simParams.put(ModelI.createRunParameterName(SolarPanelPowerModel.URI, SolarPanelPowerModel.LONGITUDE_RP_NAME), SolarPanelSimulationConfigurationI.LONGITUDE);
            simParams.put(ModelI.createRunParameterName(SolarPanelPowerModel.URI, SolarPanelPowerModel.START_INSTANT_RP_NAME), GlobalSimulationConfigurationI.START_INSTANT);
            simParams.put(ModelI.createRunParameterName(SolarPanelPowerModel.URI, SolarPanelPowerModel.ZONE_ID_RP_NAME), SolarPanelSimulationConfigurationI.ZONE);
            simParams.put(ModelI.createRunParameterName(SolarPanelPowerModel.URI, SolarPanelPowerModel.MAX_POWER_RP_NAME), SolarPanelSimulationConfigurationI.NB_SQUARE_METERS * SolarPanel.CAPACITY_PER_SQUARE_METER.getData());
            simParams.put(ModelI.createRunParameterName(SolarPanelPowerModel.URI, SolarPanelPowerModel.COMPUTATION_STEP_RP_NAME), 0.25);

            // generator params (copied)
            simParams.put(ModelI.createRunParameterName(GeneratorFuelModel.URI, GeneratorFuelModel.CAPACITY_RP_NAME), GeneratorSimulationConfiguration.TANK_CAPACITY);
            simParams.put(ModelI.createRunParameterName(GeneratorFuelModel.URI, GeneratorFuelModel.INITIAL_LEVEL_RP_NAME), GeneratorSimulationConfiguration.INITIAL_TANK_LEVEL);
            simParams.put(ModelI.createRunParameterName(GeneratorFuelModel.URI, GeneratorFuelModel.MIN_FUEL_CONSUMPTION_RP_NAME), Generator.MIN_FUEL_CONSUMPTION.getData());
            simParams.put(ModelI.createRunParameterName(GeneratorFuelModel.URI, GeneratorFuelModel.MAX_FUEL_CONSUMPTION_RP_NAME), Generator.MAX_FUEL_CONSUMPTION.getData());
            simParams.put(ModelI.createRunParameterName(GeneratorFuelModel.URI, GeneratorFuelModel.LEVEL_QUANTUM_RP_NAME), GeneratorSimulationConfiguration.STANDARD_LEVEL_INTEGRATION_QUANTUM);
            simParams.put(ModelI.createRunParameterName(GeneratorFuelModel.URI, GeneratorFuelModel.MAX_OUT_POWER_RP_NAME), Generator.MAX_POWER.getData());
            simParams.put(ModelI.createRunParameterName(GeneratorPowerModel.URI, GeneratorPowerModel.MAX_OUT_POWER_RP_NAME), Generator.MAX_POWER.getData());
            simParams.put(ModelI.createRunParameterName(GeneratorGlobalTesterModel.URI, GeneratorGlobalTesterModel.TEST_SCENARIO_RP_NAME), null);

            // set tester scenarios for Heater, AC and Charger if supported
            simParams.put(ModelI.createRunParameterName(HeaterUnitTesterModel.URI, HeaterUnitTesterModel.TEST_SCENARIO_RP_NAME), null);
            simParams.put(ModelI.createRunParameterName(ACUnitTesterModel.URI, ACUnitTesterModel.TEST_SCENARIO_RP_NAME), null);
            simParams.put(ModelI.createRunParameterName(ChargerUnitTesterModel.URI, ChargerUnitTesterModel.TEST_SCENARIO_RP_NAME), null);

            // Tracing configuration
            HairDryerElectricityModel.VERBOSE = false;
            HairDryerElectricityModel.DEBUG = false;
            HairDryerSimpleUserModel.VERBOSE = false;
            HairDryerSimpleUserModel.DEBUG = false;

            HeaterElectricityModel.VERBOSE = false;
            HeaterElectricityModel.DEBUG = false;
            HeaterTemperatureModel.VERBOSE = false;
            HeaterTemperatureModel.DEBUG = false;
            ExternalTemperatureModel.VERBOSE = false;
            ExternalTemperatureModel.DEBUG = false;
            HeaterUnitTesterModel.VERBOSE = false;
            HeaterUnitTesterModel.DEBUG = false;

            BatteriesPowerModel.VERBOSE = true;
            BatteriesPowerModel.DEBUG = false;

            if (SolarPanelSimulationConfigurationI.USE_ASTRONOMICAL_MODEL) {
                AstronomicalSunRiseAndSetModel.VERBOSE = false;
                AstronomicalSunRiseAndSetModel.DEBUG = false;
            } else {
                DeterministicSunRiseAndSetModel.VERBOSE = false;
                DeterministicSunRiseAndSetModel.DEBUG = false;
            }
            if (SolarPanelSimulationConfigurationI.USE_STOCHASTIC_SUN_INTENSITY_MODEL) {
                StochasticSunIntensityModel.VERBOSE = false;
                StochasticSunIntensityModel.DEBUG = false;
            } else {
                DeterministicSunIntensityModel.VERBOSE = false;
                DeterministicSunIntensityModel.DEBUG = false;
            }
            SolarPanelPowerModel.VERBOSE = false;
            SolarPanelPowerModel.DEBUG = false;

            GeneratorFuelModel.VERBOSE = false;
            GeneratorFuelModel.DEBUG = false;
            GeneratorPowerModel.VERBOSE = false;
            GeneratorPowerModel.DEBUG = false;
            GeneratorGlobalTesterModel.VERBOSE = false;
            GeneratorGlobalTesterModel.DEBUG = false;

            ElectricMeterElectricityModel.VERBOSE = true;
            ElectricMeterElectricityModel.DEBUG = false;

            

            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;

            // Test scenario: reuse CLASSICAL (same Gherkin text)
            CLASSICAL.setUpSimulator(se, simParams);
            Time startTime = CLASSICAL.getStartTime();
            Duration d = CLASSICAL.getEndTime().subtract(startTime);
            se.doStandAloneSimulation(startTime.getSimulatedTime(), d.getSimulatedDuration());

            GlobalReport r = (GlobalReport) se.getFinalReport();
            System.out.println(r.printout(""));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // reuse the CLASSICAL scenario from RunGlobalSimulation
    protected static TestScenario CLASSICAL = RunGlobalSimulation.CLASSICAL;
}
