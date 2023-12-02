package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelCpu;
import org.springframework.jdbc.core.JdbcTemplate;

public class CpuDao {
    private Integer idInfo;

    public void salvarCapturaFixa(ModelCpu novaCapturaCpu, Integer fkMaquina, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        if (fkMaquina != null) {
            try {
                // Salvar no MySQL
                conexaoMySQL.update("INSERT INTO info_componente (qtd_cpu_fisica, qtd_cpu_logica) VALUES (?, ?)",
                        novaCapturaCpu.getNumCPUsFisicas(), novaCapturaCpu.getNumCPUsLogicas());

                // Obter o ID inserido na tabela info_componente
                idInfo = getIdInfoMySQL(conexaoMySQL);

                // Salvar no SQL Server
                conexaoSQLServer.update("INSERT INTO info_componente (qtd_cpu_fisica, qtd_cpu_logica) VALUES (?, ?)",
                        novaCapturaCpu.getNumCPUsFisicas(), novaCapturaCpu.getNumCPUsLogicas());

                // Usar o SQL Server para salvar o componente
                conexaoSQLServer.update("INSERT INTO componente (nome, parametro, fk_info, fk_maquina) VALUES (?, ?, ?, ?)",
                        "cpu", "%", idInfo, fkMaquina);

                // Usar o SQL Server para salvar o parametro_componente
                conexaoSQLServer.update("INSERT INTO parametro_componente (valor_minimo, valor_maximo, fk_componente) VALUES (?, ?, ?)", 30, 80, idInfo);
            } catch (Exception e) {
                // Tratar exceções
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Precisa existir um processador no banco primeiro.");
        }
    }

    private Integer getIdInfoMySQL(JdbcTemplate conexaoMySQL) {
        String sql = "SELECT LAST_INSERT_ID()";
        return conexaoMySQL.queryForObject(sql, Integer.class);
    }

    public void salvarCapturaDinamica(ModelCpu novaCapturaCpu, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        if (idInfo != null) {
            try {
                // Salvar no MySQL
                conexaoMySQL.update("INSERT INTO dados_captura (uso, frequencia, data_captura, fk_componente, desligada) VALUES (?, ?, ?, ?, ?)",
                        novaCapturaCpu.getUsoCpu(),
                        novaCapturaCpu.getFreq(),
                        novaCapturaCpu.getDataCaptura(),
                        idInfo,
                        novaCapturaCpu.getDesligada()
                );

                // Salvar no SQL Server
                conexaoSQLServer.update("INSERT INTO dados_captura (uso, frequencia, data_captura, fk_componente, desligada) VALUES (?, ?, ?, ?, ?)",
                        novaCapturaCpu.getUsoCpu(),
                        novaCapturaCpu.getFreq(),
                        novaCapturaCpu.getDataCaptura(),
                        idInfo,
                        novaCapturaCpu.getDesligada()
                );
            } catch (Exception e) {
                // Tratar exceções
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("ID não foi capturado. Execute salvarCapturaFixa() primeiro.");
        }
    }

    public void buscarDadosFixo(Integer idMaquina, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        try {
            // Buscar no MySQL
            String sql = "SELECT c.id_componente\n" +
                    "FROM componente c\n" +
                    "JOIN info_componente i ON c.fk_info = i.id_info\n" +
                    "JOIN maquina m ON c.fk_maquina = m.id_maquina\n" +
                    "WHERE c.nome = ? AND m.id_maquina = ?";
            idInfo = conexaoMySQL.queryForObject(sql, Integer.class, "cpu", idMaquina);

            // Buscar no SQL Server
            // (Você precisa implementar uma lógica semelhante para buscar dados no SQL Server)
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
        }
    }
}
