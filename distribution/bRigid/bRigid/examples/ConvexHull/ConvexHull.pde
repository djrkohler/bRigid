// bRigid provides classes for an easier handling of jBullet in Processing. bRigid is thought as a kind of Processing port for the bullet physics simulation library written in C++. 
// This library allows the interaction of rigid bodies in 3D. Geometry/ Shapes are build with Processing PShape Class, for convinient display and export (c) 2013 Daniel Köhler, daniel@lab-eds.org

//here: convexHull example

import javax.vecmath.Vector3f;
import com.bulletphysics.util.ObjectArrayList;
import peasy.*;
import bRigid.*;

PeasyCam cam;

BPhysics physics;

float radius = 20;

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
}

public void draw() {
  background(255);
  lights();
  rotateY(frameCount*.01f);

  // create a list of arbitrary points around a certain radius
  ObjectArrayList<Vector3f> vertices = new ObjectArrayList<Vector3f>();
  int numVtx = (int) (frameCount*.18f+3);
  for (int i = 0; i <= numVtx; i++) {
    Vector3f v = new Vector3f(random(-radius, radius), random(-radius, radius)-200, random(-radius, radius));
    vertices.add(v);
  }
  // create a convexHull
  BConvexHull c = new BConvexHull(this, 100, vertices, new Vector3f(), true);
  physics.addBody(c);

  physics.update();
  physics.display();
}

