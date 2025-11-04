package Connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2025.bases.AdjustableCI;
import fr.sorbonne_u.components.hem2025.bases.RegistrationCI;
import fr.sorbonne_u.components.hem2025e1.equipments.heater.HeaterExternalControlJava4CI;
import fr.sorbonne_u.exceptions.PreconditionException;
import Interfaces.TumbleDryerEnergyManagerI;


public class AjustableTumbleDryerConnector extends AbstractConnector implements AdjustableCI{
	
	// -------------------------------------------------------------------------
		// Constants and variables
		// -------------------------------------------------------------------------

		/** modes will be defined by five power levels, including a power
		 *  level of 0.0 watts; note that modes go from 1 (0.0 watts) to
		 *  4 (2000.0 watts).													*/
		public static final int		MAX_MODE = 4;		
		/** the current mode of the heater.										*/
		protected int		currentMode;
		/** true if the heater has been suspended, false otherwise.				*/
		protected boolean	isSuspended;

		// -------------------------------------------------------------------------
		// Constructors
		// -------------------------------------------------------------------------

		public				AjustableTumbleDryerConnector()
		{
			super();
			this.currentMode = MAX_MODE;
			this.isSuspended = false;
		}

		



		@Override
		public int maxMode() throws Exception {
			
			return MAX_MODE;
		}
	
		@Override
		public boolean upMode() throws Exception {
			
			assert	!this.suspended() : new PreconditionException("!suspended()");
			assert	this.currentMode() < MAX_MODE :
					new PreconditionException("currentMode() < MAX_MODE");
			
			try {
				
			((TumbleDryerEnergyManagerI)this.offering).upperLevel();
			this.currentMode++;
			
			}catch(Exception e) {
				return false;
			}
			return true;
				
		}
	
		@Override
		public boolean downMode() throws Exception {
			
			assert	!this.suspended() : new PreconditionException("!suspended()");
			assert	this.currentMode() > 0 :
				new PreconditionException("currentMode() > 0");
			
			try {
				((TumbleDryerEnergyManagerI)this.offering).lowerLevel();
				this.currentMode--;

			}catch(Exception e) {
				return false;
			}
				
			return true;
		}
	
		@Override
		public boolean setMode(int modeIndex) throws Exception {
			
			assert	!this.suspended() : new PreconditionException("!suspended()");
			assert	modeIndex < 5 && modeIndex >0 
			: new PreconditionException("This modeIndex is not available");
	
			try {
				((TumbleDryerEnergyManagerI)this.offering).setPowerLevel(modeIndex);
				this.currentMode = modeIndex;

			}catch(Exception e) {
				return false;
			}
				
			return true;
		}
	
		@Override
		public int currentMode() throws Exception {

			assert	!suspended() 
			: new PreconditionException("!suspended()");

				 return this.currentMode;
		}
	
		@Override
		public double getModeConsumption(int modeIndex) throws Exception {
			
			assert	!suspended() 
			: new PreconditionException("!suspended()");
			
			return ((TumbleDryerEnergyManagerI)this.offering)
					.getNeededPowerLevel(modeIndex);

		}
	
		@Override
		public boolean suspended() throws Exception {
			
			return this.isSuspended;

		}
	
		@Override
		public boolean suspend() throws Exception {
			assert !suspended(): 
				new PreconditionException("is already suspended()");
			
			this.currentMode = 1;
			this.isSuspended= true;
			return ((TumbleDryerEnergyManagerI)this.offering)
					.timedShutdown();
		}
	
		@Override	
		public boolean resume() throws Exception {

			this.currentMode = 1;
			this.isSuspended= true;
			return ((TumbleDryerEnergyManagerI)this.offering).shutDown();
		}
	
		@Override
		public double emergency() throws Exception {
			int emergencyCatchedMode = this.currentMode;
			
			((TumbleDryerEnergyManagerI)this.offering).shutDown();
			
			return emergencyCatchedMode;
		}
		
	

	
}
