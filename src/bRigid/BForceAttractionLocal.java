package bRigid;

import javax.vecmath.Vector3f;

/**
 * attraction force for local behaviors, add on Objects
 */
public class BForceAttractionLocal implements BInterface {

	public BObject attr;

	protected float radius, radiusSquared;
	protected float strength;

	public BForceAttractionLocal(BObject attractor, float radius, float strength) {
		this.attr = attractor;
		this.strength = strength;
		setRadius(radius);
	}

	@Override
	public void apply(BPhysics p, BObject o) {
		
		Vector3f sum = new Vector3f();
		int count = 0;
		for (BObject neighbor : p.rigidBodies) {
			if (neighbor == o)
				continue;
			Vector3f pos = o.getPosition();
			Vector3f delta = neighbor.getPosition();
			delta.sub(pos);
			
			float dist = delta.lengthSquared();
			if (dist < radiusSquared) {
				delta.normalize();
				delta.scale(1.0f - dist / radiusSquared);
				delta.scale(strength);
				sum.add(delta);
				count++;
			}
		}
		if (count > 0)
			sum.scale(1.0f / count);
		if(sum.length() > .01f) {
			//sum.normalize();
			//sum.scale(.01f);
		}
		System.out.println(sum);
		o.rigidBody.applyCentralForce(sum);
		
	}

	public void setRadius(float r) {
		this.radius = r;
		this.radiusSquared = r * r;
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
