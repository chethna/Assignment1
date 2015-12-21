import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import g4p_controls.*; 
import java.util.ArrayList; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class curves_assignment_1 extends PApplet {





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

public void setup() {
  size(700, 700);//1080,720);
  background(102);
  myPoints = new ArrayList();
  createGUI();
  staticTextGUI();
  strokeWeight(1);
  smooth();
}

public void draw() {

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
          strokeWeight(0.5f);
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

public void keyPressed() {
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

public void mouseClicked() {
  if (!endPoints) {
    drawPoints = true;
  }
}

public void mousePressed() {
  if (endPoints) {
    for (int i = 0; i < myPoints.size (); i++) { 
      // note how I made it generic 
      abstractSearchObject myPointTemp = (abstractSearchObject)myPoints.get(i);
      evaluatePointSelection(myPointTemp);
    }
    //println("pressed");
  }
}

public void mouseReleased() {
  pointsBeingDragged = null; 
  if (endPoints) {
  }
} 

public void mouseDragged() {
  if ( pointsBeingDragged != null) {
    //println("dragging" + pointsBeingDragged.name);
    pointsBeingDragged.moveByMouseCoord(mouseX, mouseY);
    if (lagrangeBool || bezierBool || bSplineBool ||catmulRomBool) {
      callCurveFunction(activeCurve);
    }
  }
}  

public void callCurveFunction(int activeCurveArg) {
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

public void reset() {
  myPoints = null;
  myPoints = new ArrayList();
  print(myPoints.size());
  endPoints = false;
  activeCurve = 0;
  drawPoints = false;
  lagrangeBool = bezierBool = bSplineBool = catmulRomBool =false;
  updateGUI();
}

int knotNumber,internalPointStart, internalPointEnd;
int[] knots;
boolean bSplineBool = false;

public void bSplineCurve(){
  //println(degree);
  lagrangeBool = false;
  bezierBool = false;
  bSplineBool = false;
  catmulRomBool = false;
  totalNumberOfPoint= myPoints.size();
  numberOfBatches = 1;
  knotNumber = degree+totalNumberOfPoint;
   
  knots = new int[knotNumber];
 
   for(int i=0; i<knotNumber;i++){
     if(i<degree){
       knots[i] = 0;
     }
     else if(i >= degree && i <=totalNumberOfPoint){
       knots[i] = i - degree +1;
     }
     else if(i>totalNumberOfPoint){
       knots[i] = totalNumberOfPoint - degree + 1;
     }
   }

   int index = 0;
   pointsFound = new PVector[numberOfBatches][];
   pointsFound[0] = new PVector[100*(knots[knotNumber-1])];
  
   for(float t = 0; t<=knots[knotNumber-1] ; t+= 0.01f){
     float sumx =0;
     float sumy =0;
     for(int i=0; i<totalNumberOfPoint ; i++){
       abstractSearchObject myPointTemp0 = (abstractSearchObject)myPoints.get(i);
       double temp = calculateN(i,degree, t);
        sumx = sumx+ (myPointTemp0.qx * (float)temp);
        sumy = sumy+ (myPointTemp0.qy * (float)temp);
     //   print(sumx,sumy);
     }
     if(index == 100*(knots[knotNumber-1])){
       increaseSize();
     }
    pointsFound[0][index] = new PVector(sumx,sumy);
    //println(pointsFound[0][index]);
    index +=1;
    sumx =0;
    sumy =0;
   }
  //println("index"+index);
  lagrangeBool = false;
  bezierBool = false;
  catmulRomBool = false;
  bSplineBool = true;
  activeCurve = 3;
 }
 
 public double calculateN(int i, int j, float t){
    double Nij;
    if(j >1){
      double deno1 = (knots[i+j-1] - knots[i]);
      double deno2 = (knots[i+j] - knots[i+1]);
      double temp1 = 0;
      double temp2 = 0;
      if(deno1 != 0){
         temp1 = ((t - knots[i])/deno1) * calculateN(i,j-1,t);
      }
      if(deno2 != 0){
         temp2 =((knots[i+j] - t)/deno2) * calculateN(i+1,j-1,t);
      }
     //float temp1 = ((t - knots[i])/(knots[i+j] - knots[i]))*calculateN(i,j-1,t);
     //float temp2 = ((knots[i+j+1] - t)/(knots[i+j+1] - knots[i+1])) * calculateN(i+1,j-1,t);
    /* if(Float.isNaN(temp1)){
      temp1 =0; 
     }
     if(Float.isNaN(temp2)){
      temp2 =0; 
     }*/
     Nij = temp1 + temp2;
     
    }
    else{
      if( (t>=knots[i]) && (t < knots[i+1])){
        Nij = 1;
      }
      else{
        Nij = 0;
      }
    }
    //println(Nij);
    if(Double.isNaN(Nij)){ 
      return 0;
    }
    else{
      return Nij;
    }
   }
   
   
public void increaseSize() {
   PVector[] temp = new PVector[pointsFound[0].length + 1];
   for (int i = 0; i < pointsFound[0].length; i++){
      temp[i] = pointsFound[0][i];
   }
   pointsFound[0] = new PVector[pointsFound[0].length + 1];
   pointsFound[0] = temp;
}
int [][] PascalsTriangle = {{1}, //n=0
                           {1,1}, //n=1
                          {1,2,1}, //n=2
                         {1,3,3,1}, //n=3
                        {1,4,6,4,1}, //n=4
                       {1,5,10,10,5,1}, //n=5
                      {1,6,15,20,15,6,1}, //n=6
                     {1,7,21,35,35,21,7,1}, //n=7
                    {1,8,28,56,70,56,28,8,1}}; //n=8
float pointx, pointy;
boolean bezierBool =false;

 public void bezierCurve(){
   totalNumberOfPoint= myPoints.size();
   findNumberOfBatches();
   splitPointsIntoBatches();
   
   pointsFound = new PVector[numberOfBatches][];
//  print("numberOfBatches:"+numberOfBatches+"\n");
  for (int n=0; n<numberOfBatches; n++) {
   pointsFound[n] = new PVector[200];
   int index = 0;
   for(float t =0.005f; t<1; t+=0.005f){
    for(int i=0; i<degree+1; i++){
     abstractSearchObject myPointTemp = (abstractSearchObject)outer.get(n).get(i);
   //  println("i:"+i+", x: "+myPointTemp.qx+" , y:"+myPointTemp.qy);
     pointx +=  PascalsTriangle[degree][i]*pow((1-t),(degree-i))*pow(t,i)*myPointTemp.qx;
     pointy +=  PascalsTriangle[degree][i]*pow((1-t),(degree-i))*pow(t,i)*myPointTemp.qy;
    }
     //println(pointx, pointy);
     pointsFound[n][index] = new PVector(pointx, pointy);
    // println(index);
     index += 1;
     pointx = 0;
     pointy = 0;
   }
  }
   lagrangeBool = false;
   bezierBool = true;
   activeCurve = 2;
 }
 
 PVector[][] maxBoundingBox;
 PVector[][] minBoundingBox;
 
 public void bezierCurve2(){
   totalNumberOfPoint= myPoints.size();
   findNumberOfBatches();
   splitPointsIntoBatches();
   
   pointsFound = new PVector[numberOfBatches][];
   
   maxBoundingBox = new PVector[numberOfBatches][1];
   minBoundingBox = new PVector[numberOfBatches][1];
   storeValue();
   
   for (int n=0; n<numberOfBatches; n++) {
     pointsFound[n] = new PVector[102];
     int totalNumberOfPointInBatch= degree+1;
     PVector[][] Pstorage = new PVector[totalNumberOfPointInBatch][];
     float[][] Pstoragex = new float[totalNumberOfPointInBatch][];
     float[][] Pstoragey = new float[totalNumberOfPointInBatch][];
     Pstorage[0] = new PVector[totalNumberOfPointInBatch];
     Pstoragex[0] = new float[totalNumberOfPointInBatch];
     Pstoragey[0] = new float[totalNumberOfPointInBatch];
     for(int i = 0; i < totalNumberOfPointInBatch ; i++){
       abstractSearchObject myPointTemp0 = (abstractSearchObject)outer.get(n).get(i);;
        Pstorage[0][i] = new PVector(myPointTemp0.qx,myPointTemp0.qy);
        Pstoragex[0][i] = myPointTemp0.qx;
        Pstoragey[0][i] = myPointTemp0.qy;
     }
     int bezierIndex =0;
     for(float t = 0; t <= 1.01f ; t += 0.01f){
      int index=0;
      for(int i = 1; i < Pstorage[0].length ; i++){   
        
        Pstorage[i] = new PVector[Pstorage[0].length - index];
        Pstoragex[i] = new float[Pstorage[0].length - index];
        Pstoragey[i] = new float[Pstorage[0].length - index];
        for(int j=0; j < (Pstorage[i].length-1) ; j++){
        //  println(i+" : "+j);
          Pstoragex[i][j] = Pstoragex[i-1][j]*(1-t)+Pstoragex[i-1][j+1]*t;
          Pstoragey[i][j] = Pstoragey[i-1][j]*(1-t)+Pstoragey[i-1][j+1]*t;
          Pstorage[i][j] = new PVector(Pstoragex[i][j],Pstoragey[i][j]);
        }
        index += 1;
     }
     //max
     if(Pstoragex[Pstorage[0].length-1][0] > maxBoundingBox[n][0].x){
       maxBoundingBox[n][0].x = Pstoragex[Pstorage[0].length-1][0];
     }
     if(Pstoragey[Pstorage[0].length-1][0] > maxBoundingBox[n][0].y){
       maxBoundingBox[n][0].y = Pstoragey[Pstorage[0].length-1][0];
     }
     if(Pstoragex[Pstorage[0].length-1][0] < minBoundingBox[n][0].x){
       minBoundingBox[n][0].x = Pstoragex[Pstorage[0].length-1][0];
     }
     if(Pstoragey[Pstorage[0].length-1][0] < minBoundingBox[n][0].y){
       minBoundingBox[n][0].y = Pstoragey[Pstorage[0].length-1][0];
     }
     
     //print(Pstorage[Pstorage[0].length-1][0]);
     pointsFound[n][bezierIndex] = new PVector(Pstorage[Pstorage[0].length-1][0].x,Pstorage[Pstorage[0].length-1][0].y);
     bezierIndex +=1;
    }
    //println(maxBoundingBox[n][0].x,  maxBoundingBox[n][0].y, minBoundingBox[n][0].x, minBoundingBox[n][0].y);
   }
   
   if(intersection){
     checkBoxIntersections();
   }
  lagrangeBool = false;
  catmulRomBool = false;
  bSplineBool = false;
  bezierBool = true;
  activeCurve = 2;
 }
 
ArrayList<Integer> drawBoxes;
//ArrayList drawBoxes; 
public void checkBoxIntersections(){
 drawBoxes =new ArrayList<Integer>();
   for(int i=0; i <numberOfBatches-1 ; i++){
     for(int j=i+i; j <numberOfBatches ; j++){
       if(i!=j){
         
         if(DoBoxesIntersect(minBoundingBox[i][0], maxBoundingBox[i][0], minBoundingBox[j][0], maxBoundingBox[j][0])){
           //println("boxes:"+i+" and "+j+" intersect");
           drawBoxes.add((int)i);
           drawBoxes.add((int)j);
         }
       }
     }
   }
 }
 
 public boolean DoBoxesIntersect(PVector mina, PVector maxa, PVector minb, PVector maxb) {
  return (abs(mina.x - minb.x) * 2 < ((maxa.x-mina.x) + (maxb.x-minb.x))) &&
         (abs(mina.y - minb.y) * 2 < ((maxa.y-mina.y) + (maxb.y-minb.y)));
}
 
 public void storeValue(){
   for(int i=0; i <numberOfBatches ; i++){
      abstractSearchObject myPointTemp0 = (abstractSearchObject)outer.get(i).get(0);
      maxBoundingBox[i][0] = new PVector(myPointTemp0.qx,myPointTemp0.qy);
      minBoundingBox[i][0] = new PVector(myPointTemp0.qx,myPointTemp0.qy);
   }
 }
boolean catmulRomBool = false;
int lagrangeLevels;
int deboorLevels;
boolean special = false;
int[] catmulKnots;
boolean close =false;

public void catmulRomCurve(){
  //println("catmulDegree:"+catmulDegree);
   lagrangeLevels = catmulDegree+1;
   deboorLevels = catmulDegree;
   
   ArrayList myPointscat;
   if(special){
     myPointscat = new ArrayList();
     numberOfBatches = 255;
     for(int i=0; i<myPoints.size(); i++){
       myPointscat.add(myPoints.get(i));
     }
     String name = "point"+str(myPoints.size()+1);
     abstractSearchObject myPointTemp0 = (abstractSearchObject)myPoints.get(0);
     myPointscat.add(new Query(name,color(255,255,255),myPointTemp0.qx, myPointTemp0.qy, pointRadius, pointRadius));
   }
   else{
     numberOfBatches = 1;
     myPointscat = new ArrayList();
     for(int i=0; i<myPoints.size(); i++){
       myPointscat.add(myPoints.get(i));
     }
     if(close){
       String name = "point"+str(myPoints.size()+1);
       abstractSearchObject myPointTemp0 = (abstractSearchObject)myPoints.get(0);
       myPointscat.add(new Query(name,color(255,255,255),myPointTemp0.qx, myPointTemp0.qy, pointRadius, pointRadius));
     }
   }
   totalNumberOfPoint= myPointscat.size();
   
   PVector[][] Pstorage = new PVector[lagrangeLevels][];
   float[][] Pstoragex = new float[lagrangeLevels][];
   float[][] Pstoragey = new float[lagrangeLevels][];
   Pstorage[0] = new PVector[totalNumberOfPoint];
   Pstoragex[0] = new float[totalNumberOfPoint];
   Pstoragey[0] = new float[totalNumberOfPoint];
   
   for(int i = 0; i < totalNumberOfPoint ; i++){
    abstractSearchObject myPointTemp0 = (abstractSearchObject)myPointscat.get(i);;
    Pstorage[0][i] = new PVector(myPointTemp0.qx,myPointTemp0.qy);
    Pstoragex[0][i] = myPointTemp0.qx;
    Pstoragey[0][i] = myPointTemp0.qy;
   }
   
   catmulKnots = new int[totalNumberOfPoint];
   for(int i=0; i<totalNumberOfPoint; i++){
     catmulKnots[i] = i;
   }
   
   int draw =0;
   if(catmulDegree>1){
     draw = 2* (catmulDegree -1); 
   }
   
   pointsFound = new PVector[numberOfBatches][];
   pointsFound[0] = new PVector[100*(catmulKnots[totalNumberOfPoint-1-draw])];
   int index = 0;
   for(int n = 1; n<=numberOfBatches; n++){
      pointsFound[n-1] = new PVector[n*100*(catmulKnots[totalNumberOfPoint-1-draw])];
   for(float t=0; t < catmulKnots[totalNumberOfPoint-1]; t += 0.01f*n){
    
     //lagrange part
     
     int subtract=0;
      for(int i = 1; i < lagrangeLevels ; i++){ 
        Pstorage[i] = new PVector[Pstorage[0].length - (subtract+1)];
        Pstoragex[i] = new float[Pstorage[0].length - (subtract+1)];
        Pstoragey[i] = new float[Pstorage[0].length - (subtract+1)];
        //println("i: " +i +", length"+Pstorage[i].length);
        for(int j=0; j < (Pstorage[i].length) ; j++){
          float x1 = (catmulKnots[i+j]-t);
          float x0 = (t-catmulKnots[j]);
          float x10 = (catmulKnots[i+j]-catmulKnots[j]);
         // println("p["+(i-1)+"]["+j+"] *"+"(t"+(i+j)+"-t)");
         // println("p["+(i-1)+"]["+(j+1)+"] *"+"(t - t"+(j)+")");
          Pstoragex[i][j] = (Pstoragex[i-1][j]* x1 + Pstoragex[i-1][j+1] * x0) / x10;
          Pstoragey[i][j] = (Pstoragey[i-1][j]* x1 + Pstoragey[i-1][j+1] * x0) / x10;
          Pstorage[i][j] = new PVector(Pstoragex[i][j],Pstoragey[i][j]);
        }
        subtract += 1;
     }
     
     //deboor part
     
   float sumx =0;
   float sumy =0;
   for(int i=0; i<Pstorage[lagrangeLevels-1].length ; i++){
     double temp = calculateNcatmulrom(i,catmulDegree, t);
     sumx = sumx+ (Pstorage[lagrangeLevels-1][i].x * (float)temp);
     sumy = sumy+ (Pstorage[lagrangeLevels-1][i].y * (float)temp);
   }
   
   if(index >= 100*(catmulKnots[totalNumberOfPoint-1-draw])){
       increaseSizeCatmulRom();
     }
  if(t>=(draw/2) && t<=(totalNumberOfPoint-1-(draw/2))){  
   //println(t); 
    pointsFound[0][index] = new PVector(sumx,sumy);
    index +=1;
  }
  sumx =0;
  sumy =0;
  }
   }
  lagrangeBool = false;
  bezierBool = false;
  bSplineBool = false;
  catmulRomBool = true;

}

public double calculateNcatmulrom(int i, int j, float t){
    double Nij;
    if(j >1){
      double deno1 = (catmulKnots[i+j-1] - catmulKnots[i]);
      double deno2 = (catmulKnots[i+j] - catmulKnots[i+1]);
      double temp1 = 0;
      double temp2 = 0;
      if(deno1 != 0){
         temp1 = ((t - catmulKnots[i])/deno1) * calculateNcatmulrom(i,j-1,t);
      }
      if(deno2 != 0){
         temp2 =((catmulKnots[i+j] - t)/deno2) * calculateNcatmulrom(i+1,j-1,t);
      }
     Nij = temp1 + temp2;
     
    }
    else{
      if( (t>=catmulKnots[i]) && (t < catmulKnots[i+1])){
        Nij = 1;
      }
      else{
        Nij = 0;
      }
    }
    if(Double.isNaN(Nij)){ 
      return 0;
    }
    else{
      return Nij;
    }
}

public void increaseSizeCatmulRom() {
   PVector[] temp = new PVector[pointsFound[0].length + 1];
   for (int i = 0; i < pointsFound[0].length; i++){
      temp[i] = pointsFound[0][i];
   }
   pointsFound[0] = new PVector[pointsFound[0].length + 1];
   pointsFound[0] = temp;
}
abstract class abstractSearchObject{
  String name;
  
  int qc;
  float qx;
  float qy;
  int dQx;
  int dQy;
 
  abstractSearchObject(String name, int tempQc, float tempQx, float tempQy,int tempdQx, int tempdQy) {
    this.name = name;
     qc = tempQc;
     qx = tempQx;
     qy = tempQy;
     dQx = tempdQx;
     dQy = tempdQy;
  }
 
  public void display() {
    stroke(0);
    fill(qc);
    rectMode(RADIUS);
    ellipse(qx,qy,dQx,dQy);
  }
  
  public boolean inQuery(int x, int y){
    if((x > qx-dQx) & x < (qx+dQx)){
      if((y > qy-dQy)  & y < (qy+dQy)){
        return true;
      }
    }
    return false;
  }
  
  public void moveByMouseCoord(int mausX, int mausY){
    this.qx = mausX + dragX;
    this.qy = mausY + dragY;
  }
}


class Query extends abstractSearchObject{

  ArrayList connects = new ArrayList();
  
  
  Query(String name, int tempQc, float tempQx, float tempQy,int tempdQx, int tempdQy) {
    super( name,  tempQc,  tempQx,  tempQy, tempdQx,  tempdQy);
  }
  
  public void display(){
    super.display();  
  }
  
}

public void evaluatePointSelection(abstractSearchObject myPointTemp){ 
  if (myPointTemp.inQuery(mouseX, mouseY) & pointsBeingDragged==null){ 
    dragX = (int)myPointTemp.qx - mouseX;
    dragY = (int)myPointTemp.qy - mouseY;
    pointsBeingDragged = myPointTemp;
  }
}
PVector [][]pointsFound;
boolean lagrangeBool = false;
ArrayList<ArrayList<Query>> outer;
ArrayList<Query> inner;
int numberOfBatches;
int totalNumberOfPoint;

public void findNumberOfBatches() {
  numberOfBatches = (int)(totalNumberOfPoint/(degree+1));
}

public void splitPointsIntoBatches() {
  outer = new ArrayList<ArrayList<Query>>();
  int index = 0;
  for (int i=0; i<numberOfBatches; i++) {
    inner = new ArrayList<Query>(); 
    for (int k=0; k<(degree+1); k++) {
      if (index<=(totalNumberOfPoint-1)) {
        //print(index);
        inner.add((Query)myPoints.get(index));
        index +=1;
      }
    }
    outer.add(inner);
  }
}


public void lagrange() {
  totalNumberOfPoint= myPoints.size();
  findNumberOfBatches();
  splitPointsIntoBatches();

  pointsFound = new PVector[numberOfBatches][];
  for (int n=0; n<numberOfBatches; n++) {
    int difference = degree;
    float sumx,sumy;
    float p;
    float[] fx,fy;
    pointsFound[n] = new PVector[(int)(difference/0.01f)+1];
    int index = 0;
    for (float a = 0; a<=difference; a+= 0.01f) {
      sumx = 0;
      sumy = 0;
      p = a;
      fx = new float[degree+1];
      fy = new float[degree+1];
      for (int i=0; i<(degree+1); i++)
       {
        abstractSearchObject myPointTemp0 = (abstractSearchObject)outer.get(n).get(i);
        float tempx = 1;
        float tempy = 1;
        int k = i;
        
        for (int j=0; j<(degree+1); j++)
        { 
          if (k==j)
          {
            continue;
          } else
          { 
            tempx= tempx * ((p - j)/(i-j));
            tempy = tempy * ((p - j)/(i-j));
          }
        }
        fx[i] = myPointTemp0.qx*tempx;
        fy[i] = myPointTemp0.qy*tempy;
      }

      for (int i=0; i<degree+1; i++)
      {
        sumx += fx[i];
        sumy += fy[i];
        
      }
      for (int i=0; i<degree+1; i++)
      {
        fx[i] = 0;
        fy[i] = 0;
      }
      pointsFound[n][index] = new PVector(sumx, sumy);//new PVector(p, sum);
      index += 1;
      sumx = 0;
      sumy = 0;
    }
    lagrangeBool = true;
    bezierBool = false;
    catmulRomBool = false;
    bSplineBool = false;
    activeCurve = 1;
  }
}
public void updateGUI(){
  if(lagrangeBool || bezierBool || bSplineBool){
      label_deg.setText("Degree");
      label_deg.setOpaque(false);
      label_deg_val.setText(str(degree));
    }
    else if(catmulRomBool){
     label_deg.setText("Smoothness");
     label_deg.setOpaque(false);
     label_deg_val.setText(str(catmulDegree));
    }
    else{
     label_deg.setText("");
     label_deg.setOpaque(false);
     label_deg_val.setText("");
    }
    
     switch (activeCurve) {
    case 1:
      label_cur_val.setText("Lagrange");
      break;
    case 2:
      label_cur_val.setText("Bezier");
      break;
    case 3:
      label_cur_val.setText("B Spline");
      break;
    case 4:
      label_cur_val.setText("Catmull-Rom");
      break;
    case 5:
      label_cur_val.setText("Catmull-Rom -Special");
      break;
    default:             
     label_cur_val.setText("No Curve Selected");   
      break;
     }
      
}
    
public void staticTextGUI(){
  String inst = "INSTRUCTIONS \n\n Click on the screen to add points \n press 'e' key to edit added points \n\n Press '1' key for the program to draw Lagrange Curve \n\n Press '2' key for the program to draw Bezier Curve \n\n Press 'i' key for the program to find intersections between the Bezier Curves \n\n Press '3' key for the program to draw B-Spline Curve \n\n Press '4' key for the program to draw Catmull-Rom Curve";
 label1.setText(inst); 
 button_reset.addEventHandler(this, "handleButtonEvents");
}

public void handleButtonEvents(GButton button, GEvent event) { 
  
  if (button == button_reset){
      reset();
  }
}
/*import java.util.ArrayList;
 
ArrayList myQueries;

abstractSearchObject queryBeingDragged;


int dragX;
int dragY;
 
void setup()
{
  queryBeingDragged = null;
  myQueries = new ArrayList();
  
  size(800, 500);
  smooth();
  
  Hit h1 = new Hit("myQuery1",color(0,255, 0),width/2.0+100,height/5.0+50,100,20);
  Query q1 =  new Query("myQuery1",color(0,0,255),width/2.0+100,height/5.0+100,100,20);
  Query q2 =  new Query("myQuery2", color(255,0,0),width/2.0,4.0*height/5.0,100,20);
  
  
  
  //q1.hit = h1; 
  //myConnect1 = new Connect(color(255,0,0),width/2.0,height/5.0,width/2.0,4.0*height/5.0,+32 ,-57);
  q1.addHitLink("myConnect1", color(255,0,0), h1,+32 ,-57);
  //myConnect2 = new Connect(color(255,255,50),width/2.0,height/5.0,width/2.0,4.0*height/5.0, +30,+50);
  q1.addHitLink("myConnect2", color(255,255,50), h1, +30,+50);
  //myConnect3 = new Connect(color(0,255,50),width/2.0,height/5.0,width/2.0,4.0*height/5.0,-64,+59);
  q1.addHitLink("myConnect1", color(0, 255,50), h1,-64,+59);
  
  
  myQueries.add(h1 );
  myQueries.add(q1 );
  myQueries.add(q2 );
  
}
 
void draw()
{
  background(0);
  
  for(int i = 0; i < myQueries.size(); i++){
    abstractSearchObject myQuery1 = (abstractSearchObject)myQueries.get(i);
    myQuery1.display();
  }
}
 
 
 
void mousePressed(){
  for(int i = 0; i < myQueries.size(); i++){ 
     // note how I made it generic 
    abstractSearchObject myQuery1 = (abstractSearchObject)myQueries.get(i);
    evaluateQuerySelection(myQuery1);
  }
  println("pressed");
  
}

void mouseReleased(){
  queryBeingDragged = null; 
} 
 
void mouseDragged(){
  if( queryBeingDragged != null){
     println("dragging" + queryBeingDragged.name);
    queryBeingDragged.moveByMouseCoord(mouseX, mouseY);
   }
}  




void evaluateQuerySelection(abstractSearchObject myQuery1){ 
  if (myQuery1.inQuery(mouseX, mouseY) & queryBeingDragged==null){ 
    dragX = (int)myQuery1.qx - mouseX;
    dragY = (int)myQuery1.qy - mouseY;
    queryBeingDragged = myQuery1;
  }
}



// This is how to use inheritance.  Use interface instead if you want even more flexibility.
abstract class abstractSearchObject{
  String name;
  
  color qc;
  float qx;
  float qy;
  int dQx;
  int dQy;
 
  abstractSearchObject(String name, color tempQc, float tempQx, float tempQy,int tempdQx, int tempdQy) {
    this.name = name;
     qc = tempQc;
     qx = tempQx;
     qy = tempQy;
     dQx = tempdQx;
     dQy = tempdQy;
  }
 
  void display() {
    stroke(0);
    fill(qc);
    rectMode(RADIUS);
    rect(qx,qy,dQx,dQy);
  }
  
  boolean inQuery(int x, int y){
    if((x > qx-dQx) & x < (qx+dQx)){
  if((y > qy-dQy)  & y < (qy+dQy)){
    
    return true;
  }
    }
    return false;
  }
  
  void moveByMouseCoord(int mausX, int mausY){
    this.qx = mausX + dragX;
    this.qy = mausY + dragY;
  }
}



class Query extends abstractSearchObject{
  
  //Hit hit = null;
  ArrayList connects = new ArrayList();
  
  
  Query(String name, color tempQc, float tempQx, float tempQy,int tempdQx, int tempdQy) {
    super( name,  tempQc,  tempQx,  tempQy, tempdQx,  tempdQy);
  }
  
  void display(){
    super.display();  // see how I'm calling the parent class's draw routine
    
    for(int i=0; i < connects.size(); i++){
  Connect connect = (Connect)connects.get(i);
  connect.display();
    }
  }
  
  
  void addHitLink(String connectName, color cc, Hit hit, int connector_qry_offset, int connector_hit_offset ){
    Connect connect = new Connect(connectName, cc,  this, hit, connector_qry_offset, connector_hit_offset);
    connects.add(connect);
  }
  
}


class Hit extends abstractSearchObject{
  
  Hit(String name, color tempQc, float tempQx, float tempQy,int tempdQx, int tempdQy) {
    super( name,  tempQc,  tempQx,  tempQy, tempdQx,  tempdQy);
  }
  
}


//Connector Class Creation
class Connect {
  color cc;
  
  String name;
  
  Query query = null;
  Hit hit = null;
  //float qx;
  //...
 // offsets to define "alignment mapping" line
  int connector_qry_offset;
  int connector_hit_offset;
 
 
  Connect(String name, color tempCc, Query query, Hit hit, int tempconnector_qry_offset, int tempconnector_hit_offset) {
    this.name = name;
    cc = tempCc;
    this.query = query;
    this.hit = hit;
    
   //Note how hit and query replaces the lines below - the objects already contain the data
   //qx = tempQx;
   //...
   connector_qry_offset = tempconnector_qry_offset;
   connector_hit_offset = tempconnector_hit_offset;
  }
 
  void display() {
    stroke(cc);
    // this is where the data from query and hit objects are used.
    line(query.qx + connector_qry_offset, query.qy, hit.qx + connector_hit_offset, hit.qy);
  }
}
*/
/* =========================================================
 * ====                   WARNING                        ===
 * =========================================================
 * The code in this tab has been generated from the GUI form
 * designer and care should be taken when editing this file.
 * Only add/edit code inside the event handlers i.e. only
 * use lines between the matching comment tags. e.g.

 void myBtnEvents(GButton button) { //_CODE_:button1:12356:
     // It is safe to enter your event code here  
 } //_CODE_:button1:12356:
 
 * Do not rename this tab!
 * =========================================================
 */

synchronized public void win_draw2(GWinApplet appc, GWinData data) { //_CODE_:window1:523778:
  appc.background(230);
} //_CODE_:window1:523778:



// Create all the GUI controls. 
// autogenerated do not edit
public void createGUI(){
  G4P.messagesEnabled(false);
  G4P.setGlobalColorScheme(GCScheme.PURPLE_SCHEME);
  G4P.setCursor(ARROW);
  if(frame != null)
    frame.setTitle("Main Window");
  window1 = new GWindow(this, "GUI Window", 0, 0, 260, 510, false, JAVA2D);
  window1.setActionOnClose(G4P.EXIT_APP);
  window1.addDrawHandler(this, "win_draw2");
  label1 = new GLabel(window1.papplet, 9, 9, 244, 253);
  label1.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
  label1.setText(" ");
  label1.setLocalColorScheme(GCScheme.BLUE_SCHEME);
  label1.setOpaque(false);
  label_deg = new GLabel(window1.papplet, 10, 335, 108, 25);
  label_deg.setText(" ");
  label_deg.setTextBold();
  label_deg.setLocalColorScheme(GCScheme.BLUE_SCHEME);
  label_deg.setOpaque(false);
  label_deg_val = new GLabel(window1.papplet, 131, 340, 80, 20);
  label_deg_val.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
  label_deg_val.setText(" ");
  label_deg_val.setLocalColorScheme(GCScheme.BLUE_SCHEME);
  label_deg_val.setOpaque(false);
  label_cur = new GLabel(window1.papplet, 10, 300, 106, 22);
  label_cur.setText("CURVE :");
  label_cur.setTextBold();
  label_cur.setLocalColorScheme(GCScheme.BLUE_SCHEME);
  label_cur.setOpaque(false);
  label_cur_val = new GLabel(window1.papplet, 130, 295, 124, 26);
  label_cur_val.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
  label_cur_val.setText("No Curve Selected");
  label_cur_val.setLocalColorScheme(GCScheme.BLUE_SCHEME);
  label_cur_val.setOpaque(false);
  button_reset = new GButton(window1.papplet, 10, 371, 80, 30);
  button_reset.setText("RESET");
  button_reset.setTextBold();
  label_spc = new GLabel(window1.papplet, 8, 269, 234, 23);
  label_spc.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
  label_spc.setText("Press 's' to see special curve function");
  label_spc.setTextBold();
  label_spc.setOpaque(false);
}

// Variable declarations 
// autogenerated do not edit
GWindow window1;
GLabel label1; 
GLabel label_deg; 
GLabel label_deg_val; 
GLabel label_cur; 
GLabel label_cur_val; 
GButton button_reset; 
GLabel label_spc; 

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "curves_assignment_1" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
