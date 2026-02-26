package com.livraria.service.impl;

import com.livraria.entity.RelatorioLivrosPorAutor;
import com.livraria.repository.RelatorioLivrosPorAutorRepository;
import com.livraria.service.RelatorioService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

@Service
public class RelatorioServiceImpl implements RelatorioService {

    private static final Logger logger = LoggerFactory.getLogger(RelatorioServiceImpl.class);

    private final RelatorioLivrosPorAutorRepository relatorioRepo;
    private final ResourceLoader resourceLoader;
    private final DataSource dataSource;

    @Autowired
    public RelatorioServiceImpl(RelatorioLivrosPorAutorRepository relatorioRepo, ResourceLoader resourceLoader, DataSource dataSource) {
        this.relatorioRepo = relatorioRepo;
        this.resourceLoader = resourceLoader;
        this.dataSource = dataSource;
    }

    @Override
    public List<RelatorioLivrosPorAutor> gerarRelatorio() {
        return relatorioRepo.findAll();
    }

    @Override
    public byte[] gerarRelatorioPDF() throws Exception {
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        
        try {
            logger.info("Iniciando geração de PDF do relatório...");
            
            // Carregar o arquivo .jrxml
            Resource jrxmlResource = resourceLoader.getResource("classpath:reports/livros_por_autor.jrxml");
            InputStream jrxmlInputStream = jrxmlResource.getInputStream();
            
            logger.info("Template JasperReports carregado: {}", jrxmlResource.getFilename());
            
            // Compilar o relatório
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInputStream);
            logger.info("Relatório compilado com sucesso");
            
            // Obter conexão com banco de dados
            Connection connection = dataSource.getConnection();
            logger.info("Conexão com banco de dados estabelecida");
            
            try {
                // Preencher o relatório diretamente da conexão JDBC
                JasperPrint jasperPrint = JasperFillManager.fillReport(
                        jasperReport, 
                        new HashMap<String, Object>(), 
                        connection
                );
                logger.info("Relatório preenchido com sucesso. Páginas: {}", jasperPrint.getPages().size());
                
                // Exportar para PDF
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfOutputStream));
                exporter.exportReport();
                
                logger.info("PDF exportado com sucesso. Tamanho: {} bytes", pdfOutputStream.toByteArray().length);
                return pdfOutputStream.toByteArray();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            logger.error("Erro ao gerar relatório PDF: {}", e.getMessage(), e);
            throw new Exception("Erro ao gerar relatório PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream gerarRelatorioInputStream() throws Exception {
        byte[] pdfBytes = gerarRelatorioPDF();
        return new java.io.ByteArrayInputStream(pdfBytes);
    }
}


