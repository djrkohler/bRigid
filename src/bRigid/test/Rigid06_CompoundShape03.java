package bRigid.test;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid06_CompoundShape03 extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	ArrayList<BObject> bodies;
	BCompound bComp ;
	
	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);
		
		cam = new PeasyCam(this, 600);
	
		//create a rigid physics engine with a bounding box
		Vector3f min = new Vector3f(-120, -250, -120);
		Vector3f max = new Vector3f(120, 250, 120);
		physics = new BPhysics(min, max);
		
		physics.world.setGravity(new Vector3f(0, 500, 0));
		
		//create a list with collision specific objects
		bodies = new ArrayList<BObject>();
		BBox box0 = new BBox(this, 1, new Vector3f(18f, 0, 0), new Vector3f(12, 12, 48), true);
		BBox box1 = new BBox(this, 1, new Vector3f(0, 0,18f), new Vector3f(48, 12, 12), true);
		bodies.add(box0);
		bodies.add(box1);
		
		bComp = new BCompound(this, 5, bodies, true);

	}

	public void draw() {
		background(255);
		lights();
		rotateY(frameCount*.01f);

		if(frameCount%2==0) {
			Vector3f pos = new Vector3f(random(40), -200, random(40));
			//BCompound bComp = new BCompound(this, 5, bodies, true);
			BObject b = new BObject(this, 20, bComp, pos, true);
			//reuse the rigidBody of the sphere for performance resons
			physics.addBody(b);
		}

		physics.update();
		physics.display();
		
		//saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic12/pic12####.jpeg");

	}

}
