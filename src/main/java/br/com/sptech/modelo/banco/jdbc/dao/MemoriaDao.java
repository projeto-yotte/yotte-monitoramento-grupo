package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelMemoria;
import org.springframework.jdbc.core.JdbcTemplate;

public class MemoriaDao extends componente  {

    public MemoriaDao(Integer idInfo) {
        super(idInfo);
    }



//    public void salvarCapturaFixa(ModelMemoria novaCapturaRam, Integer idMaquina) {
//        if (idMaquina != null) {
//            Conexao conexao = new Conexao();
//            JdbcTemplate con = conexao.getConexaoDoBanco();
//
//            con.update("INSERT INTO info_componente (total)  VALUES (?)", novaCapturaRam.getRamTotal());
//
//            setIdInfo(con.queryForObject("SELECT LAST_INSERT_ID()", Integer.class));
//
//            con.update("INSERT INTO componente (nome, parametro, fk_info, fk_maquina) VALUES (?, ?, ?, ?)", "memoria", "bytes", getIdInfo(), idMaquina);
//        } else {
//            throw new RuntimeException("Precisa existir uma máquina no banco primeiro.");
//        }
//    }


    @Override
    public void salvarCapturaFixa(ModelMemoria novaCapturaRam, Integer idMaquina) {
        super.salvarCapturaFixa(novaCapturaRam, idMaquina);
    }

    public void salvarCapturaDinamica(ModelMemoria novaCapturaRam) {
        if (getIdInfo() != null) {
            Conexao conexao = new Conexao();
            JdbcTemplate con = conexao.getConexaoDoBanco();

            con.update("INSERT INTO dados_captura (uso, data_captura, fk_componente) VALUES (?, ?, ?)",
                    novaCapturaRam.getMemoriaUso(),
                    novaCapturaRam.getDataCaptura(),
                    getIdInfo()
            );
        } else {
            throw new RuntimeException("ID não foi capturado. Execute salvarCapturaFixa() primeiro.");
        }
    }

    @Override
    public void buscarDadosFixo(Integer idMaquina, String componente) {
        super.buscarDadosFixo(idMaquina, componente);
    }
}


