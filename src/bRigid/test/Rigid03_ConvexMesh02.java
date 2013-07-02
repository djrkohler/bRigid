package bRigid.test;

import javax.vecmath.Vector3f;
import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid03_ConvexMesh02 extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	boolean record = false;
	boolean pause = true;
	BConvexHull cross;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(30);
		fill(255);
		noStroke();

		cam = new PeasyCam(this, 200);
		cam.pan(0, 50);
		

		physics = new BPhysics();
		physics.world.setGravity(new Vector3f(0,300,0));

		// create a gorund plane
		BPlane ground = new BPlane(new Vector3f(0,300,0), new Vector3f(0,-1,0));
		physics.addPlane(ground);

		// file path to the .obj file to import
		String fileName = "D:/Eclipse/rigid/data/cross.obj";
		// toggle between the display of the convex collisionShape or your
		// imported file
		boolean displayImportMesh = false;
		//BConvexHull(PApplet p, float mass, String fileName, Vector3f position, boolean inertia, boolean displayImportMesh)
		//here passive body
		cross = new BConvexHull(this, 0, fileName, new Vector3f(), false, displayImportMesh);
		cross.setPosition(0,50,0);
		physics.addBody(cross);

	}

	public void draw() {
		background(255);
		lights();
		cam.rotateY(-.01);
		if(frameCount % 40 == 0) {
		//BObject(PApplet p, float mass, BObject body, Vector3f center, Vector3f scale, boolean inertia) 
		Vector3f scale = new Vector3f(random(.5f,2), random(.1f,2), random(.5f,2));
		BObject c = new BObject(this, 1, cross, new Vector3f(), scale, true);
		 physics.addBody(c);
		}
		
		if (pause)
			physics.update();
		physics.display();

	}

	public void keyPressed() {
		if (key == (' '))
			pause = !pause;

	}

	
}
