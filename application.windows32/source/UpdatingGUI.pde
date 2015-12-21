void updateGUI(){
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
    
void staticTextGUI(){
  String inst = "INSTRUCTIONS \n\n Click on the screen to add points \n press 'e' key to edit added points \n\n Press '1' key for the program to draw Lagrange Curve \n\n Press '2' key for the program to draw Bezier Curve \n\n Press 'i' key for the program to find intersections between the Bezier Curves \n\n Press '3' key for the program to draw B-Spline Curve \n\n Press '4' key for the program to draw Catmull-Rom Curve";
 label1.setText(inst); 
 button_reset.addEventHandler(this, "handleButtonEvents");
}

public void handleButtonEvents(GButton button, GEvent event) { 
  
  if (button == button_reset){
      reset();
  }
}
