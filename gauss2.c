//####################################################################
//Using Gaussian elimination to solve linear equations.
// In this version, we allow matrix of any size. This is done by treating
// the name of a 2-dimensional array as pointer to the beginning of the
// array. This makes use of the fact that arrays in C are stored in
// row-major order.
//####################################################################
#include <stdio.h>

//To disable PIVOTING, comment the next line
#define PIVOTING


//==================================================================
// prints a vector of doubles, one per line.
//==================================================================
void print_dvector(double v[], int w)
{
    int i;
    for(i=0; i<w; i++) {
	printf("%8.3f\n", v[i]);
    }
}

//==================================================================
// prints a matrix of double numbers.
// Caller should pass the address of the 1st element of a 2-d array.
//==================================================================
void print_dmatrix(double* m, int h, int w)
{
    int i, j;
    for(i=0; i<h; i++) {
        for(j=0; j<w; j++)
            printf("%8.3f  ", *(m+i*w+j));
        printf("\n");
    }
    printf("\n");
}


//==================================================================
// Gauss elimination with pivoting
//==================================================================
void genp(double* m, double v[], int h, int w)
{
    int row, next_row, col;
    double factor;

    #ifdef PIVOTING
    int max_row;
    double tmp;
    #endif
    
    for(row = 0; row < (h - 1); ++row) {
        printf("### row = %d\n", row);

        #ifdef PIVOTING
        //find out which row has the max number in the row-th column
        max_row = row;
        for(next_row = row + 1; next_row < h; ++next_row) {
            if(*(m+next_row*w + row) > *(m+max_row*w + row))
                max_row = next_row;
        }
        printf("  max_row = %d\n", max_row);

        //swap row with max_row
        if(max_row != row) {
            for(col = 0; col < w; ++col) {
                tmp = *(m+row*w + col);
                *(m+row*w + col) = *(m+max_row*w + col);
                *(m+max_row*w + col) = tmp;
            }
            printf("  After swapping row with max_row:\n");
            print_dmatrix(m, h, w);
            // also swap the v vector
            tmp = v[row];
            v[row] = v[max_row];
            v[max_row] = tmp;
	    print_dvector(v, w);
        }
        #endif //PIVOTING

        // do the elimination
        for(next_row = row + 1; next_row < h; ++next_row) {
            factor = *(m+next_row*w + row) / *(m+row*w + row);

            for(col = 0; col < w; ++col)
                *(m+next_row*w + col) -= factor * (*(m+row*w + col));

            v[next_row] -= factor * v[row];
        }
        printf("  After elimination round %d:\n", row);
        print_dmatrix(m, h, w);
	print_dvector(v, w);
    }
}

void back_substitute(double* m, double v[],
                     int h, int w){
  int row, next_row;

  for(row = h - 1; row >= 0; --row) {
    v[row] /= *(m+row*w + row);
    *(m+row*w + row) = 1;
    for(next_row = row - 1; next_row >= 0; --next_row) {
      v[next_row] -= v[row] * (*(m+next_row*w + row));
      *(m+next_row*w + row) = 0;
    }
    printf("*** row= %d\n", row);
    print_dmatrix(m, h, w);
    print_dvector(v, w);
  }
}


int main()
{
    double a[4][4]= {{2,-3,2,5},
                     {1,-1,1,2},
                     {3,2,2,1},
                     {1,1,3,-1}};
    double v[4]= {3,1,0,0};

    printf("Initially:\n");
    print_dmatrix((double*)a, 4, 4);
    print_dvector(v, 4);
    genp((double*)a, v, 4, 4);
    printf("\n\nBack substitution:\n");
    back_substitute((double*)a, v, 4, 4);
}
