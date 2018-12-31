/**
 *
 */
package scripts.orbcrafter.core.operation.control.character.crafting.parameters;

import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import scripts.dezapi.core.control.execute.utility.Hoverable;
import scripts.dezapi.core.control.parameter.StateHooker;
import scripts.dezapi.core.control.parameter.clickable.screen.interfaces.ParamMenu;
import scripts.dezapi.core.environment.wrap.EquipmentWrapper;
import scripts.dezapi.core.environment.wrap.MenuWrapper;
import scripts.dezapi.core.environment.wrap.Wrapper;
import scripts.dezapi.utility.constant.Constants;

/**
 * @author JoeDezzy1
 */
public abstract class Make extends ParamMenu
{
	/**
	 * All option selected if there are no action available.. Interface(270,
	 * 12).getText()
	 *
	 * Charge button = (270,14)
	 */
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
	 * @see scripts.dezapi.ops.impl.processor.process.exec.menu.MenuParams#option()
	 */
	@Override
	public String option()
	{
		return "Charge";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.ops.impl.processor.process.exec.menu.MenuParams#control()
	 */
	@Override
	public MenuWrapper wrapper()
	{
		return new MenuWrapper(270, 14);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.nodes.interactable.Interactable#
	 * getWaitCondition()
	 */
	@Override
	public StateHooker invalid()
	{
		final int previous = Skills.getXP(SKILLS.MAGIC);
		return () -> Skills.getXP(SKILLS.MAGIC) > previous;
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
				return new EquipmentWrapper(Constants.Items.CHARGED_GLORY);
			}

			@Override
			public String hoverOption()
			{
				return "Edgeville";
			}
		};
	}
}