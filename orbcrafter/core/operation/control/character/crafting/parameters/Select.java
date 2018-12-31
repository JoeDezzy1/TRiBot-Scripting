/**
 *
 */
package scripts.orbcrafter.core.operation.control.character.crafting.parameters;

import scripts.dezapi.core.antiban.seeding.ABC3WaitTimers;
import scripts.dezapi.core.control.execute.utility.Hoverable;
import scripts.dezapi.core.control.parameter.StateHooker;
import scripts.dezapi.core.control.parameter.clickable.screen.interfaces.panels.ParamSpell;
import scripts.dezapi.core.environment.wrap.MenuWrapper;
import scripts.dezapi.core.environment.wrap.ObjectWrapper;
import scripts.dezapi.core.environment.wrap.Wrapper;
import scripts.orbcrafter.core.api.types.Orb;

/**
 * @author JoeDezzy1
 */
public abstract class Select extends ParamSpell
{

	/**
	 * The orb type
	 */
	private Orb TYPE;

	/**
	 * Creates a spell select parameter
	 *
	 * @param TYPE
	 *            - the type of spell to select
	 */
	public Select(Orb TYPE)
	{
		this.TYPE = TYPE;
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
	 * @see scripts.dezapi.interfaces.ThoughtParam#wrapper()
	 */
	@Override
	public MenuWrapper wrapper()
	{
		return new MenuWrapper(0, 0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.interfaces.ThoughtParam#timer()
	 */
	@Override
	public ABC3WaitTimers timer()
	{
		return ABC3WaitTimers.Select;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.ops.impl.node.actprcs.prcss.mage.SpellParams#spell()
	 */
	@Override
	public String spell()
	{
		return TYPE.getSpell();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.exec.magic.SpellParams#activator()
	 */
	@Override
	public StateHooker valid()
	{
		return () -> super.valid().active() && !new MenuWrapper(270, 14).isVisible();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.controller.exec.magic.SpellParams#deactivator()
	 */
	@Override
	public StateHooker invalid()
	{
		return () -> super.invalid().active();
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
			public Wrapper hoverWrapper()
			{
				return new ObjectWrapper(14, TYPE.getName());
			}

			@Override
			public String hoverOption()
			{
				return "Cast";
			}
		};
	}
}
