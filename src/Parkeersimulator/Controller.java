package Parkeersimulator;

import java.util.Random;

import javax.swing.JOptionPane;

class Controller implements java.awt.event.ActionListener {

	// Joe: Controller has Model and View hardwired in
	Model model;
	View view;
	View myView;

	Controller() {
	}

	@Override
	public void actionPerformed(java.awt.event.ActionEvent e) {
	}

	public void addModel(Model m) {
		System.out.println("Controller: adding model");
		this.model = m;
	}

	public void addView(View v) {
		System.out.println("Controller: adding view");
		this.view = v;
	}

	public static Car getCarAt(Location location) {
		if (!locationIsValid(location)) {
			return null;
		}
		return View.cars[location.getFloor()][location.getRow()][location.getPlace()];
	}

	public static boolean setCarAt(Location location, Car car) {
		if (!locationIsValid(location)) {
			return false;
		}
		Car oldCar = getCarAt(location);
		if (oldCar == null) {
			View.cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
			car.setLocation(location);
			View.numberOfOpenSpots--;
			return true;
		}
		return false;
	}

	public static Car removeCarAt(Location location) {
		if (!locationIsValid(location)) {
			return null;
		}
		Car car = getCarAt(location);
		if (car == null) {
			return null;
		}
		View.cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
		car.setLocation(null);
		View.numberOfOpenSpots++;
		return car;
	}

	public static void setCustomValues() {
		Model.weekDayNPopup = JOptionPane
				.showInputDialog("Please enter the number of normal cars per hour during the week:", "150");
		Model.weekDayPassArrivals = Integer.parseInt(Model.weekDayNPopup);
		Model.weekEndNPopup = JOptionPane
				.showInputDialog("Please enter the number of normal cars per hour during the weekend:", "200");
		Model.weekDayPassArrivals = Integer.parseInt(Model.weekEndNPopup);
		Model.weekDayPPopup = JOptionPane
				.showInputDialog("Please enter the number of passholers per hour during the week:", "28");
		Model.weekDayPassArrivals = Integer.parseInt(Model.weekDayPPopup);
		Model.weekEndPPopup = JOptionPane
				.showInputDialog("Please enter the number of passholers per hour during the weekend:", "20");
		Model.weekDayPassArrivals = Integer.parseInt(Model.weekEndPPopup);
		Model.weekDayEPopup = JOptionPane
				.showInputDialog("Please enter the number of electric cars per hour during the week:", "20");
		Model.weekDayPassArrivals = Integer.parseInt(Model.weekDayEPopup);
		Model.weekEndEPopup = JOptionPane
				.showInputDialog("Please enter the number of electric cars per hour during the weekend:", "20");
		Model.weekDayPassArrivals = Integer.parseInt(Model.weekEndEPopup);
		Model.weekDayPEPopup = JOptionPane
				.showInputDialog("Please enter the number of electric car passholers per hour during the week:", "20");
		Model.weekDayPassArrivals = Integer.parseInt(Model.weekDayEPopup);
		Model.weekEndPEPopup = JOptionPane.showInputDialog(
				"Please enter the number of electric car passholers per hour during the weekend:", "20");
		Model.weekDayPassArrivals = Integer.parseInt(Model.weekEndEPopup);
	}

	public static Location getFirstFreeLocation(String type) {
		if (type == "N" || type == "R") {
			for (int floor = 0; floor < Model.getNumberOfFloors(); floor++) {
				if (floor == 0) {
					for (int row = 4; row < Model.getNumberOfRows(); row++) {
						for (int place = 0; place < Model.getNumberOfPlaces(); place++) {
							Location location = new Location(floor, row, place);
							if (getCarAt(location) == null) {
								return location;
							}
						}
					}
				} else if (floor == 1) {
					for (int row = 0; row < Model.getNumberOfRows(); row++) {
						for (int place = 0; place < Model.getNumberOfPlaces(); place++) {
							Location location = new Location(floor, row, place);
							if (getCarAt(location) == null) {
								return location;
							}
						}
					}
				} else {
					for (int row = 0; row < 5; row++) {
						for (int place = 0; place < Model.getNumberOfPlaces(); place++) {
							Location location = new Location(floor, row, place);
							if (getCarAt(location) == null) {
								return location;
							}
						}
					}
				}
			}
		} else if (type == "P") {
			for (int floor = 0; floor < 1; floor++) {
				for (int row = 0; row < 4; row++) {
					if (row >= 1) {
						for (int place = 0; place < Model.getNumberOfPlaces(); place++) {
							Location location = new Location(floor, row, place);
							if (getCarAt(location) == null) {
								return location;
							}
						}
					} else {
						for (int place = 15; place < Model.getNumberOfPlaces(); place++) {
							Location location = new Location(floor, row, place);
							if (getCarAt(location) == null) {
								return location;
							}
						}
					}
				}
			}
		} else if (type == "E") {
			for (int floor = 2; floor < Model.getNumberOfFloors(); floor++) {
				for (int row = 5; row < Model.getNumberOfRows(); row++) {
					for (int place = 0; place < Model.getNumberOfPlaces(); place++) {
						Location location = new Location(floor, row, place);
						if (getCarAt(location) == null) {
							return location;
						}
					}
				}
			}
		} else if (type == "PE") {
			for (int floor = 0; floor < 1; floor++) {
				for (int row = 0; row < 1; row++) {
					for (int place = 0; place < 15; place++) {
						Location location = new Location(floor, row, place);
						if (getCarAt(location) == null) {
							return location;
						}
					}
				}
			}
		}
		return null;
	}

	public static Car getFirstLeavingCar() {
		for (int floor = 0; floor < Model.getNumberOfFloors(); floor++) {
			for (int row = 0; row < Model.getNumberOfRows(); row++) {
				for (int place = 0; place < Model.getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					Car car = getCarAt(location);
					if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
						return car;
					}
				}
			}
		}
		return null;
	}

	public static void tick() {
		for (int floor = 0; floor < Model.getNumberOfFloors(); floor++) {
			for (int row = 0; row < Model.getNumberOfRows(); row++) {
				for (int place = 0; place < Model.getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					Car car = Controller.getCarAt(location);
					if (car != null) {
						car.tick();
					}
				}
			}
		}
	}

	static boolean locationIsValid(Location location) {
		int floor = location.getFloor();
		int row = location.getRow();
		int place = location.getPlace();
		if (floor < 0 || floor >= View.numberOfFloors || row < 0 || row > View.numberOfRows || place < 0
				|| place > View.numberOfPlaces) {
			return false;
		}
		return true;
	}

	public static void deleteCars() {
		// generatePreserved();
		Model.entrancePassQueue.clearQueue();
		Model.entranceCarQueue.clearQueue();
		Model.entranceElectricQueue.clearQueue();
		Model.entrancePassElectricQueue.clearQueue();
		Model.entrancePreservedQueue.clearQueue();
		Model.paymentCarQueue.clearQueue();
		Model.exitCarQueue.clearQueue();
		Model.totalCarsParkingE = 0;
		Model.totalCarsParkingN = 0;
		Model.totalCarsParkingP = 0;
		Model.totalCarsParkingPE = 0;
		Model.totalCarsParkingR = 0;

		View.piedataset.setValue("Normal cars", 0);
		View.piedataset.setValue("Passholders", 0);
		View.piedataset.setValue("Electric cars", 0);
		View.piedataset.setValue("Electric cars with pass", 0);
		View.piedataset.setValue("Preserved", 0);

		View.normalCarSet.clear();
		View.passCarSet.clear();
		View.electricCarSet.clear();
		View.electricPassCarSet.clear();
		View.preservedCarSet.clear();

		View.datasetBar.clear();
		View.datasetBar.clear();

		for (int i = 0; i < 539; i++) {
			Location usedLocation = getFirstUsedLocation();

		}

		View.totalLabel.setText("<html>Total amount of cars parked: 0<br> " + "Total amount of cars in queue: 0<br> "
				+ "Total amount of people paying: 0<br>" + "Time passed: 00:00<br> " + "Days passed: 0<br> "
				+ "Weeks passed: 0</html>");

		View.updateView();

	}

	public static Location getFirstUsedLocation() {
		for (int floor = 0; floor < Model.getNumberOfFloors(); floor++) {
			for (int row = 0; row < Model.getNumberOfRows(); row++) {
				for (int place = 0; place < Model.getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					if (getCarAt(location) != null) {
						removeCarAt(location);
						return location;
					}
				}
			}
		}
		return null;
	}

	static void carsArriving() {
		int numberOfCars = getNumberOfCars(Model.weekDayArrivals, Model.weekendArrivals);
		addArrivingCars(numberOfCars, Model.AD_HOC);
		numberOfCars = getNumberOfCars(Model.weekDayPassArrivals, Model.weekendPassArrivals);
		addArrivingCars(numberOfCars, Model.PASS);
		numberOfCars = getNumberOfCars(Model.weekDayElectricArrivals, Model.weekendElectricArrivals);
		addArrivingCars(numberOfCars, Model.Electric);
		numberOfCars = getNumberOfCars(Model.weekDayPassElectricArrivals, Model.weekendPassElectricArrivals);
		addArrivingCars(numberOfCars, Model.PassElectric);
		numberOfCars = getNumberOfCars(Model.weekDayPreservedArrivals, Model.weekendPreservedArrivals);
		addArrivingCars(numberOfCars, Model.Preserved);
	}

	static void carsEntering(CarQueue queue) {
		int i = 0;
		// Remove car from the front of the queue and assign to a parking space.
		while (queue.carsInQueue() > 0 && Model.getNumberOfOpenSpots() > 0 && i < Model.enterSpeed) {
			Car car = queue.removeCar();
			String carType = car.getType();
			Location freeLocation = Controller.getFirstFreeLocation(carType);
			if (freeLocation == null) {
				queue.addCar(car);
			} else {
				if (carType == "N") {
					Controller.setCarAt(freeLocation, car);
				} else if (carType == "P") {
					Controller.setCarAt(freeLocation, car);
				} else if (carType == "E") {
					Controller.setCarAt(freeLocation, car);
				} else if (carType == "PE") {
					Controller.setCarAt(freeLocation, car);
				} else if (carType == "R") {
					Controller.setCarAt(freeLocation, car);
				} else {
					continue;
				}
			}
			i++;
		}
	}

	static void carsReadyToLeave() {
		// Add leaving cars to the payment queue.
		Car car = Controller.getFirstLeavingCar();
		while (car != null) {
			if (car.getHasToPay()) {
				car.setIsPaying(true);
				Model.paymentCarQueue.addCar(car);
			} else {
				carLeavesSpot(car);
			}
			car = Controller.getFirstLeavingCar();
		}
	}

	static void carsPaying() {
		// Let cars pay.
		int i = 0;
		while (Model.paymentCarQueue.carsInQueue() > 0 && i < Model.paymentSpeed) {
			Car car = Model.paymentCarQueue.removeCar();
			// TODO Handle payment.
			carLeavesSpot(car);
			Model.totalIncome += 2.40;
			Model.totalIncome = Math.round(Model.totalIncome * 100.0) / 100.0;
			i++;
		}
		Model.totalCarsPaying = Model.paymentCarQueue.carsInQueue();
	}

	static void carsLeaving() {
		// Let cars leave.
		int i = 0;
		while (Model.exitCarQueue.carsInQueue() > 0 && i < Model.exitSpeed) {
			Model.exitCarQueue.removeCar();
			i++;
		}
	}

	private static int getNumberOfCars(double weekDayArrivals, double weekendArrivals) {
		Random random = new Random();

		// Get the average number of cars that arrive per hour.
		double averageNumberOfCarsPerHour = Model.day < 5 ? weekDayArrivals : weekendArrivals;

		// Calculate the number of cars that arrive this minute.
		double standardDeviation = averageNumberOfCarsPerHour * 0.3;
		double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
		return (int) Math.round(numberOfCarsPerHour / 60);

	}

	private static void addArrivingCars(int numberOfCars, String type) {
		// Add the cars to the back of the queue.
		Random random = new Random();
		switch (type) {
		case Model.AD_HOC:
			for (int i = 0; i < numberOfCars; i++) {
				if (Model.entranceCarQueue.carsInQueue() < random.nextInt(10 - 1 + 1) + 1) {
					Model.entranceCarQueue.addCar(new AdHocCar());
					Model.totalCarsParkingN++;
					Model.totalCarsEntered++;
				} else {
					Model.leaveQueue.addCar(new AdHocCar());
				}
			}
			break;
		case Model.PASS:
			for (int i = 0; i < numberOfCars; i++) {
				if (Model.entrancePassQueue.carsInQueue() < random.nextInt(10 - 1 + 1) + 1) {
					Model.entrancePassQueue.addCar(new ParkingPassCar());
					Model.totalCarsParkingP++;
					Model.totalCarsEntered++;
				} else {
					Model.leaveQueue.addCar(new ParkingPassCar());
				}
			}
			break;
		case Model.Electric:
			for (int i = 0; i < numberOfCars; i++) {
				if (Model.entranceElectricQueue.carsInQueue() < random.nextInt(10 - 1 + 1) + 1) {
					Model.entranceElectricQueue.addCar(new ParkingCarElectric());
					Model.totalCarsParkingE++;
					Model.totalCarsEntered++;
				} else {
					Model.leaveQueue.addCar(new ParkingCarElectric());
				}
			}
			break;
		case Model.PassElectric:
			for (int i = 0; i < numberOfCars; i++) {
				if (Model.entrancePassElectricQueue.carsInQueue() < random.nextInt(10 - 1 + 1) + 1) {
					Model.entrancePassElectricQueue.addCar(new ParkingCarPassElectric());
					Model.totalCarsParkingPE++;
					Model.totalCarsEntered++;
				} else {
					Model.leaveQueue.addCar(new ParkingCarPassElectric());
				}
			}
			break;
		case Model.Preserved:
			for (int i = 0; i < numberOfCars; i++) {
				if (Model.entrancePreservedQueue.carsInQueue() < random.nextInt(10 - 1 + 1) + 1) {
					Model.entrancePreservedQueue.addCar(new PreservedCar());
					Model.totalCarsParkingR++;
					Model.totalCarsEntered++;
				} else {
					Model.leaveQueue.addCar(new PreservedCar());
				}
			}
			break;
		}
	}

	private static void carLeavesSpot(Car car) {
		Controller.removeCarAt(car.getLocation());
		Model.exitCarQueue.addCar(car);
		if (car.getType() == "N") {
			Model.totalCarsParkingN--;
		} else if (car.getType() == "E") {
			Model.totalCarsParkingE--;
		} else if (car.getType() == "P") {
			Model.totalCarsParkingP--;
		} else if (car.getType() == "PE") {
			Model.totalCarsParkingPE--;
		} else if (car.getType() == "R") {
			Model.totalCarsParkingR--;
		}
	}

}
