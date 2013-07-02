// bRigid provides classes for an easier handling of jBullet in Processing. bRigid is thought as a kind of Processing port for the bullet physics simulation library written in C++. 
// This library allows the interaction of rigid bodies in 3D. Geometry/ Shapes are build with Processing PShape Class, for convinient display and export (c) 2013 Daniel Köhler, daniel@lab-eds.org

//here: use a simple collisionObject of a sphere for complex bodies
//notice: the BConvexHull you can also use for calculating ConvexHulls

import javax.vecmath.Vector3f;
import com.bulletphysics.util.ObjectArrayList;
import peasy.*;
import bRigid.*;


PeasyCam cam;

BPhysics physics;
BSphere sphere;

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

  // create the first rigidBody as Sphere
  //BSphere(PApplet p, float mass, float x, float y, float z, float radius)
  sphere = new BSphere(this, 2, 0, 0, 0, radius);
}

public void draw() {
  background(255);
  lights();
  rotateY(frameCount*.01f);

  Vector3f pos = new Vector3f(random(30), -150, random(1));
  // reuse the rigidBody of the sphere for performance resons
  //BObject(PApplet p, float mass, BObject body, Vector3f center, boolean inertia)
  BObject s = new BObject(this, 2, sphere, pos, true);

  // calculate the PShape of a ConvexHull
  PShape wigg = createConvexShape();
  // override the displayed PShape
  s.displayShape = wigg;
  physics.addBody(s);

  physics.update();
  physics.display();
}


public PShape createConvexShape() {

  // create a list of arbitrary points around a certain radius
  ObjectArrayList<Vector3f> vertices = new ObjectArrayList<Vector3f>();

  //create arbitrary vertices
  for (int i = 0; i < 50; i++) {
    Vector3f v = new Vector3f(random(-radius, radius), random(-radius, radius), random(-radius, radius));
    vertices.add(v);
  }
  // create a convexHull
  BObject c = new BConvexHull(this, 1, vertices, new Vector3f(), true);
  // return its PShape
  return c.displayShape;
}

