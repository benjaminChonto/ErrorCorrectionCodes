package ecc;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.List;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class CodeFrame extends JFrame{
	
	private CodeCollection data = new CodeCollection();
	
	public void init() {
		JTable table = new JTable();
		table.setModel(data);
	    RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(data);
	    table.setRowSorter(sorter);
		JScrollPane jsp = new JScrollPane(table);
		add(jsp,BorderLayout.CENTER); 
	}
	
	public CodeFrame() {
		super("Database of error corrections");
		try {
			data = new CodeCollection();
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("database.dat"));
			data.codes = (List<CodeData>)ois.readObject();
			ois.close();
		}catch(EOFException exc) { /*The file is empty, it's okay*/ } 
		catch(Exception ee) {ee.printStackTrace();}
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(500,300));
		init();
		setLocationRelativeTo(null);
	}
	//Serializes the collection
	public void save() {
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("database.dat"));
			os.writeObject(data.codes);
			os.close();
		}catch(IOException ex) {ex.printStackTrace();}
	}
	//Adds a new record to the collection and saves it
	public void addCode(CodeData cd) {
		data.codes.add(cd);
		save();
	}

}
