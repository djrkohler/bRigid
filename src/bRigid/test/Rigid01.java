package bRigid.test;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import processing.core.PVector;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid01 extends PApplet {

	PeasyCam cam;

	// physics engine
	BPhysics physics;
	
	int numRots =0;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);

		cam = new PeasyCam(this, 280);

		// create a physics engine for rigid bodies
		physics = new BPhysics();
		// set direction of Gravity
		physics.world.setGravity(new Vector3f(0, 30, 0));

		// create a ground plane: 1st Vector: position, 2nd Vector: direction
		BPlane ground = new BPlane(new Vector3f(0, 100, 0), new Vector3f(0, -1, 0));
		physics.addPlane(ground);

		float sizeX = 35;
		float sizeY = 5;
		float sizeZ = 20;

		for (int i = -5; i < 5; i++) {
			for (int j = 0; j < 30; j++) {
				BBox box = null;
				PVector pos = PVector.fromAngle(TWO_PI/j);
				pos.mult(100);
				if (j % 2 == 1) {
					// create a rigid box BBox(PApplet, mass, position, scale)
					box = new BBox(this, 200, sizeX - .1f, sizeY - .1f, sizeZ - .1f);
					// move box
					box.setPosition(i * sizeX*1.15f, -j * sizeY+100, 0);
					//rotate box
					//box.setRotation(new Vector3f(0,1,0), i*.12f);
				} else {
					box = new BBox(this, 200, i * sizeX*1.15f + sizeX * .5f, -j * sizeY+100, 0, sizeX - .1f, sizeY - .1f, sizeZ - .1f);
					//box.setRotation(new Vector3f(0,1,0), i*.12f);
				}
				box.rigidBody.setRestitution(.06f);
				box.rigidBody.setFriction(.9f);
				physics.addBody(box);
			}
		}

	}

	public void draw() {
		background(255);
		lights();
		rotateY(frameCount*.01f);

		physics.update();
		physics.display();
		
		//saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic09/pic09####.jpeg");
	}

	public void keyPressed() {
		// hit the space bar, scale rigid objects
		if (key == ' ') {
			//Vector3f factor = new Vector3f();
			int count = 0;
			for (BObject box : physics.rigidBodies) {
				//Vector3f f = box.getShapeScale();
				//factor.set(f.x, f.y - random(.15f), f.z);
				//box.scale(factor);
				
				box.setRotation(new Vector3f(0,1,0), count*.0005f* numRots);
				count ++;
			}
			 numRots ++;
		}
	}

}
