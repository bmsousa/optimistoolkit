/**
 * Copyright (C) 2010-2013 Barcelona Supercomputing Center
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package eu.optimis.ecoefficiencytool.core.tools;

/**For calculating a simple linear regression.
 * Adapted from http://www-stat.stanford.edu/~naras/java/course/lec2/
 * Obtained by jsubirat from http://www.koders.com/java/fid9CE2040E2918F56FAAE00E5C58C34479304E24FC.aspx?s=linear+regression#L4
 * Balasubramanian Narasimhan, Stanford
 * */
public class LinearRegression {

	/**An example.*/
	/*public static void main(String[] args) {
		double[] x = {38, 56, 59, 64, 74};
		double[] y = {41, 63, 70, 72, 84};
		LinearRegression lr = new LinearRegression(x, y);
		System.out.println(lr.getRoundedModel());
		System.out.println("calculate y given an x of 38 "+lr.calculateY(38));
		System.out.println("calculate x given a y of 41 "+lr.calculateX(41));
	}*/

	//fields
	private double[] x;
	private double[] y;
	private double meanX;
	private double meanY;
	private double slope;
	private double intercept;
	private double stndDevX;
	private double stndDevY;

	//constructor
	public LinearRegression(double[] x, double[] y) {
		this.x = x;
		this.y = y;
		compute();
                //System.out.println("**ECO-REMOVE: Regression line built. Slope: " + slope + " Interception: " + intercept);
	}

	//methods
	/**Performs linear regression*/
	private void compute() {
		double n = x.length;
		double sumy = 0.0,
		sumx = 0.0,
		sumx2 = 0.0,
		sumy2 = 0.0,
		sumxy = 0.0;
		for (int i = 0; i < n; i++) {
			sumx += x[i];
			sumx2 += x[i] * x[i];
			sumy += y[i];
			sumy2 += y[i] * y[i];
			sumxy += x[i] * y[i];
		}
		meanX = sumx / n;
		meanY = sumy / n;
                double numer = sumxy - sumx * meanY;
                double denomin = sumx2 - sumx * meanX;
                if(numer == 0.0) {
                    slope = 0.0;
                } else {
                    //To avoid having infinity, assume the value will remain constant for the next assessment.
                    if(denomin == 0.0) {
                        slope = 0.0;
                    } else {
                        slope = (sumxy - sumx * meanY) / (sumx2 - sumx * meanX);
                    }
                }
		intercept = meanY - slope * meanX;
		stndDevX = Math.sqrt((sumx2 - sumx * meanX) / (n - 1));
		stndDevY = Math.sqrt((sumy2 - sumy * meanY) / (n - 1));
	}

	/**Return approximated Y value, good for a single interpolation, multiple calls are inefficient!*/
	public static double interpolateY(double x1, double y1, double x2, double y2, double fixedX ){
		double[] x = {x1, x2};
		double[] y = {y1, y2};
		LinearRegression lr = new LinearRegression(x,y);
		return lr.calculateY(fixedX);
	}

	/**Return approximated X value, good for a single interpolation, multiple calls are inefficient!*/
	public static double interpolateX(double x1, double y1, double x2, double y2, double fixedY ){
		double[] x = {x1, x2};
		double[] y = {y1, y2};
		LinearRegression lr = new LinearRegression(x,y);
		return lr.calculateX(fixedY);
	}

	//getters
	public double getSlope() {
		return slope;
	}
	public double getIntercept() {
		return intercept;
	}
	public double getRSquared() {
		double r = slope * stndDevX / stndDevY;
		return r * r;
	}
	public double[] getX() {
		return x;
	}
	/**Returns Y=mX+b with full precision, no rounding of numbers.*/
	public String getModel(){
		return "Y= "+slope+"X + "+intercept+" RSqrd="+getRSquared();
	}
	/**Returns Y=mX+b */
	/*public String getRoundedModel(){
		return "Y= "+Num.formatNumber(slope,3)+"X + "+Num.formatNumber(intercept,3)+" RSqrd="+ Num.formatNumber(getRSquared(),3);
	}*/
	/**Calculate Y given X.*/
	public double calculateY (double x){
		return slope*x+intercept;
	}
	/**Calculate X given Y.*/
	public double calculateX (double y){
		return (y-intercept)/slope;
	}
	/**Nulls the x and y arrays.  Good to call before saving.*/
	public void nullArrays(){
		x = null;
		y = null;
	}
}
