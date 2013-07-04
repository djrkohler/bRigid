// bRigid provides classes for an easier handling of jBullet in Processing. bRigid is thought as a kind of Processing port for the bullet physics simulation library written in C++. 
// This library allows the interaction of rigid bodies in 3D. Geometry/ Shapes are build with Processing PShape Class, for convinient display and export (c) 2013 Daniel Köhler, daniel@lab-eds.org

//here: compoundShape from several objects

import javax.vecmath.Vector3f;
import peasy.*;
import bRigid.*;


PeasyCam cam;

BPhysics physics;

ArrayList<BObject> bodies;
BCompound bComp ;


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

  //create a list with collision specific objects
  bodies = new ArrayList<BObject>();
  BBox box0 = new BBox(this, 1, new Vector3f(18f, 0, 0), new Vector3f(12, 12, 48), true);
  BBox box1 = new BBox(this, 1, new Vector3f(0, 0, 18f), new Vector3f(48, 12, 12), true);
  bodies.add(box0);
  bodies.add(box1);

  bComp = new BCompound(this, 5, bodies, true);
}

public void draw() {
  background(255);
  lights();
  rotateY(frameCount*.01f);

  if (frameCount%4==0) {
    Vector3f pos = new Vector3f(random(40), -200, random(40));
    //reuse the rigidBody of the compoundShape for performance resons
    BObject b = new BObject(this, 20, bComp, pos, true);

    physics.addBody(b);
  }

  physics.update();
  physics.display();
}

