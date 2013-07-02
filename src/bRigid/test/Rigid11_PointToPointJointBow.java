package bRigid.test;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid11_PointToPointJointBow extends PApplet {

	PeasyCam cam;

	BPhysics physics;
	
	boolean pause = true;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);

		cam = new PeasyCam(this, 1200);
		cam.pan(600,300);
		cam.rotateX(.3);
		cam.rotateY(-.4);
		
		physics = new BPhysics();
		physics.world.setGravity(new Vector3f(0, 400, 0));

		int numX = 15;
		int numY = 30;

		for (int i = 0; i <= numY; i++) {
			for (int j = 0; j <= numX; j++) {
				BBox box = new BBox(this, 10, 0, 0, 0, 40, 80, 40);
				box.setPosition(40 * i, 100, 40 * j);
				physics.addBody(box);
			}

		}

		BBox nailBox0 = new BBox(this, 0, 600, 0, 600, 5, 5, 5);
		nailBox0.setPosition(600, 0, 600);
		physics.addBody(nailBox0);

		BBox nailBox1 = new BBox(this, 0, 0, 0, 0, 5, 5, 5);
		nailBox1.setPosition(600, -300, -400);
		physics.addBody(nailBox1);

		for (int i=0;i< physics.rigidBodies.size()-2;i++) {
			BObject box = physics.rigidBodies.get(i);
			BJointNail nail0 = new BJointNail(box, nailBox0);
			physics.addJoint(nail0);
			BJointNail nail1 = new BJointNail(box, nailBox1);
			physics.addJoint(nail1);
		}
	}

	public void draw() {
		background(255);
		lights();
		//rotateY(frameCount*.01f);

		if (!pause) physics.update();
		physics.display(new Vector3f(240,240,240), new Vector3f(100,100,100));
		
		saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic34/pic34####.jpeg");
	}
	
	public void keyPressed() {
		// hit the space bar, scale rigid objects
		if (key == ' ') {
			Vector3f factor = new Vector3f();
			for (int i=0;i< physics.rigidBodies.size()-2;i++) {
				BObject box = physics.rigidBodies.get(i);
				Vector3f f = box.getShapeScale();
				factor.set(f.x-.05f, f.y, f.z);
				box.scale(factor);
			}
		}
		if (key == 'p') pause = !pause;
	}

}
