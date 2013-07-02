package bRigid.test;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.constraintsolver.Generic6DofConstraint;
import com.bulletphysics.linearmath.Transform;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid06_CompoundShape04 extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	ArrayList<BObject> bodies;
	
	BPlane ground;
	
	public void setup() {
		size(1280, 720, P3D);
		frameRate(60);
		
		cam = new PeasyCam(this, 600);
	
		//create a rigid physics engine with a bounding box
		Vector3f min = new Vector3f(-120, -250, -120);
		Vector3f max = new Vector3f(120, 250, 120);
		physics = new BPhysics(min, max);
		physics.world.setGravity(new Vector3f(0, 500, 0));
		
		ground = new BPlane(new Vector3f(0, 250, 0), new Vector3f(0, -1, 0));
		physics.addPlane(ground);
		
		//create a list with collision specific objects
		bodies = new ArrayList<BObject>();
		BBox box0 = new BBox(this, 1, new Vector3f(18f, 0, 0), new Vector3f(12, 12, 48), true);
		BBox box1 = new BBox(this, 1, new Vector3f(0, 0,18f), new Vector3f(48, 12, 12), true);
		bodies.add(box0);
		bodies.add(box1);

	}

	public void draw() {
		background(255);
		lights();
		rotateY(frameCount*.01f);

		if(frameCount%10==0) {
			Vector3f pos = new Vector3f(random(40), -200, random(40));
			BCompound bComp = new BCompound(this, 5, bodies, true);
			bComp.setPosition(pos);
			//reuse the rigidBody of the sphere for performance resons
			physics.addBody(bComp);
			
			final Transform frameInA = new Transform();
			final Transform frameInB = new Transform();

			frameInA.setIdentity();
			frameInB.setIdentity();

			frameInA.origin.x = 0f;
			frameInB.origin.x = 1f;

			Generic6DofConstraint joint6DOF = new Generic6DofConstraint(bComp.rigidBody, ground.rigidBody, frameInA, frameInB, false);

			joint6DOF.setLinearLowerLimit(new Vector3f(1f, 1f, 1f));
			joint6DOF.setLinearUpperLimit(new Vector3f(0f, 0f, 0f));
			//joint6DOF.setAngularLowerLimit(new Vector3f(0, (float) (-Math.PI * 2), 0));
			//joint6DOF.setAngularUpperLimit(new Vector3f(.01f, (float) (Math.PI * 2), .01f));
			//joint6DOF.setAngularLowerLimit(new Vector3f(0, (float) (-.001*frameCount), 0));
			//joint6DOF.setAngularUpperLimit(new Vector3f(0, (float) (.001*frameCount), 0));
			joint6DOF.setAngularLowerLimit(new Vector3f(0.9f, 1, 0.1f));
			joint6DOF.setAngularUpperLimit(new Vector3f(.5f, 0, 0.001f));
			physics.addJoint(joint6DOF);
		}

		physics.update();
		physics.display();
		
		//saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic12/pic12####.jpeg");

	}

}
