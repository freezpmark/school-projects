#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <omp.h>
#include <mpi.h>

#ifdef __APPLE__
  #include <GLUT/glut.h>
#else
  #include <GL/freeglut.h>
  #include <GL/freeglut_ext.h>
#endif

#define PSIZE 512
#define KSIZE 75
#define TAMOUNT 4

typedef struct {
    char r;
    char g;
    char b;
} pixel;

pixel imageNew[PSIZE][PSIZE];
pixel image[PSIZE][PSIZE];
GLuint texture;

void init(int rank, int size) {

    // read binary image
    if(rank == 0){
        FILE *fr;
        if ((fr = fopen("image.rgb", "rb")) == NULL) {
            printf("Error opening file!\n");
            exit(0);
        }
        fread(image, sizeof(image), 1, fr);
        fclose(fr);
    }

    clock_t start = clock();

	// kernel initialization
	int i, j, y, kernel[KSIZE][KSIZE];
	for(i = 0; i < KSIZE; i++)
        for(j = 0; j < KSIZE; j++)
            kernel[i][j] = 0;
    kernel[KSIZE/2-1][KSIZE/2+1] = 1;
    kernel[KSIZE/2-3][KSIZE/2+3] = -1;

    // alloc space for global and local buffers with the size of image with rgb values
    char *globData = malloc(PSIZE*PSIZE*3*sizeof(char));
    char *locData  = malloc(PSIZE*PSIZE*3*sizeof(char));

    // load picture into local data buffer for main process
    if(rank == 0)
        for(i = y = 0; i < PSIZE; i++)
            for(j = 0; j < PSIZE; j++) {
                locData[y++] = image[i][j].r;
                locData[y++] = image[i][j].g;
                locData[y++] = image[i][j].b;
            }

    // sending local data buffer to all processes of the mpi communicator
    MPI_Bcast(locData, PSIZE*PSIZE*3, MPI_CHAR, 0, MPI_COMM_WORLD);     /// buffer struct, size, datatype, root(rank 0), mpi communicator

    // transform local data buffer into independent images for each process
    for(i = y = 0; i < PSIZE; i++)
        for(j = 0; j < PSIZE; j++) {
            image[i][j].r = locData[y++];
            image[i][j].g = locData[y++];
            image[i][j].b = locData[y++];
        }

    int         partSize = PSIZE*PSIZE*3 / size;        // calculate a portion of image data size for each process
    int from = (partSize*rank)           / (PSIZE*3);   // find x coordinate offset for each process
    int x,to = (partSize*rank+partSize)  / (PSIZE*3);   // total data points in 3d image(x+y+rgb) / 2d(rbf+y) = offset for coordinate x
    int bias = 128;                                     // convolution parameter
    int r = 0, g = 0, b = 0;

    // computation of convolution
    #pragma omp parallel for private(x,y,i,j,r,g,b) num_threads(TAMOUNT)
    for(x = from; x < to; x++) {                        // x rows
        for(y = 0; y < PSIZE; y++) {                    // y columns
            for(i = 0; i < KSIZE; i++) {                // i kernel iteration for x (xK)
                for(j = 0; j < KSIZE; j++) {            // j kernel iteration for y (yK)
                    int xK = x+i - (KSIZE / 2);
                    int yK = y+j - (KSIZE / 2);
                    if(xK < 0 || yK < 0 || xK >= PSIZE || yK >= PSIZE) {    // edge mirror handling
                        if(xK < 0)      xK *= -1;
                        if(yK < 0)      yK *= -1;
                        if(xK >= PSIZE) xK -= 2 * (xK - PSIZE) + 1;
                        if(yK >= PSIZE) yK -= 2 * (yK - PSIZE) + 1;
                    }
                    r += image[xK][yK].r * kernel[i][j];
                    g += image[xK][yK].g * kernel[i][j];
                    b += image[xK][yK].b * kernel[i][j];
                }
            }
            // saving calculated pixel into resulting image (each process do a portion)
            r += bias;      imageNew[x][y].r = r;       r = 0;
            g += bias;      imageNew[x][y].g = g;       g = 0;
            b += bias;      imageNew[x][y].b = b;       b = 0;
        }
    }

    // saving results into local data buffer
    for(i = from, y = 0; i < to; i++)
        for(j = 0; j < PSIZE; j++){
            locData[y++] = imageNew[i][j].r;
            locData[y++] = imageNew[i][j].g;
            locData[y++] = imageNew[i][j].b;
        }

    printf("%d. processor time spent: %g\n", rank+1, (double)clock()-start);

    MPI_Gather(locData, partSize, MPI_CHAR, globData, partSize, MPI_CHAR, 0, MPI_COMM_WORLD);   /// grouping up local buffers into global data buffer
    MPI_Finalize();                                                                             /// terminating MPI execution environment

    // transform global data buffer into resulting image for main process
    if(rank == 0)
        for(i = y = 0; i < PSIZE; i++)
            for(j = 0; j < PSIZE; j++) {
                imageNew[i][j].r = globData[y++];
                imageNew[i][j].g = globData[y++];
                imageNew[i][j].b = globData[y++];
            }
}

void display() {
    glBindTexture(GL_TEXTURE_2D, texture);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, PSIZE, PSIZE, 0, GL_RGB, GL_UNSIGNED_BYTE, imageNew);

    glClear(GL_COLOR_BUFFER_BIT);

    glBegin(GL_QUADS);;
        glTexCoord2f(0,0); glVertex2f( 1, 1);
        glTexCoord2f(0,1); glVertex2f( 1,-1);
        glTexCoord2f(1,1); glVertex2f(-1,-1);
        glTexCoord2f(1,0); glVertex2f(-1, 1);
    glEnd();

    glFlush();
    glutPostRedisplay();
    glutSwapBuffers();
}

void gl_init() {
    glEnable       (GL_TEXTURE_2D);
    glGenTextures  (1, &texture);
    glBindTexture  (GL_TEXTURE_2D, texture);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

    glClearColor(0,0,0,0);
    gluOrtho2D(-1,1,-1,1);
    glLoadIdentity();
    glColor3f(1,1,1);
}

int main(int argc, char **argv)
{
    int size, rank;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &size);       // get total amount of processes
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);       // get number of MPI process in our group of processes

    if (rank == 0) {                            // setting OpenGL state
        glutInit(&argc, argv);
        glutInitWindowSize(PSIZE, PSIZE);
        glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB);
        glutCreateWindow("OpenGL Window");
        gl_init();;
    }

    init(rank, size);

    if (rank == 0) {                            // generate and display image on the main process
        glutDisplayFunc(display);
        glutMainLoop();
    }

    return EXIT_SUCCESS;
}
