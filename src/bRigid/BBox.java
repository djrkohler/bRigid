/**
 * This package (pRigid) provides classes for an easier handling of jBullet in Processing
 * @author Daniel Köhler
 * @version 0.2
 * 
 * Copyright (c) 2012 Daniel Köhler, daniel@lab-eds.org
 * 
 * Java port of Bullet (jBullet) (c) 2008 Martin Dvorak <jezek2@advel.cz>
 *
 * Bullet Continuous Collision Detection and Physics Library
 * Copyright (c) 2003-2008 Erwin Coumans  http://www.bulletphysics.com/
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

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;

/**
 * Box as rigidBody
 */
public class BBox extends BObject implements BInterface {

	/**
	 * extend dimensions
	 */
	public Vector3f dim;

	/**
	 * if mass is set to 0: the body will be static
	 */
	public BBox(PApplet p, float mass, float posX, float posY, float posZ, float dimX, float dimY, float dimZ) {
		this(p, mass, new Vector3f(posX, posY,  posZ), new Vector3f(dimX, dimY, dimZ), true);
	}

	public BBox(PApplet p, float mass, float dimX, float dimY, float dimZ) {
		this(p, mass, new Vector3f(), new Vector3f(dimX, dimY, dimZ), true);
	}

	/**
	 * if mass is set to 0: the body will be static, if inertia is false, the
	 * body will collide, but not rotate
	 */
	public BBox(PApplet p, float mass, Vector3f position, Vector3f dimension, boolean inertia) {

		super(p);

		this.dim = new Vector3f();
		this.dim.set(dimension);
		dim.scale(.5f);
		collisionShape = new BoxShape(this.dim);

		if (mass > 0) {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, mass, position, inertia);
			rigidBody = new RigidBody(rigidBodyConInfo);
		} else {
			RigidBodyConstructionInfo rigidBodyConInfo = createConInfo(collisionShape, 0, null, false);
			rigidBody = new RigidBody(rigidBodyConInfo);
			setPosition(position);
		}
		drawToPShape(collisionShape);
	}

}
