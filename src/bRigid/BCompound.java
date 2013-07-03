package bRigid;

import java.util.ArrayList;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import processing.core.PApplet;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.Transform;

/**
 * CollisionShape as Compound of multiple shapes.
 */
public class BCompound extends BObject {

	/**
	 * if mass is set to 0: the body will be static
	 */
	public BCompound(PApplet p, float mass, Vector3f position, boolean inertia) {

		super(p);
		collisionShape = new CompoundShape();

		if (mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, position, inertia);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, position, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		
		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);
	}

	/**
	 * if mass is set to 0: the body will be static
	 */
	public BCompound(PApplet p, ArrayList<BObject> shapes, boolean active) {

		super(p);

		collisionShape = new CompoundShape();

		float mass = 0;
		for (BObject o : shapes) {
			Transform trans = new Transform();
			trans.setIdentity();
			// Quat4f out = new Quat4f();
			// QuaternionUtil.setRotation(out, new Vector3f(1,1,0), (float)
			// Math.random()*100);
			// trans.setRotation(out);
			trans = o.rigidBody.getMotionState().getWorldTransform(trans);
			((CompoundShape) collisionShape).addChildShape(trans, o.collisionShape);
			mass += -o.getMass();
		}

		if (active) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, new Vector3f(), true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);
		
		drawToPShape(transform, collisionShape);
		// drawToPShape(collisionShape);
		
	}

	public BCompound(PApplet p, float mass, ArrayList<BObject> shapes, boolean inertia) {

		super(p);

		collisionShape = new CompoundShape();

		for (BObject o : shapes) {
			Transform trans = new Transform();
			trans.setIdentity();
			trans = o.rigidBody.getMotionState().getWorldTransform(trans);
			((CompoundShape) collisionShape).addChildShape(trans, o.collisionShape);
		}

		if (inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, new Vector3f(), true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else if (!inertia && mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, new Vector3f(), false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		
		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);
		
		drawToPShape(transform, collisionShape);
		// drawToPShape(collisionShape);
	}

	public BCompound(PApplet p, ArrayList<CollisionShape> shapes, ArrayList<Transform> trans, boolean active) {

		super(p);

		collisionShape = new CompoundShape();

		float mass = 0;
		for (int i = 0; i < shapes.size(); i++) {
			CollisionShape o = shapes.get(i);
			Transform t = trans.get(i);

			((CompoundShape) collisionShape).addChildShape(t, o);
			mass += 1;
		}

		if (active) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, new Vector3f(), true);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, new Vector3f(), false);
			rigidBody = new RigidBody(rigidBodyConInfo);
		}
		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);

		drawToPShape(transform, collisionShape);
	}

	/**
	 * if mass is set to 0: the body will be static
	 */
	public BCompound(PApplet p, CompoundShape c, float mass) {

		super(p);

		collisionShape = c;

		RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, new Vector3f(0, 0, 0), true);
		rigidBody = new RigidBody(rigidBodyConInfo);
		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);

	}

	protected void addCollisionShape(BObject object) {
		CompoundShape c = (CompoundShape) this.collisionShape;
		Transform trans = new Transform();
		trans.setIdentity();
		trans = object.rigidBody.getMotionState().getWorldTransform(trans);
		Quat4f out = new Quat4f();

		// last val == rotation
		// QuaternionUtil.setRotation(out, new Vector3f(1,1,0), (float)
		// Math.random()*100);
		QuaternionUtil.setRotation(out, new Vector3f(1, 1, 0), (float) 0);
		trans.setRotation(out);

		c.addChildShape(trans, object.collisionShape);
		float mass = -object.getMass();
		mass += this.getMass();
		((CompoundShape) collisionShape).calculateLocalInertia(mass, new Vector3f(0, 0, 0));
		drawToPShape(transform, collisionShape);
	}

	public BCompound addCollisionShape(BPhysics physics, BObject object) {

		ArrayList<CollisionShape> shapes = new ArrayList<CollisionShape>();
		ArrayList<Transform> trans = new ArrayList<Transform>();

		int ind = ((CompoundShape) collisionShape).getNumChildShapes();
		for (int i = 0; i < ind; i++) {
			CollisionShape shape = ((CompoundShape) collisionShape).getChildShape(i);
			Transform t = new Transform();
			t = ((CompoundShape) collisionShape).getChildTransform(i, t);
			shapes.add(shape);
			trans.add(t);
		}

		CollisionShape shape = object.collisionShape;
		Transform t = object.transform;
		shapes.add(shape);
		trans.add(t);

		BCompound compo = new BCompound(p5, shapes, trans, true);
		physics.removeBody(this);
		this.displayShape = null;
		return compo;
	}

	public BCompound addCollisionShape(BPhysics physics, BObject object, boolean active) {

		ArrayList<CollisionShape> shapes = new ArrayList<CollisionShape>();
		ArrayList<Transform> trans = new ArrayList<Transform>();

		int ind = ((CompoundShape) collisionShape).getNumChildShapes();
		for (int i = 0; i < ind; i++) {
			CollisionShape shape = ((CompoundShape) collisionShape).getChildShape(i);
			Transform t = new Transform();
			t = ((CompoundShape) collisionShape).getChildTransform(i, t);
			shapes.add(shape);
			trans.add(t);
		}

		CollisionShape shape = object.collisionShape;
		Transform t = object.transform;
		shapes.add(shape);
		trans.add(t);

		BCompound compo = new BCompound(p5, shapes, trans, active);
		physics.removeBody(this);
		this.displayShape = null;
		return compo;
	}

	/*
	public void display() {
		if (displayShape != null) {
			if (getMass() < 0) {
				Transform transform = new Transform();
				transform = rigidBody.getMotionState().getWorldTransform(transform);
				Matrix4f out = new Matrix4f();
				out = transform.getMatrix(out);

				displayShape.applyMatrix(out.m00, out.m01, out.m02, out.m03, out.m10, out.m11, out.m12, out.m13, out.m20, out.m21, out.m22,
						out.m23, out.m30, out.m31, out.m32, out.m33);
				p5.shape(displayShape);
				displayShape.resetMatrix();
			} else {

				Transform trans = new Transform();
				trans = rigidBody.getWorldTransform(trans);
				Matrix4f out = new Matrix4f();
				out = transform.getMatrix(out);
				
				displayShape.translate(trans.origin.x, trans.origin.y, trans.origin.z);
				p5.shape(displayShape);
				displayShape.resetMatrix();

			}
		}

	}*/

}
