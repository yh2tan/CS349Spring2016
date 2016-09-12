import java.util.Map;

public class Segment {
	
	Game.Pair cord;
	char direction;
	boolean isTail;
	
	public Segment(Game.Pair p, char dir, boolean t) {
		cord = p;
		direction = dir;
		isTail = t;
	}
	
	public char getDirection(){
		return direction;
	}
	
	public Game.Pair getPair(){
		return cord;
	}

	public void move(Map<Integer, Character> t){
		
		// change direction
		if (t.containsKey(cord.keyGen())){
			direction = t.get(cord.keyGen()).charValue();
		}
		
		// remove any direction segments that's currently played by tail
		if (isTail && t.containsKey(cord.keyGen())){
			t.remove(cord.keyGen()); 
		}
		
		//update to new coordinates
		switch (direction){
		case 'l': cord.change(-1, 0);
			break;
		case 'r': cord.change(1, 0);
			break;
		case 'u': cord.change(0,-1);
			break;
		case 'd': cord.change(0,1);
			break;
		default: cord.change(0,0);
			break;
		}
	}

	// get x coordinates
	public int getX(){
		return cord.left();
	}

	// get y coordinates
	public int getY(){
		return cord.right();
	}
}
