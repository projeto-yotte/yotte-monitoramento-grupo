package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelUsuario;
import org.springframework.jdbc.core.JdbcTemplate;

public class UsuarioDao {
    private Integer idUsuario;

    public UsuarioDao() {}

    public Integer salvarUsuario(ModelUsuario novoUsuario, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        try {
            // Salvar no MySQL
            conexaoMySQL.update("INSERT INTO usuario (nome, email, senha, area, cargo, fk_empresa, fk_tipo_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    novoUsuario.getNome(), novoUsuario.getEmail(), novoUsuario.getSenha(), novoUsuario.getArea(),
                    novoUsuario.getCargo(), novoUsuario.getFkEmpresa(), novoUsuario.getFkTipoUsuario());

            // Salvar no SQL Server
            conexaoSQLServer.update("INSERT INTO usuario (nome, email, senha, area, cargo, fk_empresa, fk_tipo_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    novoUsuario.getNome(), novoUsuario.getEmail(), novoUsuario.getSenha(), novoUsuario.getArea(),
                    novoUsuario.getCargo(), novoUsuario.getFkEmpresa(), novoUsuario.getFkTipoUsuario());

            idUsuario = getIdUsuarioMySQL(novoUsuario.getEmail(), novoUsuario.getSenha(), conexaoMySQL); // Usando o MySQL para buscar o ID
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
        }
        return idUsuario;
    }

    private Integer getIdUsuarioMySQL(String email, String senha, JdbcTemplate conexaoMySQL) {
        String sql = "SELECT LAST_INSERT_ID()";
        return conexaoMySQL.queryForObject(sql, Integer.class);
    }

    public Boolean isUsuarioExistente(ModelUsuario usuario, JdbcTemplate conexaoMySQL) {
        try {
            String sql = "SELECT COUNT(*) FROM usuario WHERE email = ? AND senha = ?";
            Integer count = conexaoMySQL.queryForObject(sql, Integer.class, usuario.getEmail(), usuario.getSenha());

            return count > 0;
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
            return false;
        }
    }

    public Integer buscarIdUsuario(ModelUsuario usuario, JdbcTemplate conexaoMySQL) {
        try {
            String sql = "SELECT id_usuario FROM usuario WHERE email = ? AND senha = ?";
            return conexaoMySQL.queryForObject(sql, Integer.class, usuario.getEmail(), usuario.getSenha());
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
            return null;
        }
    }

    public Boolean isTokenValido(String token, JdbcTemplate conexaoMySQL) {
        try {
            String dbToken = conexaoMySQL.queryForObject("SELECT token FROM token WHERE token = ?", String.class, token);
            return token.equals(dbToken); // O token é válido
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
            return false;
        }
    }

    public Integer buscarIdToken(String token, JdbcTemplate conexaoMySQL) {
        try {
            return conexaoMySQL.queryForObject("SELECT idToken FROM token WHERE token = ?", Integer.class, token);
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
            return null;
        }
    }

    public Integer buscarEmpresaPorNome(String empresa, JdbcTemplate conexaoMySQL) {
        try {
            return conexaoMySQL.queryForObject("SELECT id_empresa FROM empresa WHERE nome LIKE ?", Integer.class, empresa.toLowerCase());
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
            return null;
        }
    }

    public Integer buscarFkTipoUsuario(ModelUsuario usuario, JdbcTemplate conexaoMySQL) {
        try {
            String sql = "SELECT fk_tipo_usuario FROM usuario WHERE email = ? AND senha = ?";
            return conexaoMySQL.queryForObject(sql, Integer.class, usuario.getEmail(), usuario.getSenha());
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
            return null;
        }
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }
}
