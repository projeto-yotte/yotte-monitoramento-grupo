package br.com.sptech.modelo.banco.jdbc.conexao;

import br.com.sptech.modelo.banco.jdbc.App;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {

    private JdbcTemplate conexaoDoBanco;
    private Boolean conexaoEstabelecida = false;

    public Conexao() {
        try {
            if (!conexaoEstabelecida) {
                BasicDataSource dataSource = new BasicDataSource();
                // Configurações para o MySQL

//                dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//                dataSource.setUrl("jdbc:mysql://localhost:3306/yotte");
//                dataSource.setUsername("root");
//                dataSource.setPassword("58214213");


//                dataSource.setUrl("jdbc:sqlserver://54.205.98.102; database=yotte; user=sa; passaword=Projetoyotte2023; trustServerCertificate=true;");
//                dataSource.setUsername("sa");
//                dataSource.setPassword("Projetoyotte2023");

                dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                dataSource.setUrl("jdbc:mysql://localhost:3306/yotte");
                dataSource.setUsername("yotte");
                dataSource.setPassword("yotte2023");

                this.conexaoDoBanco = new JdbcTemplate(dataSource);

                App.log("Conexão com o banco de dados estabelecida com sucesso.");

                dataSource.getConnection().close();
                this.conexaoDoBanco = new JdbcTemplate(dataSource);
                conexaoEstabelecida = true;  // Marcamos a conexão como estabelecida
                App.log("Conexão com o banco de dados estabelecida com sucesso.");
            }
        } catch (Exception e) {
            App.logError("Erro ao estabelecer a conexão com o banco de dados.", e);
        }
    }

    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }

    public void log(String message) {
        App.log(message);
    }

    public void logError(String errorMessage, Exception exception) {
        App.logError(errorMessage, exception);
    }
}
