// bRigid provides classes for an easier handling of jBullet in Processing. bRigid is thought as a kind of Processing port for the bullet physics simulation library written in C++. 
// This library allows the interaction of rigid bodies in 3D. Geometry/ Shapes are build with Processing PShape Class, for convinient display and export (c) 2013 Daniel Köhler, daniel@lab-eds.org

//here: how to scale a rigid Object; press the space bar

import javax.vecmath.Vector3f;
import peasy.*;
import bRigid.*;

PeasyCam cam;

BPhysics physics;
BBox box;

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
  physics.world.setGravity(new Vector3f(0, 100, 0));
}


public void draw() {
  background(255);
  lights();
  rotateY(frameCount*.01f);

  if (frameCount%3==0 && frameCount < 220) {
    Vector3f pos = new Vector3f(random(30), -150, random(30));
    BBox b = new BBox(this, 20, pos, new Vector3f(15, 60, 15), true);
    //add body to the physics engine
    physics.addBody(b);
  }
  //update physics engine every frame
  physics.update();
  // default display of every shape
  physics.display();
}

public void keyPressed() {
  // hit the space bar, scale rigid objects
  if (key == ' ') {
    Vector3f factor = new Vector3f();
    for (BObject box : physics.rigidBodies) {
      Vector3f f = box.getShapeScale();
      factor.set(f.x, f.y + +.25f, f.z );
      box.scale(factor);
    }
  }
}

