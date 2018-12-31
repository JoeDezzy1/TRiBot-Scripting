package scripts.orbcrafter.core.operation.control.character.suppliers;

import org.tribot.api.General;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSItem;

import scripts.dezapi.core.control.parameter.StateHooker;
import scripts.dezapi.core.control.parameter.Thought;
import scripts.dezapi.core.control.parameter.utility.Loadable;
import scripts.dezapi.core.environment.DzBanking;
import scripts.dezapi.core.environment.DzEquipment;
import scripts.dezapi.core.environment.DzInventory;
import scripts.dezapi.utility.constant.Constants;
import scripts.orbcrafter.core.operation.control.ScriptController;

import java.util.function.Predicate;

/**
 * Handles item equipting in a nice way
 *
 * @author JoeDezzy1
 */
public abstract class EquipmentController extends ScriptController
{
	private int size;
	private int updateFlag;

	/**
	 * Equipment processor
	 */
	public EquipmentController()
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
		return "Equipter";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Handler#active()
	 */
	@Override
	public boolean active()
	{
		return DzBanking.isInBank() ? this.canEquipt() : this.getActive() != null;
	}

	/**
	 * Determines if we can equipt items in the bank
	 *
	 * @return true if we have all the items in the inventory/equipment tab and atleast one is in the inventory
	 */
	public boolean canEquipt()
	{
		return this.getUnequiptedItemCount() > 0 && this.getEquipmentHeld() == this.size;
	}

	/**
	 * @param filter
	 * @param amount
	 * @return
	 */
	public StateHooker isEquipt(final Predicate<RSItem> filter, final int amount)
	{
		return () -> Equipment.getCount(filter) >= amount;
	}

	/**
	 * @param amount
	 * @param names
	 * @return
	 */
	public Thought<?> equipt(final int amount, final String... names)
	{
		return super.item(false,
		                  null,
		                  this.filter(names),
		                  this.isEquiptable(amount, names),
		                  this.isEquipt(filter(names), amount));
	}

	/**
	 * @param amount
	 * @param names
	 * @return
	 */
	public StateHooker isEquiptable(int amount, String... names)
	{
		final Predicate<RSItem> filter = Filters.Items.nameEquals(names);
		return () -> DzInventory.getTotalCount(filter) == 1 && DzEquipment.find(filter).length == 0;
	}

	/**
	 * Gets the amount of equipment held on the player in both the inventory and equipment tabs
	 *
	 * @return the amount of equipment held by the player
	 */
	public int getEquipmentHeld()
	{
		int holding = 0;
		for (final String equipment : this.connection().equipment().items().keySet())
		{
			final int equipt = DzEquipment.getCount(equipment);
			if (equipment.equals("Amulet of glory(6)"))
			{
				if (equipt > 0 || DzEquipment.getCount(Constants.Items.CHARGED_GLORY) > 0 ||
				    DzInventory.find(Constants.Items.CHARGED_GLORY).length > 0)
					holding++;
			}
			else if (equipt > 0 || DzInventory.find(equipment).length > 0)
				holding++;
		}
		return holding;
	}

	/**
	 * Gets the amount of unequipted items
	 *
	 * @return the count of items that are currently unequipted
	 */
	public int getUnequiptedItemCount()
	{
		int unequipted = 0;
		for (final String equipment : this.connection().equipment().items().keySet())
		{
			if (equipment.equals("Amulet of glory(6)"))
			{
				if (DzEquipment.getCount(Constants.Items.CHARGED_GLORY) == 0)
					unequipted++;
			}
			else if (DzEquipment.getCount(equipment) == 0)
				unequipted++;
		}
		return unequipted;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Handler#setNodes()
	 */
	@Override
	public void load()
	{
		General.println(getName() + "=> loading equipment...");
		for (final String equipment : this.connection().equipment().items().keySet())
			if (equipment.equals("Amulet of glory(6)"))
				super.add(0, this.equipt(1, Constants.Items.CHARGED_GLORY));
			else
				super.add(0, this.equipt(1, equipment));
		/** +1 for glory */
		this.size = this.connection().equipment().items().size();
		this.deviate(0);
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
			this.updateFlag = General.random(1, 5);
			switch (this.updateFlag)
			{
			case 1:
				if (abc().shouldMoveToAnticipated())
				{
					super.deviate(0);
					this.updateFlag = General.random(1, 5);
				}
				break;
			case 2:
				if (abc().shouldRightClick())
					super.deviate(0);
				break;
			case 3:
				if (abc().shouldMoveMouse())
					super.deviate(0);
				break;
			case 4:
				if (abc().shouldCheckXP())
				{
					super.deviate(0);
					this.updateFlag = General.random(1, 5);
				}
				break;
			case 5:
				if (abc().shouldCheckTabs())
					super.deviate(0);
				break;
			}
		};
	}
}