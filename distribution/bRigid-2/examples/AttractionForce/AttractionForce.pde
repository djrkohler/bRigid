// bRigid provides classes for an easier handling of jBullet in Processing. bRigid is thought as a kind of Processing port for the bullet physics simulation library written in C++. 
// This library allows the interaction of rigid bodies in 3D. Geometry/ Shapes are build with Processing PShape Class, for convinient display and export (c) 2013 Daniel Köhler, daniel@lab-eds.org

//here: basic example (Box) for using rigidShapes

import javax.vecmath.Vector3f;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.util.ObjectArrayList;

import peasy.*;
import bRigid.*;


PeasyCam cam;

BPhysics physics;
CollisionShape crossShape;
ArrayList<BObject> crosses = new ArrayList<BObject>();

boolean pause = false;


public void setup() {
  size(1280, 720, P3D);
  frameRate(60);

  cam = new PeasyCam(this, 150);

  physics = new BPhysics();
  physics.world.setGravity(new Vector3f(0, 0, 0));

  //add an attraction force
  //BForceAttraction(Vector3f fixedPosition, float radius, float strength)
  BForceAttraction f0 = new BForceAttraction(new Vector3f(0, 100, 0), 500, 800);
  physics.addBehavior(f0);
  BForceAttraction f1 = new BForceAttraction(new Vector3f(0, -100, 0), 500, 800);
  physics.addBehavior(f1);

  //create a few Compoundshapes
  for (int j = 0; j < 40; j++) {
    BCompound bComp = new BCompound(this, 1, new Vector3f(0, 0, 0), true);

    //add bodies, here convexhulls from random pointlists
    for (int i = 0; i < 10; i++) {
      // create a list of arbitrary points around a certain radius
      ObjectArrayList<Vector3f> vertices = new ObjectArrayList<Vector3f>();  
      for (int k = 0; k <= 32; k++) {
        Vector3f v = new Vector3f(random(-5, 5), random(-5, 5), random(-5, 5));
        vertices.add(v);
      }
      BConvexHull c = new BConvexHull(this, 100, vertices, new Vector3f(random(10), random(20), random(20)), true);
      bComp = bComp.addCollisionShape(physics, c);
    }
    bComp.setPosition(random(-50, 50), random(-100, 100), random(-100, 100));
    physics.addBody(bComp);
  }
}

public void draw() {
  background(255);
  lights();
  rotateY(frameCount*.01f);

  if (!pause)
    physics.update();
  physics.display();
}

public void keyPressed() {
  pause = !pause;
}

