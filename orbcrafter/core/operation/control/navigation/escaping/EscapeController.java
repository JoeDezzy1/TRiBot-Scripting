/**
 *
 */
package scripts.orbcrafter.core.operation.control.navigation.escaping;

import org.tribot.api.General;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Game;
import scripts.dezapi.core.control.parameter.utility.Loadable;
import scripts.dezapi.core.environment.DzLogin;
import scripts.dezapi.core.environment.DzPlayer;
import scripts.orbcrafter.core.operation.control.navigation.NavigationController;
import scripts.orbcrafter.core.api.types.Data;

/**
 * @author JoeDezzy1
 */
public abstract class EscapeController extends NavigationController
{
	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.interfaces.Processor#getName()
	 */
	@Override
	public String getName()
	{
		return "Escape Controller";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.process.Concious#active()
	 */
	@Override
	public boolean active()
	{
		return super.isPkerPresent().active();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.interfaces.Processor#load()
	 */
	@Override
	public void load()
	{
		super.addTeleportMethod(0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Thread#run()
	 */
	@Override
	public Loadable update()
	{
		return () -> {
			if (this.connection().orb().getArea().contains(DzPlayer.getPosition()) && this.isPkerPresent().active() &&
			    this.abc().getNextWorld() == -1)
			{
				General.println(this.getName() + " => Pker found!");
				this.onPker().load();
			}
		};
	}

	/**
	 * Checks for successfull worldhop
	 */
	public void checkSuccess()
	{
		final int world = this.abc().getNextWorld() + 300;
		if (world == Game.getCurrentWorld())
		{
			General.println(this.getName() + " => Successfully executed!");
			this.abc().setDelayable(true);
			this.scheduleWorldHop(false);
		}
	}

	/**
	 * Does the action to escape a pker
	 *
	 * @return the action to escape a pker
	 */
	private Loadable onPker()
	{
		return () -> {
			this.scheduleWorldHop(true);
			this.abc().setDelayable(false);
			if (General.randomBoolean())
			{
				if (DzLogin.inGame())
				{
					final String response = Data.PKER_RESPONSES[General.random(0, Data.PKER_RESPONSES.length - 1)];
					General.println(this.getName() + " => responding with: " + response);
					Keyboard.typeSend(response);
				}
			}
		};
	}
}