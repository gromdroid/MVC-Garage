package Parkeersimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class CarParkView extends JPanel {

	private Dimension size;
	private BufferedImage img;

	/**
	 * Constructor for objects of class CarPark
	 */
	public CarParkView() {
		size = new Dimension(0, 0);
	}

	/**
	 * Overridden. Tell the GUI manager how big we would like to be.
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(800, 500);
	}

	/**
	 * Overriden. The car park view component needs to be redisplayed. Copy the
	 * internal image to screen.
	 */
	@Override
	public void paintComponent(Graphics g) {
		if (img == null) {
			return;
		}

		Dimension currentSize = getSize();
		if (size.equals(currentSize)) {
			g.drawImage(img, 0, 0, null);
		} else {
			// Rescale the previous image.
			g.drawImage(img, 0, 0, currentSize.width, currentSize.height, null);
		}
	}

	public void updateView() {
		// Create a new car park image if the size has changed.
		img = new BufferedImage(850, 400, 2);
		Graphics graphics = img.getGraphics();
		for (int floor = 0; floor < Model.getNumberOfFloors(); floor++) {
			for (int row = 0; row < Model.getNumberOfRows(); row++) {
				for (int place = 0; place < Model.getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					Car car = Controller.getCarAt(location);
					Color color = car == null ? Color.gray : car.getColor();
					drawPlace(graphics, location, color);
				}
			}
		}
		repaint();
	}

	/**
	 * Paint a place on this car park view in a given color.
	 */
	private void drawPlace(Graphics graphics, Location location, Color color) {
		graphics.setColor(color);
		graphics.fillRect(location.getFloor() * 260 + (1 + (int) Math.floor(location.getRow() * 0.5)) * 75
				+ (location.getRow() % 2) * 20, 60 + location.getPlace() * 10, 20 - 1, 10 - 1); // TODO
																								// use
																								// dynamic
																								// size
																								// or
																								// constants
	}
}