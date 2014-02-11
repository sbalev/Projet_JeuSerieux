/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import metier.GestionInstance;
import metier.GestionJeu;
import modele.Instance;
import modele.Partie;

/**
 *
 * @author Jérémy
 */
public class NouvellePartie extends JDialog {
    
    private final IHM   _ihm;
    
    private JLabel      _jlPseudo;
    private JLabel      _jlDifficulte;
    private JLabel      _jlInfos;
    private JTextField  _jtfPseuso;
    private JComboBox   _jcbDifficulte;
    private JButton     _jbAnnuler;
    private JButton     _jbCommencer;

    public NouvellePartie(IHM ihm) {
        setTitle("Nouvelle partie ...");
        
        _ihm = ihm;
        initialiserComposants();
        initialiserEvenements();
        majInfos();
        
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setModal(true);
        setVisible(true);
    }
    
    private void initialiserComposants() {
        _jlPseudo       = new JLabel("Pseudo : ");
        _jlDifficulte   = new JLabel("Difficulté : ");
        _jtfPseuso      = new JTextField("Joueur");
        _jcbDifficulte  = new JComboBox(new Object[] {"Facile", "Normal", "Difficile"});
        _jbAnnuler      = new JButton("Annuler");
        _jbCommencer    = new JButton("Commencer");
        _jlInfos        = new JLabel();
        
        JPanel panelG       = new JPanel(new GridLayout(3, 2));
        JPanel hautGauche   = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel hautDroit    = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel centreGauche = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel centreDroit  = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel basGauche    = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel basDroit     = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        hautGauche.setBackground(PanelPerso.COULEUR_PANEL1);
        _jlPseudo.setForeground(PanelPerso.COULEUR_ECRITURE);
        hautDroit.setBackground(PanelPerso.COULEUR_PANEL1);
        _jtfPseuso.setPreferredSize(new Dimension(100, 20));
        centreGauche.setBackground(PanelPerso.COULEUR_PANEL1);
        _jlDifficulte.setForeground(PanelPerso.COULEUR_ECRITURE);
        centreDroit.setBackground(PanelPerso.COULEUR_PANEL1);
        basDroit.setBackground(PanelPerso.COULEUR_PANEL1);
        basGauche.setBackground(PanelPerso.COULEUR_PANEL1);
        _jlInfos.setBackground(PanelPerso.COULEUR_PANEL1);
        _jlInfos.setForeground(PanelPerso.COULEUR_ECRITURE);
        _jlInfos.setOpaque(true);
        
        hautGauche.add(_jlPseudo);
        hautDroit.add(_jtfPseuso);
        centreGauche.add(_jlDifficulte);
        centreDroit.add(_jcbDifficulte);
        basGauche.add(_jbAnnuler);
        basDroit.add(_jbCommencer);
        
        panelG.add(hautGauche);
        panelG.add(hautDroit);
        panelG.add(centreGauche);
        panelG.add(centreDroit);
        panelG.add(basGauche);
        panelG.add(basDroit);
        
        add(panelG, BorderLayout.WEST);
        add(_jlInfos, BorderLayout.CENTER);
    }
    
    private void initialiserEvenements() {
        
        _jbAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });
        
        _jbCommencer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                demarrerNouvellePartie();
            }
        });
        
        _jcbDifficulte.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                majInfos();
            }
        });
    }
    
    private void majInfos() {
        StringBuilder str = new StringBuilder();
        str.append("<html><body>");
        if(_jcbDifficulte.getSelectedItem().equals("Facile")){
            str.append(Partie.toStringFacile());
        } else if(_jcbDifficulte.getSelectedItem().equals("Normal")) {
            str.append(Partie.toStringNormal());
        } else if (_jcbDifficulte.getSelectedItem().equals("Difficile")) {
            str.append(Partie.toStringDifficile());
        }
        str.append("</body></html>");
        _jlInfos.setText(str.toString());
        pack();
    }
    
    void arretPartieEnCours() {
        Partie._abandon = true;

        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
            Logger.getLogger(NouvellePartie.class.getName()).log(Level.SEVERE, null, ex);
        }
        _ihm.remiseAZero();
        
    }
    
    void demarrerNouvellePartie() {
        arretPartieEnCours();
        
        GestionJeu gestionJeu = new GestionJeu(_ihm._naviresArrives, _ihm._naviresArrivant, _ihm._infoJeu, _ihm._partie);
        
        Partie nouvellePartie = new Partie(_jcbDifficulte.getSelectedItem().toString());
        nouvellePartie.initialiserDifficulte();
        
        gestionJeu.setInstance(creerInstance());
        gestionJeu.start();
        
        dispose();
    }
    
    Instance creerInstance() {
        GestionInstance gInstance = new GestionInstance();
        gInstance.genererAleatoirement();
        return gInstance.getInstance();
    }
}
