package bRigid.test;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid06_CompoundShape extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	ArrayList<BObject> crosses = new ArrayList<BObject>();
	
	boolean pause = false;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);

		cam = new PeasyCam(this, 100);

		physics = new BPhysics();
		physics.world.setGravity(new Vector3f(0, 0,0));
		
		//add an attraction force
		//BForceAttraction(Vector3f fixedPosition, float radius, float strength)
		BForceAttraction f = new BForceAttraction(new Vector3f(), 500, 100);
		physics.addBehavior(f);
		
		//create a list with collision specific objects
		ArrayList<BObject> bodies = new ArrayList<BObject>();
		BBox box0 = new BBox(this, 1, new Vector3f(4.5f, 0, 0), new Vector3f(3, 3, 12), true);
		BBox box1 = new BBox(this, 1, new Vector3f(0, 0,4.5f), new Vector3f(12, 3, 3), true);
		bodies.add(box0);
		bodies.add(box1);	
		
		for (int i = -10; i < 10; i++) {
			for (int j = -10; j < 10; j++) {
				
				//make a CompoundShape from the collisionobjects of the bodies-list
				//BCompound bComp = new BCompound(this, bodies, 5, true);
				BCompound bComp = new BCompound(this, 5, bodies, false);
				
				float posX = random(10) + i * 8;
				float posY = random(-150,150);
				float posZ = random(10) + j * 8;
				bComp.setPosition(posX, posY,posZ);

				physics.addBody(bComp);
			}
		}
		
	}

	public void draw() {
		background(255);
		lights();
		rotateY(frameCount*.01f);

		if(!pause) physics.update();
		physics.display();
		
		//saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic10/pic10####.jpeg");
	}
	
	public void keyPressed() {
		pause = !pause;
	}

}
