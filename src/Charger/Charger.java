package Charger;


import Charger_connections.ChargerControlInboundPort;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.SignalData;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

@OfferedInterfaces(offered = { ChargerControlCI.class, ChargerUserCI.class, ChargerControlJava4CI.class })
public class Charger extends AbstractComponent implements ChargerControlI, ChargerUserI {

	public static final String CONTROL_INBOUND_PORT_URI = "SMARTCHARGER-CONTROL-INBOUND-URI";
	public static final String USER_INBOUND_PORT_URI = "SMARTCHARGER-USER-INBOUND-URI";
	public static final String CONTROL_JAVA4_INBOUND_PORT_URI = "SMARTCHARGER-CONTROL-JAVA4-INBOUND-URI";
	public static boolean VERBOSE = true;
	protected ChargerImplementation implementation;
	protected ChargerControlInboundPort controlPort;
	protected ChargerUserInboundPort userPort;

	protected Charger() throws Exception {
		super(1, 0);
		this.implementation = new ChargerImplementation();

		this.controlPort = new ChargerControlInboundPort(CONTROL_INBOUND_PORT_URI, this);
		this.userPort = new ChargerUserInboundPort(USER_INBOUND_PORT_URI, this);
		this.controlPort.publishPort();
		this.userPort.publishPort();

		this.tracer.get().setTitle("Composant Charger");
		this.tracer.get().setRelativePosition(1, 1);
		this.toggleTracing();
	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			if (this.controlPort.isPublished())
				this.controlPort.unpublishPort();
			if (this.userPort.isPublished())
				this.userPort.unpublishPort();

		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	@Override
	public boolean isCharging() throws Exception {
		return implementation.isCharging();
	}

	@Override
	public void startCharging() throws Exception {
		implementation.startCharging();
		new Thread(() -> {
			try {
				implementation.autoChargeCycle();
			} catch (Exception ignored) {
			}
		}).start();
	}

	@Override
	public void stopCharging() throws Exception {
		implementation.stopCharging();
	}

	@Override
	public Measure<Double> getMaxChargingPower() throws Exception {
		return implementation.getMaxChargingPower();
	}

	@Override
	public SignalData<Double> getChargeLevel() throws Exception {
		return implementation.getChargeLevel();
	}

	@Override
	public Measure<Double> getCurrentPower() throws Exception {
		return implementation.getCurrentPower();
	}

	@Override
	public void setTargetPower(Measure<Double> watts) throws Exception {
		implementation.setTargetPower(watts);
	}

	@Override
	public boolean isPluggedIn() throws Exception {
		return implementation.isPluggedIn();
	}

	@Override
	public void plugin() throws Exception {
		implementation.plugin();
	}

	@Override
	public void plugout() throws Exception {
		implementation.plugout();
	}
}
