package banco.dao;

import banco.modelo.Autor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AutorDao implements Dao<Autor> {

    private static final String GET_BY_ID = "SELECT * FROM autor WHERE id = ?";
    private static final String GET_ALL = "SELECT * FROM autor";
    private static final String INSERT = "INSERT INTO autor (nome, cpf) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE autor SET nome = ?, cpf = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM autor WHERE id = ?";

    public AutorDao() {
        try {
            createTable();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela no banco.", e);
            //e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS autor"
                + "  (id           INTEGER,"
                + "   nome            VARCHAR(50),"
                + "   cpf			  BIGINT,"
                + "   PRIMARY KEY (id))";

        Connection conn = DbConnection.getConnection();

        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate);

        close(conn, stmt, null);
    }

    private Autor getClienteFromRS(ResultSet rs) throws SQLException {
        Autor cliente = new Autor();

        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCpf(rs.getLong("cpf"));

        return cliente;
    }

    @Override
    public Autor getByKey(int id) {
        Connection conn = DbConnection.getConnection();

        Autor cliente = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(GET_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = getClienteFromRS(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter autor pela chave.", e);
        } finally {
            close(conn, stmt, rs);
        }

        return cliente;
    }

    @Override
    public List<Autor> getAll() {
        Connection conn = DbConnection.getConnection();

        List<Autor> clientes = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();

            rs = stmt.executeQuery(GET_ALL);

            while (rs.next()) {
                clientes.add(getClienteFromRS(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter todos os clientes.", e);
        } finally {
            close(conn, stmt, rs);
        }

        return clientes;
    }

    @Override
    public void insert(Autor cliente) {
        Connection conn = DbConnection.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, cliente.getNome());
            stmt.setLong(2, cliente.getCpf());

            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                cliente.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autor cliente.", e);
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
            throw new RuntimeException("Erro ao remover autor.", e);
        } finally {
            close(conn, stmt, null);
        }
    }

    @Override
    public void update(Autor cliente) {
        Connection conn = DbConnection.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(UPDATE);
            stmt.setString(1, cliente.getNome());
            stmt.setLong(2, cliente.getCpf());
            stmt.setInt(3, cliente.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar autor.", e);
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
