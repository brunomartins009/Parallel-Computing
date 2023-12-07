import socket
import pickle
import numpy as np
import time

    
server_ip = input("Digite o endereço IP do servidor: ")
server_address = (server_ip, 12345)

def parse_matrix_input(input_string):
    # Converte a entrada do usuário para uma matriz NumPy
    lines = input_string.strip().split('\n')
    matrix = [[float(val) for val in line.split()] for line in lines]
    return np.array(matrix)

def generate_random_matrix(rows, cols, min_value=0, max_value=1000):
    return np.random.uniform(min_value, max_value, size=(rows, cols))


def send_matrices():

    print("Menu:")
    print("1. Digitar matrizes manualmente")
    print("2. Gerar matrizes aleatoriamente")
    choice = input("Escolha uma opção (1 ou 2): ")

    if choice == '1':
        # Solicita dimensões das matrizes
        rows_a = int(input("Digite o número de linhas da matriz A: "))
        cols_a = int(input("Digite o número de colunas da matriz A: "))

        # Solicita os valores da matriz A
        print("Digite os valores da matriz A no formato '1 2 3 ...':")
        input_string_a = '\n'.join(input() for _ in range(rows_a))
        matrix_a = parse_matrix_input(input_string_a)

        rows_b = int(input("Digite o número de linhas da matriz B: "))
        cols_b = int(input("Digite o número de colunas da matriz B: "))

        # Solicita os valores da matriz B
        print("Digite os valores da matriz B no formato '1 2 3 ...':")
        input_string_b = '\n'.join(input() for _ in range(rows_b))
        matrix_b = parse_matrix_input(input_string_b)

    elif choice == '2':
        # Gera dimensões aleatórias para as matrizes
        rows_a, cols_a = np.random.randint(2, 12, size=2)
        rows_b, cols_b = np.random.randint(2, 12, size=2)

        # Garante que as matrizes sejam multiplicáveis
        cols_a = rows_b

        # Gera matrizes aleatórias
        matrix_a = generate_random_matrix(rows_a, cols_a)
        matrix_b = generate_random_matrix(rows_b, cols_b)

        print(f"Matriz A gerada aleatoriamente ({rows_a}x{cols_a}):")
        print(matrix_a)
        print("\n")
        print(f"Matriz B gerada aleatoriamente ({rows_b}x{cols_b}):")
        print(matrix_b)
        print("\n")

    else:
        print("Escolha inválida.")
        return

    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_address = (server_ip, 12345)  # Endereço e porta do servidor

    try:
        start_time = time.time()  # Mede o tempo inicial

        client_socket.connect(server_address)
        matrices = (matrix_a, matrix_b)
        client_socket.sendall(pickle.dumps(matrices))

        # Mede o tempo após o envio dos dados
        send_time = time.time() - start_time
        print(f"Tempo de envio: {send_time:.6f} segundos")

        data = client_socket.recv(4096)

        # Mede o tempo após o recebimento dos resultados
        receive_time = time.time() - start_time
        print(f"Tempo de recebimento: {receive_time:.6f} segundos")
        print("\n")

        result = pickle.loads(data)
        return result

    finally:
        client_socket.close()


if __name__ == "__main__":
    result = send_matrices()

    print("Result Matrix:")
    # Formata os resultados para exibir uma casa decimal mesmo para números inteiros
    formatted_result = [[f"{val:.1f}" for val in row] for row in result]
    for row in formatted_result:
        print(row)
