/**
 * 
 */
package scripts.orbcrafter.core.operation.control.character.crafting.parameters;

import org.tribot.api2007.types.RSInterface;

import scripts.dezapi.core.control.parameter.StateHooker;
import scripts.dezapi.core.control.parameter.clickable.screen.interfaces.ParamMenu;
import scripts.dezapi.core.environment.wrap.MenuWrapper;

/**
 * @author JoeDezzy1
 */
public abstract class Quantify extends ParamMenu
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see scripts.dezapi.ops.impl.processor.process.exec.menu.MenuParams#option()
	 */
	@Override
	public String option() {
		return "All";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scripts.dezapi.controller.ThoughtParam#isWaitingFixed()
	 */
	@Override
	public boolean isWaitingFixed() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scripts.dezapi.handlers.nodes.interactable.Interactable#
	 * getWaitCondition()
	 */
	@Override
	public StateHooker invalid() {
		return () -> !super.active();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scripts.dezapi.ops.impl.processor.process.exec.menu.MenuParams#control()
	 */
	@Override
	public MenuWrapper wrapper() {
		return new MenuWrapper(270, 12);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scripts.dezapi.handlers.nodes.interactable.Interactable#
	 * getWaitCondition()
	 */
	@Override
	public StateHooker valid() {
		return () -> {
			if (super.valid().active()) {
				final RSInterface menu = this.wrapper().unbox();
				if (menu != null) {
					return menu.getActions() != null;
				}
			}
			return false;
		};
	}
}