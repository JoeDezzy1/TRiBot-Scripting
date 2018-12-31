/**
 *
 */
package scripts.orbcrafter.core.operation.control.navigation.parameters;

import org.tribot.api.Clicking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.Player;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import scripts.dezapi.core.control.parameter.StateHooker;
import scripts.dezapi.core.control.parameter.clickable.screen.walkables.ParamWalk;
import scripts.orbcrafter.core.api.types.Orb;

/**
 * @author JoeDezzy1
 */
public abstract class WalkDungenon extends ParamWalk
{
	private Orb TYPE;
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

	/**
	 *
	 * @param TYPE
	 */
	public WalkDungenon(Orb TYPE)
	{
		this.TYPE = TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.walk.params.WalkParam#deactivator()
	 */
	@Override
	public StateHooker valid()
	{
		return () -> Player.getPosition().getY() >= 9000 && super.valid().active();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.walk.params.WalkParam#path()
	 */
	@Override
	public RSTile[] path()
	{
		return new RSTile[] { this.destination().getRandomTile() };
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.ThoughtParam#isWaitingFixed()
	 */
	@Override
	public boolean isWaitingFixed()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.walk.params.WalkParam#destination()
	 */
	@Override
	public RSArea destination()
	{
		switch (TYPE)
		{
		case AIR_ORB:
			return WALKABLE_OBELISK_LADDER_AREA;
		case EARTH_ORB:
			return TYPE.getArea();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.walk.params.WalkParam#deactivator()
	 */
	@Override
	public StateHooker invalid()
	{
		return () -> {
			if (this.destination().contains(Player.getPosition()))
				return true;
			if (this.TYPE == Orb.AIR_ORB && Objects.find(5, TYPE.getName()).length > 0)
				return true;
			if (Player.getPosition().getY() < 9000)
				return true;
			return false;
		};
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.ops.impl.processor.process.exec. walk. WalkStimuli#
	 * inner()
	 */
	@Override
	public boolean inner()
	{
		if (this.abc().isLoaded())
		{
			if (this.abc().shouldRun())
				if (Options.setRunEnabled(true))
					this.abc().resetRunActivation();
			if (this.abc().shouldEat())
				if (Inventory.find(Filters.Items.nameContains("Eat")).length > 0)
					if (Clicking.click(Inventory.find(Filters.Items.nameContains("Eat"))))
						this.abc().resetNextEat();
		}
		return false;
	}
}
