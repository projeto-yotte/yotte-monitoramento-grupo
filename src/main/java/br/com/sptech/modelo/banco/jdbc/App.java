package br.com.sptech.modelo.banco.jdbc;

import br.com.sptech.modelo.banco.jdbc.dao.MaquinaDao;
import br.com.sptech.modelo.banco.jdbc.dao.MemoriaDao;
import br.com.sptech.modelo.banco.jdbc.dao.UsuarioDao;
import br.com.sptech.modelo.banco.jdbc.modelo.*;
import br.com.sptech.modelo.banco.jdbc.servico.Maquina;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.group.janelas.JanelaGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.core.Looca;
import br.com.sptech.modelo.banco.jdbc.validacoes.ValidacoesUsuario;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.processos.Processo;
import com.github.britooo.looca.api.group.processos.ProcessoGrupo;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class App {
    private static String logUserName = "";
    private static String getLogFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        return dateStr + "-" + logUserName + "-.txt";
    }

    public static void log(String message) {
        try {
            String logFileName = getLogFileName();
            FileWriter fw = new FileWriter(logFileName, true);
            BufferedWriter bw = new BufferedWriter(fw);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            String logMessage = timestamp + " - " + message;

            bw.write(logMessage);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logError(String errorMessage, Exception exception) {
        try {
            String logFileName = getLogFileName();
            FileWriter fw = new FileWriter(logFileName, true);
            BufferedWriter bw = new BufferedWriter(fw);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            String logMessage = timestamp + " - " + errorMessage + ": " + exception.getMessage();

            bw.write(logMessage);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        // Crie um agendador de tarefas
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Maquina maquina01 = new Maquina();

        ModelUsuario novoUsuario = new ModelUsuario();
        UsuarioDao usuarioDao = new UsuarioDao();

        Looca looca = new Looca();

        Scanner leitor = new Scanner(System.in);
        Scanner leitorTexto = new Scanner(System.in);



        Boolean logado = false;

        ValidacoesUsuario validacoesUsuario = new ValidacoesUsuario();



        System.out.println("""
                        :::   :::  ::::::::  ::::::::::: ::::::::::: ::::::::::
                        :+:   :+: :+:    :+:     :+:         :+:     :+:
                         +:+ +:+  +:+    +:+     +:+         +:+     +:+
                          +#++:   +#+    +:+     +#+         +#+     +#++:++#
                           +#+    +#+    +#+     +#+         +#+     +#+
                           #+#    #+#    #+#     #+#         #+#     #+#
                           ###     ########      ###         ###     ##########
                                
                Já tem cadastro?
                - 0 (Caso não)
                - 1 (Caso sim)
                - 2 (Para sair da aplicação)
                """);
        Integer opcao = leitor.nextInt();

        if (opcao == 0) {
            // Cadastro de novo usuário
            String nome;
            String email;
            String senha;
            String area;
            String cargo;
            String empresa;
            String ip;
            String modelo;
            String so;
            String matricula;
            Boolean todasValidacoes = false;


            do {
                try {
                    System.out.println("Fazer cadastro...");

                    System.out.println("Digite seu nome:");
                    nome = leitorTexto.nextLine();

                    System.out.println("Digite seu email:");
                    email = leitorTexto.nextLine();

                    System.out.println("Digite sua senha:");
                    senha = leitorTexto.nextLine();

                    System.out.println("Digite seu área de atuação:");
                    area = leitorTexto.nextLine();

                    System.out.println("Digite seu cargo:");
                    cargo = leitorTexto.nextLine();

                    System.out.println("Digite sua empresa:");
                    empresa = leitorTexto.nextLine();

                    System.out.println("Digite seu IP: ");
                    ip = leitorTexto.nextLine();

                    System.out.println("Digite o modelo do notebook: ");
                    modelo = leitorTexto.nextLine();

                    System.out.println("Digite qual SO você utiliza: ");
                    so = leitorTexto.nextLine();

                    System.out.println("Digite seu token de acesso: ");
                    matricula = leitorTexto.nextLine();

                    Boolean isSenhaValida = validacoesUsuario.isSenhaValida(senha);
                    Boolean isSenhaComplexa = validacoesUsuario.isSenhaComplexa(senha);
                    Boolean emailNaoTemEspacos = validacoesUsuario.naoTemEspacos(email);
                    Boolean isEmailValido = validacoesUsuario.isEmailValido(email);

                    if (isSenhaValida && emailNaoTemEspacos && isEmailValido && isSenhaComplexa) {
                        todasValidacoes = true;

                        try {
                            if (usuarioDao.isTokenValido(matricula)) {
                                if (usuarioDao.buscarEmpresaPorNome(empresa) != null) {
                                    Integer fkEmpresa = usuarioDao.buscarEmpresaPorNome(empresa);

                                    novoUsuario.setNome(nome);
                                    novoUsuario.setEmail(email);
                                    novoUsuario.setSenha(senha);
                                    novoUsuario.setArea(area);
                                    novoUsuario.setCargo(cargo);
                                    novoUsuario.setFkEmpresa(fkEmpresa);
                                    novoUsuario.setFkTipoUsuario(3);
                                    logUserName = nome;

                                    MaquinaDao maquinaDao = new MaquinaDao();
                                    ModelMaquina novaMaquina = new ModelMaquina();
                                    novaMaquina.setIp(ip);
                                    novaMaquina.setSo(so);
                                    novaMaquina.setModelo(modelo);

                                    usuarioDao.salvarUsuario(novoUsuario);
                                    maquinaDao.salvarMaquina(novaMaquina, usuarioDao.buscarIdUsuario(novoUsuario), usuarioDao.buscarIdToken(matricula));
                                    maquina01.buscarIdMaquina(usuarioDao.buscarIdUsuario(novoUsuario));

                                    logado = true;
                                    log("Cadastro bem-sucedido para o usuário " + nome);
                                    log("Área de atuação " + area);
                                    log("Sistema Operacional" + so);
                                    System.out.println("Cadastrado com sucesso!");
                                }
                            } else {
                                System.out.println("Seu token não é válido!");
                            }
                        } catch (Exception e) {
                            logError("Erro durante o processo de cadastro", e);
                        }
                        log("Email e senha válidas " + email);
                    } else {
                        System.out.println("Dados inválidos, faça o cadastro novamente!!");
                    }
                } catch (Exception e) {
                    logError("Erro durante a verificação de senha e email", e);
                }
            } while (!todasValidacoes);
        } else if (opcao == 1) {
            // Login
            Boolean todasValidacoesLogin = false;
            String validarEmail;
            String validarSenha;

            do {
                try {
                    ModelUsuario usuario = new ModelUsuario();
                    System.out.println("Digite seu email:");
                    validarEmail = leitorTexto.nextLine();
                    System.out.println("Digite sua senha:");
                    validarSenha = leitorTexto.nextLine();

                    usuario.setEmail(validarEmail);
                    usuario.setSenha(validarSenha);

                    if (usuarioDao.isUsuarioExistente(usuario)) {
                        todasValidacoesLogin = true;
                        System.out.println("Id usuario: " + usuarioDao.buscarIdUsuario(usuario));
                        maquina01.buscarIdMaquina(usuarioDao.buscarIdUsuario(usuario));
                        logado = true;

                        log("Login bem-sucedido para o usuário: " + usuario.getEmail());
                    } else {
                        System.out.println("Email ou senha incorretas. Tente novamente!");
                    }
                } catch (Exception e) {
                    logError("Erro durante o processo de login", e);
                }
            } while (!todasValidacoesLogin);


            System.out.println("Login realizado com sucesso! \uD83D\uDE04");

            // Restante do código para capturar dados de memória
        } else if (opcao == 2) {
            System.out.println("Saindo da aplicação.");
        } else {
            System.out.println("Opção inválida.");
        }

        // Fecha os recursos necessários, como Scanners
        leitor.close();
        leitorTexto.close();

        if (logado.equals(true)) {
            // Obtenha os dados da API Looca
            Memoria memoria = looca.getMemoria();

            Processador cpu = looca.getProcessador();

            DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();
            List<Disco> discos = grupoDeDiscos.getDiscos();

            JanelaGrupo grupodDeJanelas = looca.getGrupoDeJanelas();
            List<Janela> janelas = grupodDeJanelas.getJanelas();

            ProcessoGrupo grupoDeProcessos = looca.getGrupoDeProcessos();
            List<Processo> processos = grupoDeProcessos.getProcessos();

            ModelMemoria novaCapturaRam = new ModelMemoria();
            ModelCpu novaCapturaCpu = new ModelCpu();
            ModelDisco novaCapturaDisco = new ModelDisco();
            ModelJanela novaCapturaJanela = new ModelJanela();
            ModelProcesso novaCapturaProcesso = new ModelProcesso();


            // IF para captura fixa, acontece apenas 1 vez.
            if(!maquina01.isComponenteSalvo(maquina01.getIdMaquina())){
                novaCapturaRam.setRamTotal(memoria.getTotal());
                novaCapturaCpu.setNumCPUsFisicas(cpu.getNumeroCpusFisicas());
                novaCapturaCpu.setNumCPUsLogicas(cpu.getNumeroCpusLogicas());

                for (Disco disco : discos) {
                    novaCapturaDisco.setTotalDisco(disco.getTamanho());
                }

                maquina01.capturarDadosFixo(novaCapturaRam, novaCapturaCpu, novaCapturaDisco);

            } else {
                maquina01.buscarDadosFixosDosComponentes();
            };

            // Scheduler de 10 segundos para capturas dinâmicas
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    // Crie uma nova instância da sua classe com os dados capturados
                    novaCapturaRam.setMemoriaUso(memoria.getEmUso());
                    novaCapturaRam.setDataCaptura(new Date());

                    novaCapturaCpu.setUsoCpu(cpu.getUso());
                    novaCapturaCpu.setFreq(cpu.getFrequencia());
                    novaCapturaCpu.setDataCaptura(new Date());

                    for (Disco disco : discos) {
                        novaCapturaDisco.setBytesEscrita(disco.getBytesDeEscritas());
                        novaCapturaDisco.setDataCaptura(new Date());
                        novaCapturaDisco.setBytesLeitura(disco.getBytesDeLeitura());
                        novaCapturaDisco.setEscritas(disco.getEscritas());
                        novaCapturaDisco.setLeituras(disco.getLeituras());
                        novaCapturaDisco.setTamanhoFila(disco.getTamanhoAtualDaFila());
                    }

                    for (Janela janela : janelas) {
                        novaCapturaJanela.setPid(janela.getPid());
                        novaCapturaJanela.setTitulo(janela.getTitulo());
                        novaCapturaJanela.setComando(janela.getComando());
                        novaCapturaJanela.setVisivel(janela.isVisivel());
                        novaCapturaJanela.setDataCaptura(new Date());
                    }

                    for (Processo processo : processos) {
                        novaCapturaProcesso.setPid(processo.getPid());
                        novaCapturaProcesso.setUsoCpu(processo.getUsoCpu());
                        novaCapturaProcesso.setUsoMemoria(processo.getUsoMemoria());
                        novaCapturaProcesso.setBytesUtilizados(processo.getBytesUtilizados());
                    }

                    maquina01.capturarDadosDinamico(novaCapturaRam, novaCapturaCpu, novaCapturaDisco, novaCapturaJanela, novaCapturaProcesso);

                    // Imprima uma mensagem de sucesso no console
                    System.out.println("Dados de memória capturados e salvos com sucesso!");
                    log("Dados de memória capturados e salvos com sucesso" + memoria);
                } catch (Exception e) {
                    // Imprima quaisquer erros no console
                    e.printStackTrace();
                    System.err.println("Erro ao capturar e salvar dados de memória.");
                    logError("Erro ao capturar e salvar dados de memória", e);
                }
            }, 0, 10, TimeUnit.SECONDS);
        }

        // Fechando os Scanners pra não derreter a memória (ou a RAM sei lá)
        leitor.close();
        leitorTexto.close();
    }
}

