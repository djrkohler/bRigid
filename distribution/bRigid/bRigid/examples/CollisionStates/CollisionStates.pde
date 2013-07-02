// bRigid provides classes for an easier handling of jBullet in Processing. bRigid is thought as a kind of Processing port for the bullet physics simulation library written in C++. 
// This library allows the interaction of rigid bodies in 3D. Geometry/ Shapes are build with Processing PShape Class, for convinient display and export (c) 2013 Daniel Köhler, daniel@lab-eds.org

//here: collision states

import javax.vecmath.Vector3f;
import com.bulletphysics.collision.dispatch.CollisionObject;

import peasy.*;
import bRigid.*;

PeasyCam cam;

BPhysics physics;


public void setup() {
  size(1280, 720, P3D);
  frameRate(30);

  cam = new PeasyCam(this, 300);

  physics = new BPhysics();
  physics.world.setGravity(new Vector3f(0, 70, 0));

  //create a invinite plane
  BPlane ground = new BPlane(new Vector3f(0, 105, 0), new Vector3f(0, -1, 0));
  physics.addPlane(ground);

  //create a field a boxes
  for (int i = -10; i <= 10; i++) {
    for (int j = -10; j <= 10; j++) {
      BBox box = new BBox(this, 1, new Vector3f(i * 8, 100, j * 8), new Vector3f(6, 10, 6), true);
      physics.addBody(box);
    }
  }
}


public void draw() {
  background(255);
  lights();
  rotateY(frameCount * .01f);

  if (frameCount % 30 == 0) {
    BBox box = new BBox(this, 14, new Vector3f(random(10), -150, random(10)), new Vector3f(6, 6, 6), true);
    physics.addBody(box);
  }

  physics.update();

  //check the ActivationStates of each object and mark them
  if (physics.rigidBodies.size() > 0) {
    BObject ro = physics.rigidBodies.get(0);

    for (BObject r : physics.rigidBodies) {
      CollisionObject c = r.rigidBody;
      if (r.equals(ro))
        continue;

      int tag = c.getActivationState();
      // ISLAND_SLEEPING
      if (tag == 2) {
        // .display(Vector3f color, boolean fill, boolean stroke)
        r.display(200, 0, 0);
      } // active
      else if (tag == 1) {
        r.display(240, 240, 240);
      } // wants deactivation
      else if (tag == 0) {
        r.display(200, 200, 0);
      } 
      else {
        r.display(200, 100, 200);
      }
    }
  }
}

