// bRigid provides classes for an easier handling of jBullet in Processing. bRigid is thought as a kind of Processing port for the bullet physics simulation library written in C++. 
// This library allows the interaction of rigid bodies in 3D. Geometry/ Shapes are build with Processing PShape Class, for convinient display and export (c) 2013 Daniel Köhler, daniel@lab-eds.org

//here: import an .obj file as ConvexHull-CollisionShape
//press space bar for displaying the imported mesh or the convexHull of it

import javax.vecmath.Vector3f;
import peasy.*;
import bRigid.*;

PeasyCam cam;

BPhysics physics;
BConvexHull cross;

boolean displayImportMesh = true;
// file path to the .obj file to import
String fileName = "/data/cross.obj";

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

  if (frameCount < 1000) {
    Vector3f pos = new Vector3f(random(30), -200, random(1));
    Vector3f scale = new Vector3f(3.2f, 3.2f, 3.2f);    
    //BConvexHull(PApplet p, float mass, String fileName, Vector3f position, boolean inertia, boolean displayImportMesh)
    cross = new BConvexHull(this, 1, fileName, pos, true, displayImportMesh);
    cross.scale(scale);
    physics.addBody(cross );
  }
  physics.update();
  physics.display();
}



public void keyPressed() {
  // toggle between the display of the convex collisionShape or your
  if (key == ' ') displayImportMesh = !displayImportMesh;
}

