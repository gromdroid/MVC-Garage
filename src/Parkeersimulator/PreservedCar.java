package Parkeersimulator;

import java.util.Random;
import java.awt.*;

public class PreservedCar extends Car {
	private static final Color COLOR = Color.black;

	public PreservedCar() {
		Random random = new Random();
		int randomTime = random.nextInt(5 - 1) + 1;
		int stayMinutes = randomTime * 60;
		this.setMinutesLeft(stayMinutes);
		this.setHasToPay(false);
		this.setType("R");
	}

	@Override
	public Color getColor() {
		return COLOR;
	}
}
