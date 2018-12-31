/**
 *
 */
package scripts.orbcrafter.core.operation.control.character.crafting.parameters;

import org.tribot.api2007.Magic;
import org.tribot.api2007.types.RSArea;
import scripts.dezapi.core.control.execute.utility.Hoverable;
import scripts.dezapi.core.control.parameter.StateHooker;
import scripts.dezapi.core.control.parameter.clickable.models.entities.objects.ParamObject;
import scripts.dezapi.core.environment.DzInterfaces;
import scripts.dezapi.core.environment.wrap.MenuWrapper;
import scripts.dezapi.core.environment.wrap.ObjectWrapper;
import scripts.dezapi.core.environment.wrap.Wrapper;
import scripts.orbcrafter.core.api.types.Orb;

/**
 * @author JoeDezzy1
 */
public abstract class Cast extends ParamObject
{
	private Orb TYPE;

	/**
	 * Casts the spell to the object parameter
	 *
	 * @param TYPE
	 *            - the type to cast to the spell
	 */
	public Cast(Orb TYPE)
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
	 * @see scripts.dezapi.ops.impl.processor.process.exec.obj.ObjectParams#option()
	 */
	@Override
	public String option()
	{
		return "Cast";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.ops.impl.processor.process.exec.obj.ObjectParams#area()
	 */
	@Override
	public RSArea area()
	{
		return TYPE.getArea();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.ops.impl.processor.process.exec.obj.ObjectParams#object()
	 */
	@Override
	public ObjectWrapper wrapper()
	{
		return new ObjectWrapper(14, TYPE.getName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.nodes.type.interactive.
	 * InteractiveObject#active()
	 */
	@Override
	public boolean active()
	{
		return Magic.isSpellSelected() && super.active();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.nodes.type.interactive.
	 * Interactable#getWaitCondition()
	 */
	@Override
	public StateHooker invalid()
	{
		return () -> DzInterfaces.interfaceOpen(new MenuWrapper(270, 14)).active();
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
				return new MenuWrapper(270, 14);
			}

			@Override
			public String hoverOption()
			{
				return "Charge";
			}
		};
	}
}
