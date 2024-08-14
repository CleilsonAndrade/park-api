package br.com.cleilsonandrade.parkapi.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@DependsOnDatabaseInitialization
@Service
@Slf4j
@RequiredArgsConstructor
public class JasperService {
  private final ResourceLoader resourceLoader;
  private final DataSource dataSource;

  private Map<String, Object> params = new HashMap<>();

  private static final String JASPER_DIRECTORY = "classpath:reports/";

  public void addParams(String key, Object value) {
    this.params.put("IMAGE_DIRECTORY", JASPER_DIRECTORY);
    this.params.put("REPORT_LOCALE", new Locale("pt", "BR"));
    this.params.put(key, value);
  }

  public byte[] generatedPdf() {
    byte[] bytes = null;
    try {
      Resource resource = resourceLoader.getResource(JASPER_DIRECTORY.concat("estacionamentos.jasper"));
      InputStream stream = resource.getInputStream();
      JasperPrint print = JasperFillManager.fillReport(stream, params, dataSource.getConnection());
      bytes = JasperExportManager.exportReportToPdf(print);
    } catch (IOException | JRException | SQLException e) {
      log.error("Jasper Reports ::: ", e.getCause());
    }

    return bytes;
  }
}
