
/*TO DO:
 * *convert for loops to for each loops
 * *write  exceptions : NotSquareMatrix, NotSameDimensions, NotValidMatrix
 * *write lu factorisation
 * *write method for gaussian(reduced and row reduced)
 * *write methods for eigenvalues and eigenvectors
 * *write tentative division/factoring
 * *write separate, private/protected transpose and adjoint methods without exceptions and new for the inverse
 * *Write default constructor
 */

public class Matrix
{
	private double matrix[][];
	private java.util.Scanner keyboard = new java.util.Scanner(System.in);
	public final int NUMBER_OF_ROWS, NUMBER_OF_COLUMNS;

	public Matrix (int r, int c){

		this.matrix = new double[r][c];
		this.NUMBER_OF_ROWS = r;
		this.NUMBER_OF_COLUMNS = c;
	}

	public void enterValues()
	{
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
		String builder = "";
		for (double[] row : this.matrix) 
		{
			builder = builder + ("[");
			for (double element : row)
			{
				//if(matrix[r][c]==null)System.out.print("-\t");
				builder=builder + (element + "\t");
			}
			builder= builder.substring(0, builder.length() - 1 ) + ("]\n");
		}
		return builder;
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

	public double getTrace throws()
	{
		if(!isSquare())
		{throw
		}
		double trace = 0;
		for(int s = 0; s < NUMBER_OF_COLUMNS; s++)
		{
			trace+=this.matrix[s][s];
		}
		return trace;
	}

	

	

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

	public Matrix getAdjoint() throws
	{
		if(!isSquare())
		{
			throw;
		}
		Matrix adj= new Matrix(this.NUMBER_OF_ROWS, this.NUMBER_OF_COLUMNS);
		for(int r = 0; r < this.NUMBER_OF_ROWS; r++)
		{
			for(int c = 0; c < this.NUMBER_OF_COLUMNS; c++)
			{
				adj.setElement(r, c, getMinor(r, c).calculateDeterminant() * (((r + c) % 2 == 1) ? 1 : -1));
			}
		}
		return adj;
	}

	public Matrix getInverse()throws
	{
		if(!isSquare())
		{
			throw;
		}
		Matrix inv = getAdjoint().getTranspose().multiply(1 / calculateDeterminant());
		return inv;
	}

	public void swapRows(int r1, int r2)
	{
		System.out.println("Swapping rows "+ r1 + " and " r2);
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

	public void augment(double[] b) throws
	{
		if(b.length!=this.NUMBER_OF_ROWS)
		{
			throw
		}
		Matrix augmented=new Matrix(this.NUMBER_OF_ROWS,this.NUMBER_OF_COLUMNS+1);
		for(int r:)

	}

	public void augment (Matrix other) throws
	{
		if(other.NUMBER_OF_ROWS!=this.NUMBER_OF_ROWS)
		{
			throw
		}
		Matrix augmented = new Matrix(this.NUMBER_OF_ROWS, this.NUMBER_OF_COLUMNS + other.NUMBER_OF_COLUMNS);
		for(int row = 0, row < NUMBER_OF_ROWS, row++)
		{
			for(int col = 0, col < augmented.NUMBER_OF_COLUMNS, col++)
			{
				augmented.setElement(row,col,(col<NUMBER_OF_COLUMNS?(getElement(row,col)):other.getElement(row,col)));
			}
		}
	}
	public Matrix[] split (int col)
	{
		Matrix[] matrices = {new Matrix(row,col), new Matrix(row,NUMBER_OF_COLUMNS-col)};
		for(int r=0, r<NUMBER_OF_ROWS, r++)
		{
			for(int c=0,c<col,c++)
			{
				matrices[0].setElement(r,c, this.getElement(row,c));
			}
			for(int c=col,c<NUMBER_OF_COLUMNS,c++)
			{
				matrices[1].setElement(r,c-col, this.getElement(r,col));
			}
		}
	}

	public void reduce(){
		boolean checkFor1=true
				if ()
	}
	public static Matrix getZero(r,c)
	{
		Matrix zero=new Matrix(r,c);
		return zero;
	}public  static Matrix getIdentity(int s)
	{
		Matrix identity;
		
		
			identity=new Matrix(s, s);
			for(int s=0; s < NUMBER_OF_COLUMNS; s++)
			{
				identity.setElement(s, s, 1.0);
			}
			
			

		
		return identity;
	}
	
}

