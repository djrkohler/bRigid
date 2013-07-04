package bRigid;

import java.nio.ByteBuffer;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

/**
 * static BvhTriangleMeshShape integrated as noisy Terrain. tShape method by Richard Brauer
 * @author Daniel
 *
 */
public class BTerrain extends BObject {

	PVector[] pvert;
	int count, countsq, countsqp, indCount, nsscroll;
	ByteBuffer ind, vert;
	
	public BTerrain(PApplet p, int tesselation, float height, int seed, Vector3f position, Vector3f scale) {
		super(p);
		this.count = tesselation;
		countsq = count * count;
		countsqp = countsq + (count * 2 + 1);
		pvert = new PVector[countsqp];
		nsscroll = seed;

		collisionShape = tshape(height, seed);
		collisionShape.setLocalScaling(scale);
		
		RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, position, false);
		rigidBody = new RigidBody(rigidBodyConInfo);

		rigidBody.setFriction(.5f);
		rigidBody.setRestitution(.5f);
		
		drawToPShape(collisionShape);
	}

	

	protected CollisionShape tshape(float height, int seed) {
		float hdim =  count * .5f;
		float nscl = .09f;
		float zscl = 7.5f;
		ind = ByteBuffer.allocateDirect(countsq * 24);
		vert = ByteBuffer.allocateDirect(countsqp * 12);
		for (int a = 0; a < countsqp; a++) {
			int xi = a % (count + 1);
			int yi = p5.floor(a / (count + 1));
			float xx = xi - hdim;
			float zz = yi  - hdim;
			float yy = 0;
			yy += p5.noise((nsscroll + xx + 1) * nscl, zz * nscl);
			yy += p5.noise((nsscroll + xx + 1) * nscl, (zz + 1) * nscl);
			yy += p5.noise((nsscroll + xx - 1) * nscl, zz * nscl);
			yy += p5.noise((nsscroll + xx) * nscl, (zz - 1) * nscl);
			yy *= height * zscl;
			if (xi == 0 || xi == count || yi == 0 || yi == count)
				yy -= zscl * .2;
			pvert[a] = new PVector(xx, yy, zz);
					
			vert.putFloat(xx);
			vert.putFloat(yy);
			vert.putFloat(zz);
			if (a != 0 && (xi == count || a > countsq + (count - 2))) {
				continue;
			}
			indCount += 2;
			ind.putInt(a);
			ind.putInt(a + 1);
			ind.putInt(a + count + 1);
			ind.putInt(a + 1);
			ind.putInt(a + count + 2);
			ind.putInt(a + count + 1);
		}
		TriangleIndexVertexArray tiva = new TriangleIndexVertexArray(indCount, ind, 12, countsq, vert, 12);

		return new BvhTriangleMeshShape(tiva, true);
	}
}
