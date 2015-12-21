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

 void bezierCurve(){
   totalNumberOfPoint= myPoints.size();
   findNumberOfBatches();
   splitPointsIntoBatches();
   
   pointsFound = new PVector[numberOfBatches][];
//  print("numberOfBatches:"+numberOfBatches+"\n");
  for (int n=0; n<numberOfBatches; n++) {
   pointsFound[n] = new PVector[200];
   int index = 0;
   for(float t =0.005; t<1; t+=0.005){
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
 
 void bezierCurve2(){
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
     for(float t = 0; t <= 1.01 ; t += 0.01){
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
void checkBoxIntersections(){
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
 
 boolean DoBoxesIntersect(PVector mina, PVector maxa, PVector minb, PVector maxb) {
  return (abs(mina.x - minb.x) * 2 < ((maxa.x-mina.x) + (maxb.x-minb.x))) &&
         (abs(mina.y - minb.y) * 2 < ((maxa.y-mina.y) + (maxb.y-minb.y)));
}
 
 void storeValue(){
   for(int i=0; i <numberOfBatches ; i++){
      abstractSearchObject myPointTemp0 = (abstractSearchObject)outer.get(i).get(0);
      maxBoundingBox[i][0] = new PVector(myPointTemp0.qx,myPointTemp0.qy);
      minBoundingBox[i][0] = new PVector(myPointTemp0.qx,myPointTemp0.qy);
   }
 }
