package bRigid.test;

import javax.vecmath.Vector3f;
import com.bulletphysics.util.ObjectArrayList;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid03_ConvexMesh extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	boolean record = false;
	boolean pause = true;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(30);

		cam = new PeasyCam(this, 250);
		cam.pan(150, -100);

		physics = new BPhysics();
		physics.world.setGravity(new Vector3f(0, 0,300));

		// create a gorund plane
		BPlane ground = new BPlane(new Vector3f(0,0, 40), new Vector3f(0, 0,-1));
		physics.addPlane(ground);

		ObjectArrayList<Vector3f> meshVtxs = importMeshByVtx("D:/Eclipse/punktiert/data/fromRhino/meshVtx2.txt");
		BConvexHull cross0 = new BConvexHull(this, 1, meshVtxs, new Vector3f(0, 0, 0), true);
		 //physics.addBody(cross0);

		// file path to the .obj file to import
		String fileName = "D:/Eclipse/rigid/data/cross.obj";
		// toggle between the display of the convex collisionShape or your
		// imported file
		boolean displayImportMesh = true;
		BConvexHull cross1 = new BConvexHull(this, 1, fileName, new Vector3f(), true, displayImportMesh);

		for (int i = 1; i < 15; i++) {
			for (int j = 1; j < 15; j++) {
				BObject cross = new BObject(this, 1, cross1, new Vector3f(i * 15, -j*15,0), new Vector3f(i * .13f, 1,  j* .13f), true);
				 physics.addBody(cross);
			}
		}

	}

	public void draw() {
		background(255);
		lights();
		if (pause)
			physics.update();
		physics.display();

	}

	public void keyPressed() {
		if (key == (' '))
			pause = !pause;

	}

	public ObjectArrayList<Vector3f> importMeshByVtx(String fileName) {

		ObjectArrayList<Vector3f> importPos = new ObjectArrayList<Vector3f>();

		String lines[] = loadStrings(fileName);

		for (int i = 0; i < (lines.length); i++) {
			String[] txtCoord = split(lines[i], ",");
			// convert from String to float
			float x = Float.parseFloat(txtCoord[0]);
			float y = Float.parseFloat(txtCoord[1]);
			float z = Float.parseFloat(txtCoord[2]);
			// create vector and add to the PositionList
			Vector3f pos = new Vector3f(x, z, -y);
			importPos.add(pos);
		}
		return importPos;
	}
}
