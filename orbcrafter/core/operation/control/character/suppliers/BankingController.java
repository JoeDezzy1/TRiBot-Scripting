package scripts.orbcrafter.core.operation.control.character.suppliers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import scripts.dezapi.core.control.parameter.utility.Loadable;
import scripts.dezapi.core.environment.DzBanking;
import scripts.dezapi.core.environment.wrap.ENERGY;
import scripts.dezapi.utility.constant.Constants;
import scripts.orbcrafter.core.operation.control.ScriptController;
import scripts.orbcrafter.core.api.types.Data;

/**
 * @author JoeDezzy1
 */
public abstract class BankingController extends ScriptController
{
	private int deposits = 0;
	private int withdrawPriority = 0;
	private int equipmentPriority = 0;
	private int updateFlag;

	public BankingController()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Handler#getName()
	 */
	@Override
	public String getName()
	{
		return "Banker";
	}

	/**
	 * Are we out of supplies
	 *
	 * @return true if we are out of supplies
	 */
	private boolean isOutOfSupplies()
	{
		return Inventory.getCount(Data.COSMIC_RUNE) < 3 || Inventory.getCount(Data.UNPOWERED_ORB) < 1 ||
		       (this.abc().shouldEat() && this.needsFood());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Handler#active()
	 */
	@Override
	public boolean active()
	{
		return DzBanking.isInBank() ?
		       this.getActive() != null || Inventory.find(12626, 11979, 995, 568, 574, 573, 362, 1382, 229).length > 0 :
		       this.needsFood() || this.isOutOfSupplies();
	}

	/**
	 * Determines an emergency bank
	 *
	 * @return true to emergency bank
	 */
	public boolean needsFood()
	{
		final int count = Inventory.getCount(this.connection().food().getNames());
		if (Banking.isInBank())
		{
			return count < this.connection().foodAmount();
		}
		return count < 1 && this.abc().shouldEat();
	}

	/**
	 * Loads the equipment withdraws
	 */
	private void loadEquipmentWithdraws()
	{
		for (final String equipment : this.connection().equipment().items().keySet())
		{
			if (!equipment.equals("Amulet of glory(6)"))
			{
				General.println("Loaded equipment withdraw for: " + equipment);
				super.add(this.equipmentPriority, super.withdraw(1, 1, equipment));
			}
		}
	}

	/**
	 * Gets the inventory space for the withdraws that is taken up in total
	 *
	 * @return the total space these withdraws will take up
	 */
	private int loadItemWithdraws()
	{
		int inventorySpace = 0;
		withdraws:
		for (final String item : this.connection().items().items().keySet())
		{
			if (this.connection().foodAmount() == 0)
			{
				for (final String food : this.connection().food().getNames())
				{
					if (item.equals(food) || food.equals(item))
						continue withdraws;
				}
			}
			final int amount = this.connection().items().getCount(item);
			if (item.equals("Stamina potion(4)"))
			{
				super.add(this.equipmentPriority,
				          super.withdraw(1,
				                         amount,
				                         () -> this.canDrinkEnergy().active(),
				                         ENERGY.STAMINA_POTION.getNames()));
			}
			else if (item.equals("Energy potion(4)"))
			{
				inventorySpace += 1;
				super.add(this.withdrawPriority, super.withdraw(1, amount, ENERGY.ENERGY_POTION.getNames()));
			}
			else if (item.equals("Super energy(4)"))
			{
				inventorySpace += 1;
				super.add(this.withdrawPriority, super.withdraw(1, amount, ENERGY.SUPER_ENERGY.getNames()));
			}
			else
			{
				inventorySpace += amount;
				super.add(this.withdrawPriority, super.withdraw(1, amount, item));
			}
		}
		return inventorySpace;
	}

	/**
	 * Loads the bank teleport withdraws
	 *
	 * @return the amount of inventory space the bank teleports are going to take up
	 */
	private int loadBankTeleportWithdraws()
	{
		int inventorySpace = 0;
		switch (this.connection().bankMethod())
		{
		case GLORY:
			super.add(this.equipmentPriority, super.withdraw(1, 1, Constants.Items.CHARGED_GLORY));
			break;
		case HOUSE_TAB:
			inventorySpace = 1;
			final int amt = General.random(1, 10);
			super.add(this.withdrawPriority, super.withdraw(1, amt, Constants.Items.HOUSE_TAB));
			break;
		case HOUSE_RUNES:
			inventorySpace = 2;
			final int withdraw = General.random(1, 10);
			switch (this.connection().orb())
			{
			case AIR_ORB:
				super.add(this.withdrawPriority, super.withdraw(1, withdraw, Constants.Items.EARTH_RUNE));
				break;
			case EARTH_ORB:
				super.add(this.withdrawPriority, super.withdraw(1, withdraw, Constants.Items.AIR_RUNE));
				break;
			}
			super.add(this.withdrawPriority, super.withdraw(1, withdraw, Constants.Items.LAW_RUNE));
		}
		return inventorySpace;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Handler#setNodes()
	 */
	@Override
	public void load()
	{
		final int foodAmt = this.connection().foodAmount();
		int orbs = 27, cosmics = 81;
		/**
		 * Adds bank walk and teleport methods
		 */
		super.addTeleportMethod(0);
		super.addBankWalk(1);
		/**
		 * Grabs initial priority slots
		 */
		this.deposits = 2;
		this.equipmentPriority = 3;
		this.withdrawPriority = 5;
		/**
		 * Deposits for junk and amulet of glory
		 */
		super.add(this.deposits, super.deposit(Constants.Items.UNCHARGED_GLORY));
		super.add(this.deposits, super.deposit(12626, 11979, 995, 568, 574, 573, 362, 1382, 229));

		final List<String> allItems = new ArrayList<>();
		allItems.addAll(this.connection().equipment().items().keySet());
		allItems.addAll(this.connection().items().items().keySet());
		if (this.connection().energy() != null)
			allItems.addAll(Arrays.asList(this.connection().energy().getNames()));
		allItems.add(Data.UNPOWERED_ORB);
		allItems.add(Data.COSMIC_RUNE);
		allItems.addAll(Arrays.asList(Data.STAVES));
		allItems.addAll(Arrays.asList(this.connection().food().getNames()));
		switch (this.connection().bankMethod())
		{
		case GLORY:
			allItems.addAll(Arrays.asList(Constants.Items.CHARGED_GLORY));
			break;
		case HOUSE_RUNES:
			allItems.addAll(Arrays.asList("Law rune", "Earth rune"));
			break;
		case HOUSE_TAB:
			allItems.add("Teleport to house");
			break;
		default:
			break;
		}
		final String[] depositExceptThese = allItems.toArray(new String[allItems.size()]);
		super.add(this.deposits, this.depositExcept(depositExceptThese));
		/**
		 * Loads the equipment withdraws
		 */
		this.loadEquipmentWithdraws();
		/**
		 * Loads the teleport method and modifies the amount of orbs/cosmics to withdraw
		 */
		final int amount1 = this.loadBankTeleportWithdraws();
		orbs -= amount1;
		cosmics -= (amount1 * 3);
		/**
		 * Adds the deposit for stamina potions when we don't need to take one on the
		 * trip
		 */
		if (this.connection().energy() == ENERGY.STAMINA_POTION)
		{
			super.add(this.deposits,
			          super.deposit(() -> !this.canDrinkEnergy().active(), this.connection().energy().getNames()));
		}
		/**
		 * Loads the items and modifies the final amount of orbs/cosmics to withdraw
		 */
		final int amount2 = this.loadItemWithdraws();
		orbs -= amount2;
		cosmics -= (amount2 * 3);
		/**
		 * Deposits all excess equipment and supplies
		 */
		super.add(this.deposits, super.depositExcessEquipment(1, Data.STAVES));
		super.add(this.deposits, super.depositExcess(orbs, Data.UNPOWERED_ORB));
		super.add(this.deposits, super.depositExcess(cosmics, Data.COSMIC_RUNE));
		super.add(this.deposits, super.depositExcess(foodAmt, this.connection().food().getNames()));
		super.add(this.deposits, super.depositExcess(1, this.connection().energy().getNames()));
		/**
		 * Withdraws our food
		 */
		if (this.connection().foodAmount() > 0)
		{
			super.add(this.withdrawPriority, super.withdraw(foodAmt, foodAmt, this.connection().food().getNames()));
		}
		/**
		 * Withdraws the orbs and cosmic runes
		 */
		super.add(this.withdrawPriority, super.withdraw(orbs, orbs, Data.UNPOWERED_ORB));
		super.add(this.withdrawPriority, super.withdraw(cosmics, cosmics, Data.COSMIC_RUNE));
		/**
		 * Deviates the order of withdrawing
		 */
		this.deviate(this.deposits);
		this.deviate(this.withdrawPriority);
		this.deviate(this.equipmentPriority);
		this.loaded = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.prcs.ControlProcessor#update()
	 */
	@Override
	public Loadable update()
	{
		return () -> {
			if (!this.loaded)
				return;
			updateFlag = General.random(1, 5);
			switch (updateFlag)
			{
			case 1:
				if (abc().shouldMoveToAnticipated())
				{
					deviate(withdrawPriority);
					deviate(deposits);
					updateFlag = General.random(1, 5);
				}
				break;
			case 2:
				if (abc().shouldRightClick())
				{
					deviate(withdrawPriority);
					deviate(deposits);
				}
				break;
			case 3:
				if (abc().shouldMoveMouse())
				{
					deviate(withdrawPriority);
					deviate(deposits);
				}
				break;
			case 4:
				if (abc().shouldCheckXP())
				{
					deviate(withdrawPriority);
					deviate(deposits);
					updateFlag = General.random(1, 5);
				}
				break;
			case 5:
				if (abc().shouldCheckTabs())
				{
					deviate(deposits);
					deviate(withdrawPriority);
				}
				break;
			}
		};
	}
}