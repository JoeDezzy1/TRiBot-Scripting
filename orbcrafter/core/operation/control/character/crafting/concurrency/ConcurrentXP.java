package scripts.orbcrafter.core.operation.control.character.crafting.concurrency;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import scripts.dezapi.core.control.parameter.utility.Loadable;

/**
 * @author JoeDezzy1
 */
public class ConcurrentXP implements Loadable, Runnable
{
	/**
	 * Boolean flags
	 */
	private boolean casting = false;
	private int previous;
	private long timeout;

	/**
	 * Generates a timeout length to determine when its ok to start crafting
	 *
	 * @return the generated timeout
	 */
	public long generateTimeout()
	{
		return General.randomSD(9000, 20000, 14000);
	}

	/**
	 * Determines if we can cast the spell to the obelisk
	 *
	 * @return true if we can cast the spell
	 */
	public boolean casting()
	{
		return this.casting;
	}

	/**
	 * Initializes the thread
	 */
	public void initialize()
	{
		this.casting = false;
		this.update();
	}

	/**
	 * Updates the shit
	 */
	public void update()
	{
		this.previous = Skills.getXP(SKILLS.MAGIC);
		this.timeout = Timing.currentTimeMillis() + generateTimeout();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void load()
	{
		this.run();
	}

	/*
	 *
	 */
	@Override
	public void run()
	{
		if (Timing.currentTimeMillis() >= this.timeout)
		{
			if (Skills.getXP(SKILLS.MAGIC) > this.previous)
			{
				this.update();
				this.casting = true;
			}
			else
			{
				this.update();
				this.casting = false;
			}
		}
		else if (Skills.getXP(SKILLS.MAGIC) > this.previous)
		{
			this.update();
			this.casting = true;
		}
	}
}
