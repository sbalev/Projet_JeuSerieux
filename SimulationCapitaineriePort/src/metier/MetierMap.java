/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package metier;

import accesAuDonnees.ADMap;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import javax.json.JsonArray;
import javax.json.JsonObject;
import modele.Forme;
import modele.Navire;
import modele.Quai;
import modele.Terminal;
import modele.enumeration.TypeMarchandise;
import modele.enumeration.TypeShape;
import presentation.PanelMap;
import presentation.PanelInfoForme;

/**
 *
 * @author brokep
 */
public class MetierMap {
    
    
    
    private final ArrayList<Forme> _coordonneesDessin;
    
    //couleurs des différentes formes
    private static final Color NATURALCOLOR = new Color(181, 208, 208);
    private static final Color TERMINALCOLOR = new Color(222, 208, 213);
    private static final Color QUAICOLOR = new Color(52, 203, 153);
    private static final Color ROUTECOLOR = new Color(182, 181, 146);
    private static final Color BATIMENTCOLOR = Color.GRAY;
    private static final Color AUTRECOLOR = Color.GRAY;
    
    private final ADMap _accesDonneesMap;
    
    public MetierMap(PanelMap map, PanelInfoForme panelInfo) {
        _coordonneesDessin = new ArrayList<>();
        _accesDonneesMap = new ADMap();
        
    }
    
    public void construireFormes() {
        makePath2D(_accesDonneesMap.getContenu());
    }

    public ArrayList<Forme> getCoordonneesDessin() {
        return _coordonneesDessin;
    }
    
    public void makePath2D(JsonArray ja) {
        for(Object obj:ja.toArray()) {
            //variables indispensables
            boolean fill = false;
            Color c;
            JsonObject jo = (JsonObject) obj;
            long id = Long.parseLong(jo.get("_id").toString());
            String type = jo.get("_type").toString();
            String nom = jo.get("_nom").toString();
            Forme forme;

            //construction de l'objet
            if(type.equals(TypeShape.NATURAL.name())) {
                c = NATURALCOLOR;
                fill = true;
                forme = new Forme(nom, fill, c, id, TypeShape.NATURAL);
            } else if(type.equals(TypeShape.QUAI.name())) {
                
                c = QUAICOLOR;
                fill = true;
                forme = new Quai(nom, fill, c, id, new Random(System.currentTimeMillis()).nextInt(100));
                System.out.println("nom " + nom + " " + forme.getTypeForme().name());
            } else if(type.equals(TypeShape.TERMINAL.name())) {
                c = TERMINALCOLOR;
                fill = true;
                forme = new Terminal(nom, fill, c, id, new Random(System.currentTimeMillis()).nextInt(100));
            } else if(type.equals(TypeShape.HIGHWAY.name())) {
                c = ROUTECOLOR;
                forme = new Forme(nom, fill, c, id, TypeShape.HIGHWAY);
            } else if(type.equals(TypeShape.BUILDING.name())) {
                c = BATIMENTCOLOR;
                fill = true;
                forme = new Forme(nom, fill, c, id, TypeShape.BUILDING);
            } else {
                c = AUTRECOLOR;
                forme = new Forme(nom, fill, c, id, TypeShape.NULL);
            }
            
            //traitement des coordonnées de la forme
            JsonArray nodes = (JsonArray)jo.get("_nodes");
            for(Object coordonnees:nodes.toArray()) {
                JsonObject node = (JsonObject) coordonnees;
                double x = Double.parseDouble(node.get("x").toString());
                double y = Double.parseDouble(node.get("y").toString());
                forme.ajoutCoordonnee(new Point2D.Double(x, y));
            }
            _coordonneesDessin.add(forme);
        }
        
    }
    
    public void ajoutForme(Forme f) {
        _coordonneesDessin.add(f);
    }
    
    public Forme getForme(String nom) {
        for(Forme forme:_coordonneesDessin) {
            if(forme.getNom().equals(nom)) {
                return forme;
            }
        }
        return null;
    }
    
    public Forme getForme(Point2D p) {
        Forme selectionnee = null;
        for(Forme forme:_coordonneesDessin) {
            if(forme.getPath().contains(p)) {
                if(selectionnee == null) {
                selectionnee = forme;
                } else {
                    if(forme instanceof Quai ||
                        forme instanceof Terminal ||
                        forme instanceof Navire) {
                        selectionnee = forme;
                    }
                }
            }    
        }
        return selectionnee;
    }
    
    public void lierQuaisTerminaux() {
        Quai quaiAmeriques = (Quai)getForme("Quai des Amériques");
        Terminal terminalAtlantique = (Terminal)getForme("Terminal de l'Atlantique");
        quaiAmeriques.ajoutTerminal(terminalAtlantique);
        terminalAtlantique.ajoutType(TypeMarchandise.CONTENEURS);
        
        Quai quaiEurope = (Quai)getForme("Quai de l'Europe");
        Terminal terminalEurope = (Terminal)getForme("Terminal de l'Europe");
        quaiEurope.ajoutTerminal(terminalEurope);
        terminalEurope.ajoutType(TypeMarchandise.CONTENEURS);
        
        Terminal terminalNormandie = (Terminal)getForme("Terminal de Normandie");
        terminalNormandie.ajoutType(TypeMarchandise.CONTENEURS);
        
        Terminal terminalCruise = (Terminal)getForme("Cruise Terminal");
        terminalCruise.ajoutType(TypeMarchandise.PASSAGER);
    }
}
