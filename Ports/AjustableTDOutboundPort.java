package Ports;

import Connectors.AjustableTumbleDryerConnector;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.hem2025.bases.AdjustableCI;
import fr.sorbonne_u.components.hem2025.bases.RegistrationCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;

public class AjustableTDOutboundPort extends AbstractOutboundPort implements AdjustableCI{

	private static final long serialVersionUID = 1L;

	public AjustableTDOutboundPort( ComponentI owner) throws Exception {
		super(AdjustableCI.class, owner);
		
	}


	@Override
	public int			maxMode() throws Exception
	{
		int ret = ((AjustableTumbleDryerConnector)this.getConnector()).maxMode();
		assert ret > 0 : new PostconditionException("ret > 0");
		return ret;
	}

	@Override
	public boolean		upMode() throws Exception
	{
		assert	!suspended() : new PreconditionException("!suspended()");
		int oldMode = this.currentMode();
		assert	oldMode < this.maxMode() :
				new PreconditionException("oldMode < maxMode()");
		boolean ret = ((AjustableTumbleDryerConnector)this.getConnector()).upMode();
		assert	this.currentMode() > oldMode :
				new PostconditionException("currentMode() > oldMode");
		return ret;
	}

	@Override
	public boolean		downMode() throws Exception
	{
		assert	!suspended() : new PreconditionException("!suspended()");
		int oldMode = this.currentMode();
		assert	oldMode > 1 : new PreconditionException("oldMode > 1");
		boolean ret = ((AjustableTumbleDryerConnector)this.getConnector()).downMode();
		assert	this.currentMode() < oldMode :
				new PostconditionException("currentMode() < oldMode");
		return ret;
	}

	@Override
	public boolean		setMode(int modeIndex) throws Exception
	{
		assert	!suspended() : new PreconditionException("!suspended()");
		assert	modeIndex > 0 && modeIndex <= this.maxMode() :
				new PreconditionException(
						"modeIndex > 0 && modeIndex <= maxMode()");
		boolean ret = ((AjustableTumbleDryerConnector)this.getConnector()).setMode(modeIndex);
		assert	this.currentMode() == modeIndex :
				new PostconditionException("currentMode() == modeIndex");
		return ret;
	}
	@Override
	public int			currentMode() throws Exception
	{
		assert	!suspended() : new PreconditionException("!suspended()");
		int ret = ((AjustableTumbleDryerConnector)this.getConnector()).currentMode();
		assert	ret > 0 && ret <= this.maxMode() :
				new PostconditionException("return > 0 && return <= maxMode()");
		return ret;
	}

	@Override
	public double		getModeConsumption(int modeIndex) throws Exception
	{
		assert	modeIndex > 0 && modeIndex <= this.maxMode() :
				new PreconditionException(
						"modeIndex > 0 && modeIndex <= maxMode()");
		double ret = ((AjustableTumbleDryerConnector)this.getConnector()).
												getModeConsumption(modeIndex);
		assert	ret >= 0.0 : new PostconditionException("return >= 0.0");
		return ret;
	}

	@Override
	public boolean		suspended() throws Exception
	{
		return ((AjustableTumbleDryerConnector)this.getConnector()).suspended();
	}

	@Override
	public boolean		suspend() throws Exception
	{
		assert	!this.suspended() : new PreconditionException("!suspended()");
		boolean ret = ((AjustableTumbleDryerConnector)this.getConnector()).suspend();
		assert	!ret || this.suspended() :
				new PostconditionException("!return || suspended()");
		return ret;
	}

	@Override
	public boolean		resume() throws Exception
	{
		assert	this.suspended() : new PreconditionException("suspended()");
		boolean ret = ((AjustableTumbleDryerConnector)this.getConnector()).resume();
		assert	!ret || !this.suspended() :
				new PostconditionException("!return || !suspended()");
		return ret;
	}

	@Override
	public double		emergency() throws Exception
	{
		assert	this.suspended() : new PreconditionException("suspended()");
		double ret = ((AjustableTumbleDryerConnector)this.getConnector()).emergency();
		assert	ret >= 0.0 && ret <= 1.0 :
				new PostconditionException("return >= 0.0 && return <= 1.0");
		return ret;
	}
}
