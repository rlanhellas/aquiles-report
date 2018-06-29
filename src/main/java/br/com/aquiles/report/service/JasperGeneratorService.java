package br.com.aquiles.report.service;

import br.com.aquiles.core.util.StringUtils;
import br.com.aquiles.report.bean.SRPrint;
import br.com.aquiles.report.bean.SRReport;
import br.com.aquiles.report.enums.EnumSRFileType;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections4.CollectionUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.UUID;

@Log4j
@Named
@RequestScoped
public class JasperGeneratorService {
    public void generatePDF(SRPrint printer, SRReport srReport) {
        try {
            JasperPrint jasperPrint;

            if (!StringUtils.isEmpty(srReport.getDatasourceLookup())) {
                log.info("Creating a connection from datasource " + srReport.getDatasourceLookup());
                InitialContext initialContext = new InitialContext();
                DataSource dataSource = (DataSource) initialContext.lookup(srReport.getDatasourceLookup());
                Connection connection = dataSource.getConnection();
                jasperPrint = JasperFillManager
                        .fillReport(srReport.getSourceFile(), printer.getParams(), connection);
            } else {
                JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(
                        printer.getBeans());
                if (!CollectionUtils.isEmpty(printer.getBeans())) {
                    jasperPrint = JasperFillManager
                            .fillReport(srReport.getSourceFile(), printer.getParams(),
                                    beanCollectionDataSource);
                } else {
                    jasperPrint = JasperFillManager
                            .fillReport(srReport.getSourceFile(), printer.getParams(),
                                    new JREmptyDataSource());
                }
            }

            StringBuilder generatedReport = new StringBuilder(srReport.getGeneratedReportDir() + "/");
            generatedReport.append(createReportGeneratedName(srReport.getId().getCode(), EnumSRFileType.PDF));
            JasperExportManager.exportReportToPdfFile(jasperPrint, generatedReport.toString());
            printer.setGeneratedFile(generatedReport.toString());

        } catch (Throwable e) {
            log.error(e);
            printer.setError(e.getMessage());
        }
    }

    private String createReportGeneratedName(String codeReport, EnumSRFileType type) {
        return codeReport + "_" + UUID.randomUUID().toString() + "." + type.getDesc();
    }
}
