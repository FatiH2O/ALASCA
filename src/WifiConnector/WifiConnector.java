package WifiConnector;

import Wifi.BoxWifiImplementationI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2025.bases.AdjustableCI;
import fr.sorbonne_u.exceptions.PreconditionException;

public class WifiConnector extends AbstractConnector implements AdjustableCI {

	// Définition des modes
	public static final int MAX_MODE = 2;
	protected int currentMode = 0;
	protected boolean isSuspended = false;

	// Obtenir l'interface offering pour AdjustableCI
	protected AdjustableCI getAdjustableOffering() throws Exception {
		return (AdjustableCI) this.offering;
	}

	// Obtenir l'interface offering pour BoxWifiImplementationI
	protected BoxWifiImplementationI getBoxWifiOffering() throws Exception {
		return (BoxWifiImplementationI) this.offering;
	}

	@Override
	public int maxMode() throws Exception {
		return MAX_MODE;
	}

	@Override
	public boolean upMode() throws Exception {
		assert !this.suspended() : new PreconditionException("!suspended()");
		assert currentMode < MAX_MODE : new PreconditionException("currentMode < MAX_MODE");

		if (currentMode < MAX_MODE) {
			currentMode++;
			applyMode(currentMode);
			return true;
		}
		return false;
	}

	@Override
	public boolean downMode() throws Exception {
		assert !this.suspended() : new PreconditionException("!suspended()");
		assert currentMode > 0 : new PreconditionException("currentMode > 0");

		if (currentMode > 0) {
			currentMode--;
			applyMode(currentMode);
			return true;
		}
		return false;
	}

	@Override
	public boolean setMode(int modeIndex) throws Exception {
		assert !this.suspended() : new PreconditionException("!suspended()");
		assert modeIndex >= 0 && modeIndex <= MAX_MODE : new PreconditionException("modeIndex valide");

		if (modeIndex >= 0 && modeIndex <= MAX_MODE) {
			currentMode = modeIndex;
			applyMode(currentMode);
			return true;
		}
		return false;
	}

	@Override
	public int currentMode() throws Exception {
		return this.suspended() ? 0 : this.currentMode;
	}

	@Override
	public double getModeConsumption(int modeIndex) throws Exception {
		assert modeIndex >= 0 && modeIndex <= MAX_MODE : new PreconditionException("modeIndex valide");
		return computePowerLevel(modeIndex);
	}

	@Override
	public boolean suspended() throws Exception {
		return isSuspended;
	}

	@Override
	public boolean suspend() throws Exception {
		assert !this.suspended() : new PreconditionException("!suspended()");
		// Pour suspendre, on met le mode à 0 (éteint)
		getBoxWifiOffering().turnOff();
		isSuspended = true;
		return true;
	}

	@Override
	public boolean resume() throws Exception {
		assert this.suspended() : new PreconditionException("suspended()");
		applyMode(currentMode);
		isSuspended = false;
		return true;
	}

	@Override
	public double emergency() throws Exception {
		// Dans cette implémentation simplifiée, on retourne une valeur fixe
		// ou on pourrait calculer basé sur le mode courant
		switch (currentMode) {
		case 0:
			return 0.0; // OFF
		case 1:
			return 0.3; // BOX_ONLY
		case 2:
			return 0.8; // FULL_ON
		default:
			return 0.0;
		}
	}

	protected double computePowerLevel(int modeIndex) throws Exception {
		assert modeIndex >= 0 && modeIndex <= MAX_MODE : new PreconditionException("modeIndex valide");
		// On utilise les valeurs de puissance définies dans BoxWifiImplementationI
		switch (modeIndex) {
		case 0:
			return 0.0; // OFF
		case 1:
			return 8.0; // BOX_ONLY
		case 2:
			return 12.0; // FULL_ON
		default:
			return 0.0;
		}
	}

	protected void applyMode(int modeIndex) throws Exception {
		// Mettre à jour l'état physique du WiFi selon le mode
		switch (modeIndex) {
		case 0: // OFF
			getBoxWifiOffering().turnOff();
			break;
		case 1: // BOX_ONLY
			getBoxWifiOffering().turnOn();
			getBoxWifiOffering().deactivateWifi();
			break;

		case 2: // FULL_ON
			getBoxWifiOffering().turnOn();
			getBoxWifiOffering().activateWifi();
			break;
		}

		// Mettre à jour le mode courant
		currentMode = modeIndex;
	}
}