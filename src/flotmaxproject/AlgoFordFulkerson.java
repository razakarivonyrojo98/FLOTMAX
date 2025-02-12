/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flotmaxproject;

import flotmaxproject.Arcs.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.paint.Color;


public class AlgoFordFulkerson {
    Arcs gui_arc ;
    Iterator map_iter;
    Set map_set ;
    public static class Edge {
        Object source;
        Object sink;
        double capacity;
        double residual;
        Edge redge;

        public Edge(Object source, Object sink, double capacity) {
            this.source = source;
            this.sink = sink;
            this.capacity = capacity;
        }
       
        @Override
        public String toString() {
            return "Edge{" + "source=" + source + " sink=" + sink + " capacity=" + capacity + '}';
        }
       
    }

    public Map<Object, List<Edge>> adj = new HashMap<>();
    public Map<Object, List<Edge>> adjInitial = new HashMap<>();
    List<Edge> listEdge;
    public Map<Edge, Double> flow = new HashMap<>();
    public Map<Edge, Double> flowInitial = new HashMap<>();

    private List<Edge> getAdj(Object o, List<Edge> value) {
        if (adj.containsKey(o)) {
            return adj.get(o);
        } else {
            adj.put(o, value);
            
            return value;
        }
    }

    private boolean findPath(Object source, Object sink, Set<Edge> path, Set<Object> visited) {
        if (visited.contains(source)){
            return false;
        }
        visited.add(source);
        if (source == sink) {
            return true;
        } else {
            for (Edge edge : adj.get(source)) {
                edge.residual = edge.capacity - flow.get(edge);
                if (edge.residual > 0 && !path.contains(edge)) {
                    path.add(edge);
                    boolean result = findPath(edge.sink, sink, path, visited);
                    if (result) {
                        return true;
                    }
                    path.remove(edge);
                }
            }
        }
        return false;
    }

    private Set<Edge> findPath(Object source, Object sink) {
        Set<Edge> path = new HashSet<>();
        Set<Object> visited = new HashSet<>();
        if (findPath(source, sink, path, visited)) {
            return path;
        } else {
            return null;
        }
    }

    public Edge addEdge(Object u, Object v, double w, double z) {
        Edge edge = new Edge(u,v,w);
        Edge redge = new Edge(v,u,z);
        edge.redge = redge;
        redge.redge = edge;
        getAdj(u, new ArrayList<Edge>()).add(edge);
        getAdj(v, new ArrayList<Edge>()).add(redge);
        
        flow.put(edge, 0.);
        flow.put(redge, 0.);
        flowInitial.put(edge, 0.);
        flowInitial.put(redge, 0.);
        return edge;
    }
   
    public double maxFlow(Object source, Object sink) {
        Set<Edge> path = findPath(source, sink);
        while (path != null) {
            double minFlow = Double.MAX_VALUE;
            for (Edge e : path) {
                minFlow = Math.min(minFlow, e.residual);
            }
            for (Edge e : path) {
                flow.put(e, flow.get(e) + minFlow);
                
                flow.put(e.redge, flow.get(e.redge) - minFlow);
            }
            path = findPath(source, sink);
        }

        double maxFlow = 0;
        for (Edge edge : adj.get(source)) {
            maxFlow += flow.get(edge);
        }
        return maxFlow;
    }
    
     // By Lids
    
    public void initialize(Arcs a){
        //flow.clear();
        
        this.map_set=flow.entrySet();
        this.map_iter= this.map_set.iterator();
        
        while(this.map_iter.hasNext()){
            Map.Entry map_entry = (Map.Entry)this.map_iter.next();
            Edge e = (Edge) map_entry.getKey();
            String source = (String)e.source;
            String sink = (String)e.sink;
            
            if((source+sink).equals(a.get_nom())){
               
                    a.get_arc().setStroke(Color.DODGERBLUE);
                    a.head1.setStroke(Color.DODGERBLUE);
                    a.head2.setStroke(Color.DODGERBLUE);
                
                a.valeur.setText(""+e.capacity);
                }
            }
        }
    public void initialize(){
        //flow.clear();
//       System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        flow = flowInitial;
//        System.out.println(flow);
//        System.out.println(flowInitialthis.map_set=flow.entrySet();
        this.map_set=flow.entrySet();
        this.map_iter= this.map_set.iterator();
        
        while(this.map_iter.hasNext()){
            Map.Entry map_entry = (Map.Entry)this.map_iter.next();
            Edge e = (Edge) map_entry.getKey();
            String source = (String)e.source;
            String sink = (String)e.sink;
            flowInitial.put(e,0.);
            flowInitial.put(e.redge, 0.);
        }
        flow = flowInitial;
        
     }
    public void labelflow(Arcs a){
        this.map_set=flow.entrySet();
        this.map_iter= this.map_set.iterator();
        
        while(this.map_iter.hasNext()){
            Map.Entry map_entry = (Map.Entry)this.map_iter.next();
            Edge e = (Edge) map_entry.getKey();
            String source = (String)e.source;
            String sink = (String)e.sink;
            
            if((source+sink).equals(a.get_nom())){
                if(e.capacity == (Double)map_entry.getValue()){
                    
                    a.get_arc().setStroke(Color.RED);
                    a.head1.setStroke(Color.RED);
                    a.head2.setStroke(Color.RED);
                }
                else{
                    a.get_arc().setStroke(Color.DODGERBLUE);
                    a.head1.setStroke(Color.DODGERBLUE);
                    a.head2.setStroke(Color.DODGERBLUE);
                }
                a.valeur.setText(map_entry.getValue()+"/"+e.capacity);
            }
        }
        
      
    }
   
    public void updateEdge(Object u, Object v, double w,double z) {

        delEdge(u,v,w,u.toString()+v.toString());
        addEdge(u,v,z,0);
        
    }
    public void delEdge(Object u, Object v, double w,String nomArc) {
        Edge edge = new Edge(u,v,w); 
        Edge redge = new Edge(v,u,w);
        flow.remove(edge); 
        flowInitial.remove(edge);

        int index = 0;
        this.map_set=adj.entrySet();
        this.map_iter= this.map_set.iterator();
        List<Edge> le = null;
        System.out.println(adj);
        while(this.map_iter.hasNext()){
            Map.Entry map_entry = (Map.Entry)this.map_iter.next();
            Object o = (Object) map_entry.getKey();
            
                if(o.equals(u)){
                    System.out.println("blalalala");
                    if(nomArc.equals(u.toString()+v.toString())){
                        le = (List<Edge>) map_entry.getValue();
                        for (Iterator<Edge> it = le.iterator(); it.hasNext();) {
                            Edge e = it.next();
                            System.out.println(e);
                            if((e.source==u)&&(e.sink==v)&&(e.capacity==w)){
                                System.out.println("blalalala");
                                
                                adj.get(u).remove(index);
                                break;

                            }
                            index++;
                       }
                        
                }
             break;  
            }
          
        }
       System.out.println(adj);
    }
     
     public void clear(){
         flow.clear();
         adj.clear();
     }
}