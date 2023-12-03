package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelMaquina;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelUsuario;
import org.springframework.jdbc.core.JdbcTemplate;

public class MaquinaDao {
    private Integer idMaquina;

    public MaquinaDao() {
    }

    public void salvarMaquina(ModelMaquina novaMaquina, Integer fkUsuario, Integer fkToken) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        con.update("INSERT INTO maquina (ip, so, modelo, fk_usuario, fk_token) VALUES (?, ?, ?, ?, ?)",
                novaMaquina.getIp(), novaMaquina.getSo(), novaMaquina.getModelo(), fkUsuario, fkToken);

        idMaquina = con.queryForObject("SELECT SCOPE_IDENTITY()" , Integer.class);
    }

    public Integer buscarMaquinaPorUsuario(Integer fkUsuario) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        String sql = "SELECT id_maquina FROM maquina WHERE fk_usuario = ?";
        idMaquina = con.queryForObject(sql, Integer.class, fkUsuario);

        return idMaquina;
    }


    public Integer getIdMaquina() {
        return idMaquina;
    }
}
