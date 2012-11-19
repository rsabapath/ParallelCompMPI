#include <stdio.h>
#include <mpi.h>

int main(int argc,char *argv[]){
  int numprocs,rank,namelen;
  char processor_name[MPI_MAX_PROCESSOR_NAME];
  MPI_Init(&argc,&argv);
  printf("1 number of process = %d\n",numprocs);
  MPI_Comm_size(MPI_COMM_WORLD,&numprocs);
  
  printf("2 number of process = %d\n",numprocs);
  MPI_Comm_rank(MPI_COMM_WORLD,&rank);
  printf("3 number of process = %d\n",numprocs);
  MPI_Get_processor_name(processor_name,&namelen);

  printf("process %d on %s out of %d\n",rank,processor_name,numprocs);
  MPI_Finalize():

  }
