/**
 *
 */
package scripts.orbcrafter.core.operation.control.navigation.escaping.misc;

import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.dezapi.core.antiban.ABC2;
import scripts.dezapi.core.control.parameter.StateHooker;
import scripts.dezapi.core.control.parameter.clickable.models.entities.objects.ParamObject;
import scripts.dezapi.core.control.parameter.clickable.screen.walkables.ParamWalk;
import scripts.dezapi.core.environment.DzLogin;
import scripts.dezapi.core.environment.DzPlayer;
import scripts.dezapi.core.environment.wrap.ObjectWrapper;
import scripts.dezapi.utility.constant.EdgevilleData;
import scripts.orbcrafter.core.operation.control.ScriptController;

/**
 * @author JoeDezzy1
 */
public abstract class PohController extends ScriptController
{
	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.prcs.Processor#getName()
	 */
	private static final int INSIDE_PORTAL = 4525, OUTSIDE_PORTAL = 15478;

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.interfaces.Processor#getName()
	 */
	@Override
	public String getName()
	{
		return "POH Control";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.prcs.ControlProcessor#active()
	 */
	@Override
	public boolean active()
	{
		return Objects.find(50, INSIDE_PORTAL).length > 0 || Objects.findNearest(10, OUTSIDE_PORTAL).length > 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.prcs.Processor#load()
	 */
	@Override
	public void load()
	{
		super.add(0, this.outsidePortalParam());
		super.add(1, this.mountedGloryWeb());
		super.add(2, this.mountedGlory());
	}

	/**
	 *
	 * @return
	 */
	public ParamObject mountedGlory()
	{
		return new ParamObject()
		{
			@Override
			public boolean isWaitingFixed()
			{
				return false;
			}

			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}

			@Override
			public String option()
			{
				return "Edgeville";
			}

			@Override
			public ObjectWrapper wrapper()
			{
				return new ObjectWrapper(50, "Amulet of Glory");
			}

			@Override
			public StateHooker invalid()
			{
				return () -> EdgevilleData.EDGEVILLE.contains(Player.getPosition());
			}
		};
	}

	/**
	 *
	 * @return
	 */
	public ParamObject outsidePortalParam()
	{
		return new ParamObject()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see scripts.dezapi.interfaces.Antibanable#abc()
			 */
			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}

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
			 * @see scripts.dezapi.interfaces.Thought#option()
			 */
			@Override
			public String option()
			{
				return "Home";
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see scripts.dezapi.process.Validatable#invalid()
			 */
			@Override
			public StateHooker invalid()
			{
				return () -> Objects.find(10, 15478).length > 0;
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see scripts.dezapi.interfaces.Thought#wrapper()
			 */
			@Override
			public ObjectWrapper wrapper()
			{
				return new ObjectWrapper(10, new RSArea(new RSTile(2951, 3222, 0), 5), "Portal");
			}
		};
	}

	/**
	 * @return
	 */
	public ParamWalk mountedGloryWeb()
	{
		return new ParamWalk()
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
			 * @see scripts.dezapi.interfaces.Antibanable#abc()
			 */
			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see scripts.dezapi.control.parameter.ParamWalk#invalid()
			 */
			@Override
			public StateHooker invalid()
			{
				return () -> {
					final RSArea destination = destination();
					return destination != null && destination.contains(DzPlayer.getPosition());
				};
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see scripts.dezapi.control.parameter.ParamWalk#path()
			 */
			@Override
			public RSTile[] path()
			{
				final RSArea destination = destination();
				if (destination != null)
				{
					final RSTile end = destination.getRandomTile();
					final RSTile[] path = Walking.generateStraightPath(end);
					return Walking.randomizePath(path, General.random(1, 2), General.random(1, 2));
				}
				return new RSTile[0];
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see scripts.dezapi.control.parameter.ParamWalk#valid()
			 */
			@Override
			public StateHooker valid()
			{
				return () -> {
					if (DzLogin.inGame() && Game.getGameState() == 30)
					{
						final RSArea destination = destination();
						return destination != null && !destination.contains(DzPlayer.getPosition());
					}
					return false;
				};
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see scripts.dezapi.control.parameter.ParamWalk#destination()
			 */
			@Override
			public RSArea destination()
			{
				final ObjectWrapper glory = new ObjectWrapper(50, "Amulet of Glory");
				final RSObject object = glory.unbox();
				if (object != null)
				{
					final RSTile pos = object.getPosition();
					if (pos != null)
						return new RSArea(pos, General.random(5, 7));
				}
				return null;
			}
		};
	}

}
