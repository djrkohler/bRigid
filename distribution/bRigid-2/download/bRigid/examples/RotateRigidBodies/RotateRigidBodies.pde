// bRigid provides classes for an easier handling of jBullet in Processing. bRigid is thought as a kind of Processing port for the bullet physics simulation library written in C++. 
// This library allows the interaction of rigid bodies in 3D. Geometry/ Shapes are build with Processing PShape Class, for convinient display and export (c) 2013 Daniel Köhler, daniel@lab-eds.org

//here: how to rotate rigidShapes; press space bar

import javax.vecmath.Vector3f;
import peasy.*;
import bRigid.*;

PeasyCam cam;

// physics engine
BPhysics physics;

int numRots =0;

public void setup() {
  size(1280, 720, P3D);
  frameRate(60);

  cam = new PeasyCam(this, 280);

  // create a physics engine for rigid bodies
  physics = new BPhysics();
  // set direction of Gravity
  physics.world.setGravity(new Vector3f(0, 30, 0));

  // create a ground plane: 1st Vector: position, 2nd Vector: direction
  BPlane ground = new BPlane(new Vector3f(0, 100, 0), new Vector3f(0, -1, 0));
  physics.addPlane(ground);

  float sizeX = 35;
  float sizeY = 5;
  float sizeZ = 20;

  for (int i = -5; i < 5; i++) {
    for (int j = 0; j < 30; j++) {
      BBox box = null;
      if (j % 2 == 1) {
        // create a rigid box BBox(PApplet, mass, position, scale)
        box = new BBox(this, 200, sizeX - .1f, sizeY - .1f, sizeZ - .1f);
        // move box
        box.setPosition(i * sizeX*1.15f, -j * sizeY+100, 0);
      } 
      else {
        box = new BBox(this, 200, i * sizeX*1.15f + sizeX * .5f, -j * sizeY+100, 0, sizeX - .1f, sizeY - .1f, sizeZ - .1f);
      }
      box.rigidBody.setRestitution(.06f);
      box.rigidBody.setFriction(.9f);
      physics.addBody(box);
    }
  }
}

public void draw() {
  background(255);
  lights();

  physics.update();
  physics.display(50,50,50);
}


public void keyPressed() {
  // hit the space bar and rotate rigid objects
  if (key == ' ') {
    int count = 0;
    for (BObject box : physics.rigidBodies) {
      box.setRotation(new Vector3f(0, 1, 0), count*.0005f* numRots);
      count ++;
    }
    numRots ++;
  }
}
