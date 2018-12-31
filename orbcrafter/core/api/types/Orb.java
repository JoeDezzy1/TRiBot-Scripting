package scripts.orbcrafter.core.api.types;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import scripts.dezapi.utility.constant.EdgevilleDungeonData;

/**
 * @author JoeDezzy1
 */
public enum Orb
{
	AIR_ORB(EdgevilleDungeonData.AIR_OBELISK_AREA,
	        new RSTile(3088, 3569, 0),
	        Data.TO_LADDER,
	        "Obelisk of Air",
	        "Charge Air Orb"),
	EARTH_ORB(new RSArea(EdgevilleDungeonData.EARTHORB_POSITION, EdgevilleDungeonData.EARTHORB_POSITION),
	          new RSTile(3088, 3569, 0),
	          EdgevilleDungeonData.EARTHORB_TO_OBELISK,
	          "Obelisk of Earth",
	          "Charge Earth Orb");

	private RSArea area;
	private RSTile[] path;
	private RSTile position;
	private String spell;
	private String name;

	/**
	 * @param area
	 * @param position
	 * @param name
	 * @param spell
	 */
	Orb(RSArea area, RSTile position, RSTile[] path, String name, String spell)
	{
		this.position = position;
		this.spell = spell;
		this.path = path;
		this.area = area;
		this.name = name;
	}

	/**
	 * @return
	 */
	public RSTile getPosition()
	{
		return position;
	}

	/**
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return
	 */
	public String getSpell()
	{
		return spell;
	}

	/**
	 * @return
	 */
	public RSArea getArea()
	{
		return area;
	}

	/**
	 * @return
	 */
	public RSTile[] getPath()
	{
		return path;
	}}
