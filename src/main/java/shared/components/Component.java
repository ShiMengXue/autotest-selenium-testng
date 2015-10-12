package shared.components;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.setDriver.SetDriver;
import shared.validate.ValidateSeBlob;

/**
 * Base class for components that are used to compose page objects
 * 
 * NOTE: This class has evolved over time and most of its original functionality was refactored into ElementValidation
 * which it extends. It was decided to keep this class in place since so many child component classes extend it, and
 * eliminating it would create a lot of refactoring. Also there may be a future functionality that can be refactored
 * from subclasses into this parent.
 *
 * @author
 * 
 */
public abstract class Component extends ValidateSeBlob {

	private static final Logger LOG = LogManager.getLogger(Component.class);

	public Component(SetDriver driver) {
		super(driver);
		LOG.debug("component parent instantiated");
	}

}
