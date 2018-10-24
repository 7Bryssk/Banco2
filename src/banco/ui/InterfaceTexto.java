package banco.ui;

import java.util.Scanner;

public class InterfaceTexto {

    private Scanner entrada;
    private Estado estadoAtual;

    private static final int OP_CLIENTE = 1;
    private static final int OP_CONTA = 2;
    private static final int OP_AUTOR = 3;
    private static final int OP_LIVRO = 4;
    private static final int OP_SAIR = 0;
    private static final int OP_ADICIONAR = 1;
    private static final int OP_EDITAR = 2;
    private static final int OP_EXCLUIR = 3;
    private static final int OP_LISTAR = 4;

    private enum Estado {
        PRINCIPAL, CLIENTE, CONTA, AUTOR, LIVRO
    };

    public InterfaceTexto() {
        entrada = new Scanner(System.in);
        estadoAtual = Estado.PRINCIPAL;
    }

    private void imprimeMenu() {
        System.out.println();

        switch (estadoAtual) {
            case CLIENTE:
                imprimeMenuCliente();
                break;
            case CONTA:
                imprimeMenuConta();
                break;
            case AUTOR:
                imprimeMenuAutor();
                break;
            case LIVRO:
                imprimeMenuLivro();
                break;
            default:
                imprimeMenuPrincipal();
        }
        System.out.println();
        System.out.println("0 - Sair");

        System.out.println();
        System.out.print("Escolha uma opção: ");
    }

    private int leOpcao() {
        int opcao = entrada.nextInt();
        entrada.nextLine();
        return opcao;
    }

    private void imprimeMenuPrincipal() {
        System.out.println("1 - Administração de Clientes");
        System.out.println("2 - Administração de Contas");
        System.out.println("3 - Administração de Autor");
        System.out.println("4 - Administração de Livro");
    }

    private void imprimeMenuCliente() {
        System.out.println("1 - Adicionar cliente");
        System.out.println("2 - Editar cliente");
        System.out.println("3 - Excluir cliente");
        System.out.println("4 - Listar clientes");
    }

    private void imprimeMenuConta() {
        System.out.println("1 - Adicionar conta");
        System.out.println("2 - Editar conta");
        System.out.println("3 - Excluir conta");
        System.out.println("4 - Listar contas");
    }

    private void imprimeMenuAutor() {
        System.out.println("1 - Adicionar autor");
        System.out.println("2 - Editar autor");
        System.out.println("3 - Excluir autor");
        System.out.println("4 - Listar autor");
    }

    private void imprimeMenuLivro() {
        System.out.println("1 - Adicionar livro");
        System.out.println("2 - Editar livro");
        System.out.println("3 - Excluir livro");
        System.out.println("4 - Listar livro");
    }

    public void executa() {
        InterfaceModeloTexto subMenu;

        imprimeMenu();
        int opcao = leOpcao();

        while (opcao != OP_SAIR) {

            if (estadoAtual == Estado.PRINCIPAL) {

                if (opcao == OP_CLIENTE) {
                    estadoAtual = Estado.CLIENTE;
                }
                if (opcao == OP_CONTA) {
                    estadoAtual = Estado.CONTA;
                }
                if (opcao == OP_AUTOR) {
                    estadoAtual = Estado.AUTOR;
                }
                if (opcao == OP_LIVRO) {
                    estadoAtual = Estado.LIVRO;
                }

            } else {
                subMenu = estadoAtual == Estado.CLIENTE
                        ? new InterfaceClienteTexto() : new InterfaceContaTexto();

                if (estadoAtual == Estado.CLIENTE) {
                    subMenu = new InterfaceClienteTexto();
                }
                if (estadoAtual == Estado.CONTA) {
                    subMenu = new InterfaceContaTexto();
                }
                if (estadoAtual == Estado.AUTOR) {
                    subMenu = new InterfaceAutorTexto();
                }
                if (estadoAtual == Estado.LIVRO) {
                    subMenu = new InterfaceLivroTexto();
                }

                switch (opcao) {
                    case OP_ADICIONAR:
                        subMenu.adicionar();
                        break;
                    case OP_EDITAR:
                        subMenu.editar();
                        break;
                    case OP_EXCLUIR:
                        subMenu.excluir();
                        break;
                    case OP_LISTAR:
                        subMenu.listarTodos();
                        break;
                    default:
                        System.out.println("Opção Inválida. Tente novamente!");
                }
            }

            imprimeMenu();
            opcao = leOpcao();
        }

    }

}
