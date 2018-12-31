package scripts.orbcrafter.core.api.wrappers.character;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projectiles;
import org.tribot.api2007.types.RSCharacter;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSProjectile;

import scripts.dezapi.core.environment.wrap.NPCWrapper;
import scripts.dezapi.core.environment.wrap.PlayerWrapper;
/**
 * @author JoeDezzy1
 */
public class RSPlayerWrapper extends PlayerWrapper
{
	/**
	 * Creates a player around our player
	 */
	public RSPlayerWrapper(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.wrapper.Wrapper#unbox()
	 */
	@Override
	public RSPlayer unbox() {
		return Player.getRSPlayer();
	}

	/**
	 * TODO Ensure this wont flag that were attacking a monster when another monster
	 * is attacking us in multicombat
	 *
	 * @return
	 */
	public RSCharacter getTarget() {
		RSPlayer us = this.unbox();
		return us != null ? us.getInteractingCharacter() : null;
	}

	/**
	 *
	 * @param wrapper
	 * @return
	 */
	public boolean isAttacking(NPCWrapper wrapper) {
		RSCharacter target = wrapper.unbox(), actualTarget = getTarget();
		String name;
		return actualTarget != null
		       && target != null && (name = target.getName()) != null && name.equals(actualTarget.getName());
	}

	/**
	 * Gets all projectiles targeting us
	 *
	 * @return gets all projectile's targeting us
	 */
	public RSProjectile[] getTargetedProjectiles() {
		return Projectiles.getAll(new Filter<RSProjectile>() {
			@Override
			public boolean accept(RSProjectile arg0) {
				return arg0 != null && arg0.isTargetingMe();
			}
		});
	}
}