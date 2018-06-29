package br.com.aquiles.report.service;

import br.com.aquiles.core.dto.NamedParams;
import br.com.aquiles.core.exception.ServiceException;
import br.com.aquiles.core.service.GenericService;
import br.com.aquiles.core.util.StringUtils;
import br.com.aquiles.persistence.dao.IDAO;
import br.com.aquiles.report.annotation.SRDatasource;
import br.com.aquiles.report.bean.SRPrint;
import br.com.aquiles.report.bean.SRQueue;
import br.com.aquiles.report.bean.SRReport;
import br.com.aquiles.report.dataquery.SRReportDQ;
import br.com.aquiles.report.dto.SRPrintDTO;
import br.com.aquiles.report.enums.EnumSRFileType;
import br.com.aquiles.report.exception.SRException;
import br.com.aquiles.report.utils.CompressGzipUtils;
import lombok.extern.log4j.Log4j;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Log4j
public abstract class GenReportService extends GenericService {

    @Inject
    @SRDatasource
    private IDAO daoSR;

    @Inject
    private JasperGeneratorService jasperGeneratorService;

    @Override
    public IDAO getDAO() {
        return daoSR;
    }

    @Override
    public IDAO getDAO(Class qualifier) {
        return getDAO();
    }

    /**
     * Generate report
     *
     * @return Generated Report absolute path
     */

    public SRPrint generateReport(SRPrintDTO printer,
                                  String cdUsu,
                                  boolean printImmediately) throws SRException {
        return generateReport(printer, 0, cdUsu, printImmediately);
    }

    public SRPrint generateReport(SRPrintDTO printer,
                                  int priority,
                                  String cdUsu,
                                  boolean printImmediately) throws SRException {
        try {

            if (printImmediately) {
                log.info("Preparing to GENERATE report " + printer.getCodeReport() + " module " + printer.getSgMdl());
            } else {
                log.info("Preparing to QUEUE report " + printer.getCodeReport() + " module " + printer.getSgMdl());
            }

            SRReport srReport = validateReport(printer.getCodeReport(), printer.getSgMdl());
            SRPrint srPrint = generateSRPrint(srReport, printer, cdUsu);
            if (printImmediately) {
                return printNow(srPrint, cdUsu);
            } else {
                putOnQueue(srPrint, priority, cdUsu);
                return null;
            }

        } catch (Exception e) {
            log.error(e);
            throw new SRException(e);
        }

    }

    public SRPrint printNow(SRPrint printer, String cdUsu) throws SRException, ServiceException, IOException, ClassNotFoundException, JRException {

        SRReport srReport = printer.getSrReport();
        log.info("User [" + cdUsu + "] Generating report " + srReport.getId().getCode() + " module " +
                srReport.getId().getSgMdl() +
                " [Type " + printer.getFileType().name() + "]");

        log.debug("################################");
        log.debug("DETAILS REPORT");
        log.debug("Report ID: " + srReport.getId().getCode());
        log.debug("Report Module: " + srReport.getId().getSgMdl());
        log.debug("Source File: " + srReport.getSourceFile());
        log.debug("Datasource: " + srReport.getDatasourceLookup());
        log.debug("Description: " + srReport.getDescription());
        log.debug("Target Dir: " + srReport.getGeneratedReportDir());
        log.debug("Type: " + printer.getFileType().name());
        log.debug("User: " + cdUsu);
        log.debug("Beans Size: " + printer.getBeans().size());
        log.debug("Params Size: " + printer.getParams().size());
        log.debug("Report Params:");
        for (String key : printer.getParams().keySet()) {
            log.debug("key=" + key + ",value=" + printer.getParams().get(key));
        }
        log.debug("################################");


        //init printer
        printer.setStartedAt(new Date());
        if (printer.getFileType().equals(EnumSRFileType.PDF)) {
            jasperGeneratorService.generatePDF(printer, srReport);
        } else {
            throw new SRException("File Type " + printer.getFileType() + " not supported yet");
        }
        printer.setFinishedAt(new Date());
        //end printer


        super.save(printer);

        if (StringUtils.isEmpty(printer.getError())) {
            log.info("Report generated with success in path " + printer.getGeneratedFile());
        }

        return printer;
    }

    private SRReport validateReport(String code, String sgMdl) throws SRException {
        try {
            log.info("Validation report " + code + ", module " + sgMdl);
            List result = findList(SRReportDQ.FINDBY_ID, new NamedParams("id",
                    new SRReport.PK(code, sgMdl)));
            if (CollectionUtils.isEmpty(result)) {
                throw new SRException("Can't find Report with code " + code + " module "
                        + sgMdl + ". Check if REPORT is registred in database");
            } else {
                SRReport srReport = (SRReport) result.get(0);
                File sourceFile = new File(srReport.getSourceFile());

                if (!sourceFile.exists()) {
                    throw new SRException("Source File " + sourceFile.getAbsolutePath() + " don't exists, please check " +
                            "the file before generate report");
                }

                //make md5 check
                String md5;
                try {
                    md5 = getMD5FromFile(sourceFile);
                } catch (IOException e) {
                    log.error(e);
                    throw new SRException(e);
                }

                if (srReport.getMd5HashSourceFile() == null ||
                        (srReport.getMd5HashSourceFile() != null &&
                                !srReport.getMd5HashSourceFile().equals(md5))) {
                    srReport.setMd5HashSourceFile(md5);
                    srReport.setModifiedAt(new Date());
                    super.save(srReport);
                }


                return srReport;
            }
        } catch (ServiceException e) {
            log.error(e);
            throw new SRException(e);
        }
    }

    private SRPrint generateSRPrint(SRReport report, SRPrintDTO printer, String cdUsu) throws IOException,
            ServiceException {
        log.debug("Generating SRPrint...");
        SRPrint srPrint = new SRPrint();
        srPrint.setId(new SRPrint.PK(UUID.randomUUID().toString(), printer.getSgMdl()));
        srPrint.setDatasourceLookup(report.getDatasourceLookup());
        srPrint.setFileType(printer.getFileType());
        srPrint.setMd5HashSourceFile(getMD5FromFile(new File(report.getSourceFile())));
        srPrint.setSourceFile(report.getSourceFile());
        srPrint.setSrReport(report);
        srPrint.setCdUsuAtu(cdUsu);
        srPrint.setDhAtu(new Date());

        File dataDir = new File(report.getGeneratedReportDir() + "/data/");

        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        String dataBeans = dataDir.getAbsolutePath() + "/beans_" + UUID.randomUUID().toString() + ".gz";
        srPrint.setBeansGzip(CompressGzipUtils.compress(dataBeans, printer.getBeans()));

        String dataParams = dataDir + "/params_" + UUID.randomUUID().toString() + ".gz";
        srPrint.setParamsGzip(CompressGzipUtils.compress(dataParams, printer.getParams()));


        return (SRPrint) super.save(srPrint);
    }

    private void putOnQueue(SRPrint srPrint, int priority, String cdUsu) throws ServiceException {
        log.debug("Putting report on QUEUE ...");
        SRQueue srQueue = new SRQueue();
        srQueue.setId(new SRQueue.PK(UUID.randomUUID().toString(), srPrint.getId().getSgMdl()));
        srQueue.setSrPrint(srPrint);
        srQueue.setSrReport(srPrint.getSrReport());
        srQueue.setCdUsuAtu(cdUsu);
        srQueue.setDhAtu(new Date());
        srQueue.setPriority(priority);
        super.save(srQueue);
    }

    public String getMD5FromFile(File f) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(f);
        String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fileInputStream));
        fileInputStream.close();
        return md5;
    }

}
