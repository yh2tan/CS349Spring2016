import javax.swing.*;

public class Snake {

	public static void main(String[] args) {
		
		// default values for the game
		int fps = 30;
		int speed = 6;
		
		// Fetch Command-line arguments
		if (args.length >= 1){
			fps = Integer.parseInt(args[0]);
			
			// Check interval
			if (fps < 1 || fps > 100){
				System.out.println("Argument FPS is not within the correct interval (1-100), the FPS is now set to 30");
				fps = 30;
			}
		}// end FPS fetch
		
		if (args.length >= 2){
			speed = Integer.parseInt(args[1]);
			
			// check interval
			
			if (speed < 1 || speed > 10){
				System.out.println("Argument speed is not within the correct interval (1-10), the speed is now set to 3");
				speed = 3;
			}
		}// end speed fetch

		// custom UI methods
		Display display = new Display(fps, speed);

		// create windows
		Window frame = new Window(display);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.addKeyListener(frame);
	}
}
