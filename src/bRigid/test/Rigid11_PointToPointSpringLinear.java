package bRigid.test;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid11_PointToPointSpringLinear extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);

		cam = new PeasyCam(this, 400);
		cam.rotateX(-HALF_PI+.2f);

		physics = new BPhysics();
		physics.world.setGravity(new Vector3f(0, 0, -220));

		float sizeX = 30;
		float sizeY = 90;
		float sizeZ = 3;

		for (int i = -10; i <= 10; i++) {

			float mass = 10;
			//set body passive
			if (i == -10 || i == 10) {
				mass = 0;
			}

			BBox box = new BBox(this, mass, 0, 0, 0, sizeX *.95f, sizeY, sizeZ);
			box.setPosition(i * sizeX, 0, 0);
			physics.addBody(box);

			if (physics.rigidBodies.size() > 1) {
				BJointNail nail = new BJointNail(box, physics.rigidBodies.get(physics.rigidBodies.size() - 2));
				physics.addJoint(nail);
			}
		}

	}

	public void draw() {
		background(255);
		lights();

		physics.update();
		physics.display();
		
		saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic32/pic32####.jpeg");
	}

}
