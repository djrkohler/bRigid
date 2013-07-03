package bRigid;

import java.nio.ByteBuffer;

import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

public class BTerrain extends BObject{

	PVector[] pvert;
	int count, countsq, countsqp, indCount, nsscroll;
	ByteBuffer ind, vert;

	public BTerrain(PApplet p, int count) {
		super(p);
		this.count = count;
		countsq = count * count;
		countsqp = countsq + (count * 2 + 1);
		pvert = new PVector[countsqp];
		nsscroll = 1000;
		
		//collisionShape = tshape(sz);
	}

	/*
	 * void update(float sz) { float hdim = sz*count*.5; float nscl = .09; float
	 * zscl = 7.5; for(int a=0; a<countsqp; a++) { int xi = a%(count+1); int yi
	 * = floor(a/(count+1)); float xx = xi*sz-hdim; float zz = yi*sz-hdim; float
	 * yy = 0; yy += noise((nsscroll+xx+1)*nscl, zz*nscl); yy +=
	 * noise((nsscroll+xx+1)*nscl, (zz+1)*nscl); yy +=
	 * noise((nsscroll+xx-1)*nscl, zz*nscl); yy += noise((nsscroll+xx)*nscl,
	 * (zz-1)*nscl); yy *= .25*zscl; // float yy = noise(1000+xx*nscl,
	 * zz*nscl)*zscl; if(xi == 0 || xi == count || yi == 0 || yi == count) yy -=
	 * zscl*.2; pvert[a] = new PVector(xx, yy, zz); // vert.putFloat(a*4, xx);
	 * // vert.putFloat((a+1)*4, yy); // vert.putFloat((a+2)*4, zz);
	 * shape.setVertex(a, pvert[a]); } nsscroll++; } //
	 */

	protected CollisionShape tshape(float sz) {
		float hdim = sz * count * .5f;
		float nscl = .09f;
		float zscl = 7.5f;
		ind = ByteBuffer.allocateDirect(countsq * 24);
		vert = ByteBuffer.allocateDirect(countsqp * 12);
		for (int a = 0; a < countsqp; a++) {
			int xi = a % (count + 1);
			int yi = p5.floor(a / (count + 1));
			float xx = xi * sz - hdim;
			float zz = yi * sz - hdim;
			float yy = 0;
			yy += p5.noise((nsscroll + xx + 1) * nscl, zz * nscl);
			yy += p5.noise((nsscroll + xx + 1) * nscl, (zz + 1) * nscl);
			yy += p5.noise((nsscroll + xx - 1) * nscl, zz * nscl);
			yy += p5.noise((nsscroll + xx) * nscl, (zz - 1) * nscl);
			yy *= .25 * zscl;
			// float yy = noise(1000+xx*nscl, zz*nscl)*zscl;
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

	public PShape getPShape() {
		
		ByteBuffer indCopy = ind.duplicate();
		indCopy.rewind();
		PShape ret = p5.createShape();
		ret.beginShape(p5.TRIANGLES);
		// ret.stroke(80);
		for (int a = 0; a < countsq * 6; a++) {
			// float rnd = p5.random(168,255);
			int cind = indCopy.getInt();
			PVector cur = pvert[cind];
			ret.fill(240, 240, 240);
			ret.vertex(cur.x, cur.y, cur.z);
		}
		ret.endShape();
		displayShape = ret;
		return ret;
	}
}
