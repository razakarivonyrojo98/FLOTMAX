/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flotmaxproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 *
 * @author Lido
 */
public class guiController implements Initializable {
    
    @FXML
    private Slider slider;
    //Les Panes
    @FXML
    private Pane contenu_graph;
    @FXML
    private Pane nouveau_sommet_pane;
    @FXML
    private Pane nouveau_arc_pane;
    @FXML
    private Pane legend_pane;
    
    //Les Menu
   
    @FXML
    private MenuBar menu_bar;//la bar de menu
    @FXML
    private Menu menu;//Fichier, Edition, Aide
    @FXML
    private MenuItem fichier_fermer;
    @FXML
    private MenuItem fichier_ouvrir;
    @FXML
    private MenuItem fichier_nouveau_graphe;
    @FXML
    private MenuItem fichier_enregistrer;
    @FXML
    private MenuItem fichier_reinit_graphe;
    @FXML
    private MenuItem edition_nouveau_sommet;
    @FXML
    private MenuItem edition_nouveau_arc;
    @FXML
    private MenuItem aide_apropos;
    @FXML
    private MenuItem popup_nouveau_sommet;
    @FXML
    private MenuItem popup_nouveau_arc;
    @FXML
    private MenuItem popup_nsupprimertout;
    //Les Labels
    @FXML
    private Label label;
    @FXML
    private Label labelgraphics;
    @FXML
    private Label erreur_msg;
    
    
    //Les TextFields
    @FXML
    private TextField nom_sommet;
    @FXML
    private TextField valeur_arc;
    @FXML
    private TextField txtcible;
    @FXML
    private TextField txtsource;
    @FXML
    private TextField txtvalue;
    
    //Les Buttons
    @FXML
    private Button creer_sommet;
    @FXML
    private Button creer_arc;
    @FXML
    private Button plus;
    
    //Les ChoiceBoxes
    @FXML
    private ChoiceBox debut_arc;
    @FXML
    private ChoiceBox fin_arc;
    @FXML
    private ChoiceBox sommet_source;
    @FXML
    private ChoiceBox sommet_cible;
    
    @FXML
    private TableView tableau;
    @FXML
    private TableColumn col_source ;
    @FXML
    private TableColumn col_cible ;
    @FXML
    private TableColumn col_capacite ;
    // Les objets non FXML
    
    private ContextMenu popup = new ContextMenu();
    private MenuItem supprimer_arc = new MenuItem("Supprimer");
    
    private Point2D dragAnchor;
    private double initX,initY;
    Sommet sommet;
    HashMap<Sommet,String> map_sommet = new HashMap<>();
    Iterator map_iter;
    Set map_set ;
    Arcs arc;
    HashMap<Arcs,String> map_arc = new HashMap<>();
    Tableau table= new Tableau();
    AlgoFordFulkerson algo = new AlgoFordFulkerson();;
    
    FileChooser fileChooser = new FileChooser();
    File file = new File("E:\\TEST.LIDO") ;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        
        try{
        
        label.setText(""+algo.maxFlow(sommet_source.getValue().toString(), sommet_cible.getValue().toString()));
        map_set=map_arc.entrySet();
        map_iter=map_set.iterator();
        while(map_iter.hasNext()){
        
            Map.Entry map_entry = (Map.Entry)map_iter.next();
            algo.labelflow((Arcs)map_entry.getKey());
        }
        //labelgraphics.setMouseTransparent(true);
        System.out.println(algo.flow);
        fichier_reinit_graphe.setDisable(false);
        popup_nsupprimertout.setDisable(false);
        legend_pane.setVisible(true);
    }
    
        catch(Exception e){
            System.out.println("Une erreur est survenue");
            label.setText("0.0");
    }
    }
    @FXML
    private void menuNouveau_SommetAction(ActionEvent event) {
   
        nouveau_sommet_pane.setVisible(true);
        nom_sommet.clear();
        
    }
    @FXML
    private void menuReinitialiser_Action(ActionEvent event) {
        
        map_set=map_arc.entrySet();
        map_iter=map_set.iterator();
        while(map_iter.hasNext()){
        
            Map.Entry map_entry = (Map.Entry)map_iter.next();
            Arcs arcs = (Arcs)map_entry.getKey();
            arcs.initialize();
        }
        algo.initialize();
        label.setText(null);
        fichier_reinit_graphe.setDisable(true);
        popup_nsupprimertout.setDisable(true);
        System.out.println("Menu reinitialiser realisé");
        
    }
    @FXML
    private void menuNouveau_GrapheAction(ActionEvent event) {
   
      algo.clear();
      label.setText(null);
      table.tableau.getItems().clear();
      table.initialiseindex();
      sommet_source.getItems().clear();
      sommet_cible.getItems().clear();
      debut_arc.getItems().clear();
      fin_arc.getItems().clear();
      map_set=map_arc.entrySet();
      map_iter=map_set.iterator();
        while(map_iter.hasNext()){

            Map.Entry map_entry = (Map.Entry)map_iter.next();
            
            Arcs arcs = (Arcs)map_entry.getKey();
            
                arcs.supprimer_arc();
          

        }
      map_set=map_sommet.entrySet();
      map_iter=map_set.iterator();
        while(map_iter.hasNext()){

            Map.Entry map_entry = (Map.Entry)map_iter.next();
            
            Sommet smt = (Sommet)map_entry.getKey();
            
            smt.supprimer_sommet();
        }
      map_sommet.clear();
      map_arc.clear();
      legend_pane.setVisible(false);
      //contenu_graph.getChildren().clear();
        
        
    }
    @FXML
    private void menuEnregistrerAction(ActionEvent event) throws IOException {
        fileChooser.setTitle("Enregistrer le graphe .lido");
        fileChooser.getExtensionFilters().add(
         new ExtensionFilter("Lido Files", "*.lido"));
        file.createNewFile();
        try (FileOutputStream fileout = new FileOutputStream(file)) {
            fileout.write(1);
            fileout.close();
        }
        
        file = fileChooser.showSaveDialog(popup);
        
      
    }
    @FXML
    private void menuOuvrirAction(ActionEvent event) throws IOException {
        fileChooser.setTitle("Ouvrir le graphe .lido");
        fileChooser.getExtensionFilters().add(
         new ExtensionFilter("Lido Files", "*.lido"));
        //file.createNewFile();
        
        file = fileChooser.showOpenDialog(popup);
        FileInputStream filein = new FileInputStream(file);
        char a = (char) filein.read();
        
        while(!"/".equals(String.valueOf(a))){
        
            System.out.print(a);
            sommet = new Sommet();
            sommet.set_sommet(String.valueOf(a).toString());
            sommet_source.getItems().add(sommet.get_sommet().getText().toString());
            sommet_cible.getItems().add(sommet.get_sommet().getText().toString());
            debut_arc.getItems().add(sommet.get_sommet().getText().toString());
            fin_arc.getItems().add(sommet.get_sommet().getText().toString());
            map_sommet.put(sommet, sommet.get_sommet().getText().toString());
            sommet.set_conteneur(contenu_graph);
            a = (char) filein.read();
            
        }
        System.out.println("\nFin Sommets");
        char e = (char) filein.read();
        char b = (char) filein.read();
        while(!"/".equals(String.valueOf(e))){
        
            System.out.print(e);
            arc= new Arcs(algo,map_arc);
            map_set=map_sommet.entrySet();
            map_iter=map_set.iterator();
            while(map_iter.hasNext()){

                Map.Entry map_entry = (Map.Entry)map_iter.next();
                if(String.valueOf(e).toString().equals(map_entry.getValue())){

                    arc.set_source((Sommet)map_entry.getKey());
                }
                   
                
                if(String.valueOf(b).toString().equals(map_entry.getValue())){

                    arc.set_cible((Sommet)map_entry.getKey());
                }
                
            }
            char trait = (char)filein.read(); /// le trait
            char c = (char) filein.read();
            System.out.print(b);
            System.out.print(trait);
            double d;
            String str=new String();
            while(!";".equals(String.valueOf(c))){
        
            System.out.print(c);
            str +=String.valueOf(c);
            c = (char) filein.read();
            }
            //System.out.println(str);
            d = Double.parseDouble(str);
            debut_arc.setValue(String.valueOf(e).toString());
            fin_arc.setValue(String.valueOf(b).toString());
            arc.setarc(debut_arc.getValue().toString(), fin_arc.getValue().toString(), d,algo,table);
            arc.get_source().set_arcs_sortants(arc);
            arc.get_cible().set_arcs_entrants(arc);
            arc.set_conteneur(contenu_graph);
            arc.get_arc().toBack();
            map_arc.put(arc, arc.get_nom());
            table.ajout_data(arc);
            e = (char) filein.read();
            if(!"/".equals(String.valueOf(e)))
                b = (char) filein.read();
            System.out.println(arc.get_nom()+" ....  "+arc.get_nom().charAt(0));
        }
       
      
    }
     @FXML
    private void menuPlusAction(ActionEvent event) {
        
        if(!map_sommet.containsValue(txtsource.getText().toString().toUpperCase())){
        
            sommet = new Sommet();
            sommet.set_sommet(txtsource.getText().toString().toUpperCase());
            sommet_source.getItems().add(sommet.get_sommet().getText().toString());
            sommet_cible.getItems().add(sommet.get_sommet().getText().toString());
            debut_arc.getItems().add(sommet.get_sommet().getText().toString());
            fin_arc.getItems().add(sommet.get_sommet().getText().toString());
            map_sommet.put(sommet, sommet.get_sommet().getText().toString());
            sommet.set_conteneur(contenu_graph);
        }
        if(!map_sommet.containsValue(txtcible.getText().toString().toUpperCase())){
        
            sommet = new Sommet();
            sommet.set_sommet(txtcible.getText().toString().toUpperCase());
            sommet_source.getItems().add(sommet.get_sommet().getText().toString());
            sommet_cible.getItems().add(sommet.get_sommet().getText().toString());
            debut_arc.getItems().add(sommet.get_sommet().getText().toString());
            fin_arc.getItems().add(sommet.get_sommet().getText().toString());
            map_sommet.put(sommet, sommet.get_sommet().getText().toString());
            sommet.set_conteneur(contenu_graph);
        }
        arc= new Arcs(algo,map_arc);
        map_set=map_sommet.entrySet();
        map_iter=map_set.iterator();
        while(map_iter.hasNext()){
        
            Map.Entry map_entry = (Map.Entry)map_iter.next();
            if(txtsource.getText().toString().equals(map_entry.getValue())){
            
                arc.set_source((Sommet)map_entry.getKey());
            }
            if(txtcible.getText().toString().equals(map_entry.getValue())){
            
                arc.set_cible((Sommet)map_entry.getKey());
            }
        }
        double d =Double.parseDouble(txtvalue.getText().toString());
        debut_arc.setValue(txtsource.getText());
        fin_arc.setValue(txtcible.getText());
        arc.setarc(debut_arc.getValue().toString(), fin_arc.getValue().toString(), d,algo,table);
        arc.get_source().set_arcs_sortants(arc);
        arc.get_cible().set_arcs_entrants(arc);
        arc.set_conteneur(contenu_graph);
        arc.get_arc().toBack();
        map_arc.put(arc, arc.get_nom());
        table.ajout_data(arc);
               
        txtsource.clear();
        txtcible.clear();
        txtvalue.clear();
        
    }
    @FXML
    private void menuNouveau_ArcAction(ActionEvent event) {
        
        nouveau_arc_pane.setVisible(true);
        valeur_arc.clear();
        
    }
    
    @FXML
    private void buttonCreer_SommetAction(ActionEvent event) {
   
        if(!(debut_arc.getItems().contains(nom_sommet.getText().toString()))&&
                !(fin_arc.getItems().contains(nom_sommet.getText().toString()))){
            if(nom_sommet.getText().length()>1)
                erreur_msg.setText("Une Lettre seulement!");
            else
                if(nom_sommet.getText().isEmpty())
                    erreur_msg.setText("Entrer une Lettre!");
                else{
            
                    nouveau_sommet_pane.setVisible(false);
                    sommet = new Sommet();
                    sommet.set_sommet(nom_sommet.getText().toString());
                    sommet_source.getItems().add(sommet.get_sommet().getText().toString());
                    sommet_cible.getItems().add(sommet.get_sommet().getText().toString());
                    debut_arc.getItems().add(sommet.get_sommet().getText().toString());
                    fin_arc.getItems().add(sommet.get_sommet().getText().toString());
                    map_sommet.put(sommet, sommet.get_sommet().getText().toString());
                    sommet.set_conteneur(contenu_graph);
                    erreur_msg.setText(null);
                    
            }
        }
        else{
        
            erreur_msg.setText("Sommet existant!");
        }
        
    }
    @FXML
    private void buttonCreer_ArcAction(ActionEvent event) {
        
       
        nouveau_arc_pane.setVisible(false);
        arc= new Arcs(algo,map_arc);
        map_set=map_sommet.entrySet();
        map_iter=map_set.iterator();
        while(map_iter.hasNext()){
        
            Map.Entry map_entry = (Map.Entry)map_iter.next();
            if(debut_arc.getValue().toString().equals(map_entry.getValue())){
            
                arc.set_source((Sommet)map_entry.getKey());
            }
            if(fin_arc.getValue().toString().equals(map_entry.getValue())){
            
                arc.set_cible((Sommet)map_entry.getKey());
            }
        }
        double d =Double.parseDouble(valeur_arc.getText().toString());
        arc.setarc(debut_arc.getValue().toString(), fin_arc.getValue().toString(), d,algo,table);
        arc.get_source().set_arcs_sortants(arc);
        arc.get_cible().set_arcs_entrants(arc);
        arc.set_conteneur(contenu_graph);
        arc.get_arc().toBack();
        map_arc.put(arc, arc.get_nom());
        table.ajout_data(arc);
        System.out.println(arc.get_nom()+" ....  "+arc.get_nom().charAt(0));
    }
    @FXML
    private void sliderAction(ActionEvent event) {
        
    }
    @FXML
    private void fermerAction(ActionEvent event) {
        System.exit(0);
    }
    @FXML
    private void paneClicked(MouseEvent event) {
    
        nouveau_arc_pane.setVisible(false);
        nouveau_sommet_pane.setVisible(false);
    
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        slider.setMin(0);
        slider.setMax(1);
        slider.setValue(0.5);
        //label.textProperty().bind(slider.valueProperty().asString());
        
        contenu_graph.scaleXProperty().bind(slider.valueProperty().add(0.5));
        contenu_graph.scaleYProperty().bind(slider.valueProperty().add(0.5));
        
        table.initTableau(tableau, col_source, col_cible, col_capacite);
        sommet_source.getItems().clear();
        sommet_cible.getItems().clear();
        debut_arc.getItems().clear();
        fin_arc.getItems().clear();
        
        txtsource.clear();
        txtcible.clear();
        txtvalue.clear();
        //supprimer_arc.setText("Suprimmer");
        popup.getItems().add(supprimer_arc);
        nouveau_arc_pane.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                double dragX = t.getSceneX() - dragAnchor.getX();
                double dragY = t.getSceneY() - dragAnchor.getY();

                double newXPosition = initX + dragX;
                double newYPosition = initY + dragY;
                nouveau_arc_pane.setCursor(Cursor.HAND);
                nouveau_arc_pane.setLayoutX(newXPosition);
                nouveau_arc_pane.setLayoutY(newYPosition);
            }
        });
        nouveau_arc_pane.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                initX = nouveau_arc_pane.getLayoutX();
                initY = nouveau_arc_pane.getLayoutY();
                dragAnchor = new Point2D(t.getSceneX(), t.getSceneY());
                 nouveau_arc_pane.setCursor(Cursor.CLOSED_HAND);
            }
        });
        nouveau_arc_pane.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                nouveau_arc_pane.setCursor(Cursor.HAND);
            }
        });
        
        
    }    
}