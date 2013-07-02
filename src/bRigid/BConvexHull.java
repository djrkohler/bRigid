/**
 * This package (pRigid) provides classes for an easier handling of jBullet in Processing
 * @author Daniel Köhler
 * @version 0.2
 * 
 * Copyright (c) 2012 Daniel Köhler, daniel@lab-eds.org
 * 
 * Java port of Bullet (jBullet) 
 * Copyright (c) 2008 Martin Dvorak <jezek2@advel.cz>
 *
 * Bullet Continuous Collision Detection and Physics Library
 * Copyright (c) 2003-2008 Erwin Coumans  http://www.bulletphysics.com/
 * 
 * toxiclibs, library for Processing
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * pRigid is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * 
 * pRigid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with pRigid.  If not, see <http://www.gnu.org/licenses/>.
 */

package bRigid;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.util.ObjectArrayList;

/**
 * ConvexHull as rigidBody, List of vertices, PShapes or .obj files as input and calculates its convexHull for collisionDetection
 */
public class BConvexHull extends BObject implements BInterface {

	/**
	 * takes an Array of Vertices as input and calculates its ConvexHull; the
	 * list should contain only unique Vertices </p> if mass is set to 0: the
	 * body will be static, if inertia is false, the body will collide, but not
	 * rotate
	 */
	public BConvexHull(PApplet p, float mass, ObjectArrayList<Vector3f> vertices, Vector3f position, boolean inertia) {

		super(p);

		collisionShape = new ConvexHullShape(vertices);
		if (inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, position, true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else if (!inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, position, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		drawToPShape(transform, collisionShape);
	}
	
	/**
	 * takes an DataPath for an file.obj as input and calculates its ConvexHull; 
	 */
	public BConvexHull(PApplet p, float mass, String fileName, Vector3f position, boolean inertia) {
		this(p,  mass,  fileName,  position,  inertia,  true);	
	}
	
	/**
	 * takes an DataPath for an file.obj as input and calculates its ConvexHull; 
	 */
	public BConvexHull(PApplet p, float mass, String fileName, Vector3f position, boolean inertia, boolean displayImportMesh) {

		super(p);
		importObj(fileName);

		if (inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, position, true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else if (!inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, position, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		if(!displayImportMesh) {
			//drawToPShape(transform, collisionShape);
			drawToPShape(collisionShape);
		}
		
	}
	/**
	 * takes a PShape as input and calculates its ConvexHull
	 */
	public BConvexHull(PApplet p, float mass, PShape mesh, Vector3f position, boolean inertia) {

		super(p);
		translateMesh(mesh);

		if (inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, position, true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else if (!inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, position, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		drawToPShape(transform, collisionShape);
		//drawToPShape(collisionShape);
		
	}

	
	private void importObj(String fileName) {
		displayShape = p5.loadShape(fileName);
		translateMesh(displayShape);
	}

	private void translateMesh(PShape importMesh) {
		
		ObjectArrayList<Vector3f> vertices = new ObjectArrayList<Vector3f>();
		
		PShape mesh = importMesh.getTessellation();
		int numVtxs = mesh.getVertexCount();

		for (int i = 0; i < numVtxs; i++) {
			PVector vtx = mesh.getVertex(i);
			Vector3f v = new Vector3f(vtx.x, vtx.y, vtx.z);
			if(vertices.contains(v)) continue;
			vertices.add(v);
		}
		collisionShape = new ConvexHullShape(vertices);
	}

}
