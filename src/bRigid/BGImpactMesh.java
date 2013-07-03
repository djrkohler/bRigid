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

import java.nio.ByteBuffer;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import saito.objloader.OBJModel;

import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.extras.gimpact.GImpactCollisionAlgorithm;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;

/**
 * ConvexHull as rigidBody, List of vertices, PShapes or .obj files as input and calculates its convexHull for collisionDetection
 */
public class BGImpactMesh extends BObject implements BInterface {

	
	/**
	 * takes an DataPath for an file.obj as input and calculates its CollisionShape; 
	 */
	public BGImpactMesh(PApplet p, BPhysics physics, float mass, String fileName, Vector3f position, boolean inertia) {

		super(p);
		importObj(physics, fileName);

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
	}
	
		
	private void importObj(BPhysics physics, String fileName) {
		OBJModel mesh = new OBJModel(p5, fileName);
		collisionShape = translateMesh(physics, mesh );
		//drawToPShape(collisionShape);
		displayShape = getPShape(mesh);
	}

	protected CollisionShape translateMesh(BPhysics physics, OBJModel mesh) {
				
		int vertCount = mesh.getVertexCount();
	    int faceCount = mesh.getFaceCount();
	    ByteBuffer ver = ByteBuffer.allocateDirect(vertCount*12);
	    ByteBuffer ind = ByteBuffer.allocateDirect(faceCount*12);
	    for(int a=0; a<vertCount; a++) {
	      PVector cur = mesh.getVertex(a);
	      ver.putFloat(cur.x);
	      ver.putFloat(cur.y);
	      ver.putFloat(cur.z);
	    }
	    for(int a=0; a<faceCount; a++) {	    	
	      int[] cur = mesh.getFaceInSegment(0, a).getVertexIndices();
	      ind.putInt(cur[0]);
	      ind.putInt(cur[1]);
	      ind.putInt(cur[2]);
	    }
	    TriangleIndexVertexArray tiva = 
	      new TriangleIndexVertexArray(
	        faceCount, ind, 12, vertCount, ver, 12);
	    GImpactMeshShape gims = new GImpactMeshShape(tiva);
	    gims.setLocalScaling(new Vector3f(10,10,10));
	    gims.updateBound();
	    GImpactCollisionAlgorithm.registerAlgorithm(
	      (CollisionDispatcher)physics.world.getDispatcher());
	    return gims;
		
	}
	
	protected PShape getPShape(OBJModel mesh) {
	    PShape ret = p5.createShape();
	    ret.beginShape(p5.TRIANGLES);
	    for(int a=0; a<mesh.getFaceCount(); a++) {
	      PVector[] cur = mesh.getFaceVertices(a);
	      for(PVector vv : cur) {
	        float rnd = p5.random(212,255);
				ret.fill(rnd, 255, rnd);
				vv.mult(10.0f);
	        ret.vertex(vv.x, vv.y, vv.z);
	      }
	    }
	    ret.endShape();
	    return ret;
	  }
}
