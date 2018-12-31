/**
 *
 */
package scripts.orbcrafter.core.api.wrappers.character;

import org.tribot.api2007.Combat;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSCharacter;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import scripts.dezapi.core.environment.wrap.PlayerWrapper;
import scripts.venenatis.utility.Utility;

/**
 * @author JoeDezzy1
 */
public class RSPkerWrapper extends PlayerWrapper
{
	public RSPkerWrapper(final String playerName, final int playerLevel)
	{
		super((arg0) -> {
			final int wildernessLevel = Combat.getWildernessLevel();
			if (wildernessLevel <= 0)
				return false;

			if (arg0.isInteractingWithMe())
				return true;

			final RSCharacter interacting = arg0.getInteractingCharacter();
			if (interacting instanceof RSPlayer && playerName.equals(interacting.getName()))
				return true;

			final int levelOpponent = arg0.getCombatLevel();
			if (levelOpponent <= 3)
				return false;

			final RSTile positionable = Player.getPosition();
			if (positionable == null || positionable.distanceTo(arg0) > 20)
				return false;

			final int difference = Utility.getLevelDifference(levelOpponent, playerLevel);
			return difference >= 0 && difference <= wildernessLevel;
		});
	}
}