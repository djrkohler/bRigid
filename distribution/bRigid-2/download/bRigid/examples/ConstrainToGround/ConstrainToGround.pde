// bRigid provides classes for an easier handling of jBullet in Processing. bRigid is thought as a kind of Processing port for the bullet physics simulation library written in C++. 
// This library allows the interaction of rigid bodies in 3D. Geometry/ Shapes are build with Processing PShape Class, for convinient display and export (c) 2013 Daniel Köhler, daniel@lab-eds.org

//here: use a Generic6Dof joint for constraining objects to a surface

import javax.vecmath.Vector3f;
import com.bulletphysics.dynamics.constraintsolver.Generic6DofConstraint;
import com.bulletphysics.linearmath.Transform;
import peasy.*;
import bRigid.*;


PeasyCam cam;

BPhysics physics;

ArrayList<BObject> crosses = new ArrayList<BObject>();

public void setup() {
  size(1280, 720, P3D);
  frameRate(60);

  cam = new PeasyCam(this, 400);
  cam.pan(0, 100);
  cam.rotateX(.9f);

  physics = new BPhysics();
  physics.world.setGravity(new Vector3f(0, 0, 0));

  BForceAttraction f = new BForceAttraction(new Vector3f(0, 0, 0), 500, 30);
  physics.addBehavior(f);

  BPlane ground = new BPlane(new Vector3f(0, 50, 0), new Vector3f(0, -1, 0));
  physics.addPlane(ground);

  ArrayList<BObject> bodies = new ArrayList<BObject>();
  BBox box0 = new BBox(this, 2, new Vector3f(4.5f, 0, 0), new Vector3f(3, 3, 12), true);
  BBox box1 = new BBox(this, 2, new Vector3f(0, 0, 4.5f), new Vector3f(12, 3, 3), true);
  bodies.add(box0);
  bodies.add(box1);


  for (int i = -10; i < 10; i++) {
    for (int j = -10; j < 10; j++) {
      BCompound bComp = new BCompound(this, bodies, true);
      bComp.setPosition(random(5) + i * 20, 0, random(5) + j * 20);        

      physics.addBody(bComp);
      crosses.add(bComp);

      final Transform frameInA = new Transform();
      final Transform frameInB = new Transform();
      frameInA.setIdentity();
      frameInB.setIdentity();
      frameInA.origin.x = 0f;
      frameInB.origin.x = 2.5f;

      Generic6DofConstraint joint6DOF = new Generic6DofConstraint(bComp.rigidBody, ground.rigidBody, frameInA, frameInB, false);
      joint6DOF.setLinearLowerLimit(new Vector3f(1f, 0f, 1f));
      joint6DOF.setLinearUpperLimit(new Vector3f(0f, 0f, 0f));
      joint6DOF.setAngularLowerLimit(new Vector3f(0, (float) (-HALF_PI), 0));
      joint6DOF.setAngularUpperLimit(new Vector3f(0, (float) (HALF_PI), 0));
      physics.addJoint(joint6DOF);
    }
  }
}

public void draw() {
  background(255);
  lights();
  rotateY(frameCount*.01f);

  physics.update();
  physics.display();
}

