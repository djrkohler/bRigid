package bRigid;

import javax.vecmath.Vector3f;

/**
 * Attractor force
 */
public class BForceAttraction implements BInterface {

	protected Vector3f attractor;
	protected BObject attrObject;
	protected Vector3f offset;

	protected float radius, radiusSquared;
	protected float strength;

	/**
	 * can be applied global as behavior of the physics instance
	 */
	public BForceAttraction(Vector3f fixedPosition, float radius, float strength) {
		this.attractor = fixedPosition;
		this.strength = strength;
		setRadius(radius);
	}

	public BForceAttraction(BObject attractor, float radius, float strength) {
		this.attrObject = attractor;
		this.strength = strength;
		setRadius(radius);
	}

	public BForceAttraction(BObject attractor, Vector3f offset, float radius, float strength) {
		this.attrObject = attractor;
		this.offset = new Vector3f(0,0,0);
		this.offset.add(offset);
		this.strength = strength;
		setRadius(radius);
	}

	@Override
	public void apply(BPhysics p, BObject o) {
		if (o != attrObject) {
			Vector3f pos = o.getPosition();
			Vector3f delta = null;
			if (attractor != null) {
				delta = (Vector3f) attractor.clone();
			} else {

				delta = attrObject.getPosition();
				//delta.add(offset);
			}
			delta.sub(pos);
			float dist = delta.lengthSquared();
			if (dist < radiusSquared) {
				delta.normalize();
				delta.scale(1.0f - dist / radiusSquared);
				delta.scale(strength);
				o.rigidBody.applyCentralForce(delta);
			}
		}
	}

	public void setRadius(float r) {
		this.radius = r;
		this.radiusSquared = r * r;
	}

	public Vector3f getAttractor() {
		return attractor;
	}

	public void setAttractor(Vector3f attractor) {
		this.attractor = attractor;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	public float getRadius() {
		return radius;
	}

}
