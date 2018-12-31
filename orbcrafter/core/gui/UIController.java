package scripts.orbcrafter.core.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import org.tribot.api2007.Equipment;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSItemDefinition;
import org.tribot.util.Util;

import com.allatori.annotations.DoNotRename;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import scripts.dezapi.core.environment.wrap.*;
import scripts.orbcrafter.core.gui.settings.Settings;
import scripts.orbcrafter.core.api.types.Orb;
import scripts.orbcrafter.core.api.types.Setting;

/**
 * The controller for the FXUI for the orb crafter
 *
 * @author JoeDezzy1
 */
@DoNotRename
public class UIController implements Initializable
{
	@DoNotRename
	public String[] equipment;
	@FXML
	@DoNotRename
	public ComboBox<FOOD> foodtype;
	@FXML
	@DoNotRename
	public ComboBox<Orb> orbtype;
	@FXML
	@DoNotRename
	public ComboBox<String> foodpertrip;
	@FXML
	@DoNotRename
	public ComboBox<ENERGY> energybooster;
	@FXML
	@DoNotRename
	public ComboBox<BANK_METHOD> teleporttype;
	/**
	 * True if the user completed the gui
	 */
	private boolean complete;
	private boolean presetLoaded;
	/**
	 * The properties
	 */
	public Settings properties;
	/**
	 * Tracks if the user has hit the close button available
	 */
	private boolean userExited;

	/**
	 * Determines if the UI is completed
	 *
	 * @return true if completed
	 */
	@DoNotRename
	public boolean isComplete()
	{
		return this.complete;
	}

	/**
	 * Parses and adds all the user input
	 */
	@FXML
	@DoNotRename
	public void startButtonAction()
	{
		if (!presetLoaded)
			this.loadProperties();
		this.complete = true;
	}

	/**
	 * Closes the GUI
	 */
	@FXML
	@DoNotRename
	public void closeButtonAction()
	{
		try
		{
			this.complete = true;
			this.userExited = true;
		}
		catch (final Throwable e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Saves the current settings for next time
	 */
	@FXML
	@DoNotRename
	public void saveButtonAction()
	{
		final String directory = (Util.getWorkingDirectoryURL().toString().replaceFirst("\\/", "") + "/" +
		                          "AirOrbScriptLoader" + ".properties").replaceAll("file:", "");
		System.out.println("Loading the properties...");
		this.loadProperties();
		final Properties properties = new Properties();
		for (Setting setting : this.properties.keySet())
		{
			System.out.println("Loading: " + setting.toString() + " = " + this.properties.get(setting));
			properties.put(setting.toString(), this.properties.get(setting).toString());
		}
		System.out.println("Saving the file to: " + directory);
		this.saveProperties(directory, properties);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
	 */
	@Override
	@DoNotRename
	public void initialize(URL location, ResourceBundle resources)
	{
		final String directory = (Util.getWorkingDirectoryURL().toString().replaceFirst("\\/", "") + "/" +
		                          "AirOrbScriptLoader" + ".properties").replaceAll("file:", "");
		this.foodtype.setItems(FXCollections.observableArrayList(FOOD.values()));
		this.orbtype.setItems(FXCollections.observableArrayList(Orb.values()));
		this.energybooster.setItems(FXCollections.observableArrayList(ENERGY.values()));
		this.teleporttype.setItems(FXCollections.observableArrayList(BANK_METHOD.values()));
		final File file = new File(directory);
		if (file.exists())
		{
			this.properties = new Settings();
			this.loadSettings(file);
		}
	}

	/**
	 * Loads up the equipment
	 *
	 * @return the equipment to load
	 */
	private DzEquipment loadEquipment()
	{
		final DzEquipment equipment = new DzEquipment();
		if (this.equipment != null)
		{
			for (final String item : this.equipment)
			{
				if (!item.contains("Amulet of glory") && !item.contains("Staff of") && !item.contains("battlestaff"))
				{
					equipment.increase(item, 1);
				}
			}
		}
		return equipment;
	}

	/**
	 * Saves the equipment
	 */
	@FXML
	@DoNotRename
	public void setEquipmentButtonAction()
	{
		try
		{
			final ArrayList equipment = new ArrayList<String>();
			final String[] names = this.convertNames(Equipment.getItems());
			for (final String name : names)
				if (!name.contains("Staff of") && !name.contains("Amulet of glory") && !name.contains("battlestaff"))
					equipment.add(name);
			this.equipment = (String[]) equipment.toArray(new String[0]);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.out.println("That didn't quite work, please save the equipment again...");
		}
	}

	/**
	 * Converts RSItem names to a String[] array
	 *
	 * @param items
	 * @return
	 */
	@DoNotRename
	private String[] convertNames(RSItem[] items)
	{
		if (items.length == 0)
			return new String[0];

		final String[] names = new String[items.length];
		for (int x = 0; x < names.length; x++)
		{
			final String name = this.parseName(items[x]);
			if (name != null)
				names[x] = name;
		}
		return names;
	}

	/**
	 * @param item
	 * @return
	 */
	private String parseName(final RSItem item)
	{
		if (item == null)
			return null;

		final RSItemDefinition def = item.getDefinition();
		return def != null ? def.getName() : null;
	}

	/**
	 * Loads the settings from a file
	 */
	public void onLoadSettings()
	{
		final File loaded;
		final FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(Util.getWorkingDirectory());
		chooser.setTitle("Choose client properties...");
		if ((loaded = chooser.showOpenDialog(null)) != null)
		{
			this.loadSettings(loaded);
		}
	}

	/**
	 * Sets the buttons to the properties accordingly
	 */
	private void setButtonsToSettings()
	{
		this.orbtype.getSelectionModel().select(this.properties.orb());
		this.foodtype.getSelectionModel().select(this.properties.food());
		this.energybooster.getSelectionModel().select(this.properties.energy());
		this.teleporttype.getSelectionModel().select(this.properties.bankMethod());
		this.foodpertrip.getSelectionModel().select(this.properties.foodAmount());
	}

	/**
	 * Loads settings up from a file
	 *
	 * @param loaded
	 * 		- the file loaded
	 */
	private void loadSettings(File loaded)
	{
		try
		{
			final Properties properties = new Properties();
			properties.load(new FileInputStream(loaded));
			if (properties.size() > 0)
			{
				for (final String key : properties.stringPropertyNames())
				{
					final Setting value = Setting.valueOf(key.toUpperCase());
					if (value != null)
					{
						final Object result = value.parse(properties.get(key));
						if (result != null)
							this.properties.load(value, result);
					}
				}
				this.presetLoaded = true;
				this.setButtonsToSettings();
				System.out.println("Loaded settings: " + this.properties.toString());
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param directory
	 * @param properties
	 */
	private void saveProperties(String directory, Properties properties)
	{
		final File saving = new File(directory);
		try
		{
			if (saving.exists())
				saving.delete();

			if (saving.createNewFile())
			{

				FileOutputStream stream = null;
				try
				{
					stream = new FileOutputStream(directory);
					properties.store(stream, null);
					System.out.println("Saved settings to: " + directory);
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
				finally
				{
					if (stream != null)
						stream.close();
				}
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Loads the script properties
	 */
	private void loadProperties()
	{
		this.properties = new Settings();
		this.properties.load(Setting.FOOD_AMOUNT,
		                     Integer.parseInt(this.foodpertrip.getSelectionModel().getSelectedItem()));
		this.properties.load(Setting.ORB, this.orbtype.getSelectionModel().getSelectedItem());
		this.properties.load(Setting.FOOD, this.foodtype.getSelectionModel().getSelectedItem());
		this.properties.load(Setting.BANK_METHOD, this.teleporttype.getSelectionModel().getSelectedItem());
		this.properties.load(Setting.ENERGY, this.energybooster.getSelectionModel().getSelectedItem());
		final DzItems items = new DzItems();
		final DzEquipment equipment = this.loadEquipment();
		if (this.orbtype.getValue() == Orb.AIR_ORB)
			equipment.increase("Staff of air", 1);
		else
			equipment.increase("Staff of earth", 1);
		if (this.teleporttype.getValue() == BANK_METHOD.GLORY)
			equipment.increase("Amulet of glory(6)", 1);
		items.increase(this.foodtype.getValue().getNames()[0],
		               Integer.parseInt(this.foodpertrip.getSelectionModel().getSelectedItem()));
		items.increase(this.energybooster.getValue().getNames()[0], 1);
		this.properties.load(Setting.EQUIPMENT, equipment);
		this.properties.load(Setting.ITEMS, items);
	}

	boolean isUserExited()
	{
		return userExited;
	}
}