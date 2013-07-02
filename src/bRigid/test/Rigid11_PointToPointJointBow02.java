package bRigid.test;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid11_PointToPointJointBow02 extends PApplet {

	PeasyCam cam;

	BPhysics physics;


	public void setup() {
	  size(1280, 720, P3D);
	  frameRate(60);

	  cam = new PeasyCam(this, 1300);
	  cam.pan(600, 0);
	  cam.rotateY(-.4);

	  physics = new BPhysics();
	  physics.world.setGravity(new Vector3f(0, 400, 0));

	  int numY = 30;

	  for (int i = 0; i <= numY; i++) {
	      BBox box = new BBox(this, 10, 0, 0, 0, 48, 240, 125);
	      box.setPosition(40 * i, 100, 0);
	      physics.addBody(box);
	    }

	  BBox nailBox0 = new BBox(this, 0, 600, 0, 600, 5, 5, 5);
	  nailBox0.setPosition(600, -400, 300);
	  physics.addBody(nailBox0);

	  BBox nailBox1 = new BBox(this, 0, 0, 0, 0, 5, 5, 5);
	  nailBox1.setPosition(600, -400, -300);
	  physics.addBody(nailBox1);

	  for (int i=0;i< physics.rigidBodies.size()-2;i++) {
	    BObject box = physics.rigidBodies.get(i);
	    BJointNail nail0 = new BJointNail(box, nailBox0);
	    physics.addJoint(nail0);
	    BJointNail nail1 = new BJointNail(box, nailBox1);
	    physics.addJoint(nail1);
	  }
	}

	public void draw() {
	  background(255);
	  lights();

	  physics.update();
	  physics.display(new Vector3f(240, 240, 240), new Vector3f(100, 100, 100));
	  
	  saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic39/pic39####.jpeg");
	}

	public void keyPressed() {
	  // hit the space bar, scale rigid objects
	  if (key == ' ') {
	    Vector3f factor = new Vector3f();
	    for (int i=0;i< physics.rigidBodies.size()-2;i++) {
	      BObject box = physics.rigidBodies.get(i);
	      Vector3f f = box.getShapeScale();
	      factor.set(f.x-.05f, f.y, f.z);
	      box.scale(factor);
	    }
	  }
	}

}
