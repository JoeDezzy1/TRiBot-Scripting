package scripts.orbcrafter.core.api.trackers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.tribot.api.General;

import scripts.dax_api.walker_engine.WebWalkerPaint;
import scripts.dezapi.utility.web.PriceChecker;
import scripts.orbcrafter.core.api.types.Orb;

/**
 * @author JoeDezzy1
 */
public class ProgressPaint
{
	/**
	 * The progress
	 */
	public ProgressTracker progress;
	/**
	 * The font and paints URL to load
	 */
	private static final Font FONT = new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 16);
	private static final String PAINT_URL = "http://i.imgur.com/hKaNyrY.png?1";

	/**
	 * The paint image to draw over to make it look sexy
	 */
	private BufferedImage image;

	/**
	 * The profit per orb
	 */
	private int profit;

	/**
	 * Boolean if we are showing the paint or not
	 */
	private boolean show;
	private boolean paused;

	/**
	 * Initializes and shows the paint
	 */
	public ProgressPaint(final Orb orb)
	{
		this.progress = new ProgressTracker(orb);
		this.setImage();
		this.setPrices();
	}

	/**
	 * True if we should show the paint or not
	 *
	 * @return true if the paint should be shown
	 */
	public boolean show()
	{
		return show;
	}

	/**
	 * Sets the paint showing on or off
	 *
	 * @param show
	 * 		- true if we want the paint on and false if not
	 */
	public void show(boolean show)
	{
		this.show = show;
	}

	/**
	 * Sets the prices to make paint profit calculations
	 * <p>
	 * profit = orb price - (cosmic * 3 + unpowered)
	 */
	private void setPrices()
	{
		this.profit = PriceChecker.getPrice(573) - ((PriceChecker.getPrice(564) * 3) + PriceChecker.getPrice(567));
	}

	/**
	 * Pauses the paint tracking
	 */
	public void pause()
	{
		this.paused = true;
		this.progress.startBreak();
	}

	/**
	 * Unpauses the paint tracking
	 */
	public void unpause()
	{
		this.paused = false;
		this.progress.endBreak();
	}

	/**
	 * Sets the image for the orb crafters paint
	 */
	private void setImage()
	{
		try
		{
			image = ImageIO.read(new URL(PAINT_URL));
		}
		catch (IOException e)
		{
			General.println("Couldn't load paint!");
		}
	}

	/**
	 * Draws the sexy ass paint to the screen
	 *
	 * @param g
	 * 		- the graphics from the client
	 */
	public void draw(Graphics g)
	{
		/** Doesn't do anything if the paint isn't showing */
		if (this.show())
		{
			WebWalkerPaint.getInstance().drawDebug(g, true);
			this.progress.update();
			g.setFont(FONT);

			/** Draws the paints image that was downloaded */
			g.drawImage(image, 5, 290, null);

			g.setColor(Color.BLACK);

			/** Fills black rectangles to draw the paint progress onto */
			g.fillRoundRect(425, 422, 95, 35, 10, 10);
			g.fillRoundRect(425, 380, 95, 35, 10, 10);
			g.fillRoundRect(145, 362, 125, 35, 10, 10);
			g.fillRoundRect(145, 400, 125, 35, 10, 10);
			g.fillRoundRect(145, 438, 125, 35, 10, 10);

			g.setColor(Color.WHITE);

			/** Draws the paints progress */
			g.drawString(progress.getClock().toString(), 437, 403);
			g.drawString(Integer.toString(progress.getAmountGenerated()), 461, 445);
			if (!paused)
			{
				g.drawString(Integer.toString((int) progress.getClock().getRatePerHour(progress.getXpGained())),
				             160,
				             423);
				g.drawString(Integer.toString((int) progress.getClock()
				                                            .getRatePerHour(progress.getAmountGenerated() * profit)),
				             160,
				             460);
				g.drawString(Integer.toString((int) progress.getClock().getRatePerHour(progress.getAmountGenerated())),
				             160,
				             387);
			}
		}
	}

}
