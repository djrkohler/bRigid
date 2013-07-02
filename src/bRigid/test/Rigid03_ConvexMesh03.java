package bRigid.test;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid03_ConvexMesh03 extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	BConvexHull cross;
	
	boolean displayImportMesh = false;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);
		fill(250);
		noStroke();

		cam = new PeasyCam(this, 600);

		//create a rigid physics engine with a bounding box
		Vector3f min = new Vector3f(-120, -250, -120);
		Vector3f max = new Vector3f(120, 250, 120);
		physics = new BPhysics(min, max);
		
		physics.world.setGravity(new Vector3f(0, 500, 0));
		
		
		
	}

	public void draw() {
		background(255);
		lights();
		rotateY(frameCount*.01f);

		if (frameCount < 1000) {
			Vector3f pos = new Vector3f(random(30), -150, random(1));
			//Vector3f scale = new Vector3f(random(2,4), random(2,4), random(2,4));
			Vector3f scale = new Vector3f(3.2f,3.2f,3.2f);
			// toggle between the display of the convex collisionShape or your
			// imported file
			// file path to the .obj file to import
			String fileName = "D:/Eclipse/rigid/data/cross.obj";
			//BConvexHull(PApplet p, float mass, String fileName, Vector3f position, boolean inertia, boolean displayImportMesh)
			cross = new BConvexHull(this, 1, fileName,pos,true, displayImportMesh);
			cross.scale(scale);
			//BObject c = new BObject(this, 200, cross, pos, scale, true);
			physics.addBody(cross );
		}

		physics.update();
		physics.display();
		
		saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic04/pic04####.jpeg");

	}
	
	public void keyPressed() {
		if(key == ' ') displayImportMesh = !displayImportMesh;
	}

}
