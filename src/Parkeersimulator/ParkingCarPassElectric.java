package Parkeersimulator;

import java.awt.Color;
import java.util.Random;

public class ParkingCarPassElectric extends Car {
	private static final Color COLOR = Color.orange;

	public ParkingCarPassElectric() {
		Random random = new Random();
		int randomTime = random.nextInt(9 - 7) + 7;
		int stayMinutes = randomTime * 60;
		this.setMinutesLeft(stayMinutes);
		this.setHasToPay(false);
		this.setType("PE");
	}

	@Override
	public Color getColor() {
		return COLOR;
	}
}
