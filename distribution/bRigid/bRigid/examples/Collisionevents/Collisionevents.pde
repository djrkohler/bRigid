// bRigid provides classes for an easier handling of jBullet in Processing. bRigid is thought as a kind of Processing port for the bullet physics simulation library written in C++. 
// This library allows the interaction of rigid bodies in 3D. Geometry/ Shapes are build with Processing PShape Class, for convinient display and export (c) 2013 Daniel Köhler, daniel@lab-eds.org

//here: collisionEvents: counting the number of collisions

import javax.vecmath.Vector3f;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.RigidBody;
import peasy.*;
import bRigid.*;


PeasyCam cam;

BPhysics physics;


public void setup() {
  size(1280, 720, P3D);

  cam = new PeasyCam(this, 600);

  //extends of physics world
  Vector3f min = new Vector3f(-120, -250, -120);
  Vector3f max = new Vector3f(120, 250, 120);
  //create a rigid physics engine with a bounding box
  physics = new BPhysics(min, max);
  //set gravity
  physics.world.setGravity(new Vector3f(0, 100, 0));
}


public void draw() {
  background(255);
  lights();
  rotateY(frameCount*.01f);

  if (frameCount % 10 == 0) {
    Vector3f pos = new Vector3f(random(30), -200, random(30));
    BBox b = new BBox(this, 20, pos, new Vector3f(15, 60, 15), true);
    physics.addBody(b);
  }

  physics.update();

  //check if bodies are intersecting
  ArrayList<RigidBody> collisionBodies = new ArrayList<RigidBody>();
  int numManifolds = physics.world.getDispatcher().getNumManifolds();
  for (int i = 0; i < numManifolds; i++) {
    PersistentManifold contactManifold = physics.world.getDispatcher().getManifoldByIndexInternal(i);
    int numCon = contactManifold.getNumContacts();
    //change and use this number
    if (numCon > 0) {
      RigidBody rA = (RigidBody) contactManifold.getBody0();
      RigidBody rB = (RigidBody) contactManifold.getBody1();
      collisionBodies.add(rA);
      collisionBodies.add(rB);
    }
  }
  //mark the colliding bodies
  for (BObject r : physics.rigidBodies) {
    RigidBody rb = r.rigidBody;

    if (collisionBodies.contains(rb)) {
      r.display(new Vector3f(0, 240, 240));
    } else {
      r.display(new Vector3f(240, 240, 240));
    }
  }
  
}

