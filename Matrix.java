
/*TO DO:
 * *convert for loops to for each loops
 * *write  exceptions incorrect dimensions matrix
 * *write lu factorisation
 * *write method for gaussian(reduced and row reduced)
 * *write methods for eigenvalues and eigenvectors
 * *write tentative factoring
 * *write better private adjoint
 * *Write default constructor
 * *finish unwritten methods
 */

public class Matrix
{
	private double matrix[][];
	public final int NUMBER_OF_ROWS, NUMBER_OF_COLUMNS;

	public Matrix (int r, int c){

		this.matrix = new double[r][c];
		this.NUMBER_OF_ROWS = r;
		this.NUMBER_OF_COLUMNS = c;
	}
	
	public Matrix(){}

	public void enterValues()
	{	
		java.util.Scanner keyboard = new java.util.Scanner(System.in);
		System.out.println(this.toString());
		for(double[] row:matrix)
		{
			for(double element : row)
			{
				element= keyboard.nextDouble();
			}
		}
	}

	public void setElement(int r, int c, double element)
	{ 
		this.matrix[r][c] = element;
	}

	public double getElement(int r, int c)
	{
		return this.matrix[r][c];
	}

	public String toString()
	{
		StringBuilder builder = new StringBuilder(this.NUMBER_OF_ROWS*this.NUMBER_OF_COLUMNS+3*this.NUMBER_OF_COLUMNS);
		for (double[] row : this.matrix) 
		{
			builder.append('[');
			for (double element : row)
			{
				//if(matrix[r][c]==null)System.out.print("-\t");
				builder.append(element);
				builder.append('\t');
			}
			builder.deleteCharAt(builder.length()-1);
			builder.append(']');
			builder.append('\n');
			
		}
		return builder.toString();
	}
	
	public boolean isLowerTriangular();
	public boolean isUpperTriangular();

	
	
	public boolean equals(Matrix other, double fakeZero)
	{
		for(int r = 0; r < NUMBER_OF_ROWS; r++){
			for(int c = 0; c < NUMBER_OF_COLUMNS; c++){
				if(Math.abs( this.getElement(r,c)-other.getElement(r,c))!=fakeZero){
					return false;
				}
			}
		}
		return true;	
	}
	
	public boolean equals(Matrix other){
		for(int r = 0; r < NUMBER_OF_ROWS; r++){
			for(int c = 0; c < NUMBER_OF_COLUMNS; c++){
				if(this.getElement(r,c)!=other.getElement(r,c)){
					return false;
				}
			}
		}
		return true;

	}

	private boolean canBeAdded(Matrix other)
	{
		return (this.NUMBER_OF_COLUMNS == other.NUMBER_OF_COLUMNS 
					&& this.NUMBER_OF_ROWS == other.NUMBER_OF_ROWS);
	}


	private boolean canBeMultiplied(Matrix other)
	{
		return (this.NUMBER_OF_COLUMNS == other.NUMBER_OF_ROWS);
	}

	private boolean isSquare()
	{
		return (this.NUMBER_OF_ROWS == this.NUMBER_OF_COLUMNS);
	}

	public Matrix add(double a, double b, Matrix other)
	{
		if(!canBeAdded(other))
		{
			return null;
		}
		return multiply(a).add(other.multiply(b));
	}

	public Matrix subtract(double a, double b, Matrix other)
	{
		if(!canBeAdded(other))
		{
			return null;
		}
		return multiply(a).add(other.multiply(-b));
	}

	public Matrix subtract(Matrix other)
	{
		if(!canBeAdded(other))
		{
			return null;
		}
		return add(other.multiply(-1));
	}

	public Matrix add(Matrix other)
	{
		if(!canBeAdded(other))
		{
			return null;
		}
		Matrix sum = new Matrix(NUMBER_OF_ROWS, NUMBER_OF_COLUMNS);
		for(int r = 0; r < this.NUMBER_OF_ROWS; r++)
		{
			for(int c = 0; c < this.NUMBER_OF_COLUMNS; c++)
			{
				sum.setElement(r, c, this.matrix[r][c]+other.getElement(r, c));
			}
		}
		return sum;
	}

	public Matrix multiply(double k)
	{
		Matrix product = new Matrix(NUMBER_OF_ROWS, NUMBER_OF_COLUMNS);
		for(int r = 0; r < this.NUMBER_OF_ROWS; r++)
		{
			for(int c = 0; c < this.NUMBER_OF_COLUMNS; c++)
			{
				product.setElement(r, c, k * this.matrix[r][c]);
			}
		}
		return product;
	}

	public Matrix multiply(Matrix other)
	{  
		if(!canBeMultiplied(other))
		{
			return null;
		}
		double value;
		Matrix product = new Matrix(this.NUMBER_OF_ROWS, other.NUMBER_OF_COLUMNS);
		for(int r = 0; r < product.NUMBER_OF_ROWS; r++)
		{  
			for(int c = 0;c < product.NUMBER_OF_COLUMNS; c++)
			{
				value=calculateEachValueM(this.NUMBER_OF_COLUMNS - 1, r, c, other);
				product.setElement(r, c, value); 
			}
		}
		return product;
	}

	private double calculateEachValueM(int m, int r,int c,Matrix other)
	{  
		if(m==0)
		{
			return this.matrix[r][0] * other.getElement(0, c);
		}
		return this.matrix[r][m]*other.getElement(m, c) + calculateEachValueM(m - 1, r, c, other);
	}


	public double calculateDeterminant()
	{
		if(!isSquare()){
			//throw
		}  
		switch(NUMBER_OF_COLUMNS)
		{
		case 0:
			//throw;
			break;
		case 1:
			return this.matrix[0][0];
		case 2:
			return this.matrix[0][0] * this.matrix[1][1] - this.matrix[0][1] * this.matrix[1][0];
		default:
			double det = 0;
			for(int s = 0; s < NUMBER_OF_COLUMNS; s++)
			{
				det += getElement(0, s) * getMinor(0, s).calculateDeterminant() * ((s % 2 == 0) ? 1 : -1);
			}
			return det;
		}
		return 0;
	}

	public Matrix getMinor(int i, int j)
	{
		Matrix minor=new Matrix(NUMBER_OF_ROWS - 1, NUMBER_OF_COLUMNS - 1);
		for(int r = 0; r < minor.NUMBER_OF_ROWS; r++)
		{
			for(int c = 0; c < minor.NUMBER_OF_COLUMNS; c++)
			{
				minor.setElement(r, c, this.getElement(((r >= i) ? r + 1 : r),((c >= j) ? c + 1 : c)));
			}
		}
		return minor;
	}

	public double getTrace() throws IncorrectDimensionsException
	{
		if(!isSquare())
		{throw new IncorrectDimensionsException();
		}
		double trace = 0;
		for(int s = 0; s < NUMBER_OF_COLUMNS; s++)
		{
			trace+=this.matrix[s][s];
		}
		return trace;
	}
	 public double makeDeterminant(){}
	

	

	public Matrix getTranspose()
	{
		Matrix trp=new Matrix(NUMBER_OF_COLUMNS, NUMBER_OF_ROWS);
		for(int rO = 0; rO < NUMBER_OF_ROWS; rO++)
		{
			for(int cO = 0; cO < NUMBER_OF_ROWS; cO++)
			{
				trp.setElement(cO, rO, getElement(rO, cO));
			}
		}
		return trp;
	}

	public Matrix getAdjoint() throws IncorrectDimensionsException
	{
		if(!isSquare())
		{
			throw IncorrectDimensionsException;
		}
		Matrix adj= new Matrix(this.NUMBER_OF_ROWS, this.NUMBER_OF_COLUMNS);
		changeToAdjoint(adj);
		return adj;
	}
	//this is a bad idea
	private void changeToAdjoint(Matrix adj)
	{
		for(int r = 0; r < this.NUMBER_OF_ROWS; r++)
		{
			for(int c = 0; c < this.NUMBER_OF_COLUMNS; c++)
			{
				adj.setElement(r, c, getMinor(r, c).calculateDeterminant() * (((r + c) % 2 == 1) ? 1 : -1));
			}
		}
	}

	public Matrix getInverse()throws IncorrectDimensionsException
	{
		if(!isSquare())
		{
			throw IncorrectDimensionsException;
		}
		Matrix inv = getAdjoint().getTranspose().multiply(1 / calculateDeterminant());
		return inv;
	}

	public void swapRows(int r1, int r2)
	{
		System.out.println("Swapping rows "+ r1 + " and " + r2);
		double[] temp = this.matrix[r1];
		this.matrix[r1] = this.matrix[r2];
		this.matrix[r2] = temp;
	}

	public void multiplyRow(int r, double k)
	{
		for(int c = 0; c < NUMBER_OF_COLUMNS; c++){
			this.matrix[r][c] *= k;
		}
	}

	public void addRow(int rowChanged, double k, int rowAdded)
	{
		for(int c = 0; c < this.NUMBER_OF_COLUMNS; c++)
		{
			this.matrix[rowChanged][c]+=k*this.matrix[rowAdded][c];	
		}
	}

//	public void augment(double[] b) throws
//	{
//		if(b.length!=this.NUMBER_OF_ROWS)
//		{
//			throw
//		}
//		Matrix augmented=new Matrix(this.NUMBER_OF_ROWS,this.NUMBER_OF_COLUMNS+1);
//		for(int r:)
//
//	}

	public void augment (Matrix other) throws IncorrectDimensionsException
	{
		if(other.NUMBER_OF_ROWS!=this.NUMBER_OF_ROWS)
		{
			throw new IncorrectDimensionsException();
		}
		Matrix augmented = new Matrix(this.NUMBER_OF_ROWS, this.NUMBER_OF_COLUMNS + other.NUMBER_OF_COLUMNS);
		for(int row = 0; row < NUMBER_OF_ROWS; row++)
		{
			for(int col = 0; col < this.NUMBER_OF_COLUMNS; col++)
			{
				augmented.setElement(row,col,getElement(row,col));
			}
			for(int col = this.NUMBER_OF_COLUMNS ,  c =0; col < augmented.NUMBER_OF_COLUMNS; col++,c++)
			{
				augmented.setElement(row,col,other.getElement(row,c));
			}
		}
	}
	public Matrix[] split (int... cols)
	{
		Matrix[] matrices = new Matrix[cols.length+1];
		
		for(int r=0; r<NUMBER_OF_ROWS; r++)
		{
			for(int c=0;c<cols[0];c++)
			{
				matrices[0].setElement(r,c, this.getElement(r,c));
			}
		}
		if (cols.length!=1){
			for (int m=1; m<cols.length;m++){
				for(int r=0; r<NUMBER_OF_ROWS; r++)
				{
					for(int c=0,s=cols[m-1];s<cols[m];c++,s++)
					{
						matrices[m].setElement(r,c, this.getElement(r,s));
					}
				}
			}
		}
		
		for(int r=0; r<NUMBER_OF_ROWS; r++)
		{		
			for(int c=cols[cols.length-1],s=0;c<NUMBER_OF_COLUMNS;c++,s++)
			{
				matrices[cols.length].setElement(r,s, this.getElement(r,c));
			}
		}
		return matrices;
	}
	
	public Matrix splitThisInTwo(){}

	public void reduce(){
		boolean checkFor1=true;
				if (true);
	}
	public Matrix factoriseLeft(){}
	public Matrix factoriseRight(){}
	public Matrix[] factoriseLUP(){}
	public static Matrix getZero(int r,int c)
	{
		Matrix zero=new Matrix(r,c);
		return zero;
	}
	
	public  static Matrix getIdentity(int n)
	{
		Matrix identity;
			identity=new Matrix(n, n);
			for(int s=0; s < n; s++)
			{
				identity.setElement(s, s, 1.0);
			}
		return identity;
	}
	
}


