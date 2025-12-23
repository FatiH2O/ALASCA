package Tumbledryer;

import java.io.Serializable;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface TumbleDryerUserCI extends TumbleDryerUserI,RequiredCI,OfferedCI{
	
	/**
	 * interface qui permet de connecter les port car les composants
	 *  ne doivent pas implémenter directement les interfaces offert/requise	
	 *                                                                         */

	@Override
	public void switchOn() throws Exception;
	
	@Override
	public void switchOff() throws Exception;
	
	@Override
	public <T extends Serializable> void setMode(T mode) throws Exception;
	
	@Override
	public void play() throws Exception;
	
	@Override
	public <T extends Serializable> void setOption(T option) throws Exception;
	
	@Override
	public boolean on() throws Exception;
	
	@Override
	public String tcheckMode()throws Exception;
	
	@Override
	public String tcheckOption() throws Exception;
}
