package Parkeersimulator;

import java.awt.Color;
import java.util.Random;

public class ParkingCarElectric extends Car {
	private static final Color COLOR=Color.green;
	
	public ParkingCarElectric() {
		Random random = new Random();
		int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
		this.setMinutesLeft(stayMinutes);
		this.setHasToPay(true);
        this.setType("E");
	}
	
	@Override
	public Color getColor(){
		return COLOR;
	}
}
