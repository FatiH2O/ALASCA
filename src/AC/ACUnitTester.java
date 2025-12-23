package AC;

import AC_connections.ACExternalControlConnector;

import AC_connections.ACExternalControlOutboundPort;
import AC_connections.ACInternalControlConnector;
import AC_connections.ACInternalControlOutboundPort;
import AC_connections.ACUserConnector;
import AC_connections.ACUserOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.hem2025.tests_utils.TestsStatistics;

@RequiredInterfaces(required = { ACUserCI.class, ACInternalControlCI.class, ACExternalControlCI.class })
public class ACUnitTester extends AbstractComponent {
	protected ACUserOutboundPort acUserPort;
	protected ACInternalControlOutboundPort acInternalControlPort;
	protected ACExternalControlOutboundPort acExternalControlPort;
	protected TestsStatistics statistics;

	protected ACUnitTester() throws Exception {
		super(3, 0);
		this.statistics = new TestsStatistics();
		this.getTracer().setTitle("Testeur de Climatiseur");
		this.getTracer().setRelativePosition(0, 2);
		this.toggleTracing();

	}

	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();
		try {
			this.acUserPort = new ACUserOutboundPort(this);
			this.acUserPort.publishPort();
			this.acInternalControlPort = new ACInternalControlOutboundPort(this);
			this.acInternalControlPort.publishPort();
			this.acExternalControlPort = new ACExternalControlOutboundPort(this);
			this.acExternalControlPort.publishPort();
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
	}

	@Override
	public synchronized void execute() throws Exception {

		this.logMessage("Début du test du composant AC...");
		try {
			this.doPortConnection(this.acUserPort.getPortURI(), AC.USER_INBOUND_PORT_URI,
					ACUserConnector.class.getCanonicalName());
			this.doPortConnection(this.acInternalControlPort.getPortURI(), AC.INTERNAL_CONTROL_INBOUND_PORT_URI,
					ACInternalControlConnector.class.getCanonicalName());
			this.doPortConnection(this.acExternalControlPort.getPortURI(), AC.EXTERNAL_CONTROL_INBOUND_PORT_URI,
					ACExternalControlConnector.class.getCanonicalName());

			this.runAllTests();

		} catch (Exception e) {
			this.logMessage("Le test a échoue avec une exception !");
			e.printStackTrace();
		}

		this.statistics.statisticsReport(this);
		this.logMessage("Fin du test du composant AC.");
	}

	protected void runAllTests() {
		this.logMessage("Feature: Allumer et éteindre le climatiseur");
		this.logMessage("  Scenario: Allumer puis éteindre");
		this.logMessage("    Given l'AC est éteint");
		try {
			if (!this.acUserPort.isOn()) {
				this.logMessage("    Then l'état est bien OFF (OK)");
			} else {
				this.logMessage("    Then l'état n'est pas OFF (ERREUR)");
				this.statistics.incorrectResult();
			}
			this.logMessage("    When on allume l'AC");
			this.acUserPort.switchOn();
			if (this.acUserPort.isOn()) {
				this.logMessage("    Then l'état est bien ON (OK)");
			} else {
				this.logMessage("    Then l'état n'est pas ON (ERREUR)");
				this.statistics.incorrectResult();
			}
		} catch (Exception e) {
			this.statistics.incorrectResult();
			e.printStackTrace();
		}
		this.statistics.updateStatistics();

		this.logMessage("Feature: Controler le refroidissement");
		this.logMessage("  Scenario: Démarrer et arréter le refroidissement");
		this.logMessage("    Given l'AC est allumé mais ne refroidit pas");
		try {
			if (this.acUserPort.isOn() && !this.acInternalControlPort.isCooling()) {
				this.logMessage("    Then l'état initial est correct (OK)");
			} else {
				this.logMessage("    Then l'état initial est incorrect (ERREUR)");
				this.statistics.incorrectResult();
			}
			this.logMessage("    When on démarre le refroidissement");
			this.acInternalControlPort.startCooling();
			if (this.acInternalControlPort.isCooling()) {
				this.logMessage("    Then l'AC est en mode COOLING (OK)");
			} else {
				this.logMessage("    Then l'AC n'est pas en mode COOLING (ERREUR)");
				this.statistics.incorrectResult();
			}
		} catch (Exception e) {
			this.statistics.incorrectResult();
			e.printStackTrace();
		}
		this.statistics.updateStatistics();

		// ---------------------------------------------------------------------

		try {
			this.acUserPort.switchOff();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.logMessage("Switching on AC for HEM tests");
		try {
			this.acUserPort.switchOn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void finalise() throws Exception {
		this.doPortDisconnection(this.acUserPort.getPortURI());
		this.doPortDisconnection(this.acInternalControlPort.getPortURI());
		this.doPortDisconnection(this.acExternalControlPort.getPortURI());
		super.finalise();
	}
}