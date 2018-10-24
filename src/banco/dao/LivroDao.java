package banco.dao;

import banco.modelo.Autor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import banco.modelo.Livro;

public class LivroDao implements Dao<Livro> {

    private static final String GET_BY_ID = "SELECT * FROM livro NATURAL JOIN autor WHERE id = ?";
    private static final String GET_ALL = "SELECT * FROM livro NATURAL JOIN autor";
    private static final String INSERT = "INSERT INTO livro (titulo, autor_id, anoPublicacao, editora) "
            + "VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE livro SET titulo = ?, autor_id = ?, anoPublicacao = ?, "
            + "editora = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM livro WHERE id = ?";

    public LivroDao() {
        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        final String sqlCreate = "CREATE TABLE IF NOT EXISTS livro"
                + "  (id           INTEGER,"
                + "   titulo      VARCHAR(50),"
                + "   autor_id   INTEGER,"
                + "   anoPublicacao	   INTEGER,"
                + "   editora        VARCHAR(50),"
                + "   FOREIGN KEY (autor_id) REFERENCES autor(id),"
                + "   PRIMARY KEY (id))";

        Connection conn = DbConnection.getConnection();

        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate);
    }

    private Livro getContaFromRS(ResultSet rs) throws SQLException {
        Livro conta = new Livro();
        //  private static final String INSERT = "INSERT INTO livro (titulo, autor_id, anoPublicacao, editora) "

        conta.setId(rs.getInt("id"));
        conta.setTitulo(rs.getString("titulo"));
        conta.setAnoPublicacao(rs.getInt("anoPublicacao"));
        conta.setEditora(rs.getString("editora"));
        conta.setAutor(new Autor(rs.getInt("autor_id"), rs.getString("nome"), rs.getLong("cpf")));

        return conta;
    }

    @Override
    public Livro getByKey(int id) {
        Connection conn = DbConnection.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Livro conta = null;

        try {
            stmt = conn.prepareStatement(GET_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                conta = getContaFromRS(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, stmt, rs);
        }

        return conta;
    }

    @Override
    public List<Livro> getAll() {
        Connection conn = DbConnection.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        List<Livro> conta = new ArrayList<>();

        try {
            stmt = conn.createStatement();

            rs = stmt.executeQuery(GET_ALL);

            while (rs.next()) {
                conta.add(getContaFromRS(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, stmt, rs);
        }

        return conta;
    }

    @Override
    public void insert(Livro conta) {
        Connection conn = DbConnection.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, conta.getTitulo());
            stmt.setInt(2, conta.getAutor().getId());
            stmt.setInt(3, conta.getAnoPublicacao());
            stmt.setString(4, conta.getEditora());

            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                conta.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, stmt, rs);
        }

    }

    @Override
    public void delete(int id) {
        Connection conn = DbConnection.getConnection();

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(DELETE);

            stmt.setInt(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, stmt, null);
        }
    }

    @Override
    public void update(Livro conta) {
        Connection conn = DbConnection.getConnection();

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(UPDATE);
            stmt.setString(1, conta.getTitulo());
            stmt.setInt(2, conta.getAutor().getId());
            stmt.setInt(3, conta.getAnoPublicacao());
            stmt.setString(4, conta.getEditora());
            stmt.setInt(5, conta.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, stmt, null);
        }
    }

    private static void close(Connection myConn, Statement myStmt, ResultSet myRs) {
        try {
            if (myRs != null) {
                myRs.close();
            }

            if (myStmt != null) {
                myStmt.close();
            }

            if (myConn != null) {
                myConn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar recursos.", e);
        }

    }

}
