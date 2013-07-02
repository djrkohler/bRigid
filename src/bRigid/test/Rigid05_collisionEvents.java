package bRigid.test;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.RigidBody;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid05_collisionEvents extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	public void setup() {
		size(1280, 720, P3D);

		cam = new PeasyCam(this, 600);

		Vector3f min = new Vector3f(-120, -250, -120);
		Vector3f max = new Vector3f(120, 250, 120);
		physics = new BPhysics(min, max);
		physics.world.setGravity(new Vector3f(0, 200, 0));

		// BPlane ground = new BPlane(new Vector3f(0, 60, 0), new Vector3f(0,
		// -1, 0));
		// physics.addPlane(ground);
	}

	public void draw() {
		background(255);
		lights();
		rotateY(frameCount * .01f);

		if (frameCount % 10 == 0) {
			Vector3f pos = new Vector3f(random(30), -200, random(30));
			BBox b = new BBox(this, 20, pos, new Vector3f(15, 60, 15), true);
			// BBox box = new BBox(this, 1, new Vector3f(random(10), -70,
			// random(10)), new Vector3f(random(4, 16), 8, random(4, 16)),
			// true);
			physics.addBody(b);
		}

		physics.update();

		// check if bodies are intersecting
		ArrayList<RigidBody> collisionBodies = new ArrayList<RigidBody>();
		int numManifolds = physics.world.getDispatcher().getNumManifolds();
		for (int i = 0; i < numManifolds; i++) {
			PersistentManifold contactManifold = physics.world.getDispatcher().getManifoldByIndexInternal(i);
			int numCon = contactManifold.getNumContacts();
			if (numCon > 2) {
				RigidBody rA = (RigidBody) contactManifold.getBody0();
				RigidBody rB = (RigidBody) contactManifold.getBody1();

				collisionBodies.add(rA);
				collisionBodies.add(rB);
			}
		}

		// mark the colliding bodies
		for (BObject r : physics.rigidBodies) {
			RigidBody rb = r.rigidBody;
			if (collisionBodies.contains(rb)) {
				r.display(10,10, 10);
			} else {
				r.display(240, 240, 240);
			}
		}
		saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic39/pic39####.jpeg");
	}

}
