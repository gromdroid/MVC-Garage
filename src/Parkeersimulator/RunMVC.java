package Parkeersimulator;

public class RunMVC {

	//The order of instantiating the objects below will be important for some pairs of commands.
	//I haven't explored this in any detail, beyond that the order below works.

	private int start_value = 10;	//initialise model, which in turn initialises view
	View myView;
	
	public RunMVC() {

		//create Model and View
		Model myModel 	= new Model();
		//myView 	= new View(3, 6, 30);

		//create Controller. tell it about Model and View, initialise model
		Controller myController = new Controller();
		myController.addModel(myModel);
		myController.addView(myView);


	} //RunMVC()

} //RunMVC
