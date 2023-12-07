import socket
import numpy as np
import pickle
import signal
import sys
import threading

def multiply_matrices(matrix_a, matrix_b):
    return np.dot(matrix_a, matrix_b)

def handle_client(connection):
    try:
        data = connection.recv(4096)
        if data:
            matrices = pickle.loads(data)
            result = multiply_matrices(*matrices)
            connection.sendall(pickle.dumps(result))
    finally:
        connection.close()

def start_server():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_address = ('192.168.15.144', 12345)  # Defina a porta desejada

    server_socket.bind(server_address)
    server_socket.listen(5)  # Agora permite até 5 conexões pendentes

    print(f"Servidor ouvindo em {server_address}")

    def signal_handler(sig, frame):
        print('Servidor encerrado.')
        server_socket.close()
        sys.exit(0)

    # Registra o manipulador de sinal para Ctrl+C
    signal.signal(signal.SIGINT, signal_handler)

    while True:
        connection, client_address = server_socket.accept()
        print(f"Conexão recebida de {client_address}")
        
        # Cria uma nova thread para lidar com o cliente
        client_thread = threading.Thread(target=handle_client, args=(connection,))
        client_thread.start()

if __name__ == "__main__":
    start_server()
