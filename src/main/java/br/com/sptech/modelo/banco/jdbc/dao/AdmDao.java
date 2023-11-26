package br.com.sptech.modelo.banco.jdbc.dao;
import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelAdm;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelUsuario;
import com.mysql.cj.xdevapi.JsonString;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdmDao  extends UsuarioDao{

    public AdmDao() {
    }

    public List buscarFuncEmail(String email, Integer tempo){
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();


        String sql = "SELECT COUNT(*) FROM usuario WHERE email LIKE ?;";
        Integer emailFunc = con.queryForObject(sql, Integer.class, email);
        System.out.println(emailFunc);

        if (emailFunc.equals(1)){
            List dadosFunc = con.queryForList("SELECT componente.nome,uso,leituras FROM dados_captura join componente on id_componente = fk_componente JOIN maquina on fk_maquina = id_maquina\n" +
                    "                    JOIN usuario on id_usuario = fk_usuario WHERE  email like ? and  data_captura >= NOW() - INTERVAL ? hour;", email, tempo);

                  for (int i =0; i < dadosFunc.size(); i++){
                      System.out.println(dadosFunc.get(i));
                  }
        }
        return null;
    }

    public List buscarListFunc(ModelUsuario modelUsuario){
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        UsuarioDao modelUsuario1 = new UsuarioDao();
        List listFunc = con.queryForList("SELECT admin.nome FROM usuario as admin\n" +
                "                JOIN token ON admin.id_usuario = token.fk_usuario\n" +
                "                JOIN maquina ON token.idtoken = maquina.fk_token\n" +
                "                JOIN usuario as funcionario ON funcionario.id_usuario = maquina.fk_usuario WHERE funcionario.id_usuario = ?;", modelUsuario1.buscarIdUsuario(modelUsuario));
        System.out.println();
        for (var i = 0; i < listFunc.size(); i++){
            System.out.println(i);
        }
        return listFunc;
    }
}
