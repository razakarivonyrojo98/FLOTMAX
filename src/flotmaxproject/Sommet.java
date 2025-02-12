/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flotmaxproject;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

/**
 *
 * @author Lido
 */
public class Sommet {
      
    
    double initX;
    double initY;
    Point2D dragAnchor;
    ArrayList<Arcs> arcs_entrants = new ArrayList();
    ArrayList<Arcs> arcs_sortants = new ArrayList();;
    Pane conteneur;
    String nom;
    Label label;
    ContextMenu popup = new ContextMenu();
    MenuItem supprimmer = new MenuItem("Supprimmer");
    public Sommet(){
            this.popup.getItems().addAll(supprimmer);
            this.supprimmer.setOnAction(new EventHandler<ActionEvent>() {
               
                @Override
                public void handle(ActionEvent t) {
                 supprimer_sommet();
                }
            });
          }
      
    public void set_sommet(final String nom_sommet) {
        
        Label lbl_sommet = new Label(nom_sommet.toUpperCase());
      
        this.nom=nom_sommet;
        this.label=lbl_sommet;
      
        label.setLayoutX(Math.random()*350);
        label.setLayoutY(Math.random()*200);
        label.setCursor(Cursor.HAND);
        label.setContextMenu(popup);
       
        final Circle circle = new Circle(15, new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.REFLECT, new Stop[] {
                new Stop(0, Color.rgb(255,255,255)),
                new Stop(1, Color.  BLUE)
            }));
            label.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    double dragX = me.getSceneX() - dragAnchor.getX();
                    double dragY = me.getSceneY() - dragAnchor.getY();

                    double newXPosition = initX + dragX;
                    double newYPosition = initY + dragY;
                    int j=0;
                    double x1,y1,x2,y2;
                    double phi  = Math.toRadians( 30 );
                    double barb = 10;
                    
                     if ((newXPosition>=label.getTranslateX()) && (newXPosition<=720-label.getTranslateX())) {
                        label.setLayoutX(newXPosition);
                        
                    }
                       
                    if ((newYPosition>=label.getTranslateY()) && (newYPosition<=325-label.getTranslateY())){
                        label.setLayoutY(newYPosition);
                        
                    }
                }
            });
             circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    //change the z-coordinate of the circle
                    label.toFront();

                }
            });
            label.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                     //when mouse is pressed, store initial position
                    initX = label.getLayoutX();
                    initY = label.getLayoutY();
                    dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
                }
            });
            label.setGraphic(circle);
            label.setContentDisplay(ContentDisplay.CENTER);
            label.setStyle("-fx-font: bold 14px \"Arial\";; -fx-text-fill: white;");
            label.toFront();
      
    }
    public Label get_sommet() {
       
        return this.label;
      
    }
    
    public void set_conteneur(Pane conteneur) {
       this.conteneur=conteneur;
       this.conteneur.getChildren().add(label);  
    }
    
    public void set_arcs_entrants(Arcs arcs){
        
        this.arcs_entrants.add(arcs);
        
    }
    public void set_arcs_sortants(Arcs arcs){
        
        this.arcs_sortants.add(arcs);
        
    }
    public List<Arcs> get_arcs_entrants(){
                
        return this.arcs_entrants;
    }
    public List<Arcs> get_arcs_sortants(){
                
        return this.arcs_sortants;
    }
    
    final public void supprimer_sommet(){
        
        int j=0;
        while(!(arcs_entrants.isEmpty())&&(j<arcs_entrants.size())){
                try{
                this.conteneur.getChildren().removeAll(arcs_entrants.get(j).get_arc(),
                        arcs_entrants.get(j).valeur,arcs_entrants.get(j).head1,arcs_entrants.get(j).head2);
                arcs_entrants.get(j).algo.delEdge(arcs_entrants.get(j).get_source().get_sommet().getText().toString()
                , arcs_entrants.get(j).get_cible().get_sommet().getText().toString(),
                arcs_entrants.get(j).capacite,arcs_entrants.get(j).get_source().get_sommet().getText().toString()+arcs_entrants.get(j).get_cible().get_sommet().getText().toString());
                arcs_entrants.get(j).table.sup_data(arcs_entrants.get(j));
                }
                catch(Exception e){
                
                    arcs_entrants.get(j).supprimer_arc();
                }
                j++;
        }
        j=0;
        while(!(arcs_sortants.isEmpty())&&(j<arcs_sortants.size())){
                try{
                this.conteneur.getChildren().removeAll(arcs_sortants.get(j).get_arc(),
                        arcs_sortants.get(j).valeur,arcs_sortants.get(j).head1,arcs_sortants.get(j).head2);
                arcs_sortants.get(j).algo.delEdge(arcs_sortants.get(j).get_source().get_sommet().getText().toString()
                , arcs_sortants.get(j).get_cible().get_sommet().getText().toString(),
                arcs_sortants.get(j).capacite,arcs_sortants.get(j).get_source().get_sommet().getText().toString()
                +arcs_sortants.get(j).get_cible().get_sommet().getText().toString());
                arcs_sortants.get(j).table.sup_data(arcs_sortants.get(j));
            
                }
                catch(Exception e){
                
                    arcs_sortants.get(j).supprimer_arc();
                }
                j++;
        }
        j=0;
        this.conteneur.getChildren().remove(label);
        label = null;
        
    }
   public ContextMenu get_menu(){
        return this.popup;
   
   }
}
