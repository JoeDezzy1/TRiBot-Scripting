package scripts.orbcrafter.core.gui;

import java.io.ByteArrayInputStream;
import javax.swing.SwingUtilities;

import javafx.stage.WindowEvent;
import org.tribot.api.General;
import org.tribot.api.Timing;
import com.allatori.annotations.DoNotRename;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Creates a new Java FXML GUI Application for use within TRiBot, a swing native application
 *
 * @author JoeDezzy1
 */
public abstract class JFXUI extends Application
{
	@DoNotRename
	private Stage stage;
	@DoNotRename
	private Scene scene;
	@DoNotRename
	private UIController controller;
	@DoNotRename
	private boolean showing;

	/**
	 * Handle what happens on the GUI's exit within this methods abstract implementation (Stop your scripts)
	 *
	 * @param event
	 * 		- the event which invoked the GUI to exit
	 */
	public abstract void onExit(final WindowEvent event);

	/**
	 * Constructs a new GUI application within a swing native environment
	 */
	public JFXUI()
	{
		this.create();
	}

	/**
	 * Gets this GUI's stage
	 *
	 * @return the JFX GUI's stage object
	 */
	public Scene getScene()
	{
		return this.scene;
	}

	/**
	 * Gets this GUI's stage
	 *
	 * @return the JFX GUI's stage object
	 */
	public Stage getStage()
	{
		return this.stage;
	}

	/**
	 * Gets the GUI Controller for this class
	 *
	 * @return the controller for this class
	 */
	public UIController getController()
	{
		return this.controller;
	}

	/**
	 * Get whether or not the GUI is showing
	 *
	 * @return true if the GUI is showing
	 */
	public boolean isShowing()
	{
		return this.showing;
	}

	/**
	 * Get whether or not the GUI is completed
	 *
	 * @return true if the GUI has been completed
	 */
	public boolean isComplete()
	{
		return this.controller != null && this.controller.isComplete();
	}

	/**
	 * Shows the stage
	 */
	public void show()
	{
		if (this.stage != null)
		{
			this.showing = true;
			Platform.runLater(() -> this.stage.show());
		}
	}

	/**
	 * Closes the stage
	 */
	public void close()
	{
		Platform.runLater(() -> {
			if (this.stage != null)
			{
				this.showing = false;
				this.stage.close();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setTitle("Joe Dezzy's AIO Orb crafter");
		stage.setAlwaysOnTop(true);
		Platform.setImplicitExit(false);
		final FXMLLoader loader = new FXMLLoader();
		loader.setClassLoader(this.getClass().getClassLoader());
		loader.setController(this.controller = new UIController());
		final Parent box = loader.load(new ByteArrayInputStream(this.FXML.getBytes()));
		this.scene = new Scene(box);
		stage.setScene(this.scene);
		stage.setAlwaysOnTop(false);
		stage.setResizable(false);
		stage.setOnCloseRequest(event -> this.onExit(event));
	}

	/**
	 * Creates a JavaFX GUI, native to the JFX thread, on the EDT via SwingUtilities
	 */
	public void create()
	{
		System.out.println("Initializing GUI...");
		/** Invokes the GUI creation within a swing native application */
		SwingUtilities.invokeLater(() -> {
			/** You must initialize a new JFX panel to initialize the JFX Runtime */
			new JFXPanel();
			/** Starts the JFX Thread to invoke the new stage  */
			Platform.runLater(() -> {
				try
				{
					this.start(this.stage = new Stage());
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}
			});
		});
		/** Waits 10 seconds for the GUI Thread to initialize itself properly */
		Timing.waitCondition(() -> {
			General.sleep(250, 1000);
			return this.stage != null;
		}, 10000);
		/** Shows the GUI stage */
		this.show();
	}

	/**
	 * Invokes a new JFX GUI Creation
	 *
	 * @param args
	 * 		- params
	 */
	public static void main(String[] args)
	{
		new JFXUI()
		{
			/**
			 * Stop the script on exit
			 * @param event
			 */
			@Override
			public void onExit(final WindowEvent event)
			{
			}
		};
	}

	@DoNotRename
	private final String FXML =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "\n" + "<?import javafx.scene.shape.*?>\n" +
			"<?import javafx.scene.effect.*?>\n" + "<?import javafx.scene.text.*?>\n" +
			"<?import javafx.scene.image.*?>\n" + "<?import javafx.scene.control.Button?>\n" +
			"<?import javafx.collections.*?>\n" + "<?import javafx.scene.*?>\n" + "<?import javafx.scene.media.*?>\n" +
			"<?import java.lang.*?>\n" + "<?import javafx.scene.control.*?>\n" + "<?import javafx.scene.layout.*?>\n" +
			"\n" +
			"<Pane maxHeight=\"-Infinity\" maxWidth=\"-Infinity\" minHeight=\"-Infinity\" minWidth=\"-Infinity\" " +
			"prefHeight=\"293.0\" prefWidth=\"549.0\" xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx" +
			".com/fxml/1\">\n" + "   <children>\n" +
			"      <Rectangle arcHeight=\"5.0\" arcWidth=\"5.0\" fill=\"#1f93ff9d\" height=\"257.0\" layoutY=\"2.0\"" +
			" " + "stroke=\"BLACK\" strokeType=\"INSIDE\" width=\"549.0\">\n" + "         <effect>\n" +
			"            <Shadow />\n" + "         </effect>\n" + "      </Rectangle>\n" +
			"      <ToolBar layoutY=\"260.0\" prefHeight=\"38.0\" prefWidth=\"549.0\">\n" + "        <items>\n" +
			"            <Button fx:id=\"start\" mnemonicParsing=\"false\" onAction=\"#startButtonAction\" " +
			"prefHeight=\"26.0\" prefWidth=\"175.0\" text=\"Start\" />\n" +
			"            <Button fx:id=\"save\" mnemonicParsing=\"false\" onAction=\"#saveButtonAction\" " +
			"prefHeight=\"26.0\" prefWidth=\"175.0\" text=\"Save\" />\n" +
			"            <Button fx:id=\"close\" mnemonicParsing=\"false\" onAction=\"#closeButtonAction\" " +
			"prefHeight=\"26.0\" prefWidth=\"175.0\" text=\"Close\" />\n" + "        </items>\n" +
			"      </ToolBar>\n" +
			"      <ComboBox fx:id=\"orbtype\" layoutX=\"17.0\" layoutY=\"67.0\" prefWidth=\"150.0\" " +
			"promptText=\"Orb" + " " + "Types\">\n" + "      	<items>\n" +
			"	        <FXCollections fx:factory=\"observableArrayList\">\n" + "	        </FXCollections>\n" +
			"		</items>\n" + "      </ComboBox>\n" +
			"      <ComboBox fx:id=\"foodtype\" layoutX=\"195.0\" layoutY=\"67.0\" prefWidth=\"150.0\" " +
			"promptText=\"Food Type\">\n" + "    	<items>\n" +
			"	        <FXCollections fx:factory=\"observableArrayList\">\n" + "	        </FXCollections>\n" +
			"		</items>\n" + "      </ComboBox>\n" +
			"      <ComboBox fx:id=\"foodpertrip\" layoutX=\"17.0\" layoutY=\"157.0\" prefWidth=\"150.0\" " +
			"promptText=\"Food Per Trip\">\n" + "    	<items>\n" +
			"	        <FXCollections fx:factory=\"observableArrayList\">\n" +
			"	          <String fx:value=\"0\" />\n" + "	          <String fx:value=\"1\" />\n" +
			"	          <String fx:value=\"2\" />\n" + "	          <String fx:value=\"3\" />\n" +
			"	        </FXCollections>\n" + "		</items>\n" + "      </ComboBox>\n" +
			"      <ComboBox fx:id=\"energybooster\" layoutX=\"17.0\" layoutY=\"112.0\" prefWidth=\"150.0\" " +
			"promptText=\"Energy Boosters\">\n" + "    	<items>\n" +
			"	        <FXCollections fx:factory=\"observableArrayList\">\n" + "	        </FXCollections>\n" +
			"		</items>\n" + "      </ComboBox>\n" +
			"      <ComboBox fx:id=\"teleporttype\" layoutX=\"195.0\" layoutY=\"112.0\" prefWidth=\"150.0\" " +
			"promptText=\"Teleport Type\">\n" + "    	<items>\n" +
			"	        <FXCollections fx:factory=\"observableArrayList\">\n" + "	        </FXCollections>\n" +
			"		</items>\n" + "      </ComboBox>\n" +
			"      <Button fx:id=\"setEquipment\" defaultButton=\"true\" layoutX=\"14.0\" layoutY=\"200.0\" " +
			"mnemonicParsing=\"false\" onAction=\"#setEquipmentButtonAction\" prefHeight=\"26.0\" prefWidth=\"326" +
			".0\"" + " " + "text=\"Set Equipment\" />\n" +
			"      <ImageView fitHeight=\"150.0\" fitWidth=\"200.0\" layoutX=\"426.0\" layoutY=\"63.0\" " +
			"pickOnBounds=\"true\" preserveRatio=\"true\">\n" + "         <image>\n" +
			"            <Image url=\"http://i.imgur.com/OJNP6Xm.png\" />\n" + "         </image>\n" +
			"      </ImageView>\n" + "      <Pane layoutX=\"95.0\" layoutY=\"26.0\">\n" + "         <children>\n" +
			"            <Label text=\"JoeDezzy's AIO Orb Crafter\" textAlignment=\"CENTER\" " +
			"textOverrun=\"CENTER_ELLIPSIS\">\n" + "               <font>\n" +
			"                  <Font name=\"Heiti SC Medium\" size=\"27.0\" />\n" + "               </font>\n" +
			"            </Label>\n" + "         </children>\n" + "      </Pane>\n" + "   </children>\n" +
			"   <effect>\n" + "      <InnerShadow color=\"#2590c2\" />\n" + "   </effect>\n" + "</Pane>\n" + "";
}
