package Tumbledryer;



import java.io.Serializable;



import java.net.URL;

import ConnectorsGenerator.RegistationCIConnector;
import Hem.HEM;
import TumbledryerConection.RegistrationCIOP;
import TumbledryerConection.TDEnergyManagerInboundPort;
import TumbledryerConection.UserInboundPort;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.hem2025.bases.RegistrationCI;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;


//programmable



@OfferedInterfaces(offered={TumbleDryerUserCI.class,TumbleDryerEnergyManagerCI.class})

@RequiredInterfaces(required = {RegistrationCI.class})

public class TumbleDryer extends AbstractComponent implements TumbleDryerUserI, TumbleDryerEnergyManagerCI{

	protected TumbleDryer() throws Exception {
		super(1,1);
		
		//init
		this.currentState = TumbleDryerState.OFF;
		this.choosedMode = TumbleDryerMode.NULL;
		this.choosedOption= TumbleOption.NULL;
		this.currentPowerLevel= PowerLevel.LEVEL0;
		
		//Ports pour se connecter avec le user
		this.UserIP= new UserInboundPort(USER_INBOUND_PORT_URI,this);
		this.UserIP.publishPort();
		
		
	}
	

	private TumbleDryerState currentState;
	
	private TumbleDryerMode choosedMode;
	
	private TumbleOption choosedOption;
		
	public PowerLevel currentPowerLevel;
	
	/** URI of the TumbleDryer inboundport for the HEM  control.						*/
	public static final String		HEM_INBOUND_PORT_URI =
									"HEM-CONTROL-INBOUND-PORT-URI";

	public static final String USER_INBOUND_PORT_URI =
									"USER-INBOUND-PORT-URI";
	
	/**inbound port pour les tests de l'iterface user                                   */
	protected UserInboundPort UserIP;
	/**RegistrationCI outboundport pour appeler la HEM                                  */
	protected RegistrationCIOP registrationOP;

		
	//Pour l'enregistrement
	protected TDEnergyManagerInboundPort EMinboundPort; 
	
	
	//numero de serie
	public static final String NUMSERIE = "TUMBLEDRYER";
	
	
	/**************All the different Power Levels*******/
	 
	private static enum PowerLevel{
		
		LEVEL1(50),
		
		LEVEL2(30),
		
		LEVEL3(10),
		
		LEVEL0(0);
		
		
		public Measure power;
		
		PowerLevel(double power) {
			
			this.power= new Measure<>(power,MeasurementUnit.WATTS);
		}
		
		
	}
	
	/*********************************************/

	/**************All the different modes*******/
	
   public static enum TumbleDryerMode{
		
		ANTI_ALLERGY(PowerLevel.LEVEL1),
		
		ECO(PowerLevel.LEVEL3),
		
		FAST_30C(PowerLevel.LEVEL3),
		
		TOWEL(PowerLevel.LEVEL1),
		
		SPORTS(PowerLevel.LEVEL2),
		
		BED_SHEET(PowerLevel.LEVEL2),
		
		COTTON(PowerLevel.LEVEL1),
		
		WOOL(PowerLevel.LEVEL3),
		
		NULL(PowerLevel.LEVEL3);
   
   
   public PowerLevel powerLevel;
   
 
	
    TumbleDryerMode(PowerLevel power){
    	
    	this.powerLevel= power;
   	
    }
   
   }


   /**************All the different states*******/
	private static enum TumbleDryerState{
		
		ON,
		
		DRYING,
		
		OFF,
		
		DELAYED_TO_LATER,
		
		BREAK
	}
	/*******************************************/

	
	
	/**************All the different options*******/

	public static enum TumbleOption{
		
		DELAYED_START,
		
		COOL_AT_THE_END,
		
		NULL
	}
	/*******************************************/

	
	
	

	/********************* Energergy Manager implementation***********/
	
	

	@Override
	public boolean timedShutdown() {
		
		//ajouter la gestion du temps avec les simulateurs à l'etape 2
		this.currentState= TumbleDryerState.BREAK;
		this.currentPowerLevel =  PowerLevel.LEVEL0;
		
		assert this.currentState == TumbleDryerState.BREAK;
		assert this.currentPowerLevel ==  PowerLevel.LEVEL0;
		
		return 	this.currentState == TumbleDryerState.BREAK;

		
	}

	@Override
	public boolean shutDown() {
		
		assert this.currentState == TumbleDryerState.OFF 
				:new PreconditionException("already off");

		this.choosedMode = TumbleDryerMode.NULL;
		this.currentPowerLevel = PowerLevel.LEVEL0;
		this.choosedOption= TumbleOption.NULL;
		this.currentState = TumbleDryerState.OFF;
		
		try {
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		return true;
	}

	
	@Override
	public boolean off() {
		
		return this.currentState == TumbleDryerState.OFF  ;
	}



	@Override
	public double getNeededPowerLevel(int modeIndex) {
	
		assert this.currentState != TumbleDryerState.OFF 
				: new PreconditionException("is OFF");
		
		if(modeIndex == 1 ) return (double) PowerLevel.LEVEL0.power.getData();
		if(modeIndex == 2 ) return (double) PowerLevel.LEVEL1.power.getData();
		if(modeIndex == 3 ) return (double) PowerLevel.LEVEL2.power.getData();
		if(modeIndex == 4 ) return (double) PowerLevel.LEVEL3.power.getData();
		
		return 0;
		
	}


	
	@Override
	public void setPowerLevel(int mode) {
		
		assert this.currentState != TumbleDryerState.OFF 
				: new PreconditionException("is OFF");
		
		if(mode == 1 ) this.currentPowerLevel=  PowerLevel.LEVEL0;
		if(mode == 2 ) this.currentPowerLevel=  PowerLevel.LEVEL1;
		if(mode == 3 ) this.currentPowerLevel=  PowerLevel.LEVEL2;
		if(mode == 4 ) this.currentPowerLevel=  PowerLevel.LEVEL3;

				   
		
		
	}

	@Override
	public void lowerLevel() {
		
		assert this.currentPowerLevel != PowerLevel.LEVEL0 :
			new PreconditionException("is already shut");
		
		
		if(this.currentPowerLevel == PowerLevel.LEVEL2 ) {
			
			this.currentPowerLevel = PowerLevel.LEVEL1;
			
		}else if(this.currentPowerLevel == PowerLevel.LEVEL3 ) {
			
				this.currentPowerLevel = PowerLevel.LEVEL2;
				
		}else if(this.currentPowerLevel == PowerLevel.LEVEL1) {
			
			this.currentState= TumbleDryerState.BREAK;

			this.currentPowerLevel = PowerLevel.LEVEL0;
			
		}
		
	}
	
	@Override
	public void upperLevel() {
		
		
		assert this.currentPowerLevel != PowerLevel.LEVEL3 :
			new PreconditionException("In the maxLevel");
		
		if(this.currentPowerLevel == PowerLevel.LEVEL0 ) {
			
			this.currentPowerLevel = PowerLevel.LEVEL1;
			this.currentState= TumbleDryerState.DRYING;

			
		}else if(this.currentPowerLevel == PowerLevel.LEVEL1 ) {
			
			this.currentPowerLevel = PowerLevel.LEVEL2;
			
		}else if(this.currentPowerLevel == PowerLevel.LEVEL2 ) {
			
				this.currentPowerLevel = PowerLevel.LEVEL3;
			
		}
		
		
	}


	
	/***************************** User implementation***************/
	

	@Override
	public void switchOn() throws Exception {
		
		
		assert	this.currentState != TumbleDryerState.ON 
				: new PreconditionException("!on");
		
		this.currentState= TumbleDryerState.ON;
		
		assert	 this.currentState == TumbleDryerState.ON 
				: new PostconditionException("on");
		
		//port pour se connecter avec HEM dynamiquement
		this.EMinboundPort= new TDEnergyManagerInboundPort(HEM_INBOUND_PORT_URI,this);
		this.EMinboundPort.publishPort();
		
		//s'enregistrer aupres du HEM
		this.registrationOP = new RegistrationCIOP(this);
		this.registrationOP.publishPort();
		this.doPortConnection(registrationOP.getPortURI(), HEM.REGISTRATION_IP,
				RegistationCIConnector.class.getCanonicalName());
		
		URL url = this.getClass().getResource("/xml/tumbledryer_descriptor.xml");
		
		this.registrationOP.register(NUMSERIE, HEM_INBOUND_PORT_URI, url.getPath());
		
	}

	@Override
	public void switchOff() throws Exception {
		
		assert	this.currentState != TumbleDryerState.OFF : new PreconditionException("!off");
		
		this.choosedOption= TumbleOption.NULL;
		this.choosedMode= TumbleDryerMode.NULL;
		this.currentState= TumbleDryerState.OFF;
		this.currentPowerLevel = PowerLevel.LEVEL0;
		
		
		assert	 this.currentState == TumbleDryerState.OFF : new PostconditionException("off");
		
		//se desenregistrer
		this.registrationOP.unregister(NUMSERIE);
		this.registrationOP.unpublishPort();
		this.registrationOP.destroyPort();
		
		//détruire le port de connection dynamique avec la hem
		this.EMinboundPort.unpublishPort();
		this.EMinboundPort.destroyPort();
		
	
		
	}
	
	
	@Override
	public <T extends Serializable> void setMode(T mode) {
	 
		assert	this.currentState == TumbleDryerState.ON : new PreconditionException("!on");		
		
		this.choosedMode = (TumbleDryerMode) mode;
		this.currentPowerLevel = ((TumbleDryerMode) mode).powerLevel;
		
		assert this.choosedMode == mode;
		
		
	}
	
	@Override
	public <T extends Serializable> void setOption(T option) {
		
		assert	this.currentState == TumbleDryerState.ON : new PreconditionException("!on");
		
		choosedOption = (TumbleOption) option ;
		
		assert this.choosedOption == option : new PreconditionException("option set successfully");
		
	}

	@Override
	public void play() {
		
		assert	this.currentState == TumbleDryerState.ON : new PreconditionException("!on");
		
		assert this.currentState != TumbleDryerState.BREAK ||
				this.currentState != TumbleDryerState.DELAYED_TO_LATER ||
				this.currentState!=TumbleDryerState.DRYING: new PreconditionException("wait until possible");
		
	
		//appeler l'EM pour voir s'il y a assez d'energie disponible?
		
		if(this.choosedOption == TumbleOption.DELAYED_START) {
			System.out.println("hi");
			this.currentState = TumbleDryerState.BREAK;
			}
		else {
		
		this.currentState = TumbleDryerState.DRYING;
		}
		
	}

	@Override
	public boolean on() {
		
		return this.currentState == TumbleDryerState.ON;
	}
	
	@Override
	public String tcheckMode() {
		
		return this.choosedMode.toString();
	}

	@Override
	public String tcheckOption() {
		
		return this.choosedOption.toString();
	}

	
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.UserIP.unpublishPort();
			this.UserIP.destroyPort();
				
			
		} catch (Throwable e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}



	

	

	

}
