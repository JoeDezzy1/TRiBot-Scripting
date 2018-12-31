/**
 *
 */
package scripts.orbcrafter.core.operation.control.character.crafting;

import scripts.dezapi.core.antiban.ABC2;
import scripts.dezapi.core.control.parameter.StateHooker;
import scripts.dezapi.core.control.parameter.utility.Loadable;
import scripts.dezapi.core.environment.DzPlayer;
import scripts.orbcrafter.core.operation.control.character.crafting.concurrency.ConcurrentXP;
import scripts.orbcrafter.core.operation.control.ScriptController;
import scripts.orbcrafter.core.operation.control.character.crafting.parameters.Cast;
import scripts.orbcrafter.core.operation.control.character.crafting.parameters.Make;
import scripts.orbcrafter.core.operation.control.character.crafting.parameters.Quantify;
import scripts.orbcrafter.core.operation.control.character.crafting.parameters.Select;

/**
 * @author JoeDezzy1
 */
public abstract class CraftingController extends ScriptController
{
	/**
	 * Timed flag for can cast activation
	 */
	private ConcurrentXP determinant;

	/**
	 * Creates a new orb crafter control
	 */
	public CraftingController()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Handler#getName()
	 */
	@Override
	public String getName()
	{
		return "Orb Crafter";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.prcs.ControlProcessor#onEnd()
	 */
	@Override
	public void onEnd()
	{
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Handler#active()
	 */
	@Override
	public boolean active()
	{
		return !this.determinant.casting() && this.connection().orb().getArea().contains(DzPlayer.getPosition());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.orbcrafter.api.OrbScriptControl#update()
	 */
	@Override
	public Loadable update()
	{
		return () -> this.determinant.load();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Handler#setNodes()
	 */
	@Override
	public void load()
	{
		this.determinant = new ConcurrentXP();
		this.determinant.initialize();
		super.add(0, new Select(this.connection().orb())
		{
			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}

			@Override
			public StateHooker valid()
			{
				return () -> super.valid().active() && !determinant.casting();
			}
		});
		super.add(1, new Make()
		{
			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}

			@Override
			public StateHooker invalid()
			{
				return () -> getPkers().unbox() != null || determinant.casting();
			}
		});
		super.add(2, new Cast(this.connection().orb())
		{
			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}
		});
		super.add(3, new Quantify()
		{
			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}
		});
	}
}
