/**
 *
 */
package scripts.orbcrafter.core.operation.control.navigation;

import org.tribot.api2007.Player;
import scripts.dezapi.core.antiban.ABC2;
import scripts.orbcrafter.core.operation.control.ScriptController;
import scripts.orbcrafter.core.operation.control.navigation.parameters.WalkDungenon;
import scripts.orbcrafter.core.operation.control.navigation.parameters.WalkEntrance;
import scripts.orbcrafter.core.operation.control.navigation.parameters.Ladder;
import scripts.orbcrafter.core.operation.control.navigation.parameters.Trapdoor;

/**
 * @author JoeDezzy1
 */
public abstract class NavigationController extends ScriptController
{
	/*
	 * The walkable path
	 *
	 * WalkablePath walker;/
	 *
	 * /* (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Handler#getName()
	 */
	@Override
	public String getName()
	{
		return "Obelisk Navigator";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Handler#active()
	 */
	@Override
	public boolean active()
	{
		return !this.connection().orb().getArea().contains(Player.getPosition());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Handler#setNodes()
	 */
	@Override
	public void load()
	{
		final int pos = size() + 1;
		super.add(0, super.item(null, super.canDrinkStaminaInBank(), null, this.connection().energy().getNames()));
		super.addTeleportMethod(pos);
		super.add(pos + 1, new WalkEntrance()
		{
			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}
		});
		super.add(pos + 1, new Trapdoor()
		{
			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}
		});
		super.add(pos + 1, new WalkDungenon(this.connection().orb())
		{
			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}

			@Override
			public boolean inner()
			{
				return universallyEnsuredCondition(true, false, true, true).active();
			}
		});
		super.add(pos, new Ladder()
		{
			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}
		});
	}
}