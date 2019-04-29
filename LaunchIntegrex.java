import java.awt.Container;
import java.util.regex.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.*;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

/* ============================================================ 
 * Name: Abhishek Kapoor and Victor Cho 
 * Date of Completion: April 28, 2019 
 * Location: Toronto, Canada 
 * Summary: Utilizes the Apache Commons Math library 
 * to calculate the definite integrals of a polynomial 
 * function specified by the user 
 =============================================================*/

public class LaunchIntegrex extends JPanel implements ActionListener 
{

	JFrame frame;
	JLabel lblLogo; 
	//JLabel lblSymbol; 
	JButton btnCompute;
	JButton btnExit;
	JTextField txtEnterFunction; 
	JTextField txtUpperBound; 
	JTextField txtLowerBound; 
	JLabel lblIntegral; 
	JLabel lblDx; 
	int[] negativeIndices = initializeZeros(); 
	
	public LaunchIntegrex()
	{
		frame = new JFrame(" | Welcome to Integrex | ");

	    lblLogo = new JLabel(new ImageIcon(this.getClass().getResource("/imageFiles/integrexlogo.png")));
	    lblLogo.setBounds(50, 5, 540, 180);
	    
	    lblIntegral = new JLabel(new ImageIcon(this.getClass().getResource("/imageFiles/displayIntegral.png")));
	    lblIntegral.setBounds(150, 195, 120, 200);
	    
	    lblDx = new JLabel(new ImageIcon(this.getClass().getResource("/imageFiles/dxlogo.png")));
	    lblDx.setBounds(385, 275, 50, 50);
	    
	    btnCompute = new JButton(" Compute ---> ");
	    btnCompute.setBounds(500,450,120,20);
	    btnCompute.addActionListener(this);
	    
	    btnExit = new JButton(" <--- Exit ");
	    btnExit.setBounds(10,450,120,20);
	    btnExit.addActionListener(this);
	    
	    txtEnterFunction = new JTextField();
	    txtEnterFunction.setBounds(240, 290, 150, 20);
	    
	    txtUpperBound = new JTextField();
	    txtUpperBound.setBounds(220, 200, 50, 20);
	    
	    txtLowerBound = new JTextField();
	    txtLowerBound.setBounds(155, 365, 50, 20);
	    
	    frame.setContentPane(new Container());
	    frame.add(btnCompute); 
	    frame.add(btnExit); 
	    frame.add(lblLogo); 
	    frame.add(txtEnterFunction);  
	    frame.add(txtUpperBound);
	    frame.add(txtLowerBound); 
	    frame.add(lblIntegral); 
	    frame.add(lblDx); 
	    
	    frame.setResizable(true);
		frame.pack(); 
		frame.setSize(630,495);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(200, 200);
		frame.setVisible(true);
		frame.revalidate();
	}
	
	public int[] initializeZeros()
	{
		int[] temp = new int[20]; 
		for(int i = 0;i<temp.length;i++)
			temp[i] = 0; 
		return temp; 
	}
	
	public String[] capture(String function)
	{
		Pattern pattern = Pattern.compile("([+-]?[^-+]+)");
		Matcher matcher = pattern.matcher(function);
		String[] coArray = new String[20]; 
		int x = 0; 
		
		while (matcher.find()) 
			coArray[x++] = matcher.group(1); 
		return coArray; 
		
	}
	
	
	public String[] extractCoefficients(String function)
	{

		String[] coArray = capture(function); 
		
		for(int e = 0;e<coArray.length;e++)
		{   
			
			if(coArray[e] != null)
			{
				int indexFound = coArray[e].indexOf("x");
				if(coArray[e].indexOf("-") >= 0)
					negativeIndices[e] = 1;  
				
				if(indexFound >= 0)
					coArray[e] = coArray[e].substring(0, indexFound); 
			}	
			

		}
		
		for(int k = 0;k<coArray.length;k++)
		{
			if(coArray[k] != null)
			{
				if(coArray[k].indexOf("-") < 0)
					coArray[k] = coArray[k].replaceAll("^[\\D+]", "");	
				coArray[k] = coArray[k].replaceAll(" ", "");
			}
		}
		
		return coArray;  
		
	}
	
	
	public String[] extractPowers(String function)
	{
		String[] poArray = capture(function); 		
		
		for(int k = 0;k<poArray.length;k++)
		{
			if(poArray[k] != null) 
			{
				if(poArray[k].indexOf("^") < 0) // 12x + 155 
				{
					if(poArray[k].indexOf("x") < 0)
						poArray[k] = ""+0;
					else 
						poArray[k] = ""+1; 
				} 
				else 
					poArray[k] = poArray[k].substring(poArray[k].indexOf("^")+1, poArray[k].length());
			}
		}
		
		
		for(int p = 0;p<poArray.length;p++)
		{
			if(poArray[p] != null)
				poArray[p] = poArray[p].replaceAll("^\\D+", "");
		}
		
		 
		return poArray; 

	}
	
	
	public int[] antiZero(int[] ar)
	{
		int l = 0; 
		while(ar[l] != 0)
			l++; 
		
		int[] newOne = new int[l]; 
		
		int k = 0; 
		
		while(k != ar.length)
		{
			if(ar[k] != 0)
				newOne[k] = ar[k];
			k++;
		}
		
		return newOne; 
	}
	
	public int[] antiZeroPower(int[] ar, int length)
	{
		
		int[] newOne = new int[length]; 
		
		int k = 0; 
		
		while(k != length)
		{
			newOne[k] = ar[k];
			k++;
		}
		
		return newOne; 
	}
	
	public int[] removeNulls(String[] coArray)
	{
		int[] nullsRemoved = new int[coArray.length];
		for(int i = 0;i<coArray.length;i++)
		{
			if(coArray[i] != null && Integer.parseInt(coArray[i].trim()) != 0)
				nullsRemoved[i] = Integer.parseInt(coArray[i].trim()); 
		}
		
		return nullsRemoved; 
	}
	
	public ArrayList<Integer> getFinalArray(String [] coArray, String[] poArray)
	{
		
		int[] finalCoef = antiZero(removeNulls(coArray)); 
		int[] finalPwrs = antiZeroPower(removeNulls(poArray), finalCoef.length); 
		
        boolean zeroFound = false; 
        if(finalPwrs[finalPwrs.length-1] == 0)
        	zeroFound = true; 
		ArrayList<Integer> al = new ArrayList<Integer>();
		

		if(finalPwrs.length > 1)
		{
			for(int p = 0;p<finalCoef.length-1;p++)
			{
	
				al.add(finalCoef[p]);
				int difference = finalPwrs[p] - finalPwrs[p+1];
				
				if(difference != 1)
				{
					for(int q = 0;q<difference-1;q++)
						al.add(0); 
				}
	
			}
			al.add(finalCoef[finalCoef.length-1]);	
		}
		else 
		{
			al.add(finalCoef[0]);
			for(int i = 0;i<finalPwrs[0]-1;i++)
				al.add(0); 
		}
		
	    if(!zeroFound)
	    	al.add(0); 
		return al; 
		
	}
	
	public double[] convertToDouble(int[] arrayToConvert)
	{
		double[] dArray = new double[arrayToConvert.length]; 
		for(int k = 0;k<arrayToConvert.length;k++)
			dArray[k] = (double)arrayToConvert[k];  
		return dArray; 
	}
	
	public int[] convertToArray(ArrayList<Integer> al)
	{
		int[] theArray = new int[al.size()]; 
		for(int i = 0;i<al.size();i++)
			theArray[i] = al.get(i); 
		return theArray; 
	}
	
	public int[] reverse(int[] theArray)
	{
		int[] reversed = new int[theArray.length]; 
		int k = 0; 
		for(int i = theArray.length -1; i>=0;i--)
			reversed[k++] = theArray[i]; 
		return reversed; 
	}
	
	public int[] extractBounds()
	{
		int[] bounds = new int[2]; 
		
		String upper = txtUpperBound.getText().trim(); 
		int upperInt = Integer.parseInt(upper); 
		
		String lower = txtLowerBound.getText().trim(); 
		int lowerInt = Integer.parseInt(lower);
		
		if(upperInt < 0 || lowerInt < 0)
			JOptionPane.showMessageDialog(null, "Bound cannot be negative!", "Error", JOptionPane.ERROR_MESSAGE);
			
		bounds[0] = lowerInt; 
		bounds[1] = upperInt; 
		return bounds; 
	}
	
	public static void main(String[] args)
	{
		LaunchIntegrex obj = new LaunchIntegrex();
		obj.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		if(event.getActionCommand() == " Compute ---> ")
		{
			String[] coefficients = extractCoefficients(txtEnterFunction.getText().trim());
			String[] powers = extractPowers(txtEnterFunction.getText().trim());

			ArrayList<Integer> finalArray = getFinalArray(coefficients, powers);
			int[] toConstructor = reverse(convertToArray(finalArray)); 
			PolynomialFunction p = new PolynomialFunction(convertToDouble(toConstructor)); 
			TrapezoidIntegrator p1 = new TrapezoidIntegrator();
			
			int[] bounds = extractBounds(); 
			double integralAnswer = p1.integrate(100000000, p, bounds[0], bounds[1]);
			
			JOptionPane.showMessageDialog(null, "The integral evaluates to: "+integralAnswer, "Success!", JOptionPane.OK_OPTION); 
			
		}
	}
	
}
