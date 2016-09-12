import java.util.*;

public class Game {
	
	private int framerate;
	private int speed;
	private int score = 0;
    private boolean pause = false;
    private boolean started = false;
	final int maxWidth = 36; // max unit width
	final int maxHeight = 22; // max unit height (10 pixels per unit)
	private HashSet<Integer> used; // track any occupied units
	private HashSet<Integer> foods;
	private Python snake;
	private boolean gameOver = false;
	private int occurence = 0;
    private int length = 0;

    //Power up options:
    private int powerUps;
    private int steroidDuration = 0;
	
	Game(int fps, int s){
		framerate = fps;
		speed = s;
		start();
	}

    //start function used to configurate/re-configurate the game
    public void start(){
        snake = new Python(this, new Game.Pair(4, 4), new Game.Pair(3, 4));
        used = new HashSet<Integer>();
        foods = new HashSet<Integer>();
        powerUps = 0;
        newFood();
        gameOver = false;
		pause = false;
        score = 0;
        length = 0;

        steroidDuration = 0;
    }
    
    public boolean started(){
    	return started;
    }

    //Steroid Powerups:
    public void addSteroid(){
        Pair newpair;

        used.addAll(snake.getSnake());// update snake
        Random rand = new Random();

        do {
            int x = rand.nextInt(maxWidth);
            int y = rand.nextInt(maxHeight);
            newpair = new Pair(x,y);
        }while (used.contains(newpair)); // Find another two points

        used.add(newpair.keyGen());
        powerUps = newpair.keyGen();
    }

    //Steroid Powerups:
    public void addLaxation(){
        Pair newpair;

        used.addAll(snake.getSnake());// update snake
        Random rand = new Random();

        do {
            int x = rand.nextInt(maxWidth);
            int y = rand.nextInt(maxHeight);
            newpair = new Pair(x,y);
        }while (used.contains(newpair)); // Find another two points

        used.add(newpair.keyGen());
        powerUps = 10000 + newpair.keyGen();
    }

    public void steroid(){
        steroidDuration = 3*framerate;
    }

    public int hadSteroid(){
        return steroidDuration;
    }

	//add one new food to the food map
	public void newFood(){
		Pair newpair;
		
		// Create new food and make check if it's a occupied unit
		
		used.addAll(snake.getSnake());// update snake
        Random rand = new Random();
		
		do {
			int x = rand.nextInt(maxWidth);
			int y = rand.nextInt(maxHeight);
			newpair = new Pair(x,y);
		}while (used.contains(newpair)); // Find another two points

        used.add(newpair.keyGen());
		foods.add(newpair.keyGen());
	}
	
	// remove the food
	public void removeFood( Pair p ){
		if (foods.contains(p)){
			used.remove(p);
			foods.remove(p);
		}
	}

    public void addSpeed( int s ){
        speed += s;
    }
	
	// Return The list of foods as an ArrayList
	public ArrayList<Integer> getFoods(){
		return new ArrayList<Integer>(foods);
	}

    public int getPower(){
        return powerUps;
    }

    // Return The list of encoded x-y coordinates of the snake.
	public ArrayList<Integer> getSnake(){

        if (snake == null){
            return new ArrayList<Integer>(); // return a empty list if there are no snakes.
        }

        return snake.getSnake();
	}

    // assign new movement to the snake.
	public void snakeMove(char a){
		snake.newDirection(a);
	}
	
	//add s to score
	public void addScore(int s){
        score += 10 + speed * s * (steroidDuration > 0 ? 2 : 1);
	}
	
	//return score
	public int getScore(){
		return score;
	}
	
	//return encoded integer of snake's head
	public int getHead(){
		return snake.head().keyGen();
	}
	
	// a single cycle of game loop execution
	public void gameRun(){

        // trigger start flag
        if (!started){
            started = true;
        }

        // the game simply dies if paused
        if (pause){
            return;
        }

        ArrayList<Integer> snakes = snake.getSnake();
        
        Integer next = new Integer(snake.head().keyGen());
        char dir = snake.head.direction;
        
        switch (dir){
        case 'l':
        	next = next - 1;
        	break;
        case 'r':
        	next = next + 1;
        	break;
        case 'u':
        	next = next - 100;
        	break;
        case 'd':
        	next = next + 100;
        	break;
        default:
        	break;
        }

        int timeSlice = (framerate/((speed + speed/4) * (steroidDuration > 0 ? 2 : 1)));

        // boundary check
        if (timeSlice < 1){
            timeSlice = 1;
        }

        // generate steroids
        Random rand = new Random();

        if (steroidDuration < 1 && rand.nextInt(10000) < 5 && powerUps == 0){
            addSteroid();
        }else if (rand.nextInt(10000) < 10 && powerUps == 0){
            addLaxation();
        }

        // react on correct frame
        if (occurence % timeSlice == 0) {
        	if (foods.contains(next)){ // potentially eating food.
        		snake.eat(new Pair(decodeX(next), decodeY(next)));
        		foods.remove(next);
                length++;
            }else{
        		snake.updateSnake();
        	}
        }

        // update data structures
        used.clear();
        used.addAll(snake.getSnake());
        used.addAll(foods);
        if (powerUps != 0) {
            used.add(powerUps % 10000);
        }

		occurence++;

        if (steroidDuration > 0 ) {
            steroidDuration--;
        }
		snakes.remove(new Integer(snake.head().keyGen())); // removes the head.

        // detect if it ate a power up
        if (powerUps != 0 && powerUps%10000 == snake.head().keyGen()){
            if (powerUps/10000 == 0 && steroidDuration == 0){// steroid
                steroid();
                powerUps = 0;
            }else {
                int cut = (length/3 + 1) > 5 ? 5: (length/3 + 1);
                snake.diet(cut);
                length -= cut;
                powerUps = 0;
            }
        }

        // detect if the snake hits the wall
        if (snake.hitBoundary(maxWidth, maxHeight) || snakes.contains(snake.head().keyGen())){
            gameOver();
        }
	}

    //clean-up the screen
    public void clean(){
        started = false;
        used.clear();
        snake = null;
        foods.clear();
    }

    //toggle pause
    public void togglePause(){
        pause = !pause;
        occurence = 0;
    }

	public boolean gamePaused(){
		return pause;
	}

	// Good Game, Well Played
	public void gameOver(){
        gameOver = true;
	}

	// Decode the Pair's key into its X/Y components
	public static int decodeX(int key){ 
		// edge case
		if (key%100 == 99){
			return -1;
		}
		return Math.abs(key%100); 
	}		
	
	public static int decodeY(int key){ 
		// edge cases
		if (key < 0){
			return -1;
		}
		return Math.abs(key/100); 
	}

    // Return frame displayed
    public int frame(){
        return occurence;
    }

	// Detect if the game has stopped
    public boolean gameStop() { return gameOver; }
	
	// Pair class 
	class Pair {
		private int left;
		private int right;

        Pair (Pair p) {
            this.left = p.left();
            this.right = p.right();
        }

		Pair(int l, int r){
			left = l;
			right = r;
		}
		
		// return left value
		public int left() { 
			return left; 
		}
		
		//return right value
		public int right() { 
			return right; 
		}
		
		public void change(int dx, int dy){
			left += dx;
			right += dy;
		}
		
		// Generates a unique integer key for the pair
		public int keyGen(){
			if (left == -1){ // edge case
				return 99+100*right;
			}else if (right == -1){
				return -100 - left;
			}
			
			return left + 100*right;
		}

        @Override
        public String toString(){
            return String.format("(%d, %d)", left, right);
        }
	} // end of inner class Pair

}
