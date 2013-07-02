package bRigid.test;

import javax.vecmath.Vector3f;

import com.bulletphysics.linearmath.Transform;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid11_SliderJoint extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);

		cam = new PeasyCam(this, 600);

		physics = new BPhysics();
		physics.world.setGravity(new Vector3f(0, 0, -90));

		BPlane ground = new BPlane(new Vector3f(0, 0, -400), new Vector3f(0, 0, 1));
		physics.addPlane(ground);

		float sizeX = 30;
		float sizeY = 30;
		float sizeZ = 5;
		
		int numX = 1;
		int numY = 20;

		for (int i = 0; i <= numY; i++) {
		for (int j = 0; j <= numX; j++) {

			float mass = 1;
			if (i == 10) {
				mass = 0;
			}

			BBox box = new BBox(this, mass, sizeX, sizeY , sizeZ * .5f);
			box.rigidBody.setAngularFactor(1);
			box.setPosition(j*sizeX*1.05f, i * sizeX, 0);
			physics.addBody(box);

			//if (physics.rigidBodies.size() > 1) {
				if (i > 0) {

				BBox prevBox = (BBox) physics.rigidBodies.get(physics.rigidBodies.size() - numX - 2);
				// VParticle above = physics.getParticle(particles.get(particles.size() - (int) amountX - 2));

				Vector3f dist = box.rigidBody.getCenterOfMassPosition(new Vector3f());
				Vector3f t = prevBox.rigidBody.getCenterOfMassPosition(new Vector3f());
				dist.sub(t);
				Vector3f pivA = (Vector3f) dist.clone();
				pivA.scale(-.5f);
				Vector3f pivB = (Vector3f) dist.clone();
				pivB.scale(.5f);

				Transform localA = new Transform();
				Transform localB = new Transform();

				localA.setIdentity();
				localB.setIdentity();

				localA.origin.set(pivA);
				localB.origin.set(pivB);

				BJointSlider slider = new BJointSlider(box, prevBox, localA, localB);
				//slider.setUpperLinLimit(.1f*i);
				//slider.setMaxAngMotorForce(1);
				//slider.setPoweredAngMotor(true);
				//slider.setTargetAngMotorVelocity(2);

				physics.addJoint(slider);

			}
		}
		}
	}

	public void draw() {
		background(255);
		lights();

		physics.update();
		physics.display();
	}

}
