package Tumbledryer;

import java.io.Serializable;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

//services offert au user
public interface TumbleDryerUserI {

	
	public void switchOn() throws Exception;
	
	public void switchOff() throws Exception;
	
	public <T extends Serializable> void setMode(T mode) throws Exception;
	
	public void play() throws Exception;
	
	public <T extends Serializable> void setOption(T option) throws Exception;
	
	public boolean on() throws Exception;
	
	public String tcheckMode()throws Exception;
	
	public String tcheckOption() throws Exception;
	
	
	
	
}
