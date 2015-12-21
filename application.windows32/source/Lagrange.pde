PVector [][]pointsFound;
boolean lagrangeBool = false;
ArrayList<ArrayList<Query>> outer;
ArrayList<Query> inner;
int numberOfBatches;
int totalNumberOfPoint;

void findNumberOfBatches() {
  numberOfBatches = (int)(totalNumberOfPoint/(degree+1));
}

void splitPointsIntoBatches() {
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


void lagrange() {
  totalNumberOfPoint= myPoints.size();
  findNumberOfBatches();
  splitPointsIntoBatches();

  pointsFound = new PVector[numberOfBatches][];
  for (int n=0; n<numberOfBatches; n++) {
    int difference = degree;
    float sumx,sumy;
    float p;
    float[] fx,fy;
    pointsFound[n] = new PVector[(int)(difference/0.01)+1];
    int index = 0;
    for (float a = 0; a<=difference; a+= 0.01) {
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
