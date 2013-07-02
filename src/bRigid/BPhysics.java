package bRigid;

/**
 * @author daniel
 * @modified 23/08/2012
 * @version	0.2
 */
import java.util.ArrayList;

import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.broadphase.*;
import com.bulletphysics.dynamics.*;
import com.bulletphysics.dynamics.constraintsolver.*;
import com.bulletphysics.linearmath.Clock;

import javax.vecmath.*;

/**
 * solver class, physics world; the BPhysics class acts like a container; every
 * rigid object or spring should be added here </p> thanks to the examples of
 * Giulio Piacentino and Richard Brauer </p> jBullet is a Java port of Bullet
 * (c) 2008 Martin Dvorak http://jbullet.advel.cz/ ; </p> Bullet Continuous
 * Collision Detection and Physics Library (c) 2003-2008 Erwin Coumans
 * http://www.bulletphysics.com/ 
 * </p> bRigid written by Daniel Koehler - 2013 www.lab-eds.org - for feedback please
 * contact me at: daniel@lab-eds.org
 */
public class BPhysics {

	/**
	 * List of all rigidObjects
	 */
	public ArrayList<BObject> rigidBodies;

	public ArrayList<BInterface> behaviors;
	/**
	 * jBullet world node
	 */
	public DynamicsWorld world;
	private Clock clock;

	public CollisionDispatcher dispatcher;

	/**
	 * initializes a BPhysics World instance, default with a AxisSweep3
	 * broadphase
	 */
	public BPhysics() {

		CollisionConfiguration collConf = new DefaultCollisionConfiguration();
		dispatcher = new CollisionDispatcher(collConf);
		Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);
		Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);
		// typical broadphase
		BroadphaseInterface broadphase = new AxisSweep3(worldAabbMin, worldAabbMax);
		// precise broadphase
		// BroadphaseInterface broadphase = new AxisSweep3_32(worldAabbMin,
		// worldAabbMax);
		// for a lot of moving bodies
		// BroadphaseInterface broadphase = new DbvtBroadphase();
		ConstraintSolver sequImpConstSolver = new SequentialImpulseConstraintSolver();

		world = new DiscreteDynamicsWorld(dispatcher, broadphase, sequImpConstSolver, collConf);
		rigidBodies = new ArrayList<BObject>();

		clock = new Clock();
	}

	/**
	 * initializes a BPhysics World instance, with a rigid boundingBox
	 * 
	 * @param min
	 *            Vector
	 * @param max
	 *            Vector
	 */
	public BPhysics(Vector3f min, Vector3f max) {
		this();
		initeBBox(min, max);
	}

	/**
	 * add every RigidObject to the BPhysics instance for simulation
	 */
	public void addBody(BObject rig) {
		RigidBody rb = rig.rigidBody;
		world.addRigidBody(rb);
		rigidBodies.add(rig);

	}

	/**
	 * add BPlane with this method
	 */
	public void addPlane(BPlane rig) {
		RigidBody rb = rig.rigidBody;
		world.addRigidBody(rb);
	}

	public void addJoint(TypedConstraint constraint) {
		world.addConstraint(constraint, true);
	}

	public void addBehavior(BInterface b) {
		if (this.behaviors == null)
			behaviors = new ArrayList<BInterface>();
		behaviors.add(b);
	}

	public void removeBody(BObject o) {
		RigidBody rig = o.rigidBody;
		world.removeRigidBody(rig);
		this.rigidBodies.remove(o);
		rig.destroy();

	}

	public void removeRigidObject(BObject rig) {
		RigidBody rb = rig.rigidBody;
		world.removeRigidBody(rb);
		rb.destroy();
	}

	public void removeBehavior(BInterface b) {
		behaviors.remove(b);
	}

	/**
	 * simulates over all containing objects and constraints
	 */
	public void update() {
		// world.stepSimulation(1.0f / 60.0f, 8, 1.0f / 60.0f);
		updateBehaviors();
		updateObjects();
		float stepTime = getDeltaTimeMicroseconds();
		world.stepSimulation(stepTime / 1000000f);
	}

	private void updateObjects() {
		for (BObject o : rigidBodies) {
			if (o.behaviors != null) {
				for (BInterface b : o.behaviors) {
					b.apply(this, o);
				}
			}
		}
	}

	private void updateBehaviors() {
		if (behaviors != null) {
			for (BObject o : rigidBodies) {
				for (BInterface b : behaviors) {
					b.apply(this, o);
				}
			}
		}
	}

	/**
	 * diplays all containing rigid objects
	 */
	public void display() {
		for (BObject o : rigidBodies) {
			o.display();
		}
	}

	public void display(float r, float g, float b) {
		for (BObject o : rigidBodies) {
			o.display(new Vector3f(r,g,b));
		}
	}
	
	public void display(float r, float g, float b, boolean fill, boolean stroke) {
		for (BObject o : rigidBodies) {
			o.display(new Vector3f(r,g,b), fill, stroke);
		}
	}
	
	public void display(Vector3f rgb, boolean fill, boolean stroke) {
		for (BObject o : rigidBodies) {
			o.display(rgb, fill, stroke);
		}
	}
	
	public void display(Vector3f fill, Vector3f stroke) {
		for (BObject o : rigidBodies) {
			o.display(fill, stroke);
		}
	}

	private void initeBBox(Vector3f min, Vector3f max) {
		BPlane p0 = new BPlane(new Vector3f(0, min.y, 0), new Vector3f(0, 1, 0));
		addPlane(p0);
		BPlane p1 = new BPlane(new Vector3f(0, 0, min.z), new Vector3f(0, 0, 1));
		addPlane(p1);
		BPlane p2 = new BPlane(new Vector3f(0, max.y, 0), new Vector3f(0, -1, 0));
		addPlane(p2);
		BPlane p3 = new BPlane(new Vector3f(max.x, 0, 0), new Vector3f(-1, 0, 0));
		addPlane(p3);
		BPlane p4 = new BPlane(new Vector3f(min.x, 0, 0), new Vector3f(1, 0, 0));
		addPlane(p4);
		BPlane p5 = new BPlane(new Vector3f(0, 0, max.z), new Vector3f(0, 0, -1));
		addPlane(p5);
	}

	private float getDeltaTimeMicroseconds() {
		float dt = clock.getTimeMicroseconds();
		clock.reset();
		return dt;
	}

}
