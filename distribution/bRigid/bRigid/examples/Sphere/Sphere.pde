// bRigid provides classes for an easier handling of jBullet in Processing. bRigid is thought as a kind of Processing port for the bullet physics simulation library written in C++. 
// This library allows the interaction of rigid bodies in 3D. Geometry/ Shapes are build with Processing PShape Class, for convinient display and export (c) 2013 Daniel Köhler, daniel@lab-eds.org

//here: basic example (Sphere) for using rigidShapes

import javax.vecmath.Vector3f;
import peasy.*;
import bRigid.*;

PeasyCam cam;

BPhysics physics;
BSphere sphere;

boolean toggle = false;

public void setup() {
  size(1280, 720, P3D);
  frameRate(60);

  cam = new PeasyCam(this, 600);

  //extends of physics world
  Vector3f min = new Vector3f(-120, -250, -120);
  Vector3f max = new Vector3f(120, 250, 120);
  //create a rigid physics engine with a bounding box
  physics = new BPhysics(min, max);
  //set gravity
  physics.world.setGravity(new Vector3f(0, 500, 0));

  //create the first rigidBody as Box or Sphere
  //BSphere(PApplet p, float mass, float x, float y, float z, float radius)
  sphere = new BSphere(this, 2, 0, 0, 0, 20);
}

public void draw() {
  background(255);
  lights();
  rotateY(frameCount*.01f);

  if (frameCount%4==0) {
    Vector3f pos = new Vector3f(random(30), -150, random(1));
    //reuse the rigidBody of the sphere for performance resons
    //BObject(PApplet p, float mass, BObject body, Vector3f center, boolean inertia)
    BObject r = new BObject(this, 100, sphere, pos, true);
    //add body to the physics engine
    physics.addBody(r);
  }
  //update physics engine every frame
  physics.update();
  // default display of every shape
  physics.display();
}


