package Connectors;

import java.io.Serializable;

import Interfaces.TumbleDryerUserCI;
import Interfaces.TumbleDryerUserI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class TDUserConector extends AbstractConnector implements TumbleDryerUserCI {

	@Override
	public void switchOn() throws Exception  {
		
		((TumbleDryerUserCI)this.offering).switchOn();
	}

	@Override
	public void switchOff() throws Exception {
		((TumbleDryerUserCI)this.offering).switchOff();
		
	}

	@Override
	public <T extends Serializable> void setMode(T mode) throws Exception {
		((TumbleDryerUserCI)this.offering).setMode(mode);
		
	}

	@Override
	public void play() throws Exception {
		((TumbleDryerUserCI)this.offering).play();
		
	}

	@Override
	public <T extends Serializable> void setOption(T option) throws Exception {
		
		((TumbleDryerUserCI)this.offering).setOption(option);

	}

	@Override
	public boolean on() throws Exception {
		
		return 	((TumbleDryerUserCI)this.offering).on();

	}

	@Override
	public String tcheckMode() throws Exception {
		
		return ((TumbleDryerUserCI)this.offering).tcheckMode();
	}

	@Override
	public String tcheckOption() throws Exception {
		
		return ((TumbleDryerUserCI)this.offering).tcheckOption();
	}

	
}
