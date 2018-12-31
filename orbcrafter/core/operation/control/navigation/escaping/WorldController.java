/**
 *
 */
package scripts.orbcrafter.core.operation.control.navigation.escaping;

import scripts.dezapi.core.environment.DzPlayer;
import scripts.dezapi.utility.constant.Constants;
import scripts.orbcrafter.core.operation.control.ScriptController;

/**
 * @author JoeDezzy1
 */
public abstract class WorldController extends ScriptController
{
	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.prcs.Processor#getName()
	 */
	@Override
	public String getName()
	{
		return "World hopper";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.prcs.ControlProcessor#active()
	 */
	@Override
	public boolean active()
	{
		return this.connection().shouldHopWorlds();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.prcs.Processor#load()
	 */
	@Override
	public void load()
	{
		final int pos = size() + 1;
		super.addTeleportMethod(pos);
		super.add(pos, super.worldHop(() -> Constants.Areas.EDGEVILLE.contains(DzPlayer.getPosition())));
		this.deviate(pos);
		this.loaded = true;
	}

}