package bRigid.test;

import java.util.ArrayList;

import javax.vecmath.Vector3f;
import com.bulletphysics.util.ObjectArrayList;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid03_ImportMesh extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	boolean record = false;

	public void setup() {

		size(1280, 720, P3D);

		cam = new PeasyCam(this, 500);

		physics = new BPhysics();
		physics.world.setGravity(new Vector3f(0, 100, 0));

		ArrayList<ObjectArrayList> meshText = importMeshByVtx("D:/Eclipse/rigid/data/fromRhino/roofs.txt");
		importLandscape("D:/Eclipse/rigid/data/fromRhino/landscape.txt");

		String fileName = "D:/Eclipse/rigid/data/cross.obj";
		BConvexHull cross = new BConvexHull(this, 1, fileName, new Vector3f(random(100), 0, random(100)), true);

		for (int i = -8; i < 8; i++) {
			for (int j = -8; j < 8; j++) {
				BObject c = new BObject(this, 1, cross, new Vector3f(i * 15 + 0, 0, j * 15), true);
				physics.addBody(c);
			}
		}

	}

	public void draw() {
		background(255);
		lights();
		if (record)
			beginRaw(DXF, "D:/Eclipse/bRigid/data/output.dxf");

		physics.update();
		physics.display();

		if (record == true) {
			endRaw();
			record = false; // Stop recording to the file
		}
	}

	public void keyPressed() {
		if (key == 'R' || key == 'r') { // Press R to save the file
			record = true;
		}
	}

	public ArrayList<ObjectArrayList> importMeshByVtx(String fileName) {

		ArrayList<ObjectArrayList> importText = new ArrayList<ObjectArrayList>();
		String lines[] = loadStrings(fileName);

		for (int i = 0; i < (lines.length); i++) {
			ObjectArrayList<Vector3f> importPos = new ObjectArrayList<Vector3f>();
			String[] txtVtxs = split(lines[i], ";");

			for (int j = 0; j < (txtVtxs.length - 1); j++) {
				String[] txtCoord = split(txtVtxs[j], ",");
				// convert from String to float
				float x = Float.parseFloat(txtCoord[0]);
				float y = Float.parseFloat(txtCoord[1]);
				float z = Float.parseFloat(txtCoord[2]);
				// create vector and add to the PositionList
				Vector3f pos = new Vector3f(x, -z, -y);
				importPos.add(pos);
			}
			importText.add(importPos);
		}
		return importText;
	}

	public void importLandscape(String fileName) {

		ArrayList<ObjectArrayList> meshText = importMeshByVtx(fileName);

		BCompound bComp = new BCompound(this, 0, new Vector3f(), false);

		for (ObjectArrayList meshVtxs : meshText) {
			BConvexHull cross0 = new BConvexHull(this, 0, meshVtxs, new Vector3f(0, 0, 0), false);
			bComp = bComp.addCollisionShape(physics, cross0, false);
		}
		physics.addBody(bComp);
	}
}
