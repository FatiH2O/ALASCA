package Ports;

import java.io.Serializable;


import Interfaces.TumbleDryerUserCI;
import Interfaces.TumbleDryerUserI;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;


public class UserInboundPort extends AbstractInboundPort implements TumbleDryerUserCI{

	

	private static final long serialVersionUID = 1L;

	public UserInboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri, TumbleDryerUserCI.class, owner);
		
		assert	owner instanceof TumbleDryerUserI :
			new PreconditionException("owner instanceof not TumbleDryerUserI");
			}
	
	

	@Override
	public void switchOn() throws Exception {
				
		this.getOwner().handleRequest(
				o -> {	((TumbleDryerUserI)o).switchOn();
						return null;
				});
	}

	@Override
	public void switchOff() throws Exception {
		
		this.getOwner().handleRequest(
				o -> {	((TumbleDryerUserI)o).switchOff();
						return null;
				});
		
	}

	@Override
	public <T extends Serializable> void setMode(T mode) throws Exception {
		
		this.getOwner().handleRequest(
				o -> {	((TumbleDryerUserI)o).setMode(mode);
						return null;
				});
		
	}

	@Override
	public  void play() throws Exception {
		
		this.getOwner().handleRequest(
				o -> {	((TumbleDryerUserI)o).play();
						return null;
				});
		
	}

	@Override
	public <T extends Serializable> void setOption(T option) throws Exception {
		
		this.getOwner().handleRequest(
				o -> {	((TumbleDryerUserI)o).setOption(option);
						return null;
				});
	}

	@Override
	public boolean on() throws Exception {

		return this.getOwner().handleRequest(o-> 
								((TumbleDryerUserI)o).on());

	}

	@Override
	public String tcheckMode() throws Exception {
		
		return this.getOwner().handleRequest(o-> 
								((TumbleDryerUserI)o).tcheckMode());
	}

	@Override
	public String tcheckOption() throws Exception {
		

		return this.getOwner().handleRequest(o-> 
								((TumbleDryerUserI)o).tcheckOption());
	}

	

}
