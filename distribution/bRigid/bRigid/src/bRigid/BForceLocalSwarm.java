package bRigid;

import javax.vecmath.Vector3f;

public class BForceLocalSwarm implements BInterface {

	Vector3f force;
	float cohesionRadius, alignmentRadius, seperationRadius, cohesionScale, alignScale, seperationScale;
	float cohRadSquared, alRadSquared, sepRadSquared;
	float maxSpeed, maxForce;

	/**
	 * default Constructor input: rangeRadius for Seperation 25, rangeRadius for
	 * Alignment 25, rangeRadius for Cohesion 25, defaultScale: 1.5;1.0;1.0,
	 * default maxSpeed: 3.0, default maxForce .05
	 */

	public BForceLocalSwarm() {
		this(20, 20, 15, 1.5f, 1.0f, 1.0f, 30.0f, .05f);

	}

	public BForceLocalSwarm(float seperationRadius, float alignmentRadius, float cohesionRadius, float seperationScale, float alignScale,
			float cohesionScale, float maxSpeed, float maxForce) {

		this.cohesionRadius = cohesionRadius;
		this.alignmentRadius = alignmentRadius;
		this.seperationRadius = seperationRadius;
		cohRadSquared = cohesionRadius * cohesionRadius;
		alRadSquared = alignmentRadius * alignmentRadius;
		sepRadSquared = seperationRadius * seperationRadius;
		this.cohesionScale = cohesionScale;
		this.alignScale = alignScale;
		this.seperationScale = seperationScale;
		this.maxSpeed = maxSpeed;
		this.maxForce = maxForce;
	}

	@Override
	public void apply(BPhysics p, BObject o) {
		Vector3f f = swarm(p, o);
		//System.out.println(f);
		f.scale(100);
		o.rigidBody.applyCentralForce(f);
	}

	public Vector3f swarm(BPhysics p, BObject o) {

		// empty vectors to accumulate all locations
		Vector3f coh = new Vector3f();
		Vector3f sep = new Vector3f();
		Vector3f ali = new Vector3f();

		// counters for average division
		int countCoh = 0;
		int countAli = 0;
		int countSep = 0;

		Vector3f pos0 = o.getPosition();

		for (BObject neighbor : p.rigidBodies) {
			if (neighbor == o)
				continue;

			Vector3f pos1 = neighbor.getPosition();
			Vector3f delta = (Vector3f) pos1.clone();
			delta.sub(pos0);
			float d = delta.lengthSquared();

			// COHESION:
			if (d < cohRadSquared) {
				coh.add(pos1);
				countCoh++;
			}
			// SEPARATION:
			if (d < sepRadSquared) {
				Vector3f diff = (Vector3f) pos0.clone();
				diff.sub(pos1);
				diff.normalize();
				diff.scale(1.0f / d);
				sep.add(diff);
				countSep++;
			}
			// ALIGNMENT:
			if (d < alRadSquared) {
				ali.add(neighbor.getVelocity());
				countAli++;
			}
		}

		// COHESION:
		if (countCoh > 0) {
			coh.scale(1.0f / countCoh);
			coh = seek(coh, o);
			if (coh.lengthSquared() > maxForce * maxForce) {
				coh.normalize();
				coh.scale(maxForce);
			}

		}

		// SEPARATION:
		if (countSep > 0) {
			sep.scale(1.0f / countSep);
		}

		if (sep.lengthSquared() > 0) {
			sep.normalize();
			sep.scale(maxSpeed);
			sep.sub(o.getVelocity());
			if (sep.lengthSquared() > maxForce * maxForce) {
				sep.normalize();
				sep.scale(maxForce);
			}
		}

		// ALIGNMENT:
		if (countAli > 0) {
			ali.scale(1.0f / countAli);
		}
		if (ali.lengthSquared() > 0) {
			ali.normalize();
			ali.scale(maxSpeed);
			ali.sub(o.getVelocity());
			if (ali.lengthSquared() > maxForce * maxForce) {
				ali.normalize();
				ali.scale(maxForce);
			}
		}

		// ---------------------------------------------------

		sep.scale(seperationScale);
		coh.scale(cohesionScale);
		ali.scale(alignScale);

		Vector3f acc = new Vector3f();
		acc.add(sep);
		acc.add(ali);
		acc.add(coh);

		return acc;
	}

	private Vector3f seek(Vector3f target, BObject o) {
		Vector3f steer;
		Vector3f desired = (Vector3f) target.clone();
		desired.sub(o.getPosition());
		float d = desired.lengthSquared();
		if (d > 0) {
			desired.normalize();
			desired.scale(maxSpeed);
			desired.sub(o.getVelocity());
			if (desired.lengthSquared() > maxForce * maxForce) {
				desired.normalize();
				desired.scale(maxForce);
			}
			steer = (Vector3f) desired.clone();
		} else {
			steer = new Vector3f();
		}
		return steer;
	}
	
	

	public float getCohesionRadius() {
		return cohesionRadius;
	}

	public void setCohesionRadius(float cohesionRadius) {
		this.cohesionRadius = cohesionRadius;
	}

	public float getAlignmentRadius() {
		return alignmentRadius;
	}

	public void setAlignmentRadius(float alignmentRadius) {
		this.alignmentRadius = alignmentRadius;
	}

	public float getSeperationRadius() {
		return seperationRadius;
	}

	public void setSeperationRadius(float seperationRadius) {
		this.seperationRadius = seperationRadius;
	}

	public float getCohesionScale() {
		return cohesionScale;
	}

	public void setCohesionScale(float cohesionScale) {
		this.cohesionScale = cohesionScale;
	}

	public float getAlignScale() {
		return alignScale;
	}

	public void setAlignScale(float alignScale) {
		this.alignScale = alignScale;
	}

	public float getSeperationScale() {
		return seperationScale;
	}

	public void setSeperationScale(float seperationScale) {
		this.seperationScale = seperationScale;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}

}
