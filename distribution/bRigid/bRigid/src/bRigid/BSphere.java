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
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
/**
 * Sphere as rigidBody
 */
public class BSphere extends BObject implements BInterface {

	public float radius;
	/**
	 * if mass is set to 0: the body will be static
	 */
	public BSphere(PApplet p, float mass, float posX, float posY, float posZ, float radius) {

		super(p);

		this.radius = radius;
		collisionShape = new SphereShape(radius);
		
		if (mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, new Vector3f(posX, posY, posZ), true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		drawToPShape(transform, collisionShape);

	}
	/**
	 * if mass is set to 0: the body will be static, if inertia is false, the body will collide, but not rotate
	 */
	public BSphere(PApplet p, float mass, float radius, Vector3f position, boolean inertia) {

		super(p);

		this.radius = radius;
		collisionShape = new SphereShape(radius);
		
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
	 * imports a stl-file, but uses its BoundingSphere for rigidBehavior
	 */
	/*
	public BSphere(PApplet p, float mass, String fileName, boolean inertia) {

		super(p);

		importMesh(fileName);

		if (inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, calculateMeshCentroid(), true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else if (!inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, calculateMeshCentroid(), false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
	}*/
	
	/*
	private void importMesh(String pathToSTL) {
		displayShape = (TriangleMesh) new STLReader().loadBinary(pathToSTL, STLReader.TRIANGLEMESH);
		Vec3D extent = displayShape.getBoundingBox().getExtent();
		radius = extent.magnitude()*.5f;
		
		collisionShape = new SphereShape(radius);
	}*/
/*
	private Vector3f calculateMeshCentroid() {

		Vec3D c = displayShape.computeCentroid();
		Vector3f centroid = new Vector3f(c.x(), c.y(), c.z());
		return centroid;
	}*/
	/**
	 * displays the imported stl-file as toxi.geom.TriangleMesh or a PApplet Sphere; usually called from the BPhysics instance
	 */
	/*
	public void display() {
		Transform transform = new Transform();
		transform = rigidBody.getMotionState().getWorldTransform(transform);

		p.pushMatrix();

		p.translate(transform.origin.x, transform.origin.y, transform.origin.z);

		p.applyMatrix(transform.basis.m00, transform.basis.m01, transform.basis.m02, 0, transform.basis.m10, transform.basis.m11, transform.basis.m12, 0,
				transform.basis.m20, transform.basis.m21, transform.basis.m22, 0, 0, 0, 0, 1);

		if (displayShape != null) {
			p.beginShape(PConstants.TRIANGLES);
			for (Face f : displayShape.getFaces()) {
				p.normal(f.normal.x, f.normal.y, f.normal.z);
				p.vertex(f.a.x, f.a.y, f.a.z);
				p.vertex(f.b.x, f.b.y, f.b.z);
				p.vertex(f.c.x, f.c.y, f.c.z);
			}
			p.endShape();
		} else {
			p.sphereDetail(10);
			p.sphere(radius);
		}		
		p.popMatrix();
	}
	*/

}
