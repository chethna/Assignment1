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