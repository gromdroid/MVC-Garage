package Parkeersimulator;

import java.util.Random;
import java.awt.*;

public class ParkingPassCar extends Car {
	private static final Color COLOR = Color.blue;

	public ParkingPassCar() {
		Random random = new Random();
		int randomTime = random.nextInt(9 - 7) + 7;
		int stayMinutes = randomTime * 60;
		this.setMinutesLeft(stayMinutes);
		this.setHasToPay(false);
		this.setType("P");
	}

	@Override
	public Color getColor() {
		return COLOR;
	}
}
