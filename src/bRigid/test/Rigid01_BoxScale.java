package bRigid.test;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid01_BoxScale extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	BBox box;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);
		
		cam = new PeasyCam(this, 600);
	
		//create a rigid physics engine with a bounding box
		Vector3f min = new Vector3f(-120, -250, -120);
		Vector3f max = new Vector3f(120, 250, 120);
		physics = new BPhysics(min, max);
		physics.world.setGravity(new Vector3f(0, 100, 0));
	}

	public void draw() {
		background(255);
		lights();
		rotateY(frameCount*.01f);
		
		if(frameCount%3==0 && frameCount < 220) {
			Vector3f pos = new Vector3f(random(30), -150, random(30));
			BBox b = new BBox(this, 20, pos, new Vector3f(15,60,15), true);
			physics.addBody(b);
		}

		physics.update();
		physics.display();
		
		//saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic08/pic08####.jpeg");

	}
	
	public void keyPressed() {
		// hit the space bar, scale rigid objects
		if (key == ' ') {
			Vector3f factor = new Vector3f();
			for (BObject box : physics.rigidBodies) {
				Vector3f f = box.getShapeScale();
				factor.set(f.x , f.y + +.25f, f.z );
				box.scale(factor);
			}
		}
	}

}
