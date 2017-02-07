/**
 * 
 * 
 */
package Parkeersimulator;

import org.jfree.chart.axis.NumberTickUnit;

public class Model extends java.util.Observable {

	static CarParkView carParkView;

	static final String AD_HOC = "1";
	static final String PASS = "2";
	static final String Electric = "3";
	static final String PassElectric = "4";
	static final String Preserved = "5";

	View myView;

	static CarQueue entranceCarQueue;
	static CarQueue entrancePassQueue;
	static CarQueue entranceElectricQueue;
	static CarQueue entrancePassElectricQueue;
	static CarQueue entrancePreservedQueue;
	static CarQueue leaveQueue;

	static CarQueue paymentCarQueue;
	static CarQueue exitCarQueue;

	static int week = 0;
	static int day = 0;
	static int hour = 0;
	static int hourGraph = 0;
	static int minute = 0;
	static String time;

	static int i = 5;

	static String timeHour;

	static String timeMinute;

	static int totalCarsParkingE;
	static int totalCarsParkingN;
	static int totalCarsParkingP;
	static int totalCarsParkingPE;
	static int totalCarsParkingR;
	static int totalCarsQueue;
	static int totalCarsPaying;

	static int totalNPlaces, totalPPlaces, totalEPlaces, totalPEPlaces;

	static int totalCarsEntered;

	static double totalIncome = 0;
	static double totalEstimate = 0;

	static long tickPause = 100;
	static boolean pauseState = false;
	static int tickHundred = 0;

	static double weekDayArrivals = 190; // average number of arriving cars per
											// hour
	static double weekendArrivals = 190; // average number of arriving cars per
											// hour
	static double weekDayPassArrivals = 30; // average number of arriving cars
											// per hour
	static double weekendPassArrivals = 30; // average number of arriving cars
											// per hour
	static double weekDayElectricArrivals = 20; // average number of electric
												// cars per hour
	static double weekendElectricArrivals = 20; // average number of electric
												// cars per hour
	static double weekDayPassElectricArrivals = 18; // average number of
													// electric cars per hour
	static double weekendPassElectricArrivals = 20; // average number of
													// electric cars per hour

	static int weekDayPreservedArrivals = 20; // average number of electric cars
												// per hour
	static int weekendPreservedArrivals = 20; // average number of electric cars
												// per hour

	static int enterSpeed = 4; // number of cars that can enter per minute
	static int paymentSpeed = 7; // number of cars that can pay per minute
	static int exitSpeed = 5; // number of cars that can leave per minute

	static String weekDayNPopup, weekEndNPopup, weekDayPPopup, weekEndPPopup, weekDayEPopup, weekEndEPopup,
			weekDayPEPopup, weekEndPEPopup;

	public Model() {
		entranceCarQueue = new CarQueue();
		entrancePassQueue = new CarQueue();
		entranceElectricQueue = new CarQueue();
		entrancePassElectricQueue = new CarQueue();
		entrancePreservedQueue = new CarQueue();
		leaveQueue = new CarQueue();
		paymentCarQueue = new CarQueue();
		exitCarQueue = new CarQueue();
		myView = new View(3, 6, 30);

		Controller.setCustomValues();

		run();
	}

	public void run() {
		// Controller.generatePreserved();
		while (true) {
			if (day == 3 && hour > 17) {
				weekDayArrivals = Integer.parseInt(weekDayNPopup) * 0.5;
				weekDayPassArrivals = Integer.parseInt(weekDayPPopup) * 0.5;
				weekDayElectricArrivals = Integer.parseInt(weekDayEPopup) * 0.5;
				weekDayPassElectricArrivals = Integer.parseInt(weekDayPEPopup) * 0.5;
			} else if (day == 3 && hour > 21) {
				weekDayArrivals = 20;
				weekDayPassArrivals = 0;
				weekDayElectricArrivals = 0;
				weekDayPassElectricArrivals = 0;
			} else if (hour < 7 && day < 5) {
				weekDayArrivals = 20;
				weekDayPassArrivals = 0;
				weekDayElectricArrivals = 0;
				weekDayPassElectricArrivals = 0;
				weekDayPreservedArrivals = 0;
			} else if (hour > 17 && day < 5) {
				weekDayArrivals = 20;
				weekDayPassArrivals = 0;
				weekDayElectricArrivals = 0;
				weekDayPassElectricArrivals = 0;
				weekDayPreservedArrivals = 0;
			} else if (hour > 10 && day < 5) {
				weekDayArrivals = Integer.parseInt(weekDayNPopup);
				weekDayPassArrivals = 0;
				weekDayElectricArrivals = 20;
				weekDayPassElectricArrivals = 0;
				weekDayPreservedArrivals = 20;
			} else if (day < 5) {
				weekDayArrivals = Integer.parseInt(weekDayNPopup);
				weekDayPassArrivals = Integer.parseInt(weekDayPPopup);
				weekDayElectricArrivals = Integer.parseInt(weekDayEPopup);
				weekDayPassElectricArrivals = Integer.parseInt(weekDayPEPopup);
				weekDayPreservedArrivals = 20;
			} else if (hour > 18 && day >= 5) {
				weekendPassArrivals = 0;
				weekendArrivals = 20;
				weekendElectricArrivals = 0;
				weekendPassElectricArrivals = 0;
			} else if (hour > 10 && day >= 5) {
				weekendArrivals = Integer.parseInt(weekEndNPopup);
				weekendPassArrivals = 0;
				weekendElectricArrivals = Integer.parseInt(weekEndEPopup);
				weekendPassElectricArrivals = Integer.parseInt(weekEndPEPopup);
				weekendPreservedArrivals = 20;
			} else if (hour < 9 && day >= 5) {
				weekendPassArrivals = 0;
				weekendArrivals = 20;
				weekendElectricArrivals = 0;
				weekendPassElectricArrivals = 0;
			} else if (day >= 5) {
				weekendPassArrivals = Integer.parseInt(weekEndPPopup);
				weekendArrivals = Integer.parseInt(weekEndNPopup);
				weekendElectricArrivals = Integer.parseInt(weekEndEPopup);
				weekendPassElectricArrivals = Integer.parseInt(weekEndPEPopup);
			}
			tick();
		}
	}

	static void tick() {
		if (tickHundred != 0) {
			pauseState = false;
			int i = 0;
			tickPause = 0;
			while (i < tickHundred) {
				advanceTime();
				handleExit();
				updateViews();
				handleEntrance();

				i++;
			}
			tickHundred = 0;
			tickPause = 100;
			pauseState = true;
		}
		advanceTime();
		handleExit();
		updateViews();
		// Pause.
		try {
			Thread.sleep(tickPause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		handleEntrance();
	}

	private static void advanceTime() {
		if (!pauseState) {
			// Advance the time by one minute.
			minute++;
			while (minute > 59) {
				minute -= 60;
				hour++;
				hourGraph++;

				View.normalCarSet.add(hourGraph, totalCarsParkingN);
				View.passCarSet.add(hourGraph, totalCarsParkingP);
				View.electricCarSet.add(hourGraph, totalCarsParkingE);
				View.electricPassCarSet.add(hourGraph, totalCarsParkingPE);
				View.preservedCarSet.add(hourGraph, totalCarsParkingR);

			}
			while (hour > 23) {
				hour -= 24;
				day++;
				View.xAxis.setTickUnit(new NumberTickUnit(i));
				i = i + 5;
				totalCarsEntered = 0;
				leaveQueue.clearQueue();
				// Controller.exportImage();

				// Controller.generatePreserved();
			}
			while (day > 6) {
				day -= 7;
				week++;
			}
		}

	}

	private static void handleEntrance() {
		if (!pauseState) {
			Controller.carsEntering(entrancePreservedQueue);
			Controller.carsArriving();
			Controller.carsEntering(entrancePassQueue);
			Controller.carsEntering(entranceCarQueue);
			Controller.carsEntering(entranceElectricQueue);
			Controller.carsEntering(entrancePassElectricQueue);
		}
	}

	private static void handleExit() {
		if (!pauseState) {
			Controller.carsReadyToLeave();
			Controller.carsPaying();
			Controller.carsLeaving();
		}
	}

	public static void updateViews() {
		if (!pauseState) {
			Controller.tick();
			// Update the car park view.
			View.updateView();

			int totalFreeSpots = 540 - (totalCarsParkingN + totalCarsParkingP + totalCarsParkingE + totalCarsParkingPE
					+ totalCarsParkingR);
			View.piedataset.setValue("Normal cars", totalCarsParkingN);
			View.piedataset.setValue("Passholders", totalCarsParkingP);
			View.piedataset.setValue("Electric cars", totalCarsParkingE);
			View.piedataset.setValue("Electric cars with pass", totalCarsParkingPE);
			View.piedataset.setValue("Preserved spots", totalCarsParkingR);
			View.piedataset.setValue("Free spots", totalFreeSpots);

			View.datasetBar.addValue(totalCarsEntered, "Cars entered", "");
			View.datasetBar.addValue(leaveQueue.carsInQueue(), "Cars passed", "");

			if (hour < 10) {
				timeHour = "0" + hour;
			} else {
				timeHour = String.valueOf(hour);
			}

			if (minute < 10) {
				timeMinute = "0" + minute;
			} else {
				timeMinute = String.valueOf(minute);
			}
			int totalQueue = (entrancePassQueue.carsInQueue() + entranceCarQueue.carsInQueue()
					+ entranceElectricQueue.carsInQueue() + entrancePassElectricQueue.carsInQueue()
					+ entrancePreservedQueue.carsInQueue());
			time = timeHour + ":" + timeMinute;
			View.totalLabel.setText("<html>Total amount of cars parked: "
					+ String.valueOf(totalCarsParkingE + totalCarsParkingN + totalCarsParkingP + totalCarsParkingPE
							+ totalCarsParkingR)
					+ "<br> " + "Total amount of cars in queue: " + String.valueOf(totalQueue) + "<br> "
					+ "&#09;Normal cars: " + String.valueOf(entranceCarQueue.carsInQueue()) + "<br> "
					+ "&#09;Passholders: " + String.valueOf(entrancePassQueue.carsInQueue()) + "<br> "
					+ "&#09;Electric cars: " + String.valueOf(entranceElectricQueue.carsInQueue()) + "<br> "
					+ "&#09;Electric passholders: " + String.valueOf(entrancePassElectricQueue.carsInQueue()) + "<br> "
					+ "&#09;Preserved: " + String.valueOf(entrancePreservedQueue.carsInQueue()) + "<br> "
					+ "Total amount of people paying:" + String.valueOf(totalCarsPaying) + "<br>"
					+ "Total amount of money gained: €" + String.valueOf(totalIncome) + "0<br>"
					+ "Estimated money to gain from current parking cars €"
					+ String.valueOf(Math.round(((totalCarsParkingN + totalCarsParkingE) * 2.40) * 100.0) / 100.0)
					+ "0<br>" + "Time passed: " + time + "<br> " + "Days passed: " + String.valueOf(day) + "<br> "
					+ "Weeks passed: " + String.valueOf(week) + "</html>");
		} else {
			// do nothing
		}
	}

	public static void updateView() {
		View.carParkView.updateView();
	}

	public static int getNumberOfFloors() {
		return View.numberOfFloors;
	}

	public static int getNumberOfRows() {
		return View.numberOfRows;
	}

	public static int getNumberOfPlaces() {
		return View.numberOfPlaces;
	}

	public static int getNumberOfOpenSpots() {
		return View.numberOfOpenSpots;
	}

} // Model
