/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flotmaxproject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Lido
 */
public class Arcs {
    
    DoubleProperty x1 = new SimpleDoubleProperty(),
                   dx = new SimpleDoubleProperty(),
                   dy = new SimpleDoubleProperty(),
                   x  = new SimpleDoubleProperty(),
                   y  = new SimpleDoubleProperty(),
                   z  = new SimpleDoubleProperty(),
                   t  = new SimpleDoubleProperty(),
                   theta = new SimpleDoubleProperty(),
                   rho = new SimpleDoubleProperty(),
                   rho1 = new SimpleDoubleProperty(),
                   x2 = new SimpleDoubleProperty(),
                   y1 = new SimpleDoubleProperty(),
                   y2 = new SimpleDoubleProperty();
    Line line1, head1, head2;;
    Polygon poly;
    double capacite;
    double          phi         = Math.toRadians( 30 );
    double          barb        = 7;
    Sommet source;
    Sommet cible;
    String nom;
    Label valeur;
    Pane conteneur;
    Line line;
    ContextMenu popup = new ContextMenu();
    MenuItem supprimmer = new MenuItem("Supprimmer");
    TextField txt = new TextField();
    guiController gui = new guiController();
    AlgoFordFulkerson algo = new AlgoFordFulkerson();
    HashMap<Arcs,String> map_arc = new HashMap<>();
    Tableau table;
    private int index;

    public Arcs(AlgoFordFulkerson algo,HashMap<Arcs,String> map_arc) {
        txt.setPrefWidth(30);
        txt.setVisible(false);
        //this.algo=algo;
        this.map_arc=map_arc;
        this.popup.getItems().addAll(supprimmer);
            this.supprimmer.setOnAction(new EventHandler<ActionEvent>() {
               
                @Override
                public void handle(ActionEvent t) {
                 supprimer_arc();
                }

            });

    }
    public Arcs() {
        txt.setPrefWidth(30);
        txt.setVisible(false);
    }
    
    public void set_source(Sommet source) {

        this.source = source;
    }

    public void set_cible(Sommet cible) {
        this.cible = cible;
    }

    @SuppressWarnings("empty-statement")
    public Arcs setarc(final String s, String c, double val,AlgoFordFulkerson algo,final Tableau table) {
        
        this.table=table;
        Iterator map_iter;
        Set map_set ;
        capacite = val;
        this.algo=algo;
        this.algo.addEdge(s, c, val, 0);
        this.nom = s.toString() + c.toString();
        this.valeur = new Label(val+"");
        line = new Line();
        this.valeur.setContextMenu(popup);
        head1 = new Line(  );
        head2 = new Line(  );
        line.setStroke(Color.DODGERBLUE);
        line.setStrokeWidth(1);
        head1.setStroke(Color.DODGERBLUE);
        head1.setStrokeWidth(2);
        head2.setStroke(Color.DODGERBLUE);
        head2.setStrokeWidth(2);
       
        final Label tmp_source, tmp_cible;
        tmp_source = this.source.get_sommet();
        tmp_cible = this.cible.get_sommet();
        
        /*line.setStartX(tmp_source.getLayoutX() + 15);
        line.setStartY(tmp_source.getLayoutY() + 15);
        line.setEndX(tmp_cible.getLayoutX() + 15);
        line.setEndY(tmp_cible.getLayoutY() + 15);*/
        
        
        line.startXProperty().bind(tmp_source.layoutXProperty().add(15));
        line.startYProperty().bind(tmp_source.layoutYProperty().add(15));
        line.endXProperty().bind(tmp_cible.layoutXProperty().add(15));
        line.endYProperty().bind(tmp_cible.layoutYProperty().add(15));
        
        /// Création de flèche binding
        x1.bind(line.startXProperty());
        y1.bind(line.startYProperty());
        x2.bind((line.startXProperty().add(line.endXProperty().multiply(3))).divide(4));
        y2.bind((line.startYProperty().add(line.endYProperty().multiply(3))).divide(4));
        dx.bind(x2.subtract(x1));
        dy.bind(y2.subtract(y1));
        
        theta.bind(new DoubleBinding() {
            {
                bind(dx);
                bind(dy);
            }
            @Override
            protected double computeValue() {
                return Math.atan2( dy.doubleValue(), dx.doubleValue() );        
            }
            }); 
        rho.bind(theta.add(phi));
        x.bind(new DoubleBinding() {
            {
                bind(x2);
                bind(rho);
            }
            @Override
            protected double computeValue() {
                return x2.doubleValue() - barb * Math.cos( rho.doubleValue() );        
            }
            });
        y.bind(new DoubleBinding() {
            {
                bind(y2);
                bind(rho);
            }
            @Override
            protected double computeValue() {
                return y2.doubleValue() - barb * Math.sin( rho.doubleValue() );        
            }
            });
        head1.startXProperty().bind(x2);
        head1.startYProperty().bind(y2);
        head1.endXProperty().bind(x);
        head1.endYProperty().bind(y);
        rho1.bind(theta.subtract(phi));
        z.bind(new DoubleBinding() {
            {
                bind(x2);
                bind(rho1);
            }
            @Override
            protected double computeValue() {
                return x2.doubleValue() - barb * Math.cos( rho1.doubleValue() );        
            }
            });
        t.bind(new DoubleBinding() {
            {
                bind(y2);
                bind(rho);
            }
            @Override
            protected double computeValue() {
                return y2.doubleValue() - barb * Math.sin( rho1.doubleValue() );        
            }
            });
        head2.startXProperty().bind(x2);
        head2.startYProperty().bind(y2);
        head2.endXProperty().bind(z);
        head2.endYProperty().bind(t);
        // Fin de Création de flèche
        
        //poly.setFill(Color.DODGERBLUE);
        this.valeur.layoutXProperty().bind((line.startXProperty().add(line.endXProperty().multiply(3))).divide(4));
        this.valeur.layoutYProperty().bind((line.startYProperty().add(line.endYProperty().multiply(3))).divide(4));
        
        this.valeur.setCursor(Cursor.HAND);
        this.valeur.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    txt.layoutXProperty().bind((line.startXProperty().add(line.endXProperty().multiply(3))).divide(4));
                    txt.layoutYProperty().bind((line.startYProperty().add(line.endYProperty().multiply(3))).divide(4));
                    txt.setVisible(true);
                    txt.setText(valeur.getText());
                    txt.selectAll();
                    txt.toFront();
                    
                }
            }
        });
        this.txt.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if(t.getCode()==KeyCode.ESCAPE){
                    System.out.println("Enter");
                }
                    /*update_valeur_arc(source.get_sommet().getText().toString()
                                     ,cible.get_sommet().getText().toString(),
                                     capacite,Double.parseDouble(txt.getText().toString()));*/
                    //txt.setVisible(false);
               
            }
        });
        this.txt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                
                    update_valeur_arc(source.get_sommet().getText().toString()
                                     ,cible.get_sommet().getText().toString(),
                                     capacite,Double.parseDouble(txt.getText().toString()));
                    txt.setVisible(false);
                    
               
            }
        });
        this.valeur.setStyle("-fx-font: bold 11px \"Arial\";-fx-text-fill: black;");
        line.toBack();
        return this;
        
    }

    public Line get_arc() {
        return this.line;
    }
    public Arcs get_arc(String source, String cible) {
        if(this.get_source().get_sommet().getText().equals(source)&&
                this.get_cible().get_sommet().getText().equals(cible)){
        return this;
        }
        else {return null;}
    }
    public Sommet get_source() {
        return this.source;
    }

    public Sommet get_cible() {
        return this.cible;
    }

    public void set_conteneur(Pane conteneur) {
        this.conteneur = conteneur;
        this.conteneur.getChildren().addAll(line ,head1, head2, valeur,txt);
        
    }

    public String get_nom() {
        return this.nom;
    }
    public void Setindex(int index){
        this.index=index;
    }
    public int Getindex(){
        return index;
    }
    public void supprimer_arc() {
        try{
            this.algo.delEdge(this.source.get_sommet().getText().toString()
                    , this.cible.get_sommet().getText().toString(),capacite,this.source.get_sommet().getText().toString()+this.cible.get_sommet().getText().toString());
            this.table.sup_data(this);
            this.conteneur.getChildren().removeAll(line,txt,valeur,head1,head2);
        }
        catch(Exception e){
        
            System.out.println("Suppression skipper!!");
        }
    }
    public void initialize(){
    
        this.algo.initialize(this);
    }
    final public void update_valeur_arc(String source, String cible, double ancien,double nouveau) {
        
        this.algo.updateEdge(source, cible, ancien, nouveau);
        this.valeur.setText("" + nouveau + "");
        this.table.modif_data(this,this.Getindex());

    }
}
