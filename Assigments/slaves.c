#include <stdio.h>
#include <stdlib.h>
#include <mpi.h>

#define MASTER_RANK 0
#define TAG_OPERATION 0
#define TAG_RESULT 1
#define TAG_FINALIZE 10

void master(int num_slaves);
void slave();

int main(int argc, char **argv) {
    int rank, size;

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (size < 2) {
        printf("O programa requer pelo menos 2 processos MPI (1 mestre e 1 escravo).\n");
        MPI_Abort(MPI_COMM_WORLD, 1);
    }

    if (rank == MASTER_RANK) {
        master(size - 1);
    } else {
        slave();
    }

    MPI_Finalize();
    return 0;
}

void master(int num_slaves) {
    int *random_numbers;
    int array_size;
    int i;

    // Gera um tamanho aleatório para o vetor entre 1000 e 2000
    array_size = rand() % (2000 - 1000 + 1) + 1000;

    // Gera números aleatórios entre 0 e 99
    random_numbers = (int *)malloc(array_size * sizeof(int));
    for (i = 0; i < array_size; i++) {
        random_numbers[i] = rand() % 100;
    }

    // Envia o tamanho do vetor para os escravos
    for (i = 1; i <= num_slaves; i++) {
        MPI_Send(&array_size, 1, MPI_INT, i, TAG_OPERATION, MPI_COMM_WORLD);
    }

    // Envia o vetor e a operação para cada escravo
    for (i = 1; i <= num_slaves; i++) {
        MPI_Send(random_numbers, array_size, MPI_INT, i, TAG_OPERATION, MPI_COMM_WORLD);
    }

    // Recebe os resultados dos escravos
    for (i = 1; i <= num_slaves; i++) {
        int result;
        MPI_Recv(&result, 1, MPI_INT, i, TAG_RESULT, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        printf("Resultado do escravo %d: %d\n", i, result);
    }

    // Finaliza a execução dos escravos
    for (i = 1; i <= num_slaves; i++) {
        MPI_Send(NULL, 0, MPI_INT, i, TAG_FINALIZE, MPI_COMM_WORLD);
    }

    free(random_numbers);
}

void slave() {
    int array_size;
    MPI_Recv(&array_size, 1, MPI_INT, MASTER_RANK, TAG_OPERATION, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

    // Se o array_size for 0, o escravo deve encerrar
    while (array_size > 0) {
        int *random_numbers = (int *)malloc(array_size * sizeof(int));
        MPI_Recv(random_numbers, array_size, MPI_INT, MASTER_RANK, TAG_OPERATION, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // Calcula a operação solicitada
        int operation_tag;
        MPI_Recv(&operation_tag, 1, MPI_INT, MASTER_RANK, TAG_OPERATION, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        int result;
        switch (operation_tag) {
            case 0: // Soma dos valores do vetor
                result = 0;
                for (int i = 0; i < array_size; i++) {
                    result += random_numbers[i];
                }
                break;

            case 1: // Média dos valores do vetor
                result = 0;
                for (int i = 0; i < array_size; i++) {
                    result += random_numbers[i];
                }
                result /= array_size;
                break;

            case 2: // Maior valor do vetor
                result = random_numbers[0];
                for (int i = 1; i < array_size; i++) {
                    if (random_numbers[i] > result) {
                        result = random_numbers[i];
                    }
                }
                break;

            case 3: // Mediana dos valores do vetor (assumindo que o vetor está ordenado)
                if (array_size % 2 == 0) {
                    result = (random_numbers[array_size / 2 - 1] + random_numbers[array_size / 2]) / 2;
                } else {
                    result = random_numbers[array_size / 2];
                }
                break;

            default:
                result = 0;
                break;
        }

        // Envia o resultado para o mestre
        MPI_Send(&result, 1, MPI_INT, MASTER_RANK, TAG_RESULT, MPI_COMM_WORLD);

        free(random_numbers);

        // Recebe o próximo tamanho do vetor ou 0 para encerrar
        MPI_Recv(&array_size, 1, MPI_INT, MASTER_RANK, TAG_OPERATION, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    }
}
