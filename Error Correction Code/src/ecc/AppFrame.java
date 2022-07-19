package ecc;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AppFrame extends JFrame {
	

	private static final long serialVersionUID = 1L;
	private Object[] err = {1,2,3};
	private JComboBox jcb = new JComboBox(err);
	
	JLabel BLSlabel = new JLabel("Systematic Block code");
	JLabel Hamminglabel = new JLabel("Hamming code");
	JLabel RSlabel = new JLabel("Reed-Solomon code");;
	
	private JTextField BLS_k = new JTextField();
	private JTextField BLS_n = new JTextField();
	private JTextField Hamming_k = new JTextField();
	private JTextField Hamming_n = new JTextField();
	private JTextField RS_k = new JTextField();
	private JTextField RS_n = new JTextField();
	
	private JButton compute = new JButton("Compute!");
	private JButton ok = new JButton("OK!");
	private JButton DataB = new JButton("Show Results");
	private JButton delete = new JButton("Clear");
	
	private JLabel BLSResult = new JLabel();
	private JLabel BLSTime = new JLabel();
	private JLabel HammingResult = new JLabel();
	private JLabel HammingTime = new JLabel();
	private JLabel RSResult = new JLabel();
	private JLabel RSTime = new JLabel();
	
	public AppFrame() {
		super("Error Correcting Codes");
		JPanel t = new JPanel();
		JPanel init = new JPanel(new GridLayout(3,3));
		JPanel res = new JPanel(new GridLayout(2,4));
		JPanel main = new JPanel(new GridLayout(6,1));
		JLabel errT = new JLabel("Number of Errors: ");
		ok.addActionListener(new OKListener());
		t.add(errT); t.add(jcb); t.add(ok);
		
		JPanel Bmessage = new JPanel(new GridLayout(1,2));
		JPanel Hmessage = new JPanel(new GridLayout(1,2));
		JPanel RSmessage = new JPanel(new GridLayout(1,2));
		Bmessage.add(new JLabel("message: ")); Hmessage.add(new JLabel("message: ")); 
		RSmessage.add(new JLabel("message: "));
		Bmessage.add(BLS_k); Hmessage.add(Hamming_k); RSmessage.add(RS_k); 
		
		JPanel Berr = new JPanel(new GridLayout(1,2));
		JPanel Herr = new JPanel(new GridLayout(1,2));
		JPanel RSerr = new JPanel(new GridLayout(1,2));
		Berr.add(new JLabel("error: ")); Herr.add(new JLabel("error: ")); 
												RSerr.add(new JLabel("error: "));
		Berr.add(BLS_n); Herr.add(Hamming_n); RSerr.add(RS_n); 
		
		init.add(BLSlabel);
		init.add(Hamminglabel); 
		init.add(RSlabel);
		init.add(Bmessage); init.add(Hmessage); init.add(RSmessage);
		init.add(Berr); init.add(Herr); init.add(RSerr);
		
		res.add(BLSResult); res.add(HammingResult); res.add(RSResult);
		res.add(BLSTime); res.add(HammingTime); res.add(RSTime);
		
		JLabel resultLabel = new JLabel("Results:");
		
		compute.addActionListener(new ComputeListener());
		DataB.addActionListener(new DataBaseListener());
		delete.addActionListener(new ClearButtonListener());
		
		JPanel db = new JPanel(); db.add(DataB); db.add(delete);
		main.add(t); main.add(init); main.add(compute); main.add(resultLabel); main.add(res);
		main.add(db);
		add(main); setSize(600,400); this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	public ArrayList<Integer> StringToList(String s) {
		ArrayList<Integer> res = new ArrayList<>();
		for(int i=0; i<s.length(); ++i)
			res.add(Character.getNumericValue(s.charAt(i)));
		return res;
	}
	
	//To show the parameters of each coder
	public class OKListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int t = (int)jcb.getSelectedItem();
			try {
				BLS b = new BLS(t);
				Hamming h = new Hamming();
				ReedSolomon rs = new ReedSolomon(t);
				int bk = b.getMessageLen(); int bn = b.getCodeLen();
				int hk = h.getMessageLen(); int hn = h.getCodeLen();
				int rk = rs.getMessageLen();int rn = rs.getCodeLen();
				
				BLSlabel.setText("Systematic Block code ("+bk +","+bn+")");
				Hamminglabel.setText("Hamming code ("+hk+","+hn+")");
				RSlabel.setText("Reed-Solomon code ("+rk+","+rn+")"+"inGF("+rs.getGF()+")");
				
				BLS_n.setText(""); Hamming_n.setText(""); RS_n.setText("");
				BLS_k.setText(""); Hamming_k.setText(""); RS_k.setText("");
				
			} catch (FileNotFoundException | InvalidRowException e1) {e1.printStackTrace();}
		}
	}
	
	public void serialize(CodeData cd) {
		CodeFrame c = new CodeFrame();
		c.addCode(cd);
		c.dispose();
	}
	
	public class DataBaseListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			try{
				CodeFrame cf = new CodeFrame();
				cf.setVisible(true);
			}catch(Exception ex) {System.out.println("The files is still empty!");}
		}
		
	}
	public class ClearButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				new FileOutputStream("database.dat").close();
			} catch (IOException e1) { e1.printStackTrace(); }
		}
	}
	
	public class ComputeListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e){
			//BLS
			int t = (int)jcb.getSelectedItem();
			try {
				if(!(BLS_k.getText().equals("")||BLS_n.getText().equals(""))) {
					double start = System.currentTimeMillis();
					BLS b = new BLS(t);
					ArrayList<Integer> message = new ArrayList<>(StringToList(BLS_k.getText()));
					ArrayList<Integer> code = b.encode(message);
					ArrayList<Integer> error = new ArrayList<>(StringToList(BLS_n.getText()));
					if(error.size() < b.getCodeLen()) {
						for(int i=error.size(); i<b.getCodeLen(); ++i)
							error.add(0);
					}
					ArrayList<Integer> recieved = new ArrayList<>(b.add(error, code));
					ArrayList<Integer> result = b.decode(recieved);
					double end = System.currentTimeMillis();
					BLSResult.setText(result.toString());
					BLSTime.setText(Double.toString(end-start) + "ms");
					boolean correct = false;
					if(message.equals(result)) correct = true;
					CodeData cd = new CodeData("Systematic Block code",correct,end-start);
					serialize(cd);
				}
			} catch (FileNotFoundException | InvalidRowException e1) {
				BLSResult.setText("Invalid parameters!");
				System.err.println(e1);
				}
			//Hamming
			try {
				if(!(Hamming_k.getText().equals("")||Hamming_n.getText().equals(""))) {
					double start = System.currentTimeMillis();
					Hamming h = new Hamming();
					ArrayList<Integer> message = new ArrayList<>(StringToList(Hamming_k.getText()));
					ArrayList<Integer> code = h.encode(message);
					ArrayList<Integer> error = new ArrayList<>(StringToList(Hamming_n.getText()));
					if(error.size() < h.getCodeLen()) {
						for(int i=error.size(); i<h.getCodeLen(); ++i)
							error.add(0);
					}
					ArrayList<Integer> recieved = new ArrayList<>(h.add(error, code));
					ArrayList<Integer> result = h.decode(recieved);
					double end = System.currentTimeMillis();
					HammingResult.setText(result.toString());
					HammingTime.setText(Double.toString(end-start) + "ms");
					boolean correct = false;
					if(message.equals(result)) correct = true;
					CodeData cd = new CodeData("Hamming code",correct,end-start);
					serialize(cd);
				}
			} catch (FileNotFoundException | InvalidRowException e1) {
				HammingResult.setText("Invalid parameters");
				System.out.println(e1);
				}
			//Reed Solomon
			if(RS_k.getText().equals("")||RS_n.getText().equals(""))
				return;
			try {
				double start = System.currentTimeMillis();
				ReedSolomon rs = new ReedSolomon(t);
				ArrayList<Integer> message = new ArrayList<>(StringToList(RS_k.getText()));
				ArrayList<Integer> code = rs.encode(message);
				ArrayList<Integer> error = new ArrayList<>(StringToList(RS_n.getText()));
				ArrayList<Integer> recieved = new ArrayList<>(rs.add(error,code));
				ArrayList<Integer> result = rs.decode(recieved);
				double end = System.currentTimeMillis();
				RSResult.setText(result.toString());
				RSTime.setText(Double.toString(end-start) + "ms");
				boolean correct = false;
				if(message.equals(result)) correct = true;
				CodeData cd = new CodeData("Reed-Solomon code",correct,end-start);
				serialize(cd);
			}catch(InvalidRowException e1) {
				RSResult.setText("The error cannot be corrected!");
				System.out.println(e1);
			}
		}
		
	}
	
	static public void main(String args[]) throws IOException, InvalidRowException {
		
		AppFrame app = new AppFrame();
		app.setVisible(true);
	}

}
