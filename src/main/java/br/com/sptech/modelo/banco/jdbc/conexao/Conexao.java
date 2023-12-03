package br.com.sptech.modelo.banco.jdbc.conexao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.apache.commons.dbcp2.BasicDataSourceFactory.createDataSource;

public class Conexao {

    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/yotte";
    private static final String MYSQL_USERNAME = "yotte";
    private static final String MYSQL_PASSWORD = "yotte2023";

    private static final String SQL_SERVER_URL = "jdbc:sqlserver://54.205.98.102;database=yotte;user=sa;password=Projetoyotte2023;trustServerCertificate=true;";
    private static final String SQL_SERVER_USERNAME = "sa";
    private static final String SQL_SERVER_PASSWORD = "Projetoyotte2023";

    private JdbcTemplate conexaoDoBancoMySQL;
    private JdbcTemplate conexaoDoBancoSQLServer;

    public Conexao() {
        try {
            BasicDataSource dataSourceMySQL = createDataSource(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
            this.conexaoDoBancoMySQL = new JdbcTemplate(dataSourceMySQL);

            BasicDataSource dataSourceSQLServer = createDataSource(SQL_SERVER_URL, SQL_SERVER_USERNAME, SQL_SERVER_PASSWORD);
            this.conexaoDoBancoSQLServer = new JdbcTemplate(dataSourceSQLServer);

            log("Conexões com o banco de dados estabelecidas com sucesso.");
        } catch (Exception e) {
            logError("Erro ao estabelecer as conexões com o banco de dados.", e);
        }
    }

    public JdbcTemplate getConexaoDoBancoMySQL() {
        return conexaoDoBancoMySQL;
    }

    public JdbcTemplate getConexaoDoBancoSQLServer() {
        return conexaoDoBancoSQLServer;
    }

    public void fecharConexao(JdbcTemplate jdbcTemplate) {
        try {
            DataSource dataSource = jdbcTemplate.getDataSource();
            if (dataSource instanceof BasicDataSource) {
                ((BasicDataSource) dataSource).close();
                App.log("Conexão fechada com sucesso.");
            } else {
                App.logError("Não foi possível fechar a conexão. Tipo de DataSource não suportado.");
            }
        } catch (Exception e) {
            App.logError("Erro ao fechar a conexão.", e);
        }
    }

    public void log(String message) {
        System.out.println(message);
    }

    public void logError(String errorMessage, Exception exception) {
        System.err.println(errorMessage);
        exception.printStackTrace();
    }
}

