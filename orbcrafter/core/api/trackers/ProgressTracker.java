/**
 *
 */
package scripts.orbcrafter.core.api.trackers;

import org.tribot.api.Timing;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;

import scripts.dezapi.utility.time.Clock;
import scripts.orbcrafter.core.api.types.Orb;

/**
 * @author JoeDezzy1
 */
public class ProgressTracker
{
	/**
	 * The orb type enum
	 */
	public Orb TYPE;

	/**
	 * The clock for the progress
	 */
	public Clock runtime;

	/**
	 * The start xp
	 */
	public int startxp;

	/**
	 * The amount of xp gained
	 */
	public int xp;

	/**
	 * The amount of items the bot has created/collected
	 */
	public int items;

	/**
	 * The skill were tracking
	 */
	public SKILLS SKILL;

	/**
	 * The previous breaks start time
	 */
	private long breakStart;

	/**
	 * Creates a new orb progress tracker
	 *
	 * @param TYPE
	 *            - the type of orb being crafted
	 */
	public ProgressTracker(Orb TYPE)
	{
		this(SKILLS.MAGIC);
		this.TYPE = TYPE;
	}

	/**
	 * Creates a new progress instance and initializes the clock
	 */
	public ProgressTracker(SKILLS SKILL)
	{
		this.SKILL = SKILL;
		this.runtime = new Clock();
		this.startxp = Skills.getXP(SKILL);
		this.xp = startxp;
	}

	/**
	 * Gets the amount of items generated
	 *
	 * @return the current amount of items generated
	 */
	public int getAmountGenerated()
	{
		return items;
	}

	/**
	 * Gets the XP gained from the specific bot
	 *
	 * @return
	 */
	public int getXpGained()
	{
		return xp - startxp;
	}

	/**
	 * Gets the bots runtime clock
	 *
	 * @return the runtime clock
	 */
	public Clock getClock()
	{
		return runtime;
	}

	/**
	 * Resets the amount of items generated back to 0
	 */
	public void resetAmountGenerated()
	{
		this.items = 0;
	}

	/**
	 * Updates the progress
	 *
	 */
	public void track()
	{
		this.update();
	}

	/**
	 * Starts a break length timer
	 */
	public void startBreak()
	{
		this.breakStart = Timing.currentTimeMillis();
	}

	/**
	 * Ends the break length timer and accounts for it into runtime hourly
	 * calculations
	 */
	public void endBreak()
	{
		final long breakLength = Timing.currentTimeMillis() - this.breakStart;
		this.runtime.addToBreakTimeTotal(breakLength);
		this.breakStart = 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.monitors.Progress#update()
	 */
	public void update()
	{
		if (Skills.getXP(SKILLS.MAGIC) > xp)
		{
			if (this.TYPE.getArea().contains(Player.getPosition()))
				this.items++;
			this.xp = Skills.getXP(SKILLS.MAGIC);
		}
	}
}
