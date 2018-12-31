package scripts.orbcrafter;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

import org.tribot.api.General;
import org.tribot.api.util.Screenshots;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Breaking;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MousePainting;
import org.tribot.script.interfaces.MouseSplinePainting;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;

import scripts.orbcrafter.core.operation.ScriptOperator;
/**
 * @author JoeDezzy1
 */
@ScriptManifest(authors = { "JoeDezzy1" }, category = "Magic", name = "Air Orbs")
public class OrbScript extends Script
		implements Painting, MouseSplinePainting, MousePainting, KeyListener, Starting, Ending, Breaking, Arguments
{

	/**
	 * The scripts runtime length variable
	 */
	private boolean running;
	/**
	 * The scripts operator
	 */
	private ScriptOperator operator;

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tribot.script.interfaces.MousePainting#paintMouse(java.awt.Graphics,
	 * java.awt.Point, java.awt.Point)
	 */
	@Override
	public void paintMouse(Graphics arg0, Point arg1, Point arg2)
	{
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tribot.script.interfaces.MouseSplinePainting#paintMouseSpline(java.
	 * awt.Graphics, java.util.ArrayList)
	 */
	@Override
	public void paintMouseSpline(Graphics arg0, ArrayList<Point> arg1)
	{
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tribot.script.interfaces.Arguments#passArguments(java.util.HashMap)
	 */
	@Override
	public void passArguments(HashMap<String, String> arg0)
	{
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tribot.script.interfaces.Breaking#onBreakEnd()
	 */
	@Override
	public void onBreakEnd()
	{
		this.operator.onBreakEnd();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tribot.script.interfaces.Painting#onPaint(java.awt.Graphics)
	 */
	@Override
	public void onPaint(Graphics arg0)
	{
		if (this.operator != null)
			this.operator.onPaint(arg0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
		if (this.operator != null && e.getKeyCode() == KeyEvent.VK_SHIFT)
			this.operator.togglePaint();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tribot.script.interfaces.Breaking#onBreakStart(long)
	 */
	@Override
	public void onBreakStart(long arg0)
	{
		this.operator.onBreak();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tribot.script.Script#run()
	 */
	@Override
	public void run()
	{
		while (this.running)
		{
			this.setLoginBotState(!this.operator.isHoppingWorlds());
			this.operator.operate();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tribot.script.interfaces.Ending#onEnd()
	 */
	@Override
	public void onEnd()
	{
		this.running = false;
		this.operator.onEnd();
		if (Screenshots.take(true))
			println("Ended, took a screenshot");
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
			final String user = General.getTRiBotUsername();
			General.println("Welcome, " + (user != null ? user : "") + "!");
			General.useAntiBanCompliance(true);
			this.setAIAntibanState(true);
			this.operator = new ScriptOperator();
			this.operator.onStart();
			this.running = true;
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			General.println(
					"There was a problem starting the script, please copy and paste this debug, then post it on the " +
					"forum page and notify the developer");
		}
	}
}
