package bRigid.test;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid02_Sphere extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	BSphere sphere;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);
		
		cam = new PeasyCam(this, 600);
	
		//create a rigid physics engine with a bounding box
		Vector3f min = new Vector3f(-120, -250, -120);
		Vector3f max = new Vector3f(120, 250, 120);
		physics = new BPhysics(min, max);
		
		physics.world.setGravity(new Vector3f(0, 500, 0));

		//create the first rigidBody as Sphere
		sphere = new BSphere(this, 2, 0, 0, 0, 20);
	}

	public void draw() {
		background(255);
		lights();
		rotateY(frameCount*.01f);

		if (frameCount % 1 == 0 && frameCount < 500) {
			Vector3f pos = new Vector3f(random(30), -150, random(1));
			//reuse the rigidBody of the sphere for performance resons
			BObject s = new BObject(this, 2, sphere, pos, true);
			physics.addBody(s);
		}

		physics.update();
		physics.display();
		
		//saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic01/pic01####.jpeg");

	}

}
