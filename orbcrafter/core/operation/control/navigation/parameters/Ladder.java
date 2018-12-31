/**
 *
 */
package scripts.orbcrafter.core.operation.control.navigation.parameters;

import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import scripts.dezapi.core.antiban.seeding.ABC3WaitTimers;
import scripts.dezapi.core.control.parameter.StateHooker;
import scripts.dezapi.core.control.parameter.clickable.models.entities.objects.ParamObject;
import scripts.dezapi.core.environment.DzPlayer;
import scripts.dezapi.core.environment.wrap.ObjectWrapper;
import scripts.dezapi.utility.constant.Constants;
import scripts.dezapi.utility.constant.EdgevilleDungeonData;

/**
 * @author JoeDezzy1
 */
public abstract class Ladder extends ParamObject
{
	private RSArea WALKABLE_OBELISK_LADDER_AREA = new RSArea(new RSTile[] { new RSTile(3087, 9974, 0),
	                                                                        new RSTile(3087, 9973, 0),
	                                                                        new RSTile(3086, 9972, 0),
	                                                                        new RSTile(3086, 9971, 0),
	                                                                        new RSTile(3087, 9970, 0),
	                                                                        new RSTile(3087, 9967, 0),
	                                                                        new RSTile(3091, 9967, 0),
	                                                                        new RSTile(3091, 9969, 0),
	                                                                        new RSTile(3090, 9970, 0),
	                                                                        new RSTile(3090, 9971, 0),
	                                                                        new RSTile(3091, 9972, 0),
	                                                                        new RSTile(3090, 9973, 0), });

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.control.parameter.ParamObject#isWaitingFixed()
	 */
	@Override
	public boolean isWaitingFixed()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.control.parameter.ParamObject#timer()
	 */
	@Override
	public ABC3WaitTimers timer()
	{
		return ABC3WaitTimers.ClimbLadder;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.ops.impl.processor.process.exec.obj.ObjectParams#
	 * option()
	 */
	@Override
	public String option()
	{
		return Constants.Options.CLIMB_OPTION;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.exec.obj.ObjectParams#activator()
	 */
	@Override
	public StateHooker valid()
	{
		return () -> super.valid().active() && Player.getPosition().getY() >= 9000;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.ops.impl.processor.process.ThoughtControl# deactivator()
	 */
	@Override
	public StateHooker invalid()
	{
		return () -> !this.area().contains(DzPlayer.getPosition());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.ops.impl.processor.process.exec.obj.ObjectParams# area()
	 */
	@Override
	public RSArea area()
	{
		return this.WALKABLE_OBELISK_LADDER_AREA;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.ops.impl.processor.process.exec.obj.ObjectParams#
	 * object()
	 */
	@Override
	public ObjectWrapper wrapper()
	{
		return new ObjectWrapper(7,
		                         new RSArea(new RSTile(3088, 9971, 0), new RSTile(3088, 9971, 0)),
		                         EdgevilleDungeonData.LADDER_ID);
	}
}