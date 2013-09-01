/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chocolaterie;

/**
 *
 * @author IhEbusSsSss
 */
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Statement;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.GridLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import java.sql.ResultSet;
import java.util.Vector;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


class EtatStock extends JPanel{
    JTable table;
        EtatStock(FenetrePrincipale parent){
    	String req;
        req="select distinct * from matiere";
        Object [][]stk= new Object[parent.count(req)][5] ;
		try{
		parent.res=parent.st.executeQuery(req);
		int j=0;
		while(parent.res.next()){
			for(int i=2;i<7;i++) stk[j][i-2]=parent.res.getString(i);
			j++;
		}
		} catch(SQLException e){
			JOptionPane.showMessageDialog(null,"erreur de chargement des produits","Erreur",JOptionPane.ERROR_MESSAGE);

			}

    	Object [] titre = {"Designation","Quantité","Prix unitaire","Unité","Quantité limite"};
    	table = new JTable(stk,titre);
        JScrollPane jsp= new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	jsp.setPreferredSize(new Dimension(510, 300));
    	add(jsp);
    	parent.setPanel(this);
    }
    
}
class ListeClient extends JPanel{
    JTable table;
        ListeClient(FenetrePrincipale parent){
    	String req;
        req="select * from client";
        Object [][]stk= new Object[parent.count(req)][4] ;
		try{
		parent.res=parent.st.executeQuery(req);
		int j=0;
		while(parent.res.next()){
			for(int i=2;i<6;i++) stk[j][i-2]=parent.res.getString(i);
			j++;
		}
		} catch(SQLException e){
			JOptionPane.showMessageDialog(null,"erreur de chargement des produits","Erreur",JOptionPane.ERROR_MESSAGE);

			}

    	Object [] titre = {"nom","Prénom","Points","Téléphone"};
    	table = new JTable(stk,titre);
        JScrollPane jsp= new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	jsp.setPreferredSize(new Dimension(510, 300));
    	add(jsp);
    	parent.setPanel(this);
    }

}


class Composant {
    JButton b;
    JComboBox c;
    JTextField t;
    ResultSet res;
    FenetrePrincipale parent;
    String req;
    Composant (FenetrePrincipale parent,String req){
        this.parent=parent;
        this.req=req;
        b= new JButton ("+");
        c=new JComboBox();
        t=new JTextField(10);
        remplirComposant();
        
    }
    public void remplirComposant(){
        try {
            res = parent.selection(req);
            while(res.next()){
                c.addItem(res.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Composant.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

class Composants extends JPanel implements ActionListener {
    Vector <Composant> v;
    String req;
    FenetrePrincipale parent;
    
    Composants (FenetrePrincipale parent, String req){
        setLayout(new GridLayout(40,1)); //BoxLayout(this,BoxLayout.PAGE_AXIS));
        this.parent=parent;
        this.req=req;
        v=new Vector();
        ajout();
        
    }
    
    void ajout(){
        if(v.size()>=1) v.lastElement().b.setLabel("-");
        v.addElement(new Composant (parent,req));
        JPanel p=new JPanel();
        v.lastElement().b.addActionListener(this);
        p.add(v.lastElement().b);
        p.add(v.lastElement().c);
        p.add(v.lastElement().t);
        this.add(p);
        this.validate();
 }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(v.lastElement().b))
            ajout();
        else
        for (int i=0;i<v.size()-1;i++){
            if(e.getSource()==v.elementAt(i).b)
            {
                this.remove(i);
                v.remove(i);
                validate();
                                
            }
        }
        
    }
    
}

class CommandeClient extends JPanel implements ActionListener{
    Composants comp;
    private FenetrePrincipale parent;
    private String client;
    JButton val;
    ResultSet res=null;
    JComboBox clients;
    Vector <String> v;
    public CommandeClient(FenetrePrincipale parent,String client){
        setLayout(new BorderLayout());
        this.parent = parent;
        this.client = client;
        comp= new Composants(parent,"Select distinct des from article");
        JScrollPane jsp= new JScrollPane(comp);
        jsp.setToolTipText("Ajouter des article a la commande");
        jsp.setPreferredSize(new Dimension(300,250));
        val=new JButton ("Valider");
        val.addActionListener(this);
        JPanel p=new JPanel();
        p.add(val);
        //gggggggggggggggggggggggggggggggggggggggggggggggggg
        clients=new JComboBox();
        this.remplirComboClient();
        p.add(clients);
        //fdggggggggggggggggggggggggggggggggggggggggggg
        add(jsp,BorderLayout.CENTER);
        add(p,BorderLayout.SOUTH);
        parent.setPanel(this);
    }
public CommandeClient(FenetrePrincipale parent){
        setLayout(new BorderLayout());
        this.parent = parent;
        this.client = "null";
        comp= new Composants(parent,"Select distinct des from article");
        JScrollPane jsp= new JScrollPane(comp);
        jsp.setToolTipText("Ajouter des article a la commande");
        jsp.setPreferredSize(new Dimension(300,250));
        val=new JButton ("Valider");
        val.addActionListener(this);
        JPanel p=new JPanel();
        JPanel pp=new JPanel();
        p.add(val);
        clients=new JComboBox();
        this.remplirComboClient();
        pp.add(clients);
        add(jsp,BorderLayout.CENTER);
        add(p,BorderLayout.SOUTH);
        add(pp,BorderLayout.NORTH);
        parent.setPanel(this);
    }


    public void actionPerformed(ActionEvent e) {
        if(client.equals("null")) validerSlotDirect();
            else validerSlot();
    }
public void validerSlotDirect(){
        try {
            String art,qte,com;
            client=v.elementAt(clients.getSelectedIndex());
            parent.insertion("insert into commande (code_cli) values("+client+")");
            res=parent.selection("select code_com from commande where code_cli="+client);
            res.next();
            com=res.getString(1);
            for(int i=0;i<comp.v.size();i++){
            res=parent.selection("select code_art from article where des='"+comp.v.elementAt(i).c.getSelectedItem()+"'");
            res.next();
            art=res.getString(1);
            qte=comp.v.elementAt(i).t.getText();
            parent.insertion("insert into ligne_com values("+com+","+art+","+qte+")");
            parent.setPanel(parent.acceuil);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erreur d'ajout de commande car un ou plusieur champs quantité sont vides","Erreur",JOptionPane.ERROR_MESSAGE);
        }


    }

    public void validerSlot(){
        try {
            String art,qte,com;
            res = parent.selection("select code_cli from client where " + client);
            res.next();
            client=res.getString(1);
            parent.insertion("insert into commande (code_cli) values("+client+")");
            res=parent.selection("select code_com from commande where code_cli="+client);
            res.next();
            com=res.getString(1);
            for(int i=0;i<comp.v.size();i++){
            res=parent.selection("select code_art from article where des='"+comp.v.elementAt(i).c.getSelectedItem()+"'");
            res.next();
            art=res.getString(1);
            qte=comp.v.elementAt(i).t.getText();
            parent.insertion("insert into ligne_com values("+com+","+art+","+qte+")");
            parent.setPanel(parent.acceuil);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erreur d'ajout de commande car un ou plusieur champs quantité sont vides","Erreur",JOptionPane.ERROR_MESSAGE);
        }


    }
    public void remplirComboClient() {
        v=new Vector();
        int nbr=parent.count("Select * from client");
        try {
            res = parent.selection("Select * from client");
        while(res.next()){
            v.addElement(res.getString(1));
            clients.addItem(res.getString(2)+" "+res.getString(3)+" "+res.getString(5));
        }
        } catch (SQLException ex) {
            Logger.getLogger(CommandeClient.class.getName()).log(Level.SEVERE, null, ex);
        }

     }


}

class Plateau extends JPanel implements ActionListener{
    Composants comp;
    JButton val;
    private FenetrePrincipale parent;
    ResultSet res=null,rec;

    public Plateau (FenetrePrincipale parent){
        this.parent = parent;
        setLayout(new BorderLayout());
        comp=new Composants(parent,"select distinct des from article");
        JScrollPane jsp =new JScrollPane(comp);
        jsp.setPreferredSize(new Dimension(300,300));
        jsp.setToolTipText("Ajoutez les produits à préparer et leur nombre");
        add(jsp,BorderLayout.CENTER);
        val=new JButton("Valider");
        val.addActionListener(this);
        JPanel p= new JPanel();
        p.add(val);
        add(p,BorderLayout.SOUTH);
        parent.setPanel(this);
    }

    public void actionPerformed(ActionEvent e) {
        String qte,art;
        Vector <String> vec;
        String msg="Erreur de préparation de produit car un ou plusieurs champs nombre sont vides";
        try {
            for(int i=0;i<comp.v.size();i++){
                vec=new Vector();
                res=parent.selection("select code_art from article where des='"+comp.v.elementAt(i).c.getSelectedItem()+"'");
                res.next();
                art=res.getString(1);
                qte=comp.v.elementAt(i).t.getText();
                parent.insertion("insert into plateau (code_art,nombre) values("+art+","+qte+")");
                rec=parent.selection("select code_mp,qte*"+qte+" from ligne_rec where code_art="+art);
                while(rec.next()){
                     vec.addElement(rec.getString(2));
                     vec.addElement(rec.getString(1));

                }
                for(int j=0;j<vec.size();j+=2){
                    parent.insertion("update matiere set qte=qte-"+vec.elementAt(i)+" where code_mp="+vec.elementAt(i+1));
                }
            }
            res=parent.selection("select code_mp from matiere where qte<qtl");
            while(res.next()){
                int i=parent.recurence.contains(res.getString(1));
                if(i!=-1) parent.insertion("insert into bon_commande (code_mat,code_four,qte) values("+parent.recurence.v.elementAt(i).des+","+parent.recurence.v.elementAt(i).four+","+parent.recurence.v.elementAt(i).qte+")");
            }
            parent.setPanel(parent.acceuil);
        } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,msg,"Erreur",JOptionPane.ERROR_MESSAGE);
            }
    }

}
class Recette extends JPanel implements ActionListener{
    private FenetrePrincipale parent;
    Composants comp;
    JTextField des,prix;
    JButton val;
    ResultSet res;
    String req;
    Recette(FenetrePrincipale parent){
        this.parent = parent;
        setLayout(new BorderLayout());
        des=new JTextField(10);
        prix=new JTextField(10);
        JPanel p=new JPanel();
        p.add(new JLabel("Designation"));
        p.add(des);
        p.add(new JLabel("Prix"));
        p.add(prix);
        add(p,BorderLayout.NORTH);
        comp= new Composants(parent,"Select distinct des_mp from matiere");
        JScrollPane jsp= new JScrollPane(comp);
        jsp.setToolTipText("Entrez la recette de l'article pour 30 piece");
        jsp.setPreferredSize(new Dimension(300,200));
        this.add(jsp,BorderLayout.CENTER);
        val=new JButton("Valider");
        val.addActionListener(this);
        JPanel pp=new JPanel();
        pp.add(val);
        add(pp,BorderLayout.SOUTH);
        parent.setPanel(this);
    }

    public void actionPerformed(ActionEvent e) {
        String msg = "Erreur d'ajout de l'article car\n";
        if(this.des.getText().isEmpty()) msg+="Le champ désignation est vide\n";
        if(this.prix.getText().isEmpty()) msg+="Le champ prix est vide\n"; else msg+="Le champ prix est peut etre incompatible\n";
        msg+="L'un des produit de la recette est peut etre repeté ou le champ quantité est vide\n";
        try {
            parent.insertion("insert into article (des,prix) values ('"+des.getText()+"',"+prix.getText()+")");
            res=parent.selection("select code_art from article where des='"+des.getText()+"'");
            res.next();
            String art=res.getString(1);
            String mat,qte;
            for(int i=0;i<comp.v.size();i++){
            res=parent.selection("select code_mp from matiere where des_mp='"+comp.v.elementAt(i).c.getSelectedItem()+"'");
            res.next();
            mat=res.getString(1);
            qte=comp.v.elementAt(i).t.getText();
            parent.insertion("insert into ligne_rec values("+mat+","+art+","+qte+"/30)");
            parent.setPanel(parent.acceuil);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,msg,"Erreur",JOptionPane.ERROR_MESSAGE);
        }
    }

}




class AchatClient extends JPanel implements ActionListener {
    JTextField montant;
    JComboBox clients;
    FenetrePrincipale parent;
    ResultSet res;
    Vector <String> v;
    JButton val;

    AchatClient(FenetrePrincipale parent){
        this.setLayout(new GridLayout(3,3));
        this.parent = parent;
        JPanel pr[]= new JPanel[9];
        JPanel sc[]= new JPanel[6];
        for(int i=0;i<pr.length;i++){
            pr[i]=new JPanel();
        }
        for(int i=0;i<sc.length;i++){
            sc[i]=new JPanel();
        }
        montant=new JTextField (10);
        clients=new JComboBox();
        val=new JButton ("Valider");
        val.addActionListener(this);
        remplirComboClient();
        sc[0].add(clients);
        sc[1].add(new JLabel("Montant:"));
        sc[2].add(montant);
        sc[2].add(val);
        pr[4].setLayout(new GridLayout(3,1));
        for(int i=0;i<3;i++) pr[4].add(sc[i]);
        for(int i=0;i<pr.length;i++){
            add(pr[i]);
        }
        parent.setPanel(this);

    }
    public void remplirComboClient() {
        v=new Vector();
        int nbr=parent.count("Select * from client");
        try {
            res = parent.selection("Select * from client");
        while(res.next()){
            v.addElement(res.getString(1));
            clients.addItem(res.getString(2)+" "+res.getString(3)+" "+res.getString(5));
        }
        } catch (SQLException ex) {
            Logger.getLogger(CommandeClient.class.getName()).log(Level.SEVERE, null, ex);
        }

     }
    public void actionPerformed(ActionEvent e) {
        String msg="Erreur de comptabilisation d'achat car\n";
        if(this.montant.getText().isEmpty()) msg+="Le champ montant est vide"; else msg+="Le champ montant est incompatible";
        try {
            parent.insertion("update client set point=point+"+montant.getText()+" where code_cli="+v.elementAt(clients.getSelectedIndex()));
            parent.setPanel(parent.acceuil);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,msg,"Erreur",JOptionPane.ERROR_MESSAGE);
        }
    }
}
class ListeComposant extends JPanel implements ActionListener{


    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
class  AjoutProduit extends JPanel implements ActionListener{
    JComboBox unite;
    JTextField des,pu,qtl;
    JButton val;
    FenetrePrincipale parent;
    AjoutProduit(FenetrePrincipale Parent){
		parent=Parent;
        setLayout (new GridLayout(5,3));
		des=new JTextField(10);
		pu=new JTextField(10);
		qtl=new JTextField(10);
		unite=new JComboBox();
		val= new JButton("Valider");
		val.addActionListener(this);
		unite.addItem("---------------------");
		unite.addItem("Kilo");
		unite.addItem("Gramme");
		unite.addItem("Litre");
		for(int i=0;i<3;i++) this.add(new JPanel());
    	JPanel centre=new JPanel(new GridLayout(2,1));
    	JPanel sud=new JPanel(new GridLayout(2,1));
    	JPanel left=new JPanel(new GridLayout(2,1));
    	JPanel leftA=new JPanel(new GridLayout(2,1));
    	JPanel sudA=new JPanel(new GridLayout(2,1));
    	JPanel sudZ=new JPanel();
    	JPanel centreA=new JPanel();
    	JPanel centreZ=new JPanel();
    	JPanel centreE=new JPanel();
    	JPanel centreR=new JPanel();
    	JPanel centreAA=new JPanel();
    	JPanel centreZZ=new JPanel();
    	JPanel centreEE=new JPanel();
    	JPanel centreRR=new JPanel();
		centreAA.add(new JLabel("Designation:       "));
		centreA.add(des);
		centreZZ.add(new JLabel("Prix unitaire:     "));
		centreZ.add(pu);
		centreEE.add(new JLabel("Quantité minimale: "));
		centreE.add(unite);
		centreRR.add(new JLabel("Unite:             "));
		centreR.add(qtl);
		centre.add(centreA);
		centre.add(centreZ);
		sud.add(centreR);
		sud.add(centreE);
		sudZ.add(val);
		sudA.add(sudZ);
		sudA.add(new JPanel());
		left.add(centreAA);
		left.add(centreZZ);
		leftA.add(centreEE);
		leftA.add(centreRR);
		add(left);
		add(centre);
		for(int i=0;i<1;i++) add(new JPanel());
		add(leftA);
		add(sud);
		for(int i=0;i<5;i++) add(new JPanel());
		add(sudA);
		for(int i=0;i<1;i++) add(new JPanel());
		Parent.setPanel(this);
	}

    public void actionPerformed(ActionEvent e) {
        String msg="Erreur d'ajout de produit car\n";
        if(des.getText().isEmpty()) msg+="le champ designation est vide\n";
        if(pu.getText().isEmpty()) msg+="le champ prix unitaire est vide\n"; else msg+="le champ prix unitaire est peut etre incompatible\n";
        if(qtl.getText().isEmpty()) msg+="le champ quantite limite est vide\n"; else msg+="le champ unité est peut etre incompatible\n";
        try{
			parent.insertion("insert into matiere (des_mp,prix_unit,qtl,unite) values('"+des.getText()+"',"+pu.getText()+","+qtl.getText()+",'"+unite.getSelectedItem()+"')");
            parent.setPanel(parent.acceuil);
        } catch(SQLException exp){
			JOptionPane.showMessageDialog(null,msg,"Erreur",JOptionPane.ERROR_MESSAGE);

    }
}

	}



class AjoutClient extends JPanel implements ActionListener{
    JTextField nom,pren,tel;
    JButton val,com;
    private FenetrePrincipale parent;

    public AjoutClient (FenetrePrincipale parent){
        setLayout(new GridLayout(3,3));
        this.parent = parent;
        JPanel pr[]= new JPanel[9];
        JPanel sc[]= new JPanel[6];
        for (int i = 0; i < pr.length; i++) {
            pr[i] = new JPanel();
        }
        for (int i = 0; i < sc.length; i++) {
            sc[i] = new JPanel();
        }
        nom=new JTextField(10);
        pren=new JTextField(10);
        tel=new JTextField(10);
        val=new JButton("Valider");
        val.addActionListener(this);
        com=new JButton("Valider et commander");
        com.addActionListener(this);
        pr[3].setLayout(new GridLayout(3,1));
        pr[4].setLayout(new GridLayout(3,1));
        pr[7].setLayout(new BorderLayout());
        sc[0].add(nom);
        sc[1].add(pren);
        sc[2].add(tel);
        sc[3].add(val);
        sc[3].add(com);
        pr[3].add(new JLabel("                                         Nom               :"));
        pr[3].add(new JLabel("                                         Prenom         :"));
        pr[3].add(new JLabel("                                         Telephone    :"));
        for (int i = 0; i < 3; i++) {
            pr[4].add(sc[i]);
        }
        pr[7].add(sc[3],BorderLayout.SOUTH);
        for (int i = 0; i < pr.length; i++) {
            add(pr[i]);
        }
        parent.setPanel(this);

    }

    public void actionPerformed(ActionEvent e) {
        String msg="Erreur de création de client car\n";
        if(this.nom.getText().isEmpty()) msg+="Le nom champ est vide\n";
        if(this.pren.getText().isEmpty()) msg+="Le prenom champ est vide\n";
        if(this.tel.getText().isEmpty()) msg+="Le téléphone champ est vide\n"; else msg+="Le téléphone champ est incompatible\n";
        try {
            parent.insertion("insert into client (nom,pren,tel) values('"+nom.getText()+"','"+pren.getText()+"',"+tel.getText()+")");
            if(e.getSource().equals(com)) new CommandeClient(parent,"tel="+tel.getText()); else parent.setPanel(parent.acceuil);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,msg,"Erreur",JOptionPane.ERROR_MESSAGE);
        }
        
    }


}
class Commander extends JPanel implements ActionListener{
    JComboBox four,mat;
    JTextField qte;
    JCheckBox option;
    JButton val;
    FenetrePrincipale parent;
    ResultSet res;
    
    public Commander(FenetrePrincipale Parent){
    	parent=Parent;
        setLayout((new GridLayout(4,3)));
    	JPanel pr[] = new JPanel [12], sc []= new JPanel[10];
		for(int i=0;i<12;i++) pr[i]=new JPanel(new GridLayout(2,1));
		for(int i=0;i<10;i++) sc[i]=new JPanel();
    	four = new JComboBox();
    	mat = new JComboBox();
    	qte= new JTextField(10);
    	val= new JButton("Valider");
    	option= new JCheckBox("Repeter a chaque fois que la limite est atteinte");
    	mat.addActionListener(this);
    	val.addActionListener(this);
    	this.remplirComboProduit ();
    	sc[0].add(new JLabel("Poduit:"));
    	sc[2].add(new JLabel("Fournisseur:"));
    	sc[3].add(new JLabel("Quantité:"));
    	sc[4].add(mat);
    	sc[5].add(four);
    	sc[6].add(qte);
    	sc[7].add(option);
    	sc[8].add(val);
    	pr[4].setLayout(new GridLayout(2,2));
    	pr[7].setLayout(new GridLayout(2,2));
    	pr[4].add(sc[0]);
    	pr[4].add(sc[2]);
    	pr[4].add(sc[4]);
    	pr[4].add(sc[5]);
    	pr[7].add(sc[3]);
    	pr[7].add(sc[6]);
    	pr[10].add(sc[7]);
    	pr[10].add(sc[8]);
    	for(int i=0;i<12;i++) add(pr[i]);
		parent.setPanel(this);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==mat) {
	
		Object pos=mat.getSelectedItem();
			remplirComboFour ((String)pos);
			remplirComboProduit();
			mat.setSelectedItem(pos);
			}
		if(e.getSource()==val) commandeSlot();
    }
    
void remplirComboFour (String produit){
    	four.removeAllItems();
    	String req;
		try{
		req="select distinct raison from fournisseur where produit like '%"+produit+"%'";
		res=parent.selection(req);
		while(res.next()){
            four.addItem(res.getString(1));
		}
        
		} catch(SQLException e){
			JOptionPane.showMessageDialog(null,"erreur de chargement des produits","Erreur",JOptionPane.ERROR_MESSAGE);

			}
    }

    public void commandeSlot(){
    	String req,msg="Erreur de commande car\n";
        if(this.qte.getText().isEmpty()) msg+="Le champ quantité est vide"; else msg+="Le champ quantité est peut etre incompatible";

		try{
			req="select distinct code_four from fournisseur where raison='"+four.getSelectedItem()+"'";
			res=parent.selection(req);
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        	res.next();
        	String fourn=res.getString(1);
        	req="select distinct code_mp from matiere where des_mp='"+mat.getSelectedItem()+"'";
			res=parent.selection(req);
			res.next();
        	String des=res.getString(1);
        	if(!option.isSelected())
			req="insert into bon_commande(code_four,qte,etat,recurence,code_mat,dat) values("+fourn+","+qte.getText()+",0,0,"+des+",Date())";
			else {
				req="insert into bon_commande(code_four,qte,etat,recurence,code_mat,dat) values("+fourn+","+qte.getText()+",0,1,"+des+",Date())";
				parent.recurence.add(des,fourn,qte.getText());
			}
			parent.insertion(req);
            parent.setPanel(parent.acceuil);

        } catch(SQLException e){
				JOptionPane.showMessageDialog(null,msg,"Erreur",JOptionPane.ERROR_MESSAGE);

			}
    }
    void remplirComboProduit (){
    	mat.removeAllItems();
    	String req;
		try{
		req="select distinct des_mp from matiere";
		res=parent.selection(req);
		while(res.next()){
			mat.addItem(res.getString(1));
		}
		} catch(SQLException e){
			JOptionPane.showMessageDialog(null,"erreur de chargement des produits","Erreur",JOptionPane.ERROR_MESSAGE);

			}
    }
}


class ReceptionProduit extends JPanel implements ActionListener{
    JCheckBox [] commandes;
    JButton val;
    ResultSet res;
    JPanel lst;
    String mat[] ,com[],qt[];
    private FenetrePrincipale parent;
    ReceptionProduit(FenetrePrincipale parent){
        this.parent = parent;
        setLayout(new BorderLayout());
        lst=new JPanel(new GridLayout (30,1));
        remplirCommandes();
        JScrollPane jsp= new JScrollPane(lst);
        jsp.setPreferredSize(new Dimension(300,250));
        val=new JButton("Valider");
        val.addActionListener(this);
        JPanel p= new JPanel();
        p.add(val);
        add(p,BorderLayout.SOUTH);
        add(jsp,BorderLayout.CENTER);

        parent.setPanel(this);


    }
    public void remplirCommandes (){
        int nbr=0;
        try {
            nbr=parent.count("select * from fournisseur f,matiere m,bon_commande b where b.code_mat=m.code_mp and b.code_four=f.code_four and etat=0");
            res = parent.selection("select b.qte,m.unite,m.des_mp,f.raison,b.code_mat,b.code_bc from fournisseur f,matiere m,bon_commande b where b.code_mat=m.code_mp and b.code_four=f.code_four and etat=0");
            commandes = new JCheckBox[nbr];
            mat=new String[nbr];
            com=new String [nbr];
            qt=new String [nbr];
            int i=0;
            while (res.next()){
                qt[i]=res.getString(1);
                commandes[i]=new JCheckBox(qt[i]+" "+res.getString(2)+"s de "+res.getString(3)+" de "+res.getString(4));
                com[i]=res.getString(6);
                mat[i]=res.getString(5);
                lst.add(commandes[i]);
                i++;
            }
           

        } catch (SQLException ex) {
            Logger.getLogger(ReceptionProduit.class.getName()).log(Level.SEVERE, null, ex);
           }
    }

    public void actionPerformed(ActionEvent e) {
        try {
        for(int i=0;i<com.length;i++){
            if(commandes[i].isSelected())
                parent.insertion("update bon_commande set etat=1 where code_bc="+com[i]);
                parent.insertion("update matiere set qte=qte+"+qt[i]+" where code_mp="+mat[i]);
                parent.setPanel(parent.acceuil);
        }
        } catch (SQLException ex) {
                Logger.getLogger(ReceptionProduit.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

}
class AjoutFournisseur extends JPanel implements ActionListener{
    JTextField rais,tel,fax,adr;
    JButton val;
    FenetrePrincipale parent;
    JCheckBox []produits;
    ResultSet res=null;
    int ln;

    public AjoutFournisseur(FenetrePrincipale Parent){
    	setLayout(new GridLayout(4,3));
        parent=Parent;
    	JPanel pr[] = new JPanel [12], sc []= new JPanel[10];
		for(int i=0;i<12;i++) pr[i]=new JPanel(new GridLayout());
		for(int i=0;i<10;i++) sc[i]=new JPanel();
		pr[3].setLayout(new GridLayout(3,1));
        pr[4].setLayout(new GridLayout(3,1));
        pr[5].setLayout(new GridLayout(3,1));
		sc[1].setLayout(new GridLayout(1,2));
		sc[1].add(sc[3]);
		sc[1].add(sc[4]);
        pr[3].add(new JLabel("                                         Raison           :"));
        pr[3].add(new JLabel("                                         Téléphone    :"));
        pr[3].add(new JLabel("                                         Adresse        :"));
		pr[5].add(new JPanel());
        pr[5].add(new JLabel("     :             Fax"));
        rais= new JTextField(10);
		tel= new JTextField(10);
		fax= new JTextField(10);
		adr= new JTextField(10);
		val= new JButton("Valider");
        val.addActionListener(this);
		sc[5].add(val);
		pr[10].add(sc[5]);
		sc[0].add(rais);
		sc[3].add(tel);
		sc[4].add(fax);
		sc[2].add(adr);
		for(int i=0;i<3;i++)pr[4].add(sc[i]);
		sc[6].setPreferredSize(new Dimension(250,80));
        remplirCheckProduit(sc[6]);
        JScrollPane jpn=new JScrollPane(sc[6]);
        jpn.setToolTipText("Specifiez les produits distribués par ce fournisseur");
		for(int i=0;i<11;i++) if(i==7) add(jpn);
		else add(pr[i]);
        parent.setPanel(this);



    }




    int remplirCheckProduit (JPanel p){
    	String req;
        ln=0;
        try{
		req="select distinct des_mp from matiere";
		ln=parent.count(req);
        res=parent.selection(req);
		produits=new JCheckBox [ln];
		int i=0;
        while(res.next()){
			produits[i]=new JCheckBox((res.getString(1)));
            p.add(produits[i++]);
		}
		} catch(SQLException e){
			JOptionPane.showMessageDialog(null,"erreur de chargement des produits","Erreur",JOptionPane.ERROR_MESSAGE);

			}
        return ln;

    }



    public void ajoutFournisseurSlot(){
        String msg="Erreur d'ajout du fournisseur car\n";
        if(rais.getText().isEmpty()) msg+="Le champ raison est vide\n";
        if(adr.getText().isEmpty()) msg+="Le champ adresse est vide\n";
        if(tel.getText().isEmpty()) msg+="Le champ téléphone est vide\n"; else msg+="Le champ téléphone est peut etre incompatible\n";
        if(fax.getText().isEmpty()) msg+="Le champ fax est vide\n"; else msg+="Le champ fax est peut etre incompatible\n";
        String prod=new String();
        for (int i=0;i<ln;i++){
        if(produits[i].isSelected()) prod+=produits[i].getLabel()+" ";
        }
        String req;
		try{
			req="insert into fournisseur (raison,num_tel,fax,adresse,produit) values('"+rais.getText()+"',"+tel.getText()+","+fax.getText()+",'"+adr.getText()+"','"+prod+"')";
			parent.insertion(req);
            parent.setPanel(parent.acceuil);
		} catch(SQLException e){
			JOptionPane.showMessageDialog(null,msg,"Erreur",JOptionPane.ERROR_MESSAGE);

			}



    }


    public void actionPerformed(ActionEvent e) {
        ajoutFournisseurSlot();
    }

}
class Acceuil extends JPanel{
    private FenetrePrincipale parent;
    Acceuil(FenetrePrincipale parent){
        this.parent = parent;
        this.setPreferredSize(new Dimension(550,400));
    }
    public void paint(Graphics g) {
				try {
					BufferedImage image = ImageIO.read(new File(parent.chemin+"/image.jpg"));
					g.drawImage(image, 0, 0, null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}


}
class FenetrePrincipale extends JFrame implements ActionListener {
	Connection con=null;
	public Statement st=null;
	JPasswordField connectPass;
	JTextField connectUser;
	JPanel principal,acceuil,ajoutProduitPanel,receptionProduitPanel,listeClientPanel,plateauPanel,AchatClientPanel,connectPanel,ajoutClientPanel,commandePanel,commandeClientPanel,etatStockPanel,ajoutFournisseurPanel,ajoutArticlePanel;
	JButton connectButton;
	boolean isConnected=false;
    JMenuBar barre;
    JMenu stock,article,client;
    JMenuItem ajoutProduitMenu,receptionProduitMenu,listeClientMenu,plateauMenu,AchatClientMenu,commandeProduitMenu,etatStockMenu,commandeClientMenu,ajoutFournisseurMenu,ajoutArticleMenu,ajoutClientMenu;
    public ResultSet res=null;
    CommandeRecurentes recurence;
    String chemin;






    public FenetrePrincipale() {
        chemin=System.getProperty("user.dir");
        if(chemin.contains("\\dist"))chemin=chemin.substring(0,chemin.indexOf("\\dist")+1);
        this.setIconImage(new ImageIcon(chemin+"/icone.gif").getImage());
        acceuil=new Acceuil(this);
    	principal= new JPanel();
       	barre= new JMenuBar();
    	stock=new JMenu("Stock");
        article=new JMenu("Articles");
        client=new JMenu("Clients");
        listeClientMenu=new JMenuItem ("Liste des clients");
        receptionProduitMenu=new JMenuItem ("Bon de reception");
        plateauMenu=new JMenuItem ("Preparer des produits");
        commandeClientMenu=new JMenuItem ("Effectuer une commande");
        ajoutClientMenu=new JMenuItem ("Ajouter un client");
    	ajoutArticleMenu=new JMenuItem ("Ajouter un article");
        ajoutProduitMenu=new JMenuItem ("Ajouter un produit");
    	commandeProduitMenu=new JMenuItem("Commander un produit");
    	etatStockMenu=new JMenuItem("Etat du stock");
        AchatClientMenu=new JMenuItem("Comptabiliser un achat");
    	ajoutFournisseurMenu=new JMenuItem("Ajouter un fournisseur");
    	barre.add(stock);
        barre.add(article);
        barre.add(client);
        client.add(ajoutClientMenu);
        client.add(commandeClientMenu);
        client.add(listeClientMenu);
        article.add(ajoutArticleMenu);
    	article.add(plateauMenu);
        stock.add(ajoutProduitMenu);
    	stock.add(commandeProduitMenu);
    	stock.add(etatStockMenu);
        client.add(AchatClientMenu);
    	stock.add(ajoutFournisseurMenu);
        stock.add(receptionProduitMenu);
        listeClientMenu.addActionListener(this);
        receptionProduitMenu.addActionListener(this);
        plateauMenu.addActionListener(this);
        commandeClientMenu.addActionListener(this);
        AchatClientMenu.addActionListener(this);
        ajoutClientMenu.addActionListener(this);
        ajoutArticleMenu.addActionListener(this);
    	ajoutFournisseurMenu.addActionListener(this);
    	etatStockMenu.addActionListener(this);
    	commandeProduitMenu.addActionListener(this);
    	this.setJMenuBar(barre);
    	barre.setVisible(false);
    	ajoutProduitMenu.addActionListener(this);
    	connecter();
    	this.setTitle("Chocoland");
    	this.setVisible(true);
    	this.setSize(550,400);
    	this.add(principal);
    	this.setResizable(true);



    	}



    public void connectSlot(){
    	String user="admin",pass="azerty";
    	if(user.equals(connectUser.getText())&&pass.equals(connectPass.getText())){
    	  	try{
    			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				con=DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="+chemin+"/choco.mdb");
				st=con.createStatement();
				isConnected=true;
				barre.setVisible(true);
                this.setPanel(acceuil);
				recurence= new CommandeRecurentes(this);
            } catch(ClassNotFoundException e){
                    JOptionPane.showMessageDialog(null,"Erreur de chargement du driver","Erreur",JOptionPane.ERROR_MESSAGE);
                } catch(SQLException e){
					e.printStackTrace();

				}
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(null,"Nom d'utilisateur ou mot de passe erroné","Erreur",JOptionPane.ERROR_MESSAGE);
    	}


    }


    ResultSet selection (String req) throws SQLException{
    	return st.executeQuery(req);
        }
    int count (String req) {
        int i = 0;
        try {

            res = st.executeQuery(req);
            while (res.next()) i++;

        } catch (SQLException ex) {
            Logger.getLogger(FenetrePrincipale.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
        }



    public void connecter(){

    	connectButton= new JButton("Valider");
    	connectPass= new JPasswordField(10);
        connectPass.addActionListener(this);
    	connectUser= new JTextField(10);
    	connectButton.addActionListener(this);
    	connectPanel=new JPanel(new GridLayout(3,3));
    	for(int i=0;i<4;i++) connectPanel.add(new JPanel());
    	JPanel centre=new JPanel(new GridLayout(3,1));
    	JPanel centreN=new JPanel();
    	JPanel centreC=new JPanel();
    	JPanel centreS=new JPanel();
    	centreN.add(connectUser,JPanel.BOTTOM_ALIGNMENT);
    	centreC.add(connectPass);
    	centreS.add(connectButton);
    	centre.add(centreN);
    	centre.add(centreC);
    	centre.add(centreS);
    	connectPanel.add(centre);
    	for(int i=0;i<4;i++) connectPanel.add(new JPanel());
    	principal.add(connectPanel);
        


    }
    public void insertion (String req) throws SQLException{
		st.executeUpdate(req);
	}
	public void deconnection (){
		try
		{con.close();
		st.close();
		} catch(SQLException e){
			e.printStackTrace();

			}
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==connectButton) connectSlot();
        if(e.getSource()==connectPass) connectSlot();
		if(e.getSource()==ajoutProduitMenu) ajoutProduitPanel=new AjoutProduit(this);
		if(e.getSource()==commandeProduitMenu) commandePanel=new Commander(this);
		if(e.getSource()==etatStockMenu) etatStockPanel=new EtatStock(this);
		if(e.getSource()==ajoutFournisseurMenu) ajoutFournisseurPanel=new AjoutFournisseur(this);
        if(e.getSource()==ajoutArticleMenu) ajoutArticlePanel=new Recette(this);
        if(e.getSource()==ajoutClientMenu) ajoutClientPanel=new AjoutClient(this);
        if(e.getSource()==commandeClientMenu) commandeClientPanel=new CommandeClient(this);
        if(e.getSource()==plateauMenu) plateauPanel=new Plateau(this);
        if(e.getSource()==AchatClientMenu) AchatClientPanel=new AchatClient(this);
        if(e.getSource()==receptionProduitMenu) receptionProduitPanel=new ReceptionProduit(this);
        if(e.getSource()==listeClientMenu) listeClientPanel=new ListeClient(this);
	}

public void setPanel(JPanel p){

    principal.removeAll();
	principal.add(p);
	principal.validate();
    this.repaint();
    
}



}
 class CommandeRecurente {
	String des,four,qte;
	public CommandeRecurente(String des,String four,String qte){
		this.des=des;
		this.four=four;
		this.qte=qte;
	}
}


class CommandeRecurentes{
	Vector <CommandeRecurente> v;
    private FenetrePrincipale parent;
    ResultSet res;

	public CommandeRecurentes(FenetrePrincipale parent){
		v=new Vector (10,5);
        this.parent = parent;
        remplirRecurence();
	}
	public int contains(String des) {
		for(int i=0;i<v.size();i++)
		if(v.elementAt(i).des==des ) return i;
		return -1;
	}
	public void add (String des,String four,String qte){
		if(contains(des)!=-1)	v.removeElementAt(contains(des));
		v.addElement(new CommandeRecurente(des,four,qte));

	}
	public CommandeRecurente at (String des){
		return v.elementAt(contains(des));
	}
    public void remplirRecurence(){
        try {
            res = parent.selection("select code_mat,code_four,qte from bon_commande where recurence=1");
            while(res.next()) this.add(res.getString(1),res.getString(2),res.getString(3));
        } catch (SQLException ex) {
            Logger.getLogger(CommandeRecurentes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FenetrePrincipale f= new FenetrePrincipale();
    	//f.insertion();
    	//f.deconnection();
    }

}
