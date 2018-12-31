/**
 *
 */
package scripts.orbcrafter.core.operation.control.navigation.parameters;

import org.tribot.api.Clicking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Options;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import scripts.dezapi.core.control.execute.utility.Hoverable;
import scripts.dezapi.core.control.parameter.StateHooker;
import scripts.dezapi.core.control.parameter.clickable.screen.walkables.ParamWalk;
import scripts.dezapi.core.environment.DzObjects;
import scripts.dezapi.core.environment.wrap.FOOD;
import scripts.dezapi.core.environment.wrap.ObjectWrapper;
import scripts.dezapi.utility.constant.Constants;
import scripts.dezapi.utility.constant.EdgevilleData;
import scripts.orbcrafter.core.api.types.Data;

/**
 * @author JoeDezzy1
 */
public abstract class WalkEntrance extends ParamWalk
{

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.control.parameter.ParamWalk#isWaitingFixed()
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
		return Data.DUNGEON_ENTRANCE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.walk.params.WalkParam#deactivator()
	 */
	@Override
	public StateHooker valid()
	{
		return () -> Player.getPosition().getY() < 9000 && !destination().contains(Player.getPosition());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.walk.params.WalkParam#path()
	 */
	@Override
	public RSTile[] path()
	{
		return new RSTile[] { Data.DUNGEON_ENTRANCE_WALK_AREA.getRandomTile() };
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.walk.params.WalkParam#deactivator()
	 */
	@Override
	public StateHooker invalid()
	{
		return () -> this.destination().contains(Player.getPosition()) || Player.getPosition().getY() >= 9000;
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
		if (abc().isLoaded())
		{
			if (abc().shouldRun())
				if (Options.setRunEnabled(true))
					abc().resetRunActivation();
			if (abc().shouldEat())
			{
				final FOOD VALID = FOOD.getAnyValidFoodType();
				if (VALID != null)
				{
					if (Clicking.click(Inventory.find(VALID.getNames())))
						abc().resetNextEat();
				}
				else
					return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.ThoughtParam#definedHover()
	 */
	@Override
	public Hoverable definedHover()
	{
		return new Hoverable()
		{
			@Override
			public ObjectWrapper hoverWrapper()
			{
				return new ObjectWrapper(7, EdgevilleData.TRAPDOOR_IDS);
			}

			@Override
			public String hoverOption()
			{
				return DzObjects.find(7, EdgevilleData.CLOSED_TRAPDOOR_ID).length > 0 ? Constants.Options.OPEN_OPTION :
				       Constants.Options.CLIMB_DOWN_OPTION;
			}
		};
	}
}
