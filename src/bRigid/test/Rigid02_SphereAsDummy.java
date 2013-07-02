package bRigid.test;

import javax.vecmath.Vector3f;
import com.bulletphysics.util.ObjectArrayList;

import processing.core.PApplet;
import processing.core.PShape;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid02_SphereAsDummy extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	BSphere sphere;

	float radius = 20;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);
		
		cam = new PeasyCam(this, 600);

		// create a rigid physics engine with a bounding box
		Vector3f min = new Vector3f(-120, -250, -120);
		Vector3f max = new Vector3f(120, 250, 120);
		physics = new BPhysics(min, max);

		physics.world.setGravity(new Vector3f(0, 500, 0));

		// create the first rigidBody as Sphere
		sphere = new BSphere(this, 2, 0, 0, 0, radius);
	}

	public void draw() {
		background(255);
		lights();
		rotateY(frameCount*.01f);

		Vector3f pos = new Vector3f(random(30), -150, random(1));
		// reuse the rigidBody of the sphere for performance resons
		BObject s = new BObject(this, 2, sphere, pos, true);

		// calculate the PShape of a ConvexHull
		PShape wigg = createConvexShape();
		// override the displayed PShape
		s.displayShape = wigg;

		physics.addBody(s);

		physics.update();
		physics.display();
		
		saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic02/pic02####.jpeg");

	}

	public PShape createConvexShape() {

		// create a list of arbitrary points around a certain radius
		ObjectArrayList<Vector3f> vertices = new ObjectArrayList<Vector3f>();

		for (int i = 0; i < 50; i++) {
			Vector3f v = new Vector3f(random(-radius, radius), random(-radius, radius), random(-radius, radius));
			vertices.add(v);
		}
		// create a convexHull
		BObject c = new BConvexHull(this, 1, vertices, new Vector3f(), true);
		// return its PShape
		return c.displayShape;
	}

}
