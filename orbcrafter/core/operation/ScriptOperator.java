package scripts.orbcrafter.core.operation;

import java.awt.Graphics;

import javafx.stage.WindowEvent;
import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Magic;
import org.tribot.api2007.Player;
import org.tribot.api2007.ext.Filters;
import org.tribot.script.interfaces.Painting;

import scripts.dezapi.core.antiban.ABC2;
import scripts.dezapi.core.environment.DzLogin;
import scripts.dezapi.core.processor.Processor;
import scripts.dezapi.core.processor.operator.OP_RESPONSE;
import scripts.dezapi.core.processor.operator.Operations;
import scripts.dezapi.utility.constant.EdgevilleData;
import scripts.orbcrafter.core.gui.settings.Settings;
import scripts.orbcrafter.core.operation.control.*;
import scripts.orbcrafter.core.operation.control.character.suppliers.BankingController;
import scripts.orbcrafter.core.operation.control.character.crafting.CraftingController;
import scripts.orbcrafter.core.operation.control.character.suppliers.EquipmentController;
import scripts.orbcrafter.core.operation.control.character.CharacterController;
import scripts.orbcrafter.core.operation.control.navigation.escaping.EscapeController;
import scripts.orbcrafter.core.operation.control.navigation.NavigationController;
import scripts.orbcrafter.core.operation.control.navigation.escaping.misc.PohController;
import scripts.orbcrafter.core.operation.control.navigation.escaping.WorldController;
import scripts.orbcrafter.core.api.types.Orb;
import scripts.orbcrafter.core.api.types.Setting;
import scripts.orbcrafter.core.gui.JFXUI;
import scripts.dezapi.core.environment.wrap.*;
import scripts.orbcrafter.core.api.trackers.ProgressPaint;

/**
 * @author JoeDezzy1
 */
public class ScriptOperator extends Operations<String, ScriptController> implements Painting
{
	/**
	 * The paint
	 */
	private ProgressPaint paint;
	/**
	 * The orb loading properties
	 */
	private Settings properties;
	/**
	 * The GUI
	 */
	private JFXUI gui;

	/**
	 * Creates a new orb crafter
	 */
	public ScriptOperator()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.operator.Operator#getName()
	 */
	@Override
	public String getName()
	{
		return "AIO Orb Crafter";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.ops.depl.type.Operations#preOp()
	 */
	@Override
	public void onCycle()
	{
		super.abc.actions();
	}

	/**
	 * Does actions on break end
	 */
	public void onBreakEnd()
	{
		this.paint.unpause();
	}

	/**
	 * @return
	 */
	public boolean isHoppingWorlds()
	{
		return properties.shouldHopWorlds();
	}

	/**
	 * toggles the paint on and off
	 */
	public void togglePaint()
	{
		this.paint.show(this.paint.show() ? false : true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tribot.script.interfaces.Painting#onPaint(java.awt.Graphics)
	 */
	@Override
	public void onPaint(Graphics arg0)
	{
		if (this.paint != null)
			this.paint.draw(arg0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Operator#update()
	 */
	@Override
	public void update()
	{
		this.updateWorldhops();
		for (final Processor c : super.processors)
			c.update().load();
	}

	/**
	 * Initializes the gui
	 */
	private void initGUI()
	{
		General.println("Waiting for login... Please allow the script to login, or login manually");
		this.gui = new JFXUI()
		{

			/**
			 * Handle what happens on the GUI's exit within this methods abstract implementation (Stop
			 * your scripts)
			 *
			 * @param event
			 * 		- the event which invoked the GUI to exit
			 */
			@Override
			public void onExit(final WindowEvent event)
			{

			}
		};
		while (!this.gui.isComplete())
			General.sleep(150);
	}

	/**
	 * Does antiban seeding
	 */
	private void seed()
	{
		String seed = Player.getRSPlayer().getName();
		if (seed == null)
			seed = "RANDOM" + System.currentTimeMillis();
		super.abc.seed();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.operator.impl.handlers.nodes.Endable#onEnd()
	 */
	@Override
	public void onEnd()
	{
		if (this.gui != null && this.gui.isShowing())
			this.gui.close();
		
		super.abc.close();
		for (final Processor c : super.processors)
			c.onEnd();
	}

	/**
	 * Updates the world hops
	 */
	private void updateWorldhops()
	{
		if (this.properties.getNextWorld() == -1)
			return;

		if (Game.getCurrentWorld() != (this.properties.getNextWorld() + 300))
			return;

		this.abc.setDelayable(true);
		this.properties.scheduleWorldhop(false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.operator.Operator#operate()
	 */
	@Override
	public OP_RESPONSE operate()
	{
		this.update();
		final ScriptController active = (ScriptController) super.getActive();
		return active != null ? active.handle() : OP_RESPONSE.INACTIVE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tribot.script.interfaces.Starting#onStart()
	 */
	@Override
	public void onStart()
	{
		try
		{
			DzLogin.waitLogin();
			this.initGUI();
			this.properties = this.gui.getController().properties;
			super.initializeAntiban(null, Player.getRSPlayer().getName(), true, true);
			this.seed();
			this.init();
			this.togglePaint();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Does actions on Break
	 */
	public void onBreak()
	{
		this.paint.pause();
		if (EdgevilleData.EDGEVILLE.contains(Player.getPosition()))
			return;

		final BANK_METHOD METHOD = (BANK_METHOD) properties.get(Setting.BANK_METHOD);
		switch (METHOD)
		{
		case GLORY:
			Clicking.click("Edgeville", Equipment.find(Filters.Items.nameContains("Amulet of glory")));
			break;
		case HOUSE_RUNES:
			Clicking.click("Break", Inventory.find(Filters.Items.nameContains("Teleport to house")));
			break;
		case HOUSE_TAB:
			Magic.selectSpell("Teleport to house");
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see scripts.dezapi.handlers.Operator#init()
	 */
	@Override
	public void init()
	{
		this.paint = new ProgressPaint((Orb) properties.get(Setting.ORB));
		this.add(new EscapeController()
		{
			@Override
			public ABC2 abc()
			{
				return abc;
			}

			@Override
			public Settings connection()
			{
				return properties;
			}
		});
		final BANK_METHOD METHOD = (BANK_METHOD) this.properties.get(Setting.BANK_METHOD);
		if (METHOD == BANK_METHOD.HOUSE_TAB || METHOD == BANK_METHOD.HOUSE_RUNES)
		{
			this.add(new PohController()
			{
				@Override
				public ABC2 abc()
				{
					return abc;
				}

				@Override
				public Settings connection()
				{
					return properties;
				}
			});
		}
		this.add(new CharacterController()
		{
			@Override
			public ABC2 abc()
			{
				return abc;
			}

			@Override
			public Settings connection()
			{
				return properties;
			}

		});
		this.add(new WorldController()
		{
			@Override
			public ABC2 abc()
			{
				return abc;
			}

			@Override
			public Settings connection()
			{
				return properties;
			}
		});
		this.add(new EquipmentController()
		{
			@Override
			public ABC2 abc()
			{
				return abc;
			}

			@Override
			public Settings connection()
			{
				return properties;
			}
		});
		this.add(new BankingController()
		{
			@Override
			public ABC2 abc()
			{
				return abc;
			}

			@Override
			public Settings connection()
			{
				return properties;
			}

		});
		this.add(new CraftingController()
		{
			@Override
			public ABC2 abc()
			{
				return abc;
			}

			@Override
			public Settings connection()
			{
				return properties;
			}
		});
		this.add(new NavigationController()
		{
			@Override
			public ABC2 abc()
			{
				return abc;
			}

			@Override
			public Settings connection()
			{
				return properties;
			}
		});
	}
}