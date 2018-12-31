package scripts.orbcrafter.core.api.wrappers.hoverable;

/**
 * @author JoeDezzy1
 */
public class HoverNode<E>
{
	E value;
	public HoverNode next;

	public HoverNode(E hover, HoverNode next)
	{
		this.value = hover;
		this.next = next;
	}
}
