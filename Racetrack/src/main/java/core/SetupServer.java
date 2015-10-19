package src.main.java.core;


/**
 * starts server up
 */
public class SetupServer {

	public SetupServer(String[] args){
		if(args.length > 1){
			System.err.println("To many arguments! ");
		}else{

			int standardPort = 55570;

			if(args.length == 1){
				standardPort = Integer.parseInt(args[0]);
			}
			init(standardPort);	

		}
	}

	private void init(int standartPort){
		
		printWelcomeMessage();
		
		new ControllerServer(standartPort);

		DebugOutputHandler.isDebug = true;
	}

	private void printWelcomeMessage(){
		System.out.println(" --------------Racetrack SS2015--------------");
		System.out.println();
	}

	public static void main(String[] args){
		new SetupServer(args);		
	}

}
