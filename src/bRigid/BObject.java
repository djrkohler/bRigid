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

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import processing.core.PShape;
import processing.core.PApplet;


import com.bulletphysics.collision.broadphase.BroadphaseNativeType;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.collision.shapes.ConcaveShape;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.collision.shapes.ShapeHull;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.collision.shapes.TriangleCallback;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.TransformUtil;
import com.bulletphysics.util.IntArrayList;
import com.bulletphysics.util.ObjectArrayList;
import com.bulletphysics.util.ObjectPool;

/**
 * BObject is the basic RigidBody Object, all other RigidBodies extend BObject
 * and inherent all attributes ConstructionInfo by Richard Brauer --
 * http://www.r6753.com
 */
public class BObject implements BInterface {

	PApplet p5;
	/**
	 * jBullet rigidBody node
	 */
	public RigidBody rigidBody;
	/**
	 * jBullet shape node
	 */
	public CollisionShape collisionShape;
	/**
	 * PShape for Geometry display in retained mode
	 */
	public PShape displayShape;
	/**
	 * jBullet transform node
	 */
	public Transform transform;
	/**
	 * List of behaviors
	 */
	public List<BInterface> behaviors;
	
	private Vector3f shapeScale;

	public BObject(PApplet p) {
		this.p5 = p;
		//displayShape = p5.createShape(p5.GROUP);
		displayShape = p5.createShape(0);
		shapeScale = new Vector3f(1,1,1);
	}

	/**
	 * for advanced users; use the specific Shape-Classes
	 */
	public BObject(PApplet p, float mass, CollisionShape collShape, Vector3f center, boolean inertia) {
		this.p5 = p;
		collisionShape = collShape;

		if (inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, center, true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else if (!inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, center, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		drawToPShape(collisionShape);
		shapeScale = new Vector3f(1,1,1);
		
		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);
	}

	/**
	 * takes an existing rigid object as input and reuse its collisionShape for
	 * performance issues use this constructor if you can reuse your
	 * collisionGeometry
	 */
	public BObject(PApplet p, float mass, BObject body, Vector3f center, boolean inertia) {
		this(p, mass,  body,  center, new Vector3f(1,1,1),inertia);
	}
	
	public BObject(PApplet p, float mass, BObject body, Vector3f center, Vector3f scale, boolean inertia) {
		this.p5 = p;

		collisionShape = body.collisionShape;
		collisionShape.setLocalScaling(scale);

		if (inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, center, true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else if (!inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, center, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		body.displayShape.scale(scale.x, scale.y, scale.z);
		displayShape = body.displayShape.getTessellation();
		body.displayShape.resetMatrix();
		
		shapeScale = new Vector3f(1,1,1);
		
		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);
		
	}
	
	
	public BObject(PApplet p, CollisionShape s, Transform t) {
		this.p5 = p;
		transform = t;
		collisionShape = s;
		Vector3f ivect = new Vector3f(0, 0, 0);
		collisionShape.calculateLocalInertia(1, ivect);

		DefaultMotionState motionState = new DefaultMotionState();
		RigidBodyConstructionInfo rigidBodyConInfo = new RigidBodyConstructionInfo(1, motionState, collisionShape, ivect);
		rigidBody = new RigidBody(rigidBodyConInfo);
		
		shapeScale = new Vector3f(1,1,1);
		
		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);
	}

	/**
	 * Adds the given behavior to the list of behaviors applied to this object
	 * at each step.
	 */
	public BObject addBehavior(BInterface behavior) {
		if (behaviors == null) {
			behaviors = new ArrayList<BInterface>(1);
		}
		behaviors.add(behavior);
		return this;
	}

	public boolean removeBehavior(BInterface b) {
		return behaviors.remove(b);
	}

	/**
	 * scale only displayShape
	 * @param scale
	 */
	public void scaleShape(float scale) {
		PShape[] shapes = displayShape.getChildren();
		if (shapes != null) {
			for (PShape s : shapes) {				
				if(s != null) {
					s.scale(1/shapeScale.x, 1/shapeScale.y, 1/shapeScale.z);
					s.scale(scale, scale, scale);
				}
			}
		}
	shapeScale.set(scale, scale, scale);
	}
	
	public Vector3f getShapeScale() {
		return shapeScale;
	}


	public void scaleShape(float scaleX, float scaleY, float scaleZ) {
		PShape[] shapes = displayShape.getChildren();
		if (shapes != null) {
			for (PShape s : shapes) {		
				if(s != null) {
					s.scale(1/shapeScale.x, 1/shapeScale.y, 1/shapeScale.z);
					s.scale(scaleX, scaleY, scaleZ);
				}
			}
		}
	shapeScale.set(scaleX, scaleY, scaleZ);
	}
	
	/**
	 * scale rigidbody
	 * @param scaleX
	 * @param scaleY
	 * @param scaleZ
	 */
	public void scale(Vector3f scale) {
		collisionShape.setLocalScaling(scale);	
		scaleShape(scale.x, scale.y, scale.z);
	}
	
	public void scale(float scale) {
		collisionShape.setLocalScaling(new Vector3f(scale, scale, scale));
		scaleShape(scale);
	}
				
	/**
	 * usualy called from the BPhysics instance, allows individual display
	 */
	public void display() {
		if (displayShape != null) {
			if (getMass() < 0) {
				
				Transform transform = new Transform();
				transform = rigidBody.getMotionState().getWorldTransform(transform);
				
				Matrix4f out = new Matrix4f();
				out = transform.getMatrix(out);	
				this.transform.set(transform);
				
				displayShape.applyMatrix(out.m00, out.m01, out.m02, out.m03, out.m10, out.m11, out.m12, out.m13, out.m20, out.m21, out.m22,
						out.m23, out.m30, out.m31, out.m32, out.m33);
				
				
				p5.pushMatrix();
				p5.shape(displayShape);
				p5.popMatrix();
				
				displayShape.resetMatrix();
				
			} else {

				Transform trans = new Transform();
				trans = rigidBody.getWorldTransform(trans);
				displayShape.translate(trans.origin.x, trans.origin.y, trans.origin.z);
				p5.shape(displayShape);
				displayShape.resetMatrix();
			}
		}
	}

	public void display(Vector3f rgb) {
		display(rgb, true, false);
	}
	
	public void display(float r, float g, float b) {
		display(new Vector3f(r,g,b), true, false);
	}
	
	public void display(Vector3f fill, Vector3f stroke) {
		if (displayShape != null) {
			displayShape.setFill(true);
			displayShape.setStroke(true);
			displayShape.setStrokeWeight(1.1f);		
			displayShape.setFill(p5.color(fill.x, fill.y, fill.z));
			displayShape.setStroke(p5.color(stroke.x, stroke.y, stroke.z));
			
			PShape[] shapes = displayShape.getChildren();
			if (shapes != null) {
				for (PShape s : shapes) {
					s.setFill(true);
					s.setStroke(true);
					s.setStrokeWeight(1.1f);		
					s.setFill(p5.color(fill.x, fill.y, fill.z));
					s.setStroke(p5.color(stroke.x, stroke.y, stroke.z));
					
					PShape[] childs = s.getChildren();
					if (childs != null) {
						for (PShape ch : childs) {
							ch.setFill(true);
							ch.setStroke(true);
							ch.setStrokeWeight(1.1f);		
							ch.setFill(p5.color(fill.x, fill.y, fill.z));
							ch.setStroke(p5.color(stroke.x, stroke.y, stroke.z));
						}
					}
				}
			}
			display();
		}
	}

	public void display(Vector3f rgb, boolean fill, boolean stroke) {
		if (displayShape != null) {
			displayShape.setFill(fill);
			displayShape.setStroke(stroke);
			displayShape.setStrokeWeight(1.1f);		
			displayShape.setFill(p5.color(rgb.x, rgb.y, rgb.z));
			displayShape.setStroke(p5.color(rgb.x, rgb.y, rgb.z));
			
			PShape[] shapes = displayShape.getChildren();
			if (shapes != null) {
				for (PShape s : shapes) {
					s.setFill(fill);
					s.setStroke(stroke);
					s.setStrokeWeight(1.1f);	
					s.setFill(p5.color(rgb.x, rgb.y, rgb.z));
					s.setStroke(p5.color(rgb.x, rgb.y, rgb.z));
					
					PShape[] childs = s.getChildren();
					if (childs != null) {
						for (PShape ch : childs) {
							ch.setFill(fill);
							ch.setStroke(stroke);
							ch.setStrokeWeight(1.1f);
							ch.setFill(p5.color(rgb.x, rgb.y, rgb.z));
							ch.setStroke(p5.color(rgb.x, rgb.y, rgb.z));
						}
					}
				}
			}
			display();
		}
	}

	public float getMass() {
		return -rigidBody.getInvMass();
	}

	public void setMass(float mass) {
		rigidBody.setMassProps(mass, new Vector3f());
	}

	public Vector3f getPosition() {
		Transform t = rigidBody.getWorldTransform(new Transform());
		Vector3f pos = (Vector3f) t.origin.clone();
		return pos;
	}

	public Vector3f getVelocity() {
		return rigidBody.getLinearVelocity(new Vector3f());
	}

	public void setPosition(float x, float y, float z) {
		Transform t = new Transform();
		t = rigidBody.getWorldTransform(t);
		t.origin.set(new Vector3f(x, y, z));
		rigidBody.setWorldTransform(t);
		transform.set(t);
	}

	public void setPosition(Vector3f pos) {
		Transform t = new Transform();
		t = rigidBody.getWorldTransform(t);
		t.origin.set(pos);
		rigidBody.setWorldTransform(t);
		transform.set(t);
	}
	
	public void setRotation(Vector3f axis, float angle) {
		Quat4f quat = new Quat4f();
		QuaternionUtil.setRotation(quat, axis,  angle);
		Transform t = new Transform();
		t = rigidBody.getWorldTransform(t);
		t.setRotation(quat);
		rigidBody.setWorldTransform(t);
		transform.set(t);	
	}

	public void setVelocity(Vector3f vel) {
		rigidBody.setLinearVelocity(vel);
	}

	public void setVelocity(float dirX, float dirY, float dirZ) {
		rigidBody.setLinearVelocity(new Vector3f(dirX, dirY, dirZ));
	}

	/**
	 * //ConstructionInfo by Richard Brauer -- http://www.r6753.com
	 */
	protected RigidBodyConstructionInfo createConInfo(CollisionShape shape, float mass, Vector3f trans, boolean active) {
		transform = new Transform();
		transform.setIdentity();
		if (trans != null)
			transform.origin.set(trans);

		Vector3f ivect = new Vector3f(0, 0, 0);
		if (active)
			shape.calculateLocalInertia(mass, ivect);

		DefaultMotionState motionState = trans != null ? new DefaultMotionState(transform) : new DefaultMotionState();

		return new RigidBodyConstructionInfo(mass, motionState, shape, ivect);
	}

	@SuppressWarnings("static-access")
	public void drawToPShape(Transform trans, CollisionShape shape) {
		ObjectPool<Transform> transformsPool = ObjectPool.get(Transform.class);
		ObjectPool<Vector3f> vectorsPool = ObjectPool.get(Vector3f.class);
		Matrix4f out = new Matrix4f();
		out = trans.getMatrix(out);

		if (shape.getShapeType() == BroadphaseNativeType.COMPOUND_SHAPE_PROXYTYPE) {
			CompoundShape compoundShape = (CompoundShape) shape;
			Transform childTrans = transformsPool.get();
			for (int i = compoundShape.getNumChildShapes() - 1; i >= 0; i--) {
				compoundShape.getChildTransform(i, childTrans);
				CollisionShape colShape = compoundShape.getChildShape(i);

				drawToPShape(childTrans, colShape);
			}
			transformsPool.release(childTrans);
		} else {

			switch (shape.getShapeType()) {
			case BOX_SHAPE_PROXYTYPE: {
				BoxShape boxShape = (BoxShape) shape;
				Vector3f halfExtent = boxShape.getHalfExtentsWithMargin(vectorsPool.get());
				halfExtent.scale(2f);
				PShape s = p5.createShape(p5.BOX, halfExtent.x, halfExtent.y, halfExtent.z);
				s.applyMatrix(out.m00, out.m01, out.m02, out.m03, out.m10, out.m11, out.m12, out.m13, out.m20, out.m21, out.m22, out.m23,
						out.m30, out.m31, out.m32, out.m33);
				displayShape.addChild(s.getTessellation());
				vectorsPool.release(halfExtent);
				break;
			}
			case SPHERE_SHAPE_PROXYTYPE: {
				SphereShape sphereShape = (SphereShape) shape;
				float radius = sphereShape.getMargin();
				PShape s = p5.createShape(p5.SPHERE, radius);
				s.translate(trans.origin.x, trans.origin.y, trans.origin.z);
				displayShape.addChild(s.getTessellation());
				break;
			}

			case STATIC_PLANE_PROXYTYPE: {
				StaticPlaneShape staticPlaneShape = (StaticPlaneShape) shape;
				float planeConst = staticPlaneShape.getPlaneConstant();
				Vector3f planeNormal = staticPlaneShape.getPlaneNormal(vectorsPool.get());
				Vector3f planeOrigin = vectorsPool.get();
				planeOrigin.scale(planeConst, planeNormal);
				Vector3f vec0 = vectorsPool.get();
				Vector3f vec1 = vectorsPool.get();
				TransformUtil.planeSpace1(planeNormal, vec0, vec1);
				float vecLen = 200f;

				Vector3f pt0 = vectorsPool.get();
				pt0.scaleAdd(vecLen, vec0, planeOrigin);

				Vector3f pt1 = vectorsPool.get();
				pt1.scale(vecLen, vec0);
				pt1.sub(planeOrigin, pt1);

				Vector3f pt2 = vectorsPool.get();
				pt2.scaleAdd(vecLen, vec1, planeOrigin);

				Vector3f pt3 = vectorsPool.get();
				pt3.scale(vecLen, vec1);
				pt3.sub(planeOrigin, pt3);

				// add plane display
				PShape s = p5.createShape();
				s.beginShape(p5.LINES);
				s.vertex(pt0.x, pt0.y, pt0.z);
				s.vertex(pt1.x, pt1.y, pt1.z);
				s.vertex(pt2.x, pt2.y, pt2.z);
				s.vertex(pt3.x, pt3.y, pt3.z);
				s.endShape();
				displayShape.addChild(s);

				vectorsPool.release(planeNormal);
				vectorsPool.release(planeOrigin);
				vectorsPool.release(vec0);
				vectorsPool.release(vec1);
				vectorsPool.release(pt0);
				vectorsPool.release(pt1);
				vectorsPool.release(pt2);
				vectorsPool.release(pt3);

				break;
			}

			default: {
				if (shape.isConvex()) {
					ConvexShape convexShape = (ConvexShape) shape;
					if (shape.getUserPointer() == null) {
						// create a hull approximation
						ShapeHull hull = new ShapeHull(convexShape);

						float margin = shape.getMargin();
						hull.buildHull(margin + 10);
						convexShape.setUserPointer(hull);
					}

					if (shape.getUserPointer() != null) {
						ShapeHull hull = (ShapeHull) shape.getUserPointer();

						Vector3f normal = vectorsPool.get();
						Vector3f tmp1 = vectorsPool.get();
						Vector3f tmp2 = vectorsPool.get();

						if (hull.numTriangles() > 0) {
							PShape s = p5.createShape();
								s.beginShape(p5.TRIANGLES);
							int index = 0;
							IntArrayList idx = hull.getIndexPointer();
							ObjectArrayList<Vector3f> vtx = hull.getVertexPointer();
							for (int i = 0; i < hull.numTriangles(); i++) {
								int i1 = index++;
								int i2 = index++;
								int i3 = index++;
								assert (i1 < hull.numIndices() && i2 < hull.numIndices() && i3 < hull.numIndices());

								int index1 = idx.get(i1);
								int index2 = idx.get(i2);
								int index3 = idx.get(i3);
								assert (index1 < hull.numVertices() && index2 < hull.numVertices() && index3 < hull.numVertices());

								Vector3f v1 = vtx.getQuick(index1);
								Vector3f v2 = vtx.getQuick(index2);
								Vector3f v3 = vtx.getQuick(index3);
								tmp1.sub(v3, v1);
								tmp2.sub(v2, v1);
								normal.cross(tmp1, tmp2);
								normal.normalize();

								s.normal(normal.x, normal.y, normal.z);
								s.vertex(v1.x, v1.y, v1.z);
								s.vertex(v2.x, v2.y, v2.z);
								s.vertex(v3.x, v3.y, v3.z);

							}
							s.endShape();
							s.applyMatrix(out.m00, out.m01, out.m02, out.m03, out.m10, out.m11, out.m12, out.m13, out.m20, out.m21,
									out.m22, out.m23, out.m30, out.m31, out.m32, out.m33);
							displayShape.addChild(s.getTessellation());
						}
						vectorsPool.release(normal);
						vectorsPool.release(tmp1);
						vectorsPool.release(tmp2);
					}
				} else {
				}
			}
			}

			if (shape.isConcave()) {
				ConcaveShape concaveMesh = (ConcaveShape) shape;

				Vector3f aabbMax = vectorsPool.get();
				aabbMax.set(1e30f, 1e30f, 1e30f);
				Vector3f aabbMin = vectorsPool.get();
				aabbMin.set(-1e30f, -1e30f, -1e30f);

				GlDrawcallback drawCallback = new GlDrawcallback(p5);

				concaveMesh.processAllTriangles(drawCallback, aabbMin, aabbMax);
				PShape s = drawCallback.displayShape;
				displayShape.addChild(s);
				vectorsPool.release(aabbMax);
				vectorsPool.release(aabbMin);
			}

		}
	}

	@SuppressWarnings("static-access")
	public PShape drawToPShape(CollisionShape shape) {
		ObjectPool<Vector3f> vectorsPool = ObjectPool.get(Vector3f.class);

		if (shape.getShapeType() == BroadphaseNativeType.COMPOUND_SHAPE_PROXYTYPE) {
			CompoundShape compoundShape = (CompoundShape) shape;
			for (int i = compoundShape.getNumChildShapes() - 1; i >= 0; i--) {
				CollisionShape colShape = compoundShape.getChildShape(i);
				drawToPShape(colShape);
			}
		} else {

			switch (shape.getShapeType()) {
			case BOX_SHAPE_PROXYTYPE: {
				BoxShape boxShape = (BoxShape) shape;
				Vector3f halfExtent = boxShape.getHalfExtentsWithMargin(vectorsPool.get());
				halfExtent.scale(2f);
				//PShape s = p5.createShape(p5.BOX, halfExtent.x, halfExtent.y, halfExtent.z);
				PShape s = p5.createShape(41, halfExtent.x, halfExtent.y, halfExtent.z);
				displayShape.addChild(s.getTessellation());
				vectorsPool.release(halfExtent);
				break;
			}
			case SPHERE_SHAPE_PROXYTYPE: {
				SphereShape sphereShape = (SphereShape) shape;
				float radius = sphereShape.getMargin();
				//PShape s = p5.createShape(p5.SPHERE, radius);
				PShape s = p5.createShape(40, radius);
				displayShape.addChild(s.getTessellation());
				break;
			}

			case STATIC_PLANE_PROXYTYPE: {
				StaticPlaneShape staticPlaneShape = (StaticPlaneShape) shape;
				float planeConst = staticPlaneShape.getPlaneConstant();
				Vector3f planeNormal = staticPlaneShape.getPlaneNormal(vectorsPool.get());
				Vector3f planeOrigin = vectorsPool.get();
				planeOrigin.scale(planeConst, planeNormal);
				Vector3f vec0 = vectorsPool.get();
				Vector3f vec1 = vectorsPool.get();
				TransformUtil.planeSpace1(planeNormal, vec0, vec1);
				float vecLen = 200f;

				Vector3f pt0 = vectorsPool.get();
				pt0.scaleAdd(vecLen, vec0, planeOrigin);

				Vector3f pt1 = vectorsPool.get();
				pt1.scale(vecLen, vec0);
				pt1.sub(planeOrigin, pt1);

				Vector3f pt2 = vectorsPool.get();
				pt2.scaleAdd(vecLen, vec1, planeOrigin);

				Vector3f pt3 = vectorsPool.get();
				pt3.scale(vecLen, vec1);
				pt3.sub(planeOrigin, pt3);

				// add plane display
				//@SuppressWarnings("static-access")
				PShape s = p5.createShape();
				s.beginShape(p5.LINES);
				s.vertex(pt0.x, pt0.y, pt0.z);
				s.vertex(pt1.x, pt1.y, pt1.z);
				s.vertex(pt2.x, pt2.y, pt2.z);
				s.vertex(pt3.x, pt3.y, pt3.z);
				s.endShape();
				//s.end();
				displayShape.addChild(s);

				vectorsPool.release(planeNormal);
				vectorsPool.release(planeOrigin);
				vectorsPool.release(vec0);
				vectorsPool.release(vec1);
				vectorsPool.release(pt0);
				vectorsPool.release(pt1);
				vectorsPool.release(pt2);
				vectorsPool.release(pt3);

				break;
			}

			default: {
				if (shape.isConvex()) {
					ConvexShape convexShape = (ConvexShape) shape;
					if (shape.getUserPointer() == null) {
						// create a hull approximation
						ShapeHull hull = new ShapeHull(convexShape);

						float margin = shape.getMargin();
						hull.buildHull(margin + 10);
						convexShape.setUserPointer(hull);
					}

					if (shape.getUserPointer() != null) {
						ShapeHull hull = (ShapeHull) shape.getUserPointer();

						Vector3f normal = vectorsPool.get();
						Vector3f tmp1 = vectorsPool.get();
						Vector3f tmp2 = vectorsPool.get();

						if (hull.numTriangles() > 0) {
							PShape s = p5.createShape();
							s.beginShape(p5.TRIANGLES);
							int index = 0;
							IntArrayList idx = hull.getIndexPointer();
							ObjectArrayList<Vector3f> vtx = hull.getVertexPointer();
							for (int i = 0; i < hull.numTriangles(); i++) {
								int i1 = index++;
								int i2 = index++;
								int i3 = index++;
								assert (i1 < hull.numIndices() && i2 < hull.numIndices() && i3 < hull.numIndices());

								int index1 = idx.get(i1);
								int index2 = idx.get(i2);
								int index3 = idx.get(i3);
								assert (index1 < hull.numVertices() && index2 < hull.numVertices() && index3 < hull.numVertices());

								Vector3f v1 = vtx.getQuick(index1);
								Vector3f v2 = vtx.getQuick(index2);
								Vector3f v3 = vtx.getQuick(index3);
								tmp1.sub(v3, v1);
								tmp2.sub(v2, v1);
								normal.cross(tmp1, tmp2);
								normal.normalize();

								s.normal(normal.x, normal.y, normal.z);
								s.vertex(v1.x, v1.y, v1.z);
								s.vertex(v2.x, v2.y, v2.z);
								s.vertex(v3.x, v3.y, v3.z);

							}
							s.endShape();
							displayShape.addChild(s);
						}
						vectorsPool.release(normal);
						vectorsPool.release(tmp1);
						vectorsPool.release(tmp2);
					}
				} else {
				}
			}
			}

			if (shape.isConcave()) {
				ConcaveShape concaveMesh = (ConcaveShape) shape;

				Vector3f aabbMax = vectorsPool.get();
				aabbMax.set(1e30f, 1e30f, 1e30f);
				Vector3f aabbMin = vectorsPool.get();
				aabbMin.set(-1e30f, -1e30f, -1e30f);

				GlDrawcallback drawCallback = new GlDrawcallback(p5);

				concaveMesh.processAllTriangles(drawCallback, aabbMin, aabbMax);
				PShape s = drawCallback.displayShape;
				displayShape.addChild(s);

				vectorsPool.release(aabbMax);
				vectorsPool.release(aabbMin);
			}

		}
		return displayShape;
	}

	private static class GlDrawcallback extends TriangleCallback {
		PApplet p5;
		PShape displayShape;

		public GlDrawcallback(PApplet p5) {
			this.p5 = p5;
			displayShape = p5.createShape(0);
		}

		@SuppressWarnings("static-access")
		public void processTriangle(Vector3f[] triangle, int partId, int triangleIndex) {

			PShape child = p5.createShape();
			child.beginShape(p5.TRIANGLES);
			child.fill(10);
			child.vertex(triangle[0].x, triangle[0].y, triangle[0].z);
			child.vertex(triangle[1].x, triangle[1].y, triangle[1].z);
			child.vertex(triangle[2].x, triangle[2].y, triangle[2].z);
			child.endShape();

			displayShape.addChild(child);

		}
	}

	@Override
	public void apply(BPhysics p, BObject o) {
		// TODO Auto-generated method stub

	}

}
