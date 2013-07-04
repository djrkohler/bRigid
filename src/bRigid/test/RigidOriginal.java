package bRigid.test;

import java.util.ArrayList;
import java.util.Iterator;

import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.collision.broadphase.*;
import com.bulletphysics.dynamics.*;
import com.bulletphysics.dynamics.constraintsolver.*;
import com.bulletphysics.linearmath.*;

import javax.vecmath.*;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class RigidOriginal extends PApplet{

	CollisionDispatcher myCd;
	BroadphaseInterface myBi;
	ConstraintSolver myCs;
	CollisionConfiguration myCc;

	RigidBody groundRigidBody;
	ArrayList fallRigidBodies = new ArrayList(); //RigidBody[];
	Iterator ite;

	int maxProxies = 1024;
	Vector3f worldAabbMin = new Vector3f(-10000,-10000,-10000);
	Vector3f worldAabbMax = new Vector3f(10000,10000,10000);
	DynamicsWorld myWorld;

	Transform myTransform;


	public void setup(){
	  size(480, 360, P3D);
	  smooth();
	  noFill();
	  
	  
	  myCc = new DefaultCollisionConfiguration();
	  myBi = new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
	  myCd = new CollisionDispatcher(myCc);
	  myCs = new SequentialImpulseConstraintSolver();
	  
	  myWorld = new DiscreteDynamicsWorld(myCd, myBi, myCs, myCc);
	  myWorld.setGravity(new Vector3f(0,60,0));
	  
	  //ADD STATIC GROUND
	  CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0,-1,0), 1); //1 m thick under ground
	  myTransform = new Transform(); myTransform.origin.set(new Vector3f(0,330,0)); myTransform.setRotation(new Quat4f(0,0,0,1));
	  DefaultMotionState groundMotionState = new DefaultMotionState(myTransform);
	  RigidBodyConstructionInfo groundCI = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new Vector3f(0,0,0));
	  RigidBody groundRigidBody = new RigidBody(groundCI);
	  myWorld.addRigidBody(groundRigidBody);
	  
	  for(float i=width; i>0; i=i-60){
	      for(float j=300; j>1; j=j-60.1f){
	        addBox(i, j, 0, 0);
	      }
	   }
	}

	public void draw(){
	  
	    background(255);
	    
	    myWorld.stepSimulation(1.0f/60.0f,8,1.0f/60.0f);
	    
	    ite = fallRigidBodies.iterator();
	    while( ite.hasNext() ) {
	      
	      Book fallRigidBody = (Book) ite.next();
	      
	      myTransform = new Transform();
	      myTransform = fallRigidBody.getMotionState().getWorldTransform(myTransform);
	      
	      pushMatrix();
	      
	      translate(myTransform.origin.x,myTransform.origin.y,myTransform.origin.z);
	      
	      applyMatrix(myTransform.basis.m00, myTransform.basis.m01, myTransform.basis.m02, 0,
	      myTransform.basis.m10, myTransform.basis.m11, myTransform.basis.m12, 0,
	      myTransform.basis.m20, myTransform.basis.m21, myTransform.basis.m22, 0,
	      0,  0,  0,  1);
	      
	      //fill(fallRigidBody.c);
	      
	      box(6,60,30);
	      popMatrix();
	    }
	}

	public void addBox(float pX, float pY,float pZ, float quat3){
	  
	  //ADD FALLING BOX 
	  CollisionShape fallShape = new BoxShape(new Vector3f(3,30,15));
	  myTransform = new Transform(); myTransform.origin.set(new Vector3f(pX,pY,pZ)); myTransform.setRotation(new Quat4f(0,0,quat3,1));
	  DefaultMotionState fallMotionState = new DefaultMotionState(myTransform);
	  float myFallMass = 1; Vector3f myFallInertia = new Vector3f(10,0,0);
	  fallShape.calculateLocalInertia(myFallMass, myFallInertia);
	  RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(myFallMass, fallMotionState, fallShape, myFallInertia);
	  Book fallRigidBody = new Book(fallRigidBodyCI);
	  myWorld.addRigidBody(fallRigidBody);
	  
	  fallRigidBodies.add(fallRigidBody);
	}

	public void mousePressed(){
	  addBox(mouseX,mouseY,0,0);
	  println("You have "+fallRigidBodies.size()+" boxes in this simulation.");
	}

	class Book extends RigidBody{
	  
	  Book(RigidBodyConstructionInfo CI){
	  super(CI);
	  }
	}
}
