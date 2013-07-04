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

import com.bulletphysics.dynamics.constraintsolver.SliderConstraint;
import com.bulletphysics.linearmath.Transform;

/**
 * Slider constraint between two rigid bodies
 * allows the body to rotate around one axis and translate along this axis.
 */
public class BJointSlider extends SliderConstraint {
	
	public BJointSlider(BObject a, BObject b, Transform rbAFrame, Transform rbBFrame, boolean useLinearReferenceFrameA) {
		super(a.rigidBody, b.rigidBody, rbAFrame, rbBFrame, useLinearReferenceFrameA);
	}
	
	public BJointSlider(BObject a, BObject b, Transform rbAFrame, Transform rbBFrame) {
		super(a.rigidBody, b.rigidBody, rbAFrame, rbBFrame, true);
	}
	
}
