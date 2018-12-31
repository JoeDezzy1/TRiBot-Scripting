package scripts.orbcrafter.core.api.types;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
/**
 * @author JoeDezzy1
 */
public class Data
{
	public static final String[] STAVES = new String[] { "Staff of air",
	                                                     "Air battlestaff",
	                                                     "Staff of earth",
	                                                     "Earth battlestaff" };
	public static final String UNPOWERED_ORB = "Unpowered orb";
	public static final String COSMIC_RUNE = "Cosmic rune";
	public static final String[] PKER_RESPONSES = new String[] { "fag",
	                                                             "pussy",
	                                                             "youre gay",
	                                                             "gay",
	                                                             "get a life bro",
	                                                             "get a life my dude",
	                                                             "fuck off...",
	                                                             "fuck off",
	                                                             "dude gtfo",
	                                                             "dude...",
	                                                             "gtfo!",
	                                                             "gtfo...",
	                                                             "really man?",
	                                                             "really?",
	                                                             "really...",
	                                                             "sad",
	                                                             "sad...",
	                                                             "wanna stop?",
	                                                             "wanna not",
	                                                             "wow..",
	                                                             "wow.. youre sweet",
	                                                             "youre cool",
	                                                             "wtf...",
	                                                             "u dick",
	                                                             "u dickhead",
	                                                             "dickhead",
	                                                             "dick",
	                                                             "puss",
	                                                             "ur a puss" };

	public static final RSArea DUNGEON_ENTRANCE_WALK_AREA = new RSArea(new RSTile(3095, 3473, 0),
	                                                                   new RSTile(3093, 3470, 0));
	public static final RSArea DUNGEON_ENTRANCE = new RSArea(new RSTile(3097, 3473, 0), new RSTile(3092, 3468, 0));
	public static final RSArea EDGE_BANK = new RSArea(new RSTile(3091, 3499, 0), new RSTile(3098, 3488, 0));
	public static final RSArea LUMBRIDGE_BANK = new RSArea(new RSTile(3207, 3222, 2), new RSTile(3210, 3218, 2));
	public static final RSTile[] TO_LADDER = { new RSTile(3096, 9867, 0), new RSTile(3096, 9873, 0),
			new RSTile(3096, 9879, 0), new RSTile(3095, 9884, 0), new RSTile(3096, 9891, 0), new RSTile(3096, 9898, 0),
			new RSTile(3096, 9903, 0), new RSTile(3098, 9907, 0), new RSTile(3103, 9909, 0), new RSTile(3110, 9909, 0),
			new RSTile(3115, 9909, 0), new RSTile(3121, 9908, 0), new RSTile(3125, 9909, 0), new RSTile(3129, 9911, 0),
			new RSTile(3130, 9913, 0), new RSTile(3131, 9916, 0), new RSTile(3132, 9917, 0), new RSTile(3132, 9922, 0),
			new RSTile(3132, 9928, 0), new RSTile(3132, 9934, 0), new RSTile(3132, 9940, 0), new RSTile(3132, 9946, 0),
			new RSTile(3128, 9949, 0), new RSTile(3124, 9952, 0), new RSTile(3120, 9954, 0), new RSTile(3111, 9957, 0),
			new RSTile(3108, 9954, 0), new RSTile(3103, 9954, 0), new RSTile(3097, 9957, 0), new RSTile(3095, 9959, 0),
			new RSTile(3090, 9961, 0), new RSTile(3088, 9964, 0), new RSTile(3089, 9968, 0),
			new RSTile(3088, 9970, 0) };
}
