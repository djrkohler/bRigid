package bRigid.test;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.dispatch.GhostPairCallback;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid05_collisionGhosts extends PApplet {

	PeasyCam cam;

	BPhysics physics;
	GhostObject ghostObject;
	BBox boxMain;
	GhostPairCallback ghostPairCallback;

	public void setup() {
	  size(1280, 720, P3D);
	  frameRate(60);

	  cam = new PeasyCam(this, 250);

	  physics = new BPhysics();
	  physics.world.setGravity(new Vector3f(0, 200, 0));

	  ghostPairCallback = new GhostPairCallback();
	  physics.world.getPairCache().setInternalGhostPairCallback(ghostPairCallback);

	  BPlane ground = new BPlane(new Vector3f(0, 100, 0), new Vector3f(0, -1, 0));
	  physics.addPlane(ground);

	  boxMain = new BBox(this, 1, new Vector3f(0, 100, 0), new Vector3f(100, 10, 100), true);

	  ghostObject = new GhostObject();
	  ghostObject.setCollisionShape(boxMain.collisionShape);
	  ghostObject.setCollisionFlags(new CollisionFlags().NO_CONTACT_RESPONSE);
	  ghostObject.setWorldTransform(boxMain.transform);
	  physics.world.addCollisionObject(ghostObject);
	}

	public void draw() {
	  background(255);
	  lights();
	  rotateY(frameCount*.01f);

	  if (frameCount % 60 == 0) {
	    BBox b = new BBox(this, 1, new Vector3f(0, -100, 0), new Vector3f(8, 8, 8), true);
	    physics.addBody(b);
	  }

	  physics.update();

	  // ObjectArrayList<CollisionObject> pairs =
	  // ghostObject.getOverlappingPairs();
	  int numPairs = ghostObject.getNumOverlappingObjects();

	//change the color if more then 4 bodies interact in the ghost area
	  if (numPairs > 4) {
	    //.display(Vector3f color, boolean fill, boolean stroke)
	    boxMain.display(new Vector3f(200, 0, 0), false, true);
	  } 
	  else {
	    boxMain.display(new Vector3f(200, 200, 200), false, true);
	  }

	  physics.display();
	  
	  saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic37/pic37####.jpeg");
	}

}
