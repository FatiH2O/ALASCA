package CVM;


import fr.sorbonne_u.components.cvm.AbstractCVM;



import Components.TumbleDryer;
import UnitTest.TumbleDryerUnitTest;
import fr.sorbonne_u.components.AbstractComponent;

// -----------------------------------------------------------------------------

 
public class			CVMUnitTest
extends		AbstractCVM
{
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				CVMUnitTest() throws Exception
	{
	}

	// -------------------------------------------------------------------------
	// CVM life-cycle
	// -------------------------------------------------------------------------

	
	@Override
	public void			deploy() throws Exception
	{
		AbstractComponent.createComponent(
				TumbleDryer.class.getCanonicalName(),
				new Object[]{});

		AbstractComponent.createComponent(
				TumbleDryerUnitTest.class.getCanonicalName(),
				new Object[]{TumbleDryer.USER_INBOUND_PORT_URI});	

		super.deploy();
	}

	public static void	main(String[] args)
	{
		try {
			CVMUnitTest cvm = new CVMUnitTest();
			cvm.startStandardLifeCycle(1000L);
			Thread.sleep(100000L);
			System.exit(0);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		System.out.println("hi");
	}
}
// -----------------------------------------------------------------------------
