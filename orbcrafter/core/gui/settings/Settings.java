/**
 *
 */
package scripts.orbcrafter.core.gui.settings;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.tribot.api2007.Game;
import scripts.dezapi.core.environment.DzWorldHopper;
import scripts.dezapi.core.environment.wrap.*;
import scripts.orbcrafter.core.api.types.Orb;
import scripts.orbcrafter.core.api.types.Setting;

/**
 * @author JoeDezzy1
 */
public class Settings
{
	private final Map<Setting, Object> values;
	private int nextWorld;

	public Settings()
	{
		this.values = new ConcurrentHashMap<>();
	}

	/**
	 *
	 * @param key
	 * @param loading
	 */
	public void load(final Setting key, final Object loading)
	{
		this.values.put(key, loading);
	}

	/**
	 *
	 * @return
	 */
	public boolean shouldHopWorlds()
	{
		return nextWorld > 0 && Game.getCurrentWorld() != nextWorld;
	}

	/**
	 *
	 * @return
	 */
	public int getNextWorld()
	{
		return nextWorld;
	}

	/**
	 *
	 * @return
	 */
	public Orb orb()
	{
		return (Orb) get(Setting.ORB);
	}

	/**
	 *
	 * @return
	 */
	public DzItems items()
	{
		return (DzItems) get(Setting.ITEMS);
	}

	/**
	 *
	 * @return
	 */
	public DzEquipment equipment()
	{
		return (DzEquipment) get(Setting.EQUIPMENT);
	}

	/**
	 *
	 * @return
	 */
	public int foodAmount()
	{
		return Integer.parseInt(get(Setting.FOOD_AMOUNT).toString());
	}

	/**
	 *
	 * @return
	 */
	public FOOD food()
	{
		return (FOOD) get(Setting.FOOD);
	}

	/**
	 *
	 * @return
	 */
	public ENERGY energy()
	{
		return (ENERGY) get(Setting.ENERGY);
	}

	/**
	 *
	 * @return
	 */
	public BANK_METHOD bankMethod()
	{
		return (BANK_METHOD) get(Setting.BANK_METHOD);
	}

	/**
	 *
	 * @return
	 */
	public Object get(Setting object)
	{
		return values.get(object);
	}

	/**
	 *
	 * @return
	 */
	public Set<Setting> keySet()
	{
		return this.values.keySet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.values.toString();
	}

	/**
	 *
	 * @param on
	 */
	public void scheduleWorldhop(boolean on)
	{
		if (on)
		{
			if (this.nextWorld == -1)
				this.nextWorld = DzWorldHopper.getRandomWorld(true);
		}
		else
			this.nextWorld = -1;
	}

	/**
	 * @return a default set of properties
	 */
	public void setDefault()
	{
		// init.put("ORB", "AIR_ORB");
		// init.put("FOOD", null);
		// init.put("ENERGY", null);
		// init.put("FOOD_AMOUNT", "0");
		// init.put("BANK_METHOD", "GLORY");
		// init.put("RECHARGE_GLORY", "false");
		// DzItems items = new DzItems();
		// DzEquipment equipment = new DzEquipment();
		// init.put("ITEMS", items);
		// init.put("EQUIPMENT", equipment);
		// return init;
	}
}