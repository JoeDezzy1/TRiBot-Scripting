package scripts.orbcrafter.core.api.types;

import scripts.dezapi.core.environment.wrap.DzEquipment;
import scripts.dezapi.core.environment.wrap.DzItems;

/**
 * @author JoeDezzy1
 */
public enum Setting
{FOOD, ENERGY, BANK_METHOD, ORB, ITEMS, EQUIPMENT, FOOD_AMOUNT;

	/**
	 * @param object
	 * @return
	 */
	public Object parse(Object object)
	{
		switch (this)
		{
		case FOOD_AMOUNT:
			return Integer.parseInt(object.toString());
		case FOOD:
			return FOOD.valueOf(object.toString());
		case ENERGY:
			return ENERGY.valueOf(object.toString());
		case BANK_METHOD:
			return BANK_METHOD.valueOf(object.toString());
		case ORB:
			return Orb.valueOf(object.toString());
		case ITEMS:
			final DzItems items = new DzItems();
			final String value = (String) object;
			value.substring(1, value.length() - 1);
			final String[] keyValuePairs = value.split(",");
			for (final String pair : keyValuePairs)
			{
				final String[] entry = pair.split("=");
				if (entry.length > 1)
					items.increase(entry[0].trim().replaceAll("\\{", ""),
					               Integer.parseInt(entry[1].trim().replaceAll("\\}", "")));
				else
					items.increase(entry[0].trim().replaceAll("\\{", ""), 1);
			}
			return items;
		case EQUIPMENT:
			final DzEquipment equipment = new DzEquipment();
			final String value2 = (String) object;
			value2.substring(1, value2.length() - 1);
			final String[] keyValuePairs2 = value2.split(",");
			for (String pair2 : keyValuePairs2)
			{
				final String[] entry2 = pair2.split("=");
				if (entry2.length > 1)
					equipment.increase(entry2[0].trim().replaceAll("\\{", ""),
					                   Integer.parseInt(entry2[1].trim().replaceAll("\\}", "")));
				else
					equipment.increase(entry2[0].trim().replaceAll("\\{", ""), 1);

			}
			return equipment;
		}
		return null;
	}}
