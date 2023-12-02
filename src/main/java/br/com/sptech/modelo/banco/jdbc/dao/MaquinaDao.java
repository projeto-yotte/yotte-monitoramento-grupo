package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.modelo.ModelMaquina;
import org.springframework.jdbc.core.JdbcTemplate;
import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;

public class MaquinaDao {
    private Integer idMaquina;

    public MaquinaDao() {
    }

    public void salvarMaquina(ModelMaquina novaMaquina, Integer fkUsuario, Integer fkToken, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        try {
            // Salvar no MySQL
            conexaoMySQL.update("INSERT INTO maquina (ip, so, modelo, fk_usuario, fk_token) VALUES (?, ?, ?, ?, ?)",
                    novaMaquina.getIp(), novaMaquina.getSo(), novaMaquina.getModelo(), fkUsuario, fkToken);

            // Salvar no SQL Server
            conexaoSQLServer.update("INSERT INTO maquina (ip, so, modelo, fk_usuario, fk_token) VALUES (?, ?, ?, ?, ?)",
                    novaMaquina.getIp(), novaMaquina.getSo(), novaMaquina.getModelo(), fkUsuario, fkToken);

            idMaquina = getIdMaquinaMySQL(fkUsuario, conexaoMySQL); // Usando o MySQL para buscar o ID
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
        }
    }

    private Integer getIdMaquinaMySQL(Integer fkUsuario, JdbcTemplate conexaoMySQL) {
        String sql = "SELECT id_maquina FROM maquina WHERE fk_usuario = ?";
        return conexaoMySQL.queryForObject(sql, Integer.class, fkUsuario);
    }

    public Integer buscarMaquinaPorUsuario(Integer fkUsuario, JdbcTemplate conexaoMySQL) {
        try {
            String sql = "SELECT id_maquina FROM maquina WHERE fk_usuario = ?";
            idMaquina = conexaoMySQL.queryForObject(sql, Integer.class, fkUsuario);
            return idMaquina;
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
            return null;
        }
    }

    public Integer getIdMaquina() {
        return idMaquina;
    }
}
