package bRigid.test;

import javax.vecmath.Vector3f;
import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid09_Forces_01 extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(30);
		fill(240);
		noStroke();

		cam = new PeasyCam(this, 500);

		physics = new BPhysics();
		physics.world.setGravity(new Vector3f(0, 0, 0));

		String fileName = "D:/Eclipse/rigid/data/cross.obj";
		boolean displayImportMesh = true;
		BConvexHull cross = new BConvexHull(this, 1, fileName, new Vector3f(random(100), 0, random(100)), true, displayImportMesh);
				
		
		for (int i = -8; i < 8; i++) {
			for (int j = -8; j < 8; j++) {
				BObject c = new BObject(this, 1, cross, new Vector3f(i * 15 + 0, 0, j * 15), true);
				physics.addBody(c);
			}
		}
		
		BForceAttraction f = new BForceAttraction(new Vector3f(0,-100,0),500, 10);
		physics.addBehavior(f);

	}

	public void draw() {
		background(255);
		lights();
		
		physics.update();
		physics.display();
	}

}
