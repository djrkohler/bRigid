package bRigid.test;

import javax.vecmath.Vector3f;
import com.bulletphysics.util.ObjectArrayList;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid03_ConvexMesh04 extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	BSphere sphere;

	float radius = 20;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);

		cam = new PeasyCam(this, 600);

		//create a rigid physics engine with a bounding box
		Vector3f min = new Vector3f(-120, -250, -120);
		Vector3f max = new Vector3f(120, 250, 120);
		physics = new BPhysics(min, max);
		
		physics.world.setGravity(new Vector3f(0, 500, 0));

	}

	public void draw() {
		background(255);
		lights();
		rotateY(frameCount*.01f);

		// create a list of arbitrary points around a certain radius
		ObjectArrayList<Vector3f> vertices = new ObjectArrayList<Vector3f>();
		int numVtx = (int) (frameCount*.18f+3);
		
		for (int i = 0; i <= numVtx; i++) {
			Vector3f v = new Vector3f(random(-radius, radius), random(-radius, radius)-150, random(-radius, radius));
			vertices.add(v);
		}
		// create a convexHull
		BConvexHull c = new BConvexHull(this, 100, vertices, new Vector3f(), true);
		physics.addBody(c);

		physics.update();
		physics.display();
		
		//saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic04/pic04####.jpeg");

	}

}
