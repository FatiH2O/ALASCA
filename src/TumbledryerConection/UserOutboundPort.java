package TumbledryerConection;

import java.io.Serializable;

import Tumbledryer.TumbleDryerUserCI;
import Tumbledryer.TumbleDryerUserI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class UserOutboundPort extends AbstractOutboundPort implements TumbleDryerUserCI{

	private static final long serialVersionUID = 1L;

	public UserOutboundPort(ComponentI owner) throws Exception {
		super( TumbleDryerUserCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void switchOn() {
		try {
			((TumbleDryerUserCI)this.getConnector()).switchOn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void switchOff() {

		try {
			((TumbleDryerUserCI)this.getConnector()).switchOff();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public <T extends Serializable> void setMode(T mode) {
		try {
			((TumbleDryerUserCI)this.getConnector()).setMode(mode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void play() {
		try {
			((TumbleDryerUserCI)this.getConnector()).play();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public <T extends Serializable> void setOption(T option) {
		try {
			((TumbleDryerUserCI)this.getConnector()).setOption(option);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public boolean on() {
		
		try {
			return 	((TumbleDryerUserCI)this.getConnector()).on();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
      return false;
	}

	@Override
	public String tcheckMode() {
		
		try {
			return ((TumbleDryerUserCI)this.getConnector()).tcheckMode();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error in user outbound port";
	}

	@Override
	public String tcheckOption() {
		try {
			return ((TumbleDryerUserCI)this.getConnector()).tcheckOption();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error in user outbound port";
	}
	

	

}
