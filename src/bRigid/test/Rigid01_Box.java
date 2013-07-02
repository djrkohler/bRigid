package bRigid.test;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid01_Box extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	BObject rigid;

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
		rigid = new BBox(this, 1, 15,60,15);
		//box.setVelocity(random(-10,10), random(-10,10), random(-10,10));
		//box.rigidBody.setAngularVelocity(new Vector3f (random(-1,1), random(-1,1), random(-1,1)));
	}

	public void draw() {
		background(255);
		lights();
		rotateY(frameCount*.01f);

		if(frameCount%2==0) {
			Vector3f pos = new Vector3f(random(30), -150, random(1));
			//reuse the rigidBody of the sphere for performance resons
			BObject r = new BObject(this, 100, rigid, pos, true);
			physics.addBody(r);
		}

		physics.update();
		physics.display();
		
		//saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic05/pic05####.jpeg");

	}

}
