package bRigid.test;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid11_HingeJoint02 extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(30);

		cam = new PeasyCam(this,1000);
		cam.rotateX(-HALF_PI + .2f);

		physics = new BPhysics();
		physics.world.setGravity(new Vector3f(0, 0, 0));

		float sizeX = 20;
		float sizeY = 60;
		float sizeZ = 1;

		int numX = 20;
		int numY = 60;

		for (int i = 0; i <= numY; i++) {
			for (int j = 0; j <= numX; j++) {

				float mass = 1;
				// set body passive
				if (i == numY) {
					mass = 0;
				}

				BBox box = new BBox(this, mass, sizeX, sizeY, sizeZ);
				box.rigidBody.setAngularFactor(1f);
				box.setPosition((i-numY*.5f)*sizeX, (j-numX*.5f) * sizeY*1.01f,0);
				physics.addBody(box);

				if (i > 0) {
					BBox prevBox = (BBox) physics.rigidBodies.get(physics.rigidBodies.size() - numX - 2);

					// get the neighbor
					// calculate the distance to its center
					Vector3f dist = box.rigidBody.getCenterOfMassPosition(new Vector3f());
					Vector3f t = prevBox.rigidBody.getCenterOfMassPosition(new Vector3f());
					dist.sub(t);
					Vector3f pivA = (Vector3f) dist.clone();
					pivA.scale(-.5f);
					Vector3f pivB = (Vector3f) dist.clone();
					pivB.scale(.5f);

					// axxis for hinge rotation
					Vector3f axisInA = new Vector3f(0, 1, 0);
					axisInA.normalize();
					Vector3f axisInB = (Vector3f) axisInA.clone();
					BJointHinge hinge = new BJointHinge(box, prevBox, pivA, pivB, axisInA, axisInB);

					physics.addJoint(hinge);

					 //enableAngularMotor(boolean enableMotor, float targetVelocity, float maxMotorImpulse)
					 hinge.enableAngularMotor(true, 2, 4f);
					 float lim = .03f*j+.05f;
					 hinge.setLimit(0, lim);
					//hinge.setLimit(0, 0.1f, .1f, .1f, .001f);
				}
			}
		}
	}

	public void draw() {
		background(255);
		lights();
		rotateZ(frameCount*.01f);

		physics.update();
		physics.display(240, 240, 240);
		
		saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic31/pic31####.jpeg");
	}

}
