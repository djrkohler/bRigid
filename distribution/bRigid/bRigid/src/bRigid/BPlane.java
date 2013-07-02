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

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

/**
 * infinite non-moving (static) collision plane. 
 */
public class BPlane {

	protected CollisionShape planeShape;
	public RigidBody rigidBody;
/**
 * infinite plane placed with Vector3f position; orientation by Vector3f normal; 
 * normal direction points towards the expected collisions
 */
	public BPlane(Vector3f position, Vector3f normal) {

		CollisionShape planeShape = new StaticPlaneShape(normal, 1);
		Transform transform = new Transform();
		transform.origin.set(position);
		transform.setRotation(new Quat4f(0, 0, 0, 1));
		DefaultMotionState groundMotionState = new DefaultMotionState(transform);
		RigidBodyConstructionInfo groundConInfo = new RigidBodyConstructionInfo(0, groundMotionState, planeShape, new Vector3f(0, 0, 0));
		rigidBody = new RigidBody(groundConInfo);
	}

}
