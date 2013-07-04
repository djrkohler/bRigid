package bRigid;

import javax.vecmath.Vector3f;

/**
 * linear force 
 */

public class BForce implements BInterface{

	Vector3f force;
	
	/**
	 * can be applied global as behavior of the physics instance or local as behavior on a BObject
	 * magnitude of vector == strength; vector == direction
	 * @param f
	 */
	public BForce(Vector3f f) {
		force = f;
	}
	@Override
	public void apply(BPhysics p, BObject o) {
		o.rigidBody.applyCentralForce(force);	
	}


}
