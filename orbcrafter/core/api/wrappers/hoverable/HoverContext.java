/**
 *
 */
package scripts.orbcrafter.core.api.wrappers.hoverable;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * @author JoeDezzy1
 */
public class HoverContext<E>
{
	private Integer index;
	private BooleanSupplier state;
	private List<E> hoverables;

	/**
	 *
	 * @param state
	 * @param hoverables
	 */
	public HoverContext(BooleanSupplier state, E... hoverables)
	{
		this.state = state;
		this.hoverables = new LinkedList<>();
		for (final E wrapper : hoverables)
		{
			this.hoverables.add(wrapper);
		}
	}

	/**
	 *
	 * @return
	 */
	public E get()
	{
		return this.hoverables.get(this.index);
	}

	/**
	 *
	 * @return
	 */
	public boolean isActive()
	{
		return this.state.getAsBoolean();
	}

	/**
	 * Updates the hover context, increases (and or) resets, the index
	 */
	public void update()
	{
		this.index++;
		if (this.index >= this.hoverables.size())
		{
			this.index = 0;
		}
	}
}
