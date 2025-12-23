package Tumbledryer;



import Tumbledryer.TumbleDryer.TumbleDryerMode;
import Tumbledryer.TumbleDryer.TumbleOption;
import TumbledryerConection.TDUserConector;
import TumbledryerConection.UserOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.hem2025.tests_utils.TestsStatistics;



@RequiredInterfaces(required={TumbleDryerUserCI.class})


public class TumbleDryerUnitTest extends	AbstractComponent{


	protected TumbleDryerUnitTest(String UserInboundPortURI) throws Exception {
		super(1, 1);
		
		this.UserInboundPortURI= UserInboundPortURI;
		
		
		this.tracer.get().setTitle("TumbleDryer Unittester component");
		this.toggleTracing();
		this.statistics = new TestsStatistics();
			}

		
	/**URI of user interface iP                                                  */
	protected String UserInboundPortURI;
	/**userCI outboundport                                                  */
	protected UserOutboundPort UserOP;
	
	/** collector of test statistics.										*/
	protected TestsStatistics	statistics;
	
	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	
	
	
	/** Testing the state of the TumbleDryer when off
	 * Given the heater hasnt been used yet, its state should be off
	 */
	
	protected void		testOff()
	{
		this.logMessage("                                      ");

		this.logMessage("  Scenario: getting the state of the TumbleDryer when off");
		this.logMessage("   Given the TumbleDryer has not been used yet");
		try {
			this.logMessage("    When I test the state of the TumbleDryer");
			boolean result = this.UserOP.on();
			if (!result) {
				this.logMessage("    Then the state of the heater is off");
			} else {
				this.logMessage("     but was: on");
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();
	}
	
	
	
	
	
	/**
	 * Testing if switchon() and switchoff() function correctly
	 * tchecking the state of the tumble dryer after switchon(): 
	 * should be ON
	 * 
	 *Then tchecking the state of the tumble dryer after switchoff(): 
	 * should be off  */
	
	protected void		testSwitchOnSwitchOff()
	{
		this.logMessage("                                      ");

		this.logMessage("Feature: switching on and off the heater");

		this.logMessage("  Scenario: switching on the heater when off");
		this.logMessage("    And the heater has not been used yet");
		boolean result;
		try {
			this.logMessage("    When I switch on the heater");
			this.UserOP.switchOn();
			result = this.UserOP.on();
			if (result) {
				this.logMessage("    Then the state of the heater is ON");
			} else {
				this.logMessage("     but was: OFF");
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

//		this.statistics.updateStatistics();
//		this.logMessage("                                      ");
//
//		this.logMessage("  Scenario: switching off the heater when on");
//		this.logMessage("    And the heater is on");
//		try {
//			this.logMessage("    When I switch off the heater");
//			this.UserOP.switchOff();
//			result = this.UserOP.on();
//			if (!result) {
//				this.logMessage("    Then the state of the heater is OFF");
//			} else {
//				this.logMessage("     but was: on");
//				this.statistics.incorrectResult();
//			}
//		} catch (Throwable e) {
//			this.statistics.incorrectResult();
//			this.logMessage("     but the exception " + e + " has been raised");
//		}
//
//		this.statistics.updateStatistics();
	}
	
	
	/**
	 * Testing switching on the Tumbledryer and setting mode and option, then switching it off
	 * 
	 * When switching on the Tumbledryer , the state should be ON
	 * 
	 * When setting mode, the current mode should be the same as asked
	 * 
	 * When setting options, the current option should be the same as asked
	 * 
	 * We only tcheck with one mode and one option, given if it works with
	 * one , it'll work with the others
	 * 
	 * When calling play() the mode and option should remain the same, the 
	 * state of the tumbledryer should be Drying, we ensure it by tchecking
	 * if the state isnt ON instead of adding a new function
	 * to the interface for priority reasons
	 * 
	 * Then we tcheck try to set mode and option again but it should be
	 * inpossible because we already pressed the play button
	 * 
	 * When switching off the Tumbledryer, the state should be OFF and mode
	 * and option should be reset
	 * 
	 * 
	 * 
	 */
	
	//demander au prof si c'est necessaire les blocks catch a chaque fois parce que flemme
	
	protected void		testSetModeOption()
	{
		this.logMessage("                                      ");

		this.logMessage("Feature: Setting mode and option given the tumble dryer"
				+ "is ON");

		this.logMessage("  Scenario: switching on the tumbledryer");
		this.logMessage("   given it has not been used yet");
		this.logMessage("    And the tumbledryer is OFF");
		
		boolean result;
		try {
			this.UserOP.switchOn();
			result = this.UserOP.on();
			if (result) {
				this.logMessage("  Then the state of the heater is ON");
			}else {
				this.logMessage("but wa off");
				this.statistics.incorrectResult();
			}
			
			this.statistics.updateStatistics();

			this.logMessage("                                      ");

			this.logMessage(" When I set the MODE to ANTI_ALLERGY ");
			this.UserOP.setMode(TumbleDryerMode.ANTI_ALLERGY);
			
			String Mode= this.UserOP.tcheckMode();
			String Option= this.UserOP.tcheckOption();
			
			String expectedMode= "ANTI_ALLERGY";
			String expectedOption= "NULL";
			
			if(Mode.equals(expectedMode) && Option.equals(expectedOption) ) {
				this.logMessage(" Then the mode is ANTI_ALLERGY and the option is NULL ");
			}else {
				this.logMessage(" but was Mode: " + Mode + " Option: " + Option );
				this.statistics.incorrectResult();
			}
			this.statistics.updateStatistics();

			this.logMessage(" When I set the Option to DELAYED_START ");
			this.UserOP.setOption(TumbleOption.DELAYED_START);
			
			Option= this.UserOP.tcheckOption();
			Mode= this.UserOP.tcheckMode();
			expectedOption= "DELAYED_START";
			
			if(Option.equals(expectedOption)  && Mode.equals(expectedMode)) {
				this.logMessage(" Then the Option is DELAYED_START and "
						+"The mode remains the same");
			}else {	
				this.logMessage(" but was Mode: " + Mode + " Option: " + Option );	
				this.statistics.incorrectResult();
			}
			this.statistics.updateStatistics();
			
			this.logMessage("                                      ");

			this.logMessage("  Scenario:Pressing the play button");
			this.logMessage("    Given Mode and option are set");
			this.logMessage("    And the Tumble dryer is ON");
			this.logMessage("                                      ");

			this.logMessage("    When I play the tumble dryer");
			this.UserOP.play();
			Option= this.UserOP.tcheckOption();
			Mode= this.UserOP.tcheckMode();
			
			//tcheck the neuw state of the Tumbledryer
			result = this.UserOP.on();
			
			if(Mode.equals(expectedMode) && Option.equals(expectedOption)
					&& !result) {
				
				this.logMessage("then the mOde and option remain the same and "
						+"the state is not on ON");	
			}else {
				this.logMessage("but was MODE: " + Mode + " Option: "+ Option
						+" the state is ON");	
				
				this.statistics.incorrectResult();
			}

			this.statistics.updateStatistics();
			
			this.logMessage("                                      ");

			this.logMessage("  Scenario: changing the mode and option");
			this.logMessage("    Given the tumbledryer is already drying");
			
			this.UserOP.setMode(TumbleDryerMode.NULL);
			this.UserOP.setOption(TumbleOption.NULL);
			Option= this.UserOP.tcheckOption();
			Mode= this.UserOP.tcheckMode();
			
			if(Option.equals(expectedOption) && Mode.equals(expectedMode) ) {
				
				this.logMessage(" Then the tumbledryer thhrows an assertion"
						+ "exeption comming from setoption()/setmode()");
			}else {
				this.logMessage("Mode and option changed");
				this.statistics.incorrectResult();
			}
				
			this.statistics.updateStatistics();
			this.logMessage("                                      ");
			this.logMessage("  Scenario: switching off the tumbledryer");
			this.logMessage("   given it finished drying");
			
			this.UserOP.switchOff();
			Option= this.UserOP.tcheckOption();
			Mode= this.UserOP.tcheckMode();
			expectedMode= "NULL";
			expectedOption= "NULL";
			
			if(Mode.equals(expectedMode)  && Option.equals(expectedOption)
					&& !result) {
				
				this.logMessage("The tumbledryer is off");
			}else {
				this.logMessage(" but wasn't off " + "Option: " +
						Option + "Mode " + Mode + "ON?? " + this.UserOP.on());
				this.statistics.incorrectResult();
			}
		
		}finally {
			
			this.logMessage("                                      ");

			
			this.logMessage("All the unit test are finished");
			
			this.logMessage(" SWITCHING ON the td for HEM  tests");
			this.UserOP.switchOn();
			
	}
	}
		

	
	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();

		try {
			this.UserOP= new UserOutboundPort(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.UserOP.publishPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.doPortConnection(
					this.UserOP.getPortURI(),
					UserInboundPortURI,
					TDUserConector.class.getCanonicalName());
			//connecter les autres ici
		} catch (Throwable e) {
			throw new ComponentStartException(e) ;
		}
	}
	
	
	
	@Override
	public synchronized void	execute() throws Exception
	{
		this.testOff();
		this.testSwitchOnSwitchOff();
		this.testSetModeOption();
		
	}

	
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.UserOP.getPortURI());
		this.UserOP.unpublishPort();

		super.finalise();
	}



}
