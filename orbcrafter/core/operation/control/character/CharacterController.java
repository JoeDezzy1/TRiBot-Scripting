/**
 *
 */
package scripts.orbcrafter.core.operation.control.character;

import scripts.orbcrafter.core.operation.control.ScriptController;

/**
 * @author JoeDezzy1
 */
public abstract class CharacterController extends ScriptController
{

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.prcs.Processor#getName()
	 */
	@Override
	public String getName()
	{
		return "Player Monitor";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.prcs.Processor#load()
	 */
	@Override
	public void load()
	{
		this.addStatRestorers(super.size() + 1);
	}

	/**
	 * Add stat restore nodes
	 *
	 * @param pos - the priority position
	 */
	public void addStatRestorers(int pos)
	{
		super.add(pos, super.eat(this.connection().food()));
		super.add(pos, super.energy(this.connection().energy()));
	}
}