package banco.ui;

import banco.dao.AutorDao;
import java.util.List;

import banco.dao.ClienteDao;
import banco.dao.ContaDao;
import banco.dao.LivroDao;
import banco.modelo.Autor;
import banco.modelo.Cliente;
import banco.modelo.Conta;
import banco.modelo.Livro;

public class InterfaceLivroTexto extends InterfaceModeloTexto {

    private LivroDao dao;
    private AutorDao clienteDao;

    public InterfaceLivroTexto() {
        super();
        dao = new LivroDao();
        clienteDao = new AutorDao();
    }

    @Override
    public void adicionar() {
        System.out.println("Adicionar Livro");
        System.out.println();

        Livro novaConta = obtemDadosConta(null);
        dao.insert(novaConta);
    }

    private Livro obtemDadosConta(Livro conta) {
        System.out.print("Insira o titulo do livro: ");
        String titulo = entrada.nextLine();

        System.out.print("Insira o editora do livro: ");
        String editora = entrada.nextLine();

        System.out.print("Insira o ano de publicação do livro: ");
        int anoPublicacao = entrada.nextInt();

        System.out.print("Insira o ID do autor: ");
        int idAutor = entrada.nextInt();

        Autor cliente = clienteDao.getByKey(idAutor);

        return new Livro(0, titulo, anoPublicacao, editora, cliente);
    }

    @Override
    public void listarTodos() {
        List<Livro> contas = dao.getAll();

        System.out.println("Lista de livros");
        System.out.println();

        System.out.println("id\tTitulo\tEditora\tAno\tAutor\tNome");

        for (Livro conta : contas) {
            imprimeItem(conta);
        }
    }

    @Override
    public void editar() {
        listarTodos();

        System.out.println("Editar livro");
        System.out.println();

        System.out.print("Entre o id do livro: ");
        int id = entrada.nextInt();
        entrada.nextLine();

        Livro contaAModificar = dao.getByKey(id);

        Livro novaConta = obtemDadosConta(contaAModificar);

        novaConta.setId(id);

        dao.update(novaConta);
    }

    @Override
    public void excluir() {
        listarTodos();

        System.out.println("Excluir livro");
        System.out.println();

        System.out.print("Entre o id do livro: ");
        int id = entrada.nextInt();
        entrada.nextLine();

        dao.delete(id);
    }

}
