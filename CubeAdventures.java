// The "CubeAdventures" class.
import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.*;
//PRESS P TO GO ONTO NEXT LEVEL
public class CubeAdventures extends JFrame implements KeyListener
{
    //Declare all the variables needed
    boolean left = false, right = false, tutorial = true, jump = false, stageDone = false, onGround = true, leftWall = false, rightWall = false; //Boolean variables for movement and keypresses
    int yPos = 275, xPos = 10, endPointXPos = 0, endPointYPos = 0; //Integer variables for an object's position
    int jumptimer = 12, blockNum = 0, coinNum = 0, enemyNum = 0, levelCount = 0, coins = 0, deaths = 0; //Integer variables for game management (how long you jump for/what level your are on, etc)
    boolean enemyRight = true, enemyLeft = false; //Enemy block movement
    boolean blockRight = true, blockLeft = false; //Moving platforms

    //Arrays to determine where and how the blocks are drawn each level - maximum of 30 blocks per stage
    int blockXPos[] = new int [30]; //Block x position
    int blockYPos[] = new int [30]; //Block y position
    int blockWidth[] = new int [30]; //Block width
    int blockHeight[] = new int [30]; //Block height

    //Arrays to determine where the coins are drawn each level (size is predetermined)
    int coinsXPos[] = new int [20];
    int coinsYPos[] = new int [20];

    //Arrays to determine where and how the enemies are drawn each level
    int enemyXPos[] = new int [20];
    int enemyYPos[] = new int [20];
    int enemyWidth[] = new int [20];
    int enemyHeight[] = new int [20];

    //Name and score arrays for high scores
    String nam[] = new String [21];
    int score[] = new int [21];

    Container frame; //Create frame for graphics

    //Key typed method
    public void keyTyped (KeyEvent e)
    {

    }


    //If a key is pressed
    public void keyPressed (KeyEvent e)
    {
	if (e.getKeyCode () == KeyEvent.VK_LEFT && tutorial == false) //If left arrow is clicked, make "left" true
	{
	    left = true;
	}
	if (e.getKeyCode () == KeyEvent.VK_RIGHT && tutorial == false) //If right arrow is clicked, make "right" true
	{
	    right = true;

	}
	if (e.getKeyCode () == KeyEvent.VK_SPACE && tutorial == false || e.getKeyCode () == KeyEvent.VK_UP && tutorial == false) //Jumping can be done by pressing space or up arrow
	{
	    jump = true;
	}
	if (e.getKeyCode () == KeyEvent.VK_ESCAPE) //Escape either turns off or on the tutorial
	{
	    if (tutorial == false) //If tutorial is false, it will show the tutorial when pressed
	    {
		tutorial = true;
	    }
	    else //Otherwise tutorial will go away
	    {
		tutorial = false;
	    }
	    repaint (); //Repaint because tutorial is only painted once
	}
	//CHEAT COMMAND!!! MR. WRAY, HERE'S THE CHEAT COMMAND!
	if (e.getKeyCode () == KeyEvent.VK_P && levelCount != 4) //Press P to skip to the next level
	{
	    levelCount++;
	    stageDone = true;
	    coins = 0;
	    whichLevel ();
	}
    }


    //If a key is released
    public void keyReleased (KeyEvent e)
    {
	//If the buttons get unpressed, it will make the corresponding variables false
	if (e.getKeyCode () == KeyEvent.VK_LEFT)
	{
	    left = false;

	}
	if (e.getKeyCode () == KeyEvent.VK_RIGHT)
	{
	    right = false;
	}
    }


    public CubeAdventures ()
    {
	super ("CubeAdventures");       // Set the frame's name
	setSize (740, 420);     // Set the frame's size
	frame = getContentPane ();
	addKeyListener (this);
	setResizable (false);

	Timer timer = new Timer (40, new ActionListener ()  //Declare and activate a timer (runs every 40 milliseconds)
	{
	    public void actionPerformed (ActionEvent evt)
	    {
		movementMethod (); //The timer runs the movement method, which runs everything else
	    }
	}
	);
	timer.start ();

	show ();        // Show the frame
	whichLevel (); //Runs the method to set the stage for the first level (level 0)
    } // Constructor


    public void paint (Graphics g)
    {
	Color ground = new Color (70, 24, 10); //Declare the color of the ground
	if (tutorial == false) //Only paints if tutorial is false
	{
	    int i = 0; //Declare the loop variable 'i'

	    //Create the background as a big rectangle
	    Color background = new Color (98, 164, 255);
	    g.setColor (background);
	    g.fillRect (0, 0, 740, 420);

	    //Draws the ground/wall pieces for every level
	    g.setColor (ground);
	    for (i = 0 ; i < blockNum ; i++) //Loop only runs to the declared amount (declared every level)
	    {
		g.fillRect (blockXPos [i], blockYPos [i], blockWidth [i], blockHeight [i]);
	    }

	    //Draws the player
	    g.setColor (Color.GREEN);
	    g.fillRect (xPos, yPos, 25, 25);

	    //Draws the end point
	    g.setColor (Color.BLUE);
	    g.fillRect (endPointXPos, endPointYPos, 35, 35);

	    //Draws the coins for every level
	    g.setColor (Color.YELLOW);
	    for (i = 0 ; i < coinNum ; i++) //Loop only runs to the declared amount (declared every level)
	    {
		g.fillOval (coinsXPos [i], coinsYPos [i], 18, 18);
	    }

	    //Draws the enemies for every level
	    g.setColor (Color.RED);
	    for (i = 0 ; i < enemyNum ; i++) //Loop only runs to the declared amount (declared every level)
	    {
		g.fillRect (enemyXPos [i], enemyYPos [i], enemyWidth [i], enemyHeight [i]);
	    }

	    if (levelCount == 4) //If you reached the end screen, it will paint the end screen stuff
	    {
		//Declare font
		Font fontEndScreen = new Font ("Arial", 2, 24);

		//Display all scores box
		g.setColor (Color.PINK);
		g.fillRect (300, 295, 30, 30);

		//Search for name box
		g.setColor (Color.MAGENTA);
		g.fillRect (600, 295, 30, 30);

		//Displays text
		g.setColor (Color.BLACK);
		g.drawString ("Touch this block to search", 250, 230);
		g.drawString ("the highscores list for a name", 240, 250);
		g.drawString ("|", 317, 265);
		g.drawString ("V", 315, 277);
		g.drawString ("Touch this block to display", 550, 230);
		g.drawString ("the whole highscore list", 555, 250);
		g.drawString ("|", 617, 265);
		g.drawString ("V", 615, 277);

		//Displays end screen farewell text
		g.setFont (fontEndScreen);
		g.drawString ("And so ends the journey of the little cube.", 165, 50);
		g.drawString ("The cube lived happily ever after for all of eternity.", 110, 80);
		g.drawString ("Thank you for playing CubeAdventures!", 170, 110);
	    }
	}
	else if (tutorial == true) //Only paints this if tutorial is true
	{
	    //Declaring fonts
	    Font font1 = new Font ("Arial", 1, 22);
	    Font font2 = new Font ("Arial", 1, 12);
	    Font font3 = new Font ("Arial", 1, 14);

	    //Paints a gray background
	    g.setColor (Color.LIGHT_GRAY);
	    g.fillRect (0, 0, 740, 420);

	    //Setting text for different types of blocks
	    g.setFont (font2);
	    g.setColor (Color.BLACK);
	    g.drawString ("List of blocks in this game", 40, 145);
	    g.drawString ("Your Character:", 40, 175);
	    g.drawString ("Ground/Wall Blocks:", 40, 200);
	    g.drawString ("Enemy Blocks:", 40, 225);
	    g.drawString ("Coins:", 40, 250);
	    g.drawString ("End Point:", 40, 275);

	    //Setting text for controls
	    g.setFont (font3);
	    g.drawString ("Controls", 250, 90);
	    g.drawString ("Left Arrow - moves character left", 250, 110);
	    g.drawString ("Right Arrow - moves character right", 250, 130);
	    g.drawString ("Up Arrow or Space Bar - character jumps", 250, 150);

	    //Setting text for objective of game
	    g.drawString ("Instructions", 250, 190);
	    g.drawString ("The goal of this game is to adventure through the levels and", 250, 210);
	    g.drawString ("obstacles to reach the blue end point. On your way to the end", 250, 230);
	    g.drawString ("point, you can gather coins to boost your score, or die by", 250, 250);
	    g.drawString ("falling off the map or touching enemies - which lose your", 250, 270);
	    g.drawString ("points. Each level features a unique landscape, with unique", 250, 290);
	    g.drawString ("features such as moving blocks. Try and reach the end with the", 250, 310);
	    g.drawString ("least amount of deaths and the most amount of points to get the", 250, 330);
	    g.drawString ("highest score! Good luck, have fun!", 250, 350);

	    //Displaying all the colors of blocks
	    g.setFont (font2);
	    g.setColor (Color.GREEN);
	    g.fillRect (200, 164, 15, 15);
	    g.setColor (ground);
	    g.fillRect (200, 189, 15, 15);
	    g.setColor (Color.RED);
	    g.fillRect (200, 214, 15, 15);
	    g.setColor (Color.YELLOW);
	    g.fillOval (200, 239, 15, 15);
	    g.setColor (Color.BLUE);
	    g.fillRect (200, 264, 15, 15);

	    //Other text
	    g.setFont (font1);
	    g.setColor (Color.BLACK);
	    g.drawString ("How To Play", 295, 60);
	    g.drawString ("Press escape to continue", 235, 390);
	}

    } // paint method


    public void movementMethod ()  //CHARACTER MOVEMENT METHOD
    {
	if (right == true && xPos < 710) //Moves block right
	{
	    xPos += 5;
	}

	if (left == true && xPos > 5) //Moves block left
	{
	    xPos -= 5;
	}

	if (jump == true && jumptimer > 0) //Moves block upwards
	{
	    jumptimer -= 1; //Jumptimer is the set amount of blocks the character can move up until
	    yPos -= 5; //Jumptimer is always reset to 12, so the total height the character can jump to is 60 pixels(5 * 12)
	}

	if (yPos > 430) //Falls off the map (death)
	{
	    xPos = 10;
	    yPos = 345;
	    deaths++; //D E A T H C O U N T E R
	}
	blockMovement (); //Runs the blockmovement method to check if anything will need to move
	collisionMethod (); //Finally, it runs the collision method to see if the character actually collides with anything

	if (tutorial == false) //Only repaints if you're not in tutorial
	{
	    repaint (); //Redraws the paint method to account for any changed graphics
	}
    }


    public void collisionMethod ()  //COLLISION DETECTION METHOD)
    {
	onGround = false; //Assumes that you are always falling
	int i = 0; //Declare for loop variable as integer 'i'

	for (i = 0 ; i < blockNum ; i++) //Checks through all the blocks for collision
	{
	    if (yPos + 25 == blockYPos [i] && xPos + 25 > blockXPos [i] && xPos < blockXPos [i] + blockWidth [i]) //Detects collision with the top of the block
	    {
		jumptimer = 12; //If you touch the top of the block, it enables you to jump again
		jump = false; //Sets it so that you are not in the middle of jumping
		onGround = true; //Sets it so that you are actually not falling
		if (levelCount == 2) //This if statement only works for the 2nd level, in which your character sticks to moving platforms
		{
		    if (blockRight == true && i == 1 || blockLeft == true && i == 2 || blockLeft == true && i == 11 || blockLeft == true && i == 12)
		    {
			xPos += 5; //If block [1] is moving right and you're on the top of block [1], you will move right with it
			//Same goes for the other blocks but in the other direction because they move directly opposite to block [1]
		    }
		    else if (blockLeft == true && i == 1 || blockRight == true && i == 2 || blockRight == true && i == 11 || blockRight == true && i == 12)
		    {
			xPos -= 5; //Moves your character left
		    }
		}
	    }
	    else if (yPos == blockYPos [i] + blockHeight [i] - 5 && xPos + 25 > blockXPos [i] && xPos < blockXPos [i] + blockWidth [i]) //Detects collision with the bottom of the block
	    {
		yPos += 5; //Pushes you out of the block first
		jumptimer = 0; //Then sets jumptimer to 0 so you immediately fall
	    }

	    if (yPos + 25 > blockYPos [i] && yPos < blockYPos [i] + blockHeight [i]) //Parameters in y position when touching the sides of the blocks
	    {
		if (xPos + 20 == blockXPos [i] || xPos + 15 == blockXPos [i]) //Detects collision with left side of the block (detects whether you're 5 pixels in the block or 10)
		{
		    xPos = blockXPos [i] - 25; //Moves your character so it can't go through the block (moves it to it's left side)
		}
		if (xPos == blockXPos [i] + blockWidth [i] - 5 || xPos == blockXPos [i] + blockWidth [i] - 10) //Detects collision with right side of the block (detects whether you're 5 pixels in the block or 10)
		{
		    xPos = blockXPos [i] + blockWidth [i]; //Moves your character to it can't go through the block (moves it to it's right side)
		}
	    }
	}

	if (jumptimer == 0 && tutorial == false || onGround == false && yPos < 420 && jump == false && tutorial == false || yPos < 17 && tutorial == false) //Detects when the player should fall due to gravity
	{
	    yPos += 5; //Moves character downwards
	    jumptimer = 0; //Sets their ability to jump to 0
	}

	for (i = 0 ; i < coinNum ; i++) //Collision with coins
	{
	    if (xPos + 25 > coinsXPos [i] && xPos < coinsXPos [i] + 15) //Coin collision detection (in x domain)
	    {
		if (yPos + 25 > coinsYPos [i] && yPos < coinsYPos [i] + 15) //Coin collision detection (in y domain)
		{
		    coins++; //Gives you a coin in the coin counter
		    coinsXPos [i] = -300; //Moves the coin far away
		}
	    }
	}

	if (xPos + 24 > endPointXPos && xPos < endPointXPos + 35) //Coliision with the end point (in x domain)
	{
	    if (yPos + 24 > endPointYPos && yPos < endPointYPos + 35) //Coliision with the end point (in y domain)
	    {
		stageDone = true; //YOU FINISHED, CONGRATZZ!!!
		levelCount++; //Makes your level count go up one
		whichLevel (); //Runs the next level
	    }
	}

	for (i = 0 ; i < enemyNum ; i++) //Collision with enemies
	{
	    if (xPos + 24 > enemyXPos [i] && xPos < enemyXPos [i] + enemyWidth [i]) //Collision with enemies (in x domain)
	    {
		if (yPos + 24 > enemyYPos [i] && yPos < enemyYPos [i] + enemyHeight [i]) //Collision with enemies (in y domain)
		{
		    xPos = 10; //Repawn point x
		    yPos = 345; //Respawn point y
		    deaths++; //D E A T H C O U N T E R
		}
	    }
	}

	if (levelCount == 4 && yPos + 25 > 295 && yPos < 325) //Collision in the end screen when you want to search for a person
	{
	    if (xPos + 25 > 300 && xPos < 330)
	    {
		readFile (nam, score); //Reads the file
		String name = JOptionPane.showInputDialog ("Which person do you want to search for?"); //Asks you to search for a person
		positionSearch (name, nam, score); //Runs the position search method
		right = false; //MAKES IT SO YOU DON'T REPEATEDLY MOVE RIGHT -- REALLY ANNOYING
		xPos = 265; //sets xPos so you're not in the block anymore
	    }

	    if (xPos + 25 > 600 && xPos < 630) //Collision in the end screen when you want to display all scores
	    {
		String everythingOut = ""; //Declare a string to get all information
		readFile (nam, score); //Reads the file
		for (i = 0 ; i < nam.length - 1 ; i++) //Runs through all top 20 names
		{
		    everythingOut = everythingOut + nam [i] + " - " + score [i] + "\n"; //Puts all the numbers into the string
		}
		JOptionPane.showMessageDialog (null, everythingOut); //OUTPUT EVERYTHING
		right = false; //MAKES IT SO YOU DON'T REPEATEDLY MOVE RIGHT -- REALLY ANNOYING
		xPos = 565; //sets xPos so you're not in the block anymore
	    }
	}

    }


    public void blockMovement ()  //BLOCK MOVEMENT METHOD (MOVING PLATFORMS, MOVING ENEMIES)
    {
	if (levelCount == 1 && tutorial == false) //The blocks only move on level 1 and if tutorial is false
	{
	    if (enemyXPos [4] == 165) //Moves right depending on where it is
	    {
		enemyLeft = false;
		enemyRight = true;
	    }

	    if (enemyXPos [4] == 705) //Moves left depending on where it is
	    {
		enemyRight = false;
		enemyLeft = true;
	    }

	    if (enemyRight == true) //Makes all the blocks move based on the variable enemyLeft and enemyRight
	    {
		enemyXPos [4] = enemyXPos [4] + 15;
		enemyXPos [5] = enemyXPos [5] - 10;
		enemyXPos [6] = enemyXPos [6] + 10;
		coinsXPos [4] = coinsXPos [4] - 10;
		coinsXPos [5] = coinsXPos [5] + 10;
	    }
	    else if (enemyLeft == true)
	    {
		enemyXPos [4] = enemyXPos [4] - 15;
		enemyXPos [5] = enemyXPos [5] + 10;
		enemyXPos [6] = enemyXPos [6] - 10;
		coinsXPos [4] = coinsXPos [4] + 10;
		coinsXPos [5] = coinsXPos [5] - 10;
	    }
	}
	else if (levelCount == 2 && tutorial == false) //The blocks only move on level 2 and if tutorial is false
	{
	    if (blockXPos [1] == 120) //Moves right depending on where it is
	    {
		blockRight = true;
		blockLeft = false;
	    }

	    if (blockXPos [1] == 600) //Moves left depending on where it is
	    {
		blockLeft = true;
		blockRight = false;
	    }

	    if (blockRight == true) //Makes all the blocks move based on the variable blockLeft and blockRight
	    {
		blockXPos [1] = blockXPos [1] + 5;
		blockXPos [2] = blockXPos [2] - 5;
		blockXPos [11] = blockXPos [11] - 5;
		blockXPos [12] = blockXPos [12] - 5;
	    }
	    else if (blockLeft == true)
	    {
		blockXPos [1] = blockXPos [1] - 5;
		blockXPos [2] = blockXPos [2] + 5;
		blockXPos [11] = blockXPos [11] + 5;
		blockXPos [12] = blockXPos [12] + 5;
	    }
	}
	else if (levelCount == 3 && tutorial == false) //The blocks only move on level 3 and if tutorial is false
	{
	    if (enemyXPos [13] == 280) //Moves right depending on where it is
	    {
		enemyLeft = false;
		enemyRight = true;
	    }

	    if (enemyXPos [13] == 440) //Moves left depending on where it is
	    {
		enemyRight = false;
		enemyLeft = true;
	    }

	    if (enemyRight == true) //Moves the block left and right depending on which variable is true
	    {
		enemyXPos [13] = enemyXPos [13] + 5;
	    }
	    else if (enemyLeft == true)
	    {
		enemyXPos [13] = enemyXPos [13] - 5;
	    }
	}
    }


    public void whichLevel ()  //Finds out which level you are on
    {
	if (levelCount == 0) //Runs level 0 right off the bat
	{
	    level0 ();
	}
	if (stageDone == true) //Only works if stage done is true
	{
	    if (levelCount == 1) //Level 1
	    {
		level1 ();
		stageDone = false;
	    }
	    else if (levelCount == 2) //Level 2
	    {
		level2 ();
		stageDone = false;
	    }
	    else if (levelCount == 3) //Level 3
	    {
		level3 ();
		stageDone = false;
	    }
	    else if (levelCount == 4) //End screen
	    {
		endScreen ();
		stageDone = false;
	    }
	}
    }


    public static void main (String[] args)
    {
	new CubeAdventures ();
    } // main method


    public void level0 ()
    {
	//Declare endpoint positions
	endPointXPos = 705;
	endPointYPos = 335;

	//Declare the number of total blocks, coins and enemies present
	blockNum = 14;
	coinNum = 3;
	enemyNum = 0;

	//Declare the loop variable 'i'
	int i = 0;

	//Draws all the blocks
	blockXPos [0] = 0;
	blockYPos [0] = 370;
	blockWidth [0] = 100;
	blockHeight [0] = 50;

	for (i = 1 ; i < 5 ; i++)
	{
	    blockWidth [i] = 30;
	    blockHeight [i] = 45;
	    blockYPos [i] = 330 - (i - 1) * 30;
	    blockXPos [i] = 100 + (i - 1) * 100;
	}

	for (i = 5 ; i < 9 ; i++)
	{
	    blockWidth [i] = 30;
	    blockHeight [i] = 15;
	}

	blockXPos [5] = 310;
	blockYPos [5] = 180;

	blockXPos [6] = 245;
	blockYPos [6] = 120;

	blockXPos [7] = 120;
	blockYPos [7] = 155;

	blockXPos [8] = 10;
	blockYPos [8] = 115;

	blockXPos [9] = 5;
	blockYPos [9] = 60;
	blockWidth [9] = 235;
	blockHeight [9] = 15;

	blockXPos [10] = 295;
	blockYPos [10] = 60;
	blockWidth [10] = 295;
	blockHeight [10] = 15;

	blockXPos [11] = 525;
	blockYPos [11] = 155;
	blockWidth [11] = 10;
	blockHeight [11] = 10;

	blockXPos [12] = 705;
	blockYPos [12] = 370;
	blockWidth [12] = 35;
	blockHeight [12] = 50;

	blockXPos [13] = 600;
	blockYPos [13] = 395;
	blockWidth [13] = 10;
	blockHeight [13] = 10;

	//Draws all the coins
	coinsXPos [0] = 16;
	coinsYPos [0] = 96;

	coinsXPos [1] = 521;
	coinsYPos [1] = 136;

	coinsXPos [2] = 596;
	coinsYPos [2] = 376;
    }


    public void level1 ()
    {
	//Declare endpoint position
	endPointXPos = 0;
	endPointYPos = 35;
	//Set player position
	xPos = 10;
	yPos = 345;

	//Declare the number of total blocks, coins and enemies present
	blockNum = 19;
	coinNum = 6;
	enemyNum = 11;

	//Declare the loop variable 'i'
	int i = 0;

	//Make all the enemies have the same parameters
	for (i = 0 ; i < 12 ; i++)
	{
	    enemyHeight [i] = 20;
	    enemyWidth [i] = 20;
	}

	//Draws all the blocks
	for (i = 0 ; i < 19 ; i++)
	{
	    blockWidth [i] = 650;
	    blockHeight [i] = 15;
	}

	blockXPos [0] = 0;
	blockYPos [0] = 370;

	blockXPos [1] = 0;
	blockYPos [1] = 270;

	blockXPos [2] = 90;
	blockYPos [2] = 170;

	blockXPos [3] = 710;
	blockYPos [3] = 315;
	blockWidth [3] = 25;

	blockXPos [4] = 0;
	blockYPos [4] = 210;
	blockWidth [4] = 25;

	blockXPos [5] = 710;
	blockYPos [5] = 115;
	blockWidth [5] = 25;

	blockXPos [6] = 0;
	blockYPos [6] = 70;
	blockWidth [6] = 60;

	blockXPos [7] = 90;
	blockYPos [7] = 70;
	blockWidth [7] = 30;

	blockXPos [8] = 150;
	blockYPos [8] = 70;
	blockWidth [8] = 15;

	blockXPos [9] = 195;
	blockYPos [9] = 70;
	blockWidth [9] = 40;

	blockXPos [10] = 230;
	blockYPos [10] = 70;
	blockWidth [10] = 15;

	blockXPos [11] = 295;
	blockYPos [11] = 70;
	blockWidth [11] = 60;

	blockXPos [12] = 380;
	blockYPos [12] = 70;
	blockWidth [12] = 15;

	blockXPos [13] = 430;
	blockYPos [13] = 70;
	blockWidth [13] = 30;

	blockXPos [14] = 520;
	blockYPos [14] = 70;
	blockWidth [14] = 50;

	blockXPos [15] = 610;
	blockYPos [15] = 70;
	blockWidth [15] = 40;

	blockXPos [15] = 605;
	blockYPos [15] = 70;
	blockWidth [15] = 10;

	blockXPos [16] = 655;
	blockYPos [16] = 70;
	blockWidth [16] = 15;

	blockXPos [17] = 335;
	blockYPos [17] = 225;
	blockWidth [17] = 15;

	//Draws all the enemies and coins
	for (i = 0 ; i < 4 ; i++)
	{
	    enemyXPos [i] = 180 + i * 150;
	    enemyYPos [i] = 150;

	    coinsXPos [i] = 182 + i * 150;
	    coinsYPos [i] = 125;
	}
	enemyXPos [4] = 180;
	enemyYPos [4] = 90;

	enemyXPos [5] = 620;
	enemyYPos [5] = 250;

	enemyXPos [6] = 10;
	enemyYPos [6] = 250;

	for (i = 7 ; i < 11 ; i++)
	{
	    enemyXPos [i] = 105 + (i - 7) * 150;
	    enemyYPos [i] = 365;
	}

	coinsXPos [4] = 622;
	coinsYPos [4] = 225;

	coinsXPos [5] = 12;
	coinsYPos [5] = 225;

    }


    public void level2 ()
    {
	//Declare endpoint position
	endPointXPos = 705;
	endPointYPos = 30;
	//Declare player position
	xPos = 10;
	yPos = 345;

	//Declare the number of total blocks, coins and enemies present
	blockNum = 14;
	coinNum = 9;
	enemyNum = 14;

	//Declare the loop variable 'i'
	int i = 0;

	//Make all enemies have the same parameters
	for (i = 0 ; i < 14 ; i++)
	{
	    enemyHeight [i] = 20;
	    enemyWidth [i] = 20;
	}

	//Draws all the blocks
	blockXPos [0] = 0;
	blockYPos [0] = 370;
	blockWidth [0] = 100;
	blockHeight [0] = 50;

	blockXPos [1] = 120;
	blockYPos [1] = 370;
	blockWidth [1] = 105;
	blockHeight [1] = 15;

	blockXPos [2] = 550;
	blockYPos [2] = 260;
	blockWidth [2] = 105;
	blockHeight [2] = 15;

	blockXPos [3] = 670;
	blockYPos [3] = 310;
	blockWidth [3] = 25;
	blockHeight [3] = 25;

	blockXPos [4] = 150;
	blockYPos [4] = 230;
	blockWidth [4] = 20;
	blockHeight [4] = 30;

	blockXPos [5] = 135;
	blockYPos [5] = 85;
	blockWidth [5] = 35;
	blockHeight [5] = 115;

	for (i = 6 ; i < 14 ; i++)
	{
	    blockHeight [i] = 15;
	}

	blockXPos [6] = 0;
	blockYPos [6] = 220;
	blockWidth [6] = 50;

	blockXPos [7] = 105;
	blockYPos [7] = 170;
	blockWidth [7] = 30;

	blockXPos [8] = 120;
	blockYPos [8] = 120;
	blockWidth [8] = 15;

	blockXPos [9] = 530;
	blockYPos [9] = 120;
	blockWidth [9] = 15;

	blockXPos [10] = 675;
	blockYPos [10] = 65;
	blockWidth [10] = 65;

	blockXPos [11] = 575;
	blockYPos [11] = 110;
	blockWidth [11] = 75;

	blockXPos [12] = 650;
	blockYPos [12] = 160;
	blockWidth [12] = 75;

	blockXPos [13] = 420;
	blockYPos [13] = 80;
	blockWidth [13] = 15;

	//Draws all the enemies
	enemyXPos [0] = 0;
	enemyYPos [0] = 255;

	for (i = 0 ; i < 4 ; i++)
	{
	    enemyXPos [i] = i * 20;
	    enemyYPos [i] = 255;
	}

	enemyXPos [4] = 65;
	enemyYPos [4] = 255;

	enemyXPos [5] = 250;
	enemyYPos [5] = 350;

	enemyXPos [6] = 425;
	enemyYPos [6] = 350;

	enemyXPos [7] = 520;
	enemyYPos [7] = 310;

	enemyXPos [8] = 585;
	enemyYPos [8] = 315;

	enemyXPos [9] = 520;
	enemyYPos [9] = 240;

	enemyXPos [10] = 400;
	enemyYPos [10] = 215;

	enemyXPos [11] = 280;
	enemyYPos [11] = 240;

	enemyXPos [12] = 300;
	enemyYPos [12] = 140;

	enemyXPos [13] = 465;
	enemyYPos [13] = 25;

	//Draws all the coins
	coinsXPos [0] = 340;
	coinsYPos [0] = 350;

	coinsXPos [1] = 630;
	coinsYPos [1] = 350;

	coinsXPos [2] = 620;
	coinsYPos [2] = 215;

	coinsXPos [3] = 402;
	coinsYPos [3] = 240;

	coinsXPos [4] = 110;
	coinsYPos [4] = 240;

	coinsXPos [5] = 5;
	coinsYPos [5] = 70;

	coinsXPos [6] = 419;
	coinsYPos [6] = 60;

	coinsXPos [7] = 529;
	coinsYPos [7] = 100;

	coinsXPos [8] = 705;
	coinsYPos [8] = 85;
    }



    public void level3 ()
    {
	//Declare endpoint position
	endPointXPos = 354;
	endPointYPos = 130;
	//Declare player position
	xPos = 10;
	yPos = 345;

	//Declare the number of total blocks, coins and enemies present
	blockNum = 26;
	coinNum = 7;
	enemyNum = 19;

	//Declare the loop variable as 'i'
	int i = 0;

	//Draws all the blocks
	blockXPos [0] = 0;
	blockYPos [0] = 370;
	blockWidth [0] = 740;
	blockHeight [0] = 50;

	blockXPos [1] = 355;
	blockYPos [1] = 335;
	blockWidth [1] = 30;
	blockHeight [1] = 45;

	blockXPos [2] = 355;
	blockYPos [2] = 280;
	blockWidth [2] = 30;
	blockHeight [2] = 15;

	blockXPos [3] = 105;
	blockYPos [3] = 65;
	blockWidth [3] = 375;
	blockHeight [3] = 15;

	for (i = 4 ; i < 9 ; i++)
	{
	    blockWidth [i] = 20;
	    blockHeight [i] = 20;
	    if (i % 2 == 0)
	    {
		blockXPos [i] = 715;
	    }
	    else
	    {
		blockXPos [i] = 665;
	    }
	    blockYPos [i] = 75 + (i - 4) * 60;
	}

	blockXPos [9] = 650;
	blockYPos [9] = 75;
	blockWidth [9] = 15;
	blockHeight [9] = 210;

	blockXPos [10] = 570;
	blockYPos [10] = 0;
	blockWidth [10] = 15;
	blockHeight [10] = 215;

	for (i = 11 ; i < 26 ; i++)
	{
	    blockWidth [i] = 25;
	    blockHeight [i] = 10;
	}

	blockXPos [11] = 590;
	blockYPos [11] = 255;

	blockXPos [12] = 535;
	blockYPos [12] = 255;

	blockXPos [13] = 480;
	blockYPos [13] = 205;

	blockXPos [14] = 455;
	blockYPos [14] = 145;

	blockXPos [15] = 545;
	blockYPos [15] = 170;

	blockXPos [16] = 545;
	blockYPos [16] = 100;

	blockXPos [17] = 570;
	blockYPos [17] = 0;

	for (i = 18 ; i < 26 ; i++)
	{
	    blockWidth [i] = 30;
	}


	blockXPos [18] = 15;
	blockYPos [18] = 240;

	blockXPos [19] = 125;
	blockYPos [19] = 115;

	blockXPos [20] = 90;
	blockYPos [20] = 210;

	blockXPos [21] = 160;
	blockYPos [21] = 210;

	blockXPos [22] = 235;
	blockYPos [22] = 255;

	blockXPos [23] = 255;
	blockYPos [23] = 170;

	blockXPos [24] = 255;
	blockYPos [24] = 115;

	blockXPos [25] = 190;
	blockYPos [25] = 125;

	//Draws all the enemies
	enemyXPos [0] = 0;
	enemyYPos [0] = 270;
	enemyWidth [0] = 285;
	enemyHeight [0] = 15;

	enemyXPos [1] = 455;
	enemyYPos [1] = 270;
	enemyWidth [1] = 195;
	enemyHeight [1] = 15;

	enemyXPos [2] = 270;
	enemyYPos [2] = 180;
	enemyWidth [2] = 15;
	enemyHeight [2] = 90;

	enemyXPos [3] = 455;
	enemyYPos [3] = 180;
	enemyWidth [3] = 15;
	enemyHeight [3] = 90;

	enemyXPos [4] = 270;
	enemyYPos [4] = 180;
	enemyWidth [4] = 200;
	enemyHeight [4] = 15;

	enemyXPos [5] = 285;
	enemyYPos [5] = 125;
	enemyWidth [5] = 15;
	enemyHeight [5] = 55;

	enemyXPos [6] = 440;
	enemyYPos [6] = 80;
	enemyWidth [6] = 15;
	enemyHeight [6] = 100;

	for (i = 7 ; i < 14 ; i++)
	{
	    enemyWidth [i] = 20;
	    enemyHeight [i] = 20;
	    enemyYPos [i] = 285;
	    if (i < 10)
	    {
		enemyXPos [i] = 100 + (i - 7) * 50;
	    }
	    else
	    {
		enemyXPos [i] = 495 + (i - 10) * 50;
	    }
	}

	enemyYPos [8] = 350;

	enemyYPos [11] = 350;

	enemyXPos [13] = 280;
	enemyYPos [13] = 225;

	for (i = 14 ; i < 17 ; i++)
	{
	    enemyWidth [i] = 10;
	    enemyHeight [i] = 10;
	    enemyXPos [i] = 585;
	    enemyYPos [i] = 95 + (i - 14) * 45;
	}

	enemyXPos [15] = 640;

	//Draws all the coins
	coinsXPos [0] = 297;
	coinsYPos [0] = 200;

	coinsXPos [1] = 425;
	coinsYPos [1] = 200;

	coinsXPos [2] = 21;
	coinsYPos [2] = 220;

	coinsXPos [3] = 241;
	coinsYPos [3] = 235;

	coinsXPos [4] = 131;
	coinsYPos [4] = 95;

	coinsXPos [5] = 585;
	coinsYPos [5] = 135;

	coinsXPos [6] = 720;
	coinsYPos [6] = 350;
    }


    public void endScreen ()
    {
	//Declare the number of total blocks, coins and enemies present
	blockNum = 7;
	coinNum = 0;
	enemyNum = 0;

	//Declare where you start
	xPos = 10;
	yPos = 345;
	endPointXPos = -200; //Moves the endpoint far far away

	//Draws all the blocks
	blockXPos [0] = 0;
	blockYPos [0] = 380;
	blockWidth [0] = 740;
	blockHeight [0] = 40;

	blockXPos [1] = 200;
	blockYPos [1] = 325;
	blockWidth [1] = 130;
	blockHeight [1] = 15;

	blockXPos [2] = 500;
	blockYPos [2] = 325;
	blockWidth [2] = 130;
	blockHeight [2] = 15;

	blockXPos [3] = 295;
	blockYPos [3] = 280;
	blockWidth [3] = 35;
	blockHeight [3] = 15;

	blockXPos [4] = 595;
	blockYPos [4] = 280;
	blockWidth [4] = 35;
	blockHeight [4] = 15;

	blockXPos [5] = 330;
	blockYPos [5] = 280;
	blockWidth [5] = 15;
	blockHeight [5] = 60;

	blockXPos [6] = 630;
	blockYPos [6] = 280;
	blockWidth [6] = 15;
	blockHeight [6] = 60;

	repaint (); //Repaint

	//Calculate score
	int totalScore;
	totalScore = 400 + (coins * 20) - (deaths * 25);
	//Shows you your score
	JOptionPane.showMessageDialog (null, "Coins Collected: " + coins + "\nDeaths: " + deaths + "\nStage Complete Score: 400" + "\nTotal Score: " + totalScore);

	
	//Set your name
	String yourName;
	yourName = JOptionPane.showInputDialog ("Enter a name to be noted as!");

	
	readFile (nam, score); //Runs the reading method
	sortAndReplace (yourName, totalScore, nam, score); //Runs the sorting and cutting off method (cuts off the 21st place score (if you beat it))
	writeFile (nam, score); //Runs the writing method
	
    }


    public void readFile (String nam[], int score[])  //Reading file method
    {
	try //try
	{
	    BufferedReader fr = new BufferedReader (new FileReader ("HighScoreCubeAdventures.txt")); //CHANGE IF YOU WANT THE READING TO WORK

	    //Declare necessary variables
	    String record = "";
	    String temporaryArray[] = new String [2];

	    for (int i = 0 ; i < nam.length ; i++) //Reads all of the document
	    {
		record = fr.readLine (); //Read the line
		if (record == null || record.equals ("")) //Breaks it if there is an error
		{
		    break;
		}
		temporaryArray = record.split (" - "); //Split the score and names
		nam [i] = temporaryArray [0]; //Get name
		score [i] = Integer.parseInt (temporaryArray [1]); //Get score

	    }
	    fr.close();
	}
	catch (Exception e)  //Catch
	{
	    JOptionPane.showMessageDialog (null, "There is an error when reading your file! Change your file location in the code"); //error
	}
    }


    public void sortAndReplace (String yourName, int totalScore, String nam[], int score[])  //Sorting method
    {
	//Adds your score to the arrays
	nam [20] = yourName;
	score [20] = totalScore;

	//Declare necessary variables
	int tempI = 0, yourScore = score [20];
	String tempS = "";

	for (int i = 0 ; i < (score.length - 1) ; i++) //For loop that keeps on subracting 1, because the bubble sort always gets the last person right for sure
	{
	    for (int j = 0 ; j < (score.length - 1 - i) ; j++) //For loop to run through all the names the correct amount of times
	    {
		if (score [j] < score [j + 1]) //Runs if the first letter of the last name is lower alphabetically than the first letter of the second name
		{
		    //Switch scores
		    tempI = score [j];
		    score [j] = score [j + 1];
		    score [j + 1] = tempI;

		    //Switch names
		    tempS = nam [j];
		    nam [j] = nam [j + 1];
		    nam [j + 1] = tempS;
		}
	    }
	}

	if (yourScore > score [20]) //Checks if you are higher than last place
	{
	    JOptionPane.showMessageDialog (null, "You're in! You're higher than the 20th place score!");
	}


	else if (yourScore <= score [20]) //Checks if you are lower or equal to last place
	{
	    JOptionPane.showMessageDialog (null, "Dang, looks like you didn't beat the 20th place score. Better luck next time!");
	}
    }


    public void writeFile (String nam[], int score[])  //Writing method
    {
	try //try
	{
	    PrintWriter pw = new PrintWriter (new FileWriter ("HighScoreCubeAdventures.txt")); 

	    for (int i = 0 ; i < nam.length ; i++)
	    {
		if (i == nam.length - 1)
		{
		    pw.print ("");
		    break;
		}
		pw.println (nam [i] + " - " + score [i]);
	    }

	    pw.close (); //Closes printwriter
	}


	catch (Exception e)  //Catch
	{
	    JOptionPane.showMessageDialog (null, "There is an error when writing to your file! Change your file location in the code"); //error
	}
    }


    public static void positionSearch (String name, String nam[], int score[])  //Position search method
    {
	int error = 0;
	for (int i = 0 ; i < nam.length ; i++) //Searches through all the names until a matching name is found
	{
	    if (nam [i].equalsIgnoreCase (name))
	    {
		JOptionPane.showMessageDialog (null, name + " - " + score [i]); //Shows your score
		error = 1;
		break; //breaks so it only displays top score!
	    }
	}

	if (error == 0) //Name was not found so error is still 0
	{
	    JOptionPane.showMessageDialog (null, "Your entered name was not found, try again"); //Error message
	}
    }
} // CubeAdventures class


