package bRigid.test;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid12_GImpMesh extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	BObject mesh;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);
		
		cam = new PeasyCam(this, 100);
	
		//create a rigid physics engine with a bounding box
		Vector3f min = new Vector3f(-120, -250, -120);
		Vector3f max = new Vector3f(120, 250, 120);
		physics = new BPhysics(min, max);
		
		physics.world.setGravity(new Vector3f(0, 20, 0));
		
		//import mesh as GImpactMesh
		//BGImpactMesh(PApplet p, BPhysics physics, float mass, String fileName, Vector3f position, boolean inertia) 
		String fileName = "D:/Eclipse/rigid/data/cross.obj";
		mesh = new BGImpactMesh(this, physics, 1, fileName, new Vector3f(0, 0, 0), true);
		physics.addBody(mesh);
	}

	public void draw() {
		background(255);
		lights();
		//rotateY(frameCount*.01f);

		
		if (frameCount % 30 == 0 && frameCount < 500) {
			/*
			Vector3f pos = new Vector3f(random(30), -200, random(30));
			//reuse the rigidBody of the sphere for performance resons
			BObject s = new BObject(this, 2, mesh, pos, true);
			physics.addBody(s);
			*/
			String fileName = "D:/Eclipse/rigid/data/cross.obj";
			BGImpactMesh mesh0 = new BGImpactMesh(this, physics, 2, fileName, new Vector3f(0, 0, 0), true);
			mesh0.rigidBody.setRestitution(.1f);
			mesh0.rigidBody.setFriction(.99f);
			physics.addBody(mesh0);
		}
		

		physics.update();
		physics.display();
		
	}

}
