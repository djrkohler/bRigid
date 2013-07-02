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

import com.bulletphysics.dynamics.constraintsolver.Generic6DofConstraint;

/**
 * Generic6DofConstraint between two rigidbodies each with a pivot point that descibes the axis location in local space.
 */
public class BJoint6DofC extends Generic6DofConstraint{
	
	BObject a,b;
	
	public BJoint6DofC(BObject a, BObject b) {
		  super(a.rigidBody, b.rigidBody, a.transform, b.transform, false);
		  setAngularLowerLimit(new Vector3f());
		  setAngularUpperLimit(new Vector3f());	  
	}
	
	
	
	
}
