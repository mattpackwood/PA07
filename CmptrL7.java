/* Matt Packwood, Orchard Ridge Campus, Monday Evening Class, Fall Semester 2003
 *
 * PA07: Seperate PA05 into applet and model classes per model-view-controller
 * (MVC) paradigm from C9-10; PA07 behavior similar to PA05 
 *
 * The applet class creates widget and two model computer objects.
 * The applet changes the model state thru setter calls to the active 
 * computer object from the event method; the repaint call from the event
 * method activates the display call to the active computer object, which
 * reflects the updated model state on the screen.  Getter calls from the 
 * applet paint method update the status display of the model state values
 *
 */
import java.awt.*;
import java.applet.*;
import java.awt.event.*; // event lib
 
public class CmptrL7 extends Applet
		implements AdjustmentListener, ActionListener { // mixed events
	Button selctB, zipB; // buttons
	Scrollbar xBar, ballBar; // scrollbar vars
	boolean zipF= false, homeF= true; // logic switchs
	int hX, wX, hY, wY, hS, wS; // base vars; h= home, w= work
	int sW, sH; // applet screen width/Ht
	int wH= 63; // space for widgets at top of screen...
	int minX= 9, maxX, incrX= 9; // base x limits and increment
	int ballCt= 0;
	CompSys home, work, actv; // ** MODEL FIG OBJECT VARS

public void init ( ) {
	Dimension size= getSize ( ); // get screen size
	sW= size.width; sH= size.height; // set screen constraints
	hS= sH/14; // initial home CPU ht
	wS= sH/12; // initial work CPU ht
	maxX= sW-(6*wS); // right edge minus offset 
	hX= minX; // home on left edge
	wX= maxX; // work on right edge
	hY= (int) (sH * 0.75f); // both 1/4 above screen bot
	wY= (int) (sH * 0.75f);
	setBackground (Color.white); // BG color
	add (new Label ("toggle buttons for home/work computer and ZIP slot; "+
		"Scrollbars: X= << move sideways >>; balls = -/+ screen balls "));  
	selctB= new Button ("HOME");
	add (selctB);
	selctB.addActionListener (this);
	zipB= new Button ("ZIP");
	add (zipB);
	zipB.addActionListener (this);
	add (new Label ("X >>"));
	xBar= new Scrollbar (Scrollbar.HORIZONTAL, wX, 1, 0, maxX); // constrain to stay within screen
	add (xBar);
	xBar.addAdjustmentListener (this);
	xBar.setUnitIncrement (incrX); // override default with x incr
 	add (new Label ("balls >>"));  
	ballBar= new Scrollbar (Scrollbar.HORIZONTAL, 0, 1, 0, 7);
	add (ballBar);
	ballBar.addAdjustmentListener (this);
	// ** CONSTRUCT MODEL FIG OBJECTS **
	home= new CompSys(hX,hY,hS);
	work= new CompSys(wX,wY,wS);
	actv= work;
   	}
public void paint (Graphics g) {
	ballCt= actv.getBallCt(); // ** GETTER METHOD CALLS **
	zipF= actv.getZipStat();
	hX= home.getX(); 
	wX= work.getX();
	showStatus ("PEx 7:"+(homeF ? "HOME" : "WORK") + " actv; ballCt="+ ballCt+ ", ZIP= "+zipF+", homeX="+hX+", workX="+wX);
	// actv object name, ball ct, ZIP status, home X, work X

	// ** OBJECT DISPLAY CALLS HERE **
	home.dsplyFig (g);
	work.dsplyFig (g);

	g.setColor (Color.black);
	g.drawString ("Home", hX+2*hS, hY+2*hS);// display names beneath objects
	g.drawString ("Work", wX+2*wS, wY+2*wS);
	}
public void actionPerformed (ActionEvent e) {
	if (e.getSource ( ) == selctB) { // ** SWITCH ACTV OBJECT **
		// toggle actv model switch here
		homeF = ! homeF; // toggle
		if (homeF) {
			// update button label here
			selctB.setLabel ("HOME"); 
			// update actv obj ref here
			actv=work;
			}
		else { // homeF false implies work....
			// update button label here
			selctB.setLabel ("WORK");
			// update actv obj ref here
			actv=home;
			}

		// set X scrollbar ctr to X value from actv object here
		xBar.setValue (actv.getX () );
		// set ball scrollbar ctr to ballCt value from actv object here
		ballBar.setValue (actv.getBallCt() );
		
		}
	else if (e.getSource ( ) == zipB) // add/remove ZIP slot
		// toggle ZIP status in active object here
		actv.toglZipStat ( );
	repaint ( );
	}
public void adjustmentValueChanged (AdjustmentEvent e) { // SB events
	// set X in actv object from X scrollbar here
	actv.setX (xBar.getValue ( )); // new val for actv fig 

	// set ball count in actv object from ball scrollbar here
	actv.setBallCt (ballBar.getValue ( )); 
	
	repaint ( ); // update display
	}
} 
class CompSys { // ** MODEL CLASS **
	// ** INSTANCE VARS; COPIED TO EACH OBJECT DURING CONSTRUCTION
	int bX, bY, bS; // base vars
	int qS, hS, dS; // work ratio vars; yours may vary... 
	int ballCt; // ball ctr
	boolean zipF; // ZIP option on/off
	Graphics g; // class-scope screen ref
// ** PUBLIC, INTERACTION METHODS **
public CompSys (int x, int y, int s) { // ** CONSTRUCTOR **
	// init global base, ballCt and zipF vals here
	bX = x;
	bY = y;
	bS = s;
	}
public void setX (int x) { // ** SETTER METHODS **
	// update bX here
	bX=x;
	}
public void setBallCt (int ct) {
	// update ballCt here
	ballCt = ct;
	}
public void toglZipStat ( ) { 
	// toggle zipF here
	zipF = ! zipF;
	}
public boolean getZipStat ( ) { // ** GETTER METHODS **
	return zipF;
	}
public int getBallCt ( ) {
	return ballCt;
	}
public int getX ( ) {
	return bX;
	}
public void dsplyFig (Graphics gg) { // ** DISPLAY METHOD **
	g= gg;
	calcRatios ( );
	//	YOUR PEx5 display stuff here....
	dsplyMonitor ( ); 
	g.setColor (Color.yellow);
	g.fillRect (bX, bY-bS, dS+dS+bS, bS); // CPU
	g.fillRect (bX, bY, dS+dS, bS); // Keyboard
	g.fillOval (bX+dS+dS+qS, bY+qS, hS, hS); // Mouse
	g.setColor (Color.black);
	g.drawRect (bX, bY-bS, dS+dS+bS, bS); // CPU outline
	g.drawRect (bX, bY, dS+dS, bS); // Keyboard outline
	g.drawOval (bX+dS+dS+qS, bY+qS, hS, hS); // Mouse outline
	g.drawLine (bX+dS+dS, bY+hS, bX+dS+dS+qS, bY+hS); // Mouse cord
	g.fillRect (bX+dS+hS, bY-hS-qS, dS, qS); // DVD slot
	g.fillRect (bX+qS, bY+qS, dS+bS, hS); // Keys
	g.fillArc (bX+dS+dS+qS, bY+qS, hS, hS, 45, 90); // Mouse 1
	g.fillArc (bX+dS+dS+qS, bY+qS, hS, hS, 225, 90); // Mouse 2
	// conditional ZIP slot display; see hat dsply technique in SF demo
	if (zipF) {
		g.fillRect (bX+hS, bY-bS+qS, bS, qS);
		}
	}
private void calcRatios ( ) { 
	// your content here
	qS= Math.round (bS/4.0f); // calc ratio vals
	hS= Math.round (bS/2.0f); // calc ratio vals
	dS= Math.round (bS*2.0f); // calc ratio vals
	}
// your private method definitions here...

private void dsplyMonitor ( ) { // ref from dsplyFig
	// same as PEx4 + 
	g.setColor (Color.yellow);
	g.fillRect (bX+hS, bY-dS-dS, dS+dS, dS+bS); // Monitor
	g.setColor (Color.black);
	g.drawRect (bX+hS, bY-dS-dS, dS+dS, dS+bS); // Monitor Outline
	g.setColor (Color.lightGray);
	g.fillRect (bX+hS+qS, bY-dS-bS-hS-qS, dS+bS+hS, dS+hS); // Screen
	// conditional screen ball display; see hair display technique in SF demo
	switch (ballCt) { // no breaks == fall-thru
		case 6: 	g.setColor (Color.black);
				g.fillOval (bX+bS+dS-(((ballCt-3)%3)*bS), bY-dS-hS, bS, bS);
		case 5: 	g.setColor (Color.red);
				g.fillOval (bX+bS+dS-(((ballCt-2)%3)*bS), bY-dS-hS, bS, bS);   
		case 4: 	g.setColor (Color.yellow);
				g.fillOval (bX+bS+dS-(((ballCt-1)%3)*bS), bY-dS-hS, bS, bS);
		case 3: 	g.setColor (Color.green);
				g.fillOval (bX+bS+dS-(((ballCt-3)%3)*bS), bY-dS-hS-((ballCt <=5 ? 0 : 1)*bS), bS, bS);
		case 2: 	g.setColor (Color.blue);
				g.fillOval (bX+bS+dS-(((ballCt-2)%3)*bS), bY-dS-hS-((ballCt <=4 ? 0 : 1)*bS), bS, bS);
		case 1: 	g.setColor (Color.white);
				g.fillOval (bX+bS+dS-(((ballCt-1)%3)*bS), bY-dS-hS-((ballCt <=3 ? 0 : 1)*bS), bS, bS);
		} // end switch; minus values ignored
	}

}
