package bRigid.test;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionObject;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid05_collisionObjects extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(30);

		cam = new PeasyCam(this, 300);

		physics = new BPhysics();
		physics.world.setGravity(new Vector3f(0, 70, 0));

		BPlane ground = new BPlane(new Vector3f(0, 105, 0), new Vector3f(0, -1, 0));
		physics.addPlane(ground);

		//create a field a boxes
		  for (int i = -20; i <= 20; i++) {
		    for (int j = -20; j <= 20; j++) {
		      BBox box = new BBox(this, 1, new Vector3f(i * 4, 100, j * 4), new Vector3f(3, 5, 3), true);
		      physics.addBody(box);
		    }
		  }
	}

	public void draw() {
		background(255);
		lights();
		rotateY(frameCount * .01f);

		if (frameCount % 30 == 0) {
			BBox box = new BBox(this, 14, new Vector3f(random(10), -150, random(10)), new Vector3f(6,6,6), true);
			physics.addBody(box);
		}

		physics.update();

		//check the ActivationStates of each object and mark them
		if (physics.rigidBodies.size() > 0) {
			BObject ro = physics.rigidBodies.get(0);

			for (BObject r : physics.rigidBodies) {
				CollisionObject c = r.rigidBody;
				if (r.equals(ro))
					continue;

				int tag = c.getActivationState();

				// ISLAND_SLEEPING
				if (tag == 2) {
					// .display(Vector3f color, boolean fill, boolean stroke)
					r.display(250, 250,250);
				} // active
				else if (tag == 1) {
					r.display(10,10,10);
				} // wants deactivation
				else if (tag == 0) {
					r.display(150, 150, 150);
				} else {
					r.display(150, 150, 150);
				}
			}
		}
		
		saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic41/pic41####.jpeg");
	}
}
