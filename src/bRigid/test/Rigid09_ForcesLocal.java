package bRigid.test;


import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid09_ForcesLocal extends PApplet {

	PeasyCam cam;

	BPhysics physics;
	
	BObject attr;
	
	boolean pause = false;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(30);
		fill(240);
		noStroke();

		cam = new PeasyCam(this, 500);

		physics = new BPhysics();
		physics.world.setGravity(new Vector3f(0, 0, 0));

		String fileName = "D:/Eclipse/rigid/data/cross.obj";
		boolean displayImportMesh = false;
		BConvexHull cross = new BConvexHull(this, 1, fileName, new Vector3f(random(100), 0, random(100)), true, displayImportMesh);
		physics.addBody(cross);

		for (int i = -10; i < 5; i++) {
			for (int j = -10; j < 5; j++) {

				if (i == 0 && j == 0) {
					attr = new BObject(this, 1, cross, new Vector3f(i * 18, 0, j * 18), true);
					physics.addBody(attr);
					physics.addBehavior(new BForceAttraction(attr, 300, 5));
				} else {
					
					BConvexHull cross0 = new BConvexHull(this, 1, fileName, new Vector3f(i * 18, 0, j * 18), true, displayImportMesh);
					physics.addBody(cross0);
				}
			}
		}
	}

	public void draw() {
		background(255);
		lights();

		if(!pause) physics.update();
		
		for(BObject b : physics.rigidBodies) {
			if(b == attr) {
				b.display(new Vector3f(100,0,0));		
			} else {
				b.display();
			}	
		}
		attr.display(new Vector3f(0,0,0));
		attr.rigidBody.applyCentralForce(new Vector3f(0,5,0));
	}
	
	public void keyPressed() {
		pause = !pause;

	}

}
