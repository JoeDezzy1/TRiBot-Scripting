/**
 * 
 */
package scripts.orbcrafter.core.api.wrappers.hoverable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author JoeDezzy1
 */
public class HoverDetermination {
	/**
	 * The index of the current context in place
	 */
	private Integer current;
	/**
	 * The hover contexts loaded into the determination
	 */
	private List<HoverContext> contexts;

	/**
	 * 
	 * @param contexts
	 */
	public HoverDetermination(HoverContext... contexts) {
		this.current = new Integer(-1);
		this.contexts = new LinkedList<HoverContext>();
		for (final HoverContext context : contexts) {
			this.contexts.add(context);
		}
	}

	/**
	 * Updates the current context
	 */
	public void update() {
		if (current != -1) {
			contexts.get(current).update();
		}
	}

	/**
	 * 
	 * @return
	 */
	public HoverContext get() {
		for (int i = 0; i < contexts.size(); i++) {
			if (contexts.get(i).isActive()) {
				current = i;
				return contexts.get(i);
			}
		}
		return null;
	}
}