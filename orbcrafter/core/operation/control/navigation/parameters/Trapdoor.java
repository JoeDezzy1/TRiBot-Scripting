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
import scripts.dezapi.core.environment.DzObjects;
import scripts.dezapi.core.environment.DzPlayer;
import scripts.dezapi.core.environment.wrap.ObjectWrapper;
import scripts.dezapi.utility.constant.Constants;
import scripts.dezapi.utility.constant.EdgevilleData;

/**
 * @author JoeDezzy1
 */
public abstract class Trapdoor extends ParamObject
{
	private final RSArea DUNGEON_ENTRANCE = new RSArea(new RSTile(3097, 3473, 0), new RSTile(3092, 3468, 0));

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
	 * @see scripts.dezapi.ops.impl.processor.process.exec.obj.ObjectParams# area()
	 */
	@Override
	public RSArea area()
	{
		return this.DUNGEON_ENTRANCE;
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
		return new ObjectWrapper(7, EdgevilleData.TRAPDOOR_IDS);
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
		return DzObjects.find(7, EdgevilleData.CLOSED_TRAPDOOR_ID).length > 0 ? Constants.Options.OPEN_OPTION :
		       Constants.Options.CLIMB_DOWN_OPTION;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.exec.obj.ObjectParams#activator()
	 */
	@Override
	public StateHooker valid()
	{
		return () -> super.valid().active() && Player.getPosition().getY() < 9000;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.ops.impl.processor.process.ThoughtControl# deactivator()
	 */
	@Override
	public StateHooker invalid()
	{
		final String prev = this.option();
		return () -> prev.equals(Constants.Options.OPEN_OPTION) ?
		             DzObjects.find(7, EdgevilleData.OPEN_TRAPDOOR_ID).length > 0 :
		             !area().contains(DzPlayer.getPosition());
	}
}