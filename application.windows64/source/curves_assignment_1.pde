import g4p_controls.*;

import java.util.ArrayList;

boolean drawPoints;
boolean endPoints = false;
int pointCount = 0;
int pointRadius = 25;
int degree =1;
int catmulDegree = 2;
//new code for dragging
int dragX;
int dragY;
ArrayList myPoints;
abstractSearchObject pointsBeingDragged;
int activeCurve;
boolean intersection = false;

void setup() {
  size(700, 700);//1080,720);
  background(102);
  myPoints = new ArrayList();
  createGUI();
  staticTextGUI();
  strokeWeight(1);
  smooth();
}

void draw() {

  stroke(0);
  if (drawPoints) {
    pointCount++;
    ellipse(mouseX, mouseY, pointRadius, pointRadius);
    String name = "point"+str(pointCount);
    myPoints.add(new Query(name, color(255, 255, 255), mouseX, mouseY, pointRadius, pointRadius));
    drawPoints = false;
  }

  if (!drawPoints) {
    background(102);
    for (int i = 0; i < myPoints.size (); i++) {
      abstractSearchObject myPointTemp = (abstractSearchObject)myPoints.get(i);
      myPointTemp.display();
    }


    if (lagrangeBool || bezierBool || bSplineBool || catmulRomBool) {
      for (int i = 0; i < numberOfBatches; i++) {
        for (int k =0; k < pointsFound[i].length-2; k++) {
          if (pointsFound[i][k+1] != null) {
            if (special) {
              stroke(color(random(255), random(255), random(255)));
            }
            line(pointsFound[i][k].x, pointsFound[i][k].y, pointsFound[i][k+1].x, pointsFound[i][k+1].y);
          }
        }
      }
      if (bezierBool && intersection) {
        for (int i = 0; i < drawBoxes.size(); i++) {
          strokeWeight(0.5);
          stroke(color(255, 0, 0));
          noFill();
          rectMode(CORNER); 
          int x = drawBoxes.get(i);
          rect( minBoundingBox[x][0].x, minBoundingBox[x][0].y, (maxBoundingBox[x][0].x-minBoundingBox[x][0].x), (maxBoundingBox[x][0].y - minBoundingBox[x][0].y));
          //quad( minBoundingBox[i][0].x,minBoundingBox[i][0].y,maxBoundingBox[i][0].x, minBoundingBox[i][0].y, maxBoundingBox[i][0].x, maxBoundingBox[i][0].y,minBoundingBox[i][0].x, maxBoundingBox[i][0].y );
        }
       
      }
    }
  }

  if (!bezierBool && intersection) {
    intersection =false;
  }
}

void keyPressed() {
  if (key == 'e' || key == 'E') {
    endPoints = true;
  }
  if (key == '=' || key == '+') {
    if (lagrangeBool || bezierBool || bSplineBool) {
      degree +=1;
      if (degree>=myPoints.size()) {
        degree = myPoints.size() -1;
      }
    } else if (catmulRomBool) {
      catmulDegree +=1;
      if (myPoints.size()%2 == 0) {
        if (catmulDegree>((myPoints.size()/2))) {
          catmulDegree = (myPoints.size()/2);
        }
      } else {
        if (catmulDegree>((myPoints.size()/2)+1)) {
          catmulDegree = (myPoints.size()/2)+1;
        }
      }
    }
    callCurveFunction(activeCurve);
  }
  if (key == '-' || key == '_') {
    if (lagrangeBool || bezierBool || bSplineBool) {
      degree -=1;
      if (degree<1) {
        degree = 1;
      }
    } else if (catmulRomBool) {
      catmulDegree -=1;
      if (catmulDegree<1) {
        catmulDegree = 1;
      }
    }
    callCurveFunction(activeCurve);
  }
  if (key == '1' || key == '1') {
    activeCurve = 1;
    special = false;
    callCurveFunction(activeCurve);
  }
  if (key == '2' || key == '2') {
    activeCurve = 2;
    special = false;
    callCurveFunction(activeCurve);
  }
  if (key == '3' || key == '#') {
    activeCurve = 3;
    special = false;
    if (degree<2) {
      degree =2;
    }
    callCurveFunction(activeCurve);
  }
  if (key == '4' || key == '$') {
    activeCurve = 4;
    special = false;
    if (degree<2) {
      degree =2;
    }
    callCurveFunction(activeCurve);
  }
  if (key == 's' || key == 'S') {
    activeCurve = 5;
    special = true;
    //if (catmulDegree<1) {
      catmulDegree =1;
   // }
    callCurveFunction(activeCurve);
  }
  if (key == 'i' || key == 'i') {
    if (activeCurve == 2) {
      intersection = true;
    }
    special = false;
    callCurveFunction(activeCurve);
  }
}

void mouseClicked() {
  if (!endPoints) {
    drawPoints = true;
  }
}

void mousePressed() {
  if (endPoints) {
    for (int i = 0; i < myPoints.size (); i++) { 
      // note how I made it generic 
      abstractSearchObject myPointTemp = (abstractSearchObject)myPoints.get(i);
      evaluatePointSelection(myPointTemp);
    }
    //println("pressed");
  }
}

void mouseReleased() {
  pointsBeingDragged = null; 
  if (endPoints) {
  }
} 

void mouseDragged() {
  if ( pointsBeingDragged != null) {
    //println("dragging" + pointsBeingDragged.name);
    pointsBeingDragged.moveByMouseCoord(mouseX, mouseY);
    if (lagrangeBool || bezierBool || bSplineBool ||catmulRomBool) {
      callCurveFunction(activeCurve);
    }
  }
}  

void callCurveFunction(int activeCurveArg) {
  switch (activeCurveArg) {
  case 1:
    lagrange();
    break;
  case 2:
    bezierCurve2();
    break;
  case 3:
    bSplineCurve();
    break;
  case 4:
    catmulRomCurve();
    break;
  case 5:
    catmulRomCurve();
    break;
  default:             
    println("No Curve Selected");   
    break;
  }
  updateGUI();
}

void reset() {
  myPoints = null;
  myPoints = new ArrayList();
  print(myPoints.size());
  endPoints = false;
  activeCurve = 0;
  drawPoints = false;
  lagrangeBool = bezierBool = bSplineBool = catmulRomBool =false;
  updateGUI();
}

