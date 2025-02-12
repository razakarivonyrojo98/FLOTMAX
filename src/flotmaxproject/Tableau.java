/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flotmaxproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/**
 *
 * @author Lido
 */
public class Tableau {
    
    final ObservableList<ArcProperties> data = FXCollections.observableArrayList();
    TableColumn sommet_source = new TableColumn();
    TableColumn sommet_cible = new TableColumn() ;
    TableColumn capacite = new TableColumn();
       
    TableView tableau = new TableView();
    private int i=0;
    public Tableau(){
       }
    public void initTableau(TableView tableau,TableColumn sommet_source
            ,TableColumn sommet_cible,
            TableColumn capacite){
        this.capacite=capacite;
        this.sommet_source=sommet_source;
        this.sommet_cible=sommet_cible;
        this.tableau = tableau;
        
        //Set cell factory for cells that allow editing
       
        
         Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
 
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCell();
                    }
                };
        
        this.sommet_source.setCellFactory(cellFactory);
        this.sommet_cible.setCellFactory(cellFactory);
        this.capacite.setCellFactory(cellFactory);
        
        this.sommet_source.setCellValueFactory(new PropertyValueFactory("source"));
        this.sommet_cible.setCellValueFactory(new PropertyValueFactory("cible"));
        this.capacite.setCellValueFactory(new PropertyValueFactory("cap"));
        updateObservableListProperties(this.sommet_source, this.sommet_cible, this.capacite);
        
    }
    public void ajout_data(Arcs arc){
 
        data.add(new ArcProperties(arc.source.label.getText().toString(),
                                   arc.cible.label.getText().toString(),
                                   arc.valeur.getText().toString()));
        
        this.tableau.setItems(data);
        System.out.println("Ajout tableau");
        arc.Setindex(i);
        i++;
    }
    public void modif_data(Arcs arc,int index){
 
        data.set(index, new ArcProperties(arc.source.label.getText().toString(),
                                   arc.cible.label.getText().toString(),
                                   arc.valeur.getText().toString()));
        
        this.tableau.setItems(data);
    }
    public void sup_data(Arcs arc){
        
        data.remove(data.size()-1);
       
        this.tableau.setItems(data);
        i--;
    }
    public void initialiseindex(){
    
        i=0;
    }
    private void updateObservableListProperties(TableColumn emailCol, TableColumn firstNameCol,
            TableColumn lastNameCol) {
        //Modifying the email property in the ObservableList
        emailCol.setOnEditCommit(new EventHandler<CellEditEvent<ArcProperties, String>>() {           
            @Override public void handle(CellEditEvent<ArcProperties, String> t) {
                ((ArcProperties) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setSource(t.getNewValue());
            }
        });
        //Modifying the firstName property in the ObservableList
        firstNameCol.setOnEditCommit(new EventHandler<CellEditEvent<ArcProperties, String>>() {          
            @Override public void handle(CellEditEvent<ArcProperties, String> t) {
                ((ArcProperties) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setCible(t.getNewValue());
            }
        });
        //Modifying the lastName property in the ObservableList
        lastNameCol.setOnEditCommit(new EventHandler<CellEditEvent<ArcProperties, String>>() {           
            @Override public void handle(CellEditEvent<ArcProperties, String> t) {
                ((ArcProperties) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setCap(t.getNewValue());
            }
        });
    }
     public static class ArcProperties {
        private StringProperty  source;
        private StringProperty cible;
        private StringProperty cap;
 
        private ArcProperties(String source, String cible, String capacite) {
            this.source = new SimpleStringProperty(source);
            this.cible =  new SimpleStringProperty(cible);
            this.cap = new SimpleStringProperty(capacite);
     }
        public StringProperty sourceProperty() { return source; }
        public StringProperty cibleProperty() { return cible; }
        public StringProperty capProperty() { return cap; }
        
        public void setSource(String s) { this.source.set(s); }
        public void setCible(String c) { this.cible.set(c); }
        public void setCap(String ca) { this.cap.set(ca); }
   }
     
     // EditingCell - for editing capability in a TableCell
    public static class EditingCell extends TableCell<ArcProperties, String> {
        private TextField textField = new TextField();
        
        public EditingCell() {
        }
        
        @Override 
        public void startEdit() {
            super.startEdit();
 
            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
        
        @Override 
        public void cancelEdit() {
            super.cancelEdit();
            setText((String) getItem());
            setGraphic(null);
        }
        
        @Override 
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
 
        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {                
                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(textField.getText());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}
