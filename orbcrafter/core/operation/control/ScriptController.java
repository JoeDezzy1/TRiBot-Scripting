/**
 *
 */
package scripts.orbcrafter.core.operation.control;

import org.tribot.api.Clicking;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSCharacter;

import scripts.dezapi.core.antiban.ABC2;
import scripts.dezapi.core.control.ControlBase;
import scripts.dezapi.core.control.parameter.StateHooker;
import scripts.dezapi.core.control.parameter.Thought;
import scripts.dezapi.core.control.parameter.clickable.ext.ParamTeleGlory;
import scripts.dezapi.core.control.parameter.clickable.ext.ParamTeleHouseSpell;
import scripts.dezapi.core.control.parameter.clickable.ext.ParamTeleHouseTab;
import scripts.dezapi.core.control.parameter.clickable.screen.interfaces.panels.ParamWorldHop;
import scripts.dezapi.core.control.parameter.clickable.screen.walkables.webbed.ParamWalkBank;
import scripts.dezapi.core.control.parameter.utility.Connection;
import scripts.dezapi.core.control.parameter.utility.Loadable;
import scripts.dezapi.core.environment.wrap.ENERGY;
import scripts.dezapi.core.environment.wrap.FOOD;
import scripts.dezapi.core.environment.wrap.WorldWrapper;
import scripts.dezapi.utility.constant.Constants;
import scripts.orbcrafter.core.gui.settings.Settings;
import scripts.orbcrafter.core.api.types.Data;
import scripts.orbcrafter.core.api.wrappers.character.RSPkerWrapper;
import scripts.orbcrafter.core.api.wrappers.character.RSPlayerWrapper;

/**
 * @author JoeDezzy1
 */
public abstract class ScriptController extends ControlBase implements Connection<Settings>
{
	public ScriptController()
	{
		super();
	}

	/**
	 * Toggle the login on or off
	 *
	 * @param on
	 */
	public void scheduleWorldHop(boolean on)
	{
		this.connection().scheduleWorldhop(on);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tribot.script.interfaces.Starting#onStart()
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		this.load();
		this.loaded = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.interfaces.Updater#update()
	 */
	public Loadable update()
	{
		return () -> {
		};
	}

	/**
	 *
	 * @return
	 */
	public StateHooker canEatFood()
	{
		return () -> this.abc().shouldEat();
	}

	/**
	 * @return a wrapper for the player character instance of our bot
	 */
	public RSPlayerWrapper getPlayer()
	{
		return new RSPlayerWrapper(Player.getRSPlayer().getName());
	}

	/**
	 * @return a wrapper for the hostile players who can kill us or otherwise get us
	 *         in trouble
	 */
	public RSPkerWrapper getPkers()
	{
		return new RSPkerWrapper(Player.getRSPlayer().getName(), Player.getRSPlayer().getCombatLevel());
	}

	/**
	 * @return true if pker is present
	 */
	public StateHooker isPkerPresent()
	{
		return () -> this.getPkers().onScreen() || this.getPkers().unbox() != null;
	}

	/**
	 * Can we drink stamina in bank
	 *
	 * @return the state when we can drink stamina potions in the bank
	 */
	public StateHooker canDrinkStaminaInBank()
	{
		return () -> Game.getSetting(Constants.GameSettings.STAMINA_POTIONS) == Constants.GameSettings.STAMINA_OFF ||
		             Game.getRunEnergy() < this.abc().generateRunActivation();
	}

	/**
	 *
	 * @return
	 */
	public StateHooker canDrinkEnergy()
	{
		return () -> this.connection().energy() == ENERGY.STAMINA_POTION ?
		             Game.getSetting(Constants.GameSettings.STAMINA_POTIONS) == Constants.GameSettings.STAMINA_OFF :
		             Game.getRunEnergy() <= this.abc().generateRunActivation();

	}

	/**
	 *
	 * @param potion
	 * @return
	 */
	public Thought energy(ENERGY potion)
	{
		return super.item(false, null, this.canDrinkEnergy(), () -> {
			if (this.canDrinkEnergy().active())
				return false;
			this.abc().resetRunActivation();
			return true;
		}, potion.getNames());
	}

	/**
	 *
	 * @param food
	 * @return
	 */
	public Thought eat(FOOD food)
	{
		return super.item(null, this.canEatFood(), () -> {
			if (!this.canEatFood().active())
				return false;
			this.abc().resetNextEat();
			return true;
		}, food.getNames());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.control.ControlBase#universallyEnsuredCondition(boolean,
	 * boolean, boolean, boolean)
	 */
	@Override
	public StateHooker universallyEnsuredCondition(boolean eat, boolean breakOnNoFood, boolean run,
	                                               boolean breakOnPker)
	{
		return () -> {
			if (eat && this.canEatFood().active())
			{
				if (breakOnNoFood &&
				    Inventory.find(Filters.Items.nameEquals(this.connection().food().getNames())).length == 0)
					return true;

				Clicking.click(Inventory.find(Filters.Items.nameEquals(this.connection().food().getNames())));
			}
			if (run && this.abc().shouldRun())
				this.instantRun();
			if (breakOnPker && this.getPkers().unbox() != null)
				return true;
			return false;
		};
	}

	/**
	 * Adds a bank walk
	 */
	public void addBankWalk(int priority)
	{
		super.add(priority, new ParamWalkBank()
		{
			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}

			@Override
			public StateHooker valid()
			{
				return () -> !Data.EDGE_BANK.contains(Player.getPosition()) &&
				             !Data.LUMBRIDGE_BANK.contains(Player.getPosition());
			}
		});
	}

	/**
	 * Tells if a logout is possible
	 *
	 * @return a state where a logout is possible do to no interacting characters &
	 *         no pkers
	 */
	public StateHooker isLogoutPossible()
	{
		return () -> Combat.getAttackingEntities().length == 0 && !Combat.isUnderAttack() &&
		             this.getPkers().unbox() == null && this.getPlayer().getTarget() == null &&
		             this.getPlayer().getTargetedProjectiles().length == 0 && Combat.getTargetEntity() == null &&
		             !this.isNPCAttackingUs().active();
	}

	/**
	 * The hook for if an npc is attacking us
	 *
	 * @return the state when an NPC is attacking us
	 */
	public StateHooker isNPCAttackingUs()
	{
		return () -> NPCs.findNearest((arg0) -> {
			if (arg0 == null)
				return false;
			final RSCharacter interacting;
			if ((interacting = arg0.getInteractingCharacter()) == null)
				return false;
			final String name = interacting.getName();
			if (name == null)
				return false;
			return name.equals(Player.getRSPlayer().getName());
		}).length > 0;
	}

	/**
	 *
	 * @return
	 */
	public Thought worldHop(StateHooker... active)
	{
		return new ParamWorldHop()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see scripts.dezapi.controller.ThoughtParam#isWaitingFixed()
			 */
			@Override
			public boolean isWaitingFixed()
			{
				return false;
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see scripts.dezapi.interfaces.Antibanable#abc()
			 */
			@Override
			public ABC2 abc()
			{
				return (ABC2) copyAntiban().get();
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see scripts.dezapi.control.parameter.ParamWorldHop#wrapper()
			 */
			@Override
			public WorldWrapper wrapper()
			{
				return new WorldWrapper(connection().getNextWorld())
				{
					@Override
					public Integer unbox()
					{
						return connection().getNextWorld();
					}
				};
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see scripts.dezapi.control.parameter.ParamWorldHop#valid()
			 */
			@Override
			public StateHooker valid()
			{
				return () -> {
					if (active.length > 0)
					{
						if (!active[0].active())
						{
							return false;
						}
					}
					if (connection().getNextWorld() != -1)
					{
						final int current = Game.getCurrentWorld();
						final int world = wrapper().unbox().intValue();
						return world != -1 && current != world + 300 && isLogoutPossible().active();
					}
					return false;
				};
			}
		};
	}

	/**
	 * Adds a teleport method
	 *
	 * @param pos
	 *            - the priority to place it under
	 */
	public void addTeleportMethod(final int pos)
	{
		switch (this.connection().bankMethod())
		{
		case GLORY:
			super.add(pos, new ParamTeleGlory()
			{
				@Override
				public StateHooker valid()
				{
					return () -> super.valid().active() && connection().getNextWorld() != -1;
				}

				@Override
				public boolean isWaitingFixed()
				{
					return getPkers().unbox() != null;
				}

				@Override
				public ABC2 abc()
				{
					return (ABC2) copyAntiban().get();
				}
			});
			break;
		case HOUSE_TAB:
			super.add(pos, new ParamTeleHouseTab()
			{
				@Override
				public StateHooker valid()
				{
					return () -> super.valid().active() && connection().getNextWorld() != -1;
				}

				@Override
				public boolean isWaitingFixed()
				{
					return getPkers().unbox() != null;
				}

				@Override
				public ABC2 abc()
				{
					return (ABC2) copyAntiban().get();
				}
			});
			break;
		case HOUSE_RUNES:
			super.add(pos, new ParamTeleHouseSpell()
			{
				@Override
				public StateHooker valid()
				{
					return () -> super.valid().active() && connection().getNextWorld() != -1;
				}

				@Override
				public boolean isWaitingFixed()
				{
					return getPkers().unbox() != null;
				}

				@Override
				public ABC2 abc()
				{
					return (ABC2) copyAntiban().get();
				}
			});
			break;
		}
	}
}
