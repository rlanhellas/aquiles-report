package br.com.aquiles.report.service;

import br.com.aquiles.core.dto.NamedParams;
import br.com.aquiles.core.exception.ServiceException;
import br.com.aquiles.core.service.GenericService;
import br.com.aquiles.persistence.dao.IDAO;
import br.com.aquiles.report.annotation.SRDatasource;
import br.com.aquiles.report.bean.SRPrint;
import br.com.aquiles.report.bean.SRQueue;
import br.com.aquiles.report.bean.SRReport;
import br.com.aquiles.report.dataquery.SRPrintDQ;
import br.com.aquiles.report.dataquery.SRQueueDQ;
import br.com.aquiles.report.dataquery.SRReportDQ;
import br.com.aquiles.report.dto.SRPrintDTO;
import br.com.aquiles.report.enums.EnumSRFileType;
import br.com.aquiles.report.exception.SRException;
import lombok.extern.log4j.Log4j;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Named
@RequestScoped
@Log4j
public class MainReportService extends GenericService {

    @Inject
    @SRDatasource
    private IDAO daoSR;

    @Inject
    private GenReportService genReportService;

    @Override
    public IDAO getDAO() {
        return daoSR;
    }

    @Override
    public IDAO getDAO(Class qualifier) {
        return getDAO();
    }

    public List<SRReport> findReportByModule(String sgMdl) throws ServiceException {
        return findList(SRReportDQ.FINDBY_SGMDL, new NamedParams("sgMdl", sgMdl));
    }

    public List<SRReport> findReportByModuleAndCode(String sgMdl, String code) throws ServiceException {
        return findList(SRReportDQ.FINDBY_SGMDL_CODE, new NamedParams("sgMdl", sgMdl, "code", code));
    }

    public List<SRReport> findReportByModuleAndLikeSourceFile(String sgMdl, String sourceFile) throws ServiceException {
        return findList(SRReportDQ.FINDBY_SGMDL_LIKE_SOURCEFILE,
                new NamedParams("sgMdl", sgMdl, "sourceFile", sourceFile));
    }

    public List<SRReport> findReportByModuleAndLikeDescription(String sgMdl, String desc) throws ServiceException {
        return findList(SRReportDQ.FINDBY_SGMDL_LIKE_DESC,
                new NamedParams("sgMdl", sgMdl, "desc", desc));
    }

    public List<SRPrint> findPrintsByModule(String sgMdl) throws ServiceException {
        return findList(SRPrintDQ.FINBY_SGMDL, new NamedParams("sgMdl", sgMdl));
    }

    public List<SRPrint> findPrintsByModuleAndLikeGeneratedFile(String sgMdl, String generatedFile) throws ServiceException {
        return findList(SRPrintDQ.FINBY_SGMDL_LIKE_GENERATEDFILE, new NamedParams("sgMdl", sgMdl,
                "generatedFile", generatedFile));
    }

    public List<SRPrint> findPrintsByModuleAndLikeSourceFile(String sgMdl, String sourceFile) throws ServiceException {
        return findList(SRPrintDQ.FINBY_SGMDL_LIKE_SOURCEFILE, new NamedParams("sgMdl", sgMdl,
                "sourceFile", sourceFile));
    }

    public List<SRPrint> findPrintsByModuleAndFileType(String sgMdl, EnumSRFileType fileType) throws ServiceException {
        return findList(SRPrintDQ.FINBY_SGMDL_FILETYPE, new NamedParams("sgMdl", sgMdl,
                "fileType", fileType));
    }

    public List<SRPrint> findPrintsByModuleAndNotGenerated(String sgMdl) throws ServiceException {
        return findList(SRPrintDQ.FINBY_SGMDL_NOTGENERATED, new NamedParams("sgMdl", sgMdl));
    }

    public List<SRPrint> findPrintsByModuleAndWithError(String sgMdl) throws ServiceException {
        return findList(SRPrintDQ.FINBY_SGMDL_WITH_ERROR, new NamedParams("sgMdl", sgMdl));
    }

    public List<SRPrint> findPrintsByModuleAndCode(String sgMdl, String code) throws ServiceException {
        return findList(SRPrintDQ.FINBY_SGMDL_CODE, new NamedParams("sgMdl", sgMdl, "code", code));
    }

    public List<SRQueue> findQueueByModule(String sgMdl) throws ServiceException {
        return findList(SRQueueDQ.FINDBY_SGMDL, new NamedParams("sgMdl", sgMdl));
    }

    public List<SRQueue> findQueueByModuleAndCode(String sgMdl, String code) throws ServiceException {
        return findList(SRQueueDQ.FINDBY_SGMDL_CODE, new NamedParams("sgMdl", sgMdl, "code", code));
    }

    public List<SRQueue> findQueueByModuleNotPrinted(String sgMdl) throws ServiceException {
        return findList(SRQueueDQ.FINDBY_SGMDL_ORDER_PRIORITY_NOT_PRINTED,
                new NamedParams("sgMdl", sgMdl));
    }

    public List<SRQueue> findQueueByModuleNotPrintedTop10(String sgMdl) throws ServiceException {
        return findList(SRQueueDQ.FINDBY_SGMDL_ORDER_PRIORITY_NOT_PRINTED,
                new NamedParams("sgMdl", sgMdl),0,10);
    }

    private SRPrint print(SRPrintDTO printDTO,
                          int priority,
                          String cdUsu, boolean immediately) throws SRException {
        return genReportService.generateReport(printDTO, priority, cdUsu, immediately);
    }

    public SRPrint printNow(SRPrintDTO printDTO,
                            String cdUsu) throws SRException {
        return print(printDTO, 0, cdUsu, true);
    }

    public SRPrint putOnQueue(SRPrintDTO printDTO,
                              int priority,
                              String cdUsu) throws SRException {
        return print(printDTO, priority, cdUsu, false);
    }

    public SRPrint printAgain(String sgMdl, String uuidPrinter, String cdUsu) throws ServiceException {
        try {
            log.info("Printing again UUID " + uuidPrinter + " module " + sgMdl);
            SRPrint.PK pk = new SRPrint.PK();
            pk.setId(uuidPrinter);
            pk.setSgMdl(sgMdl);
            SRPrint srPrint = (SRPrint) findByClass(SRPrint.class, pk);
            if (srPrint != null) {
                return genReportService.printNow(srPrint, cdUsu);
            } else {
                log.error("Can't found Printer with this UUID");
            }
        } catch (Exception e) {
            log.error(e);
            throw new ServiceException(e);
        }

        return null;
    }

    public SRReport createReport(String sgMdl, String code, String desc,
                                 String generatedDir,
                                 String sourceFile,
                                 String datasourceLookup,
                                 String cdUsu) throws ServiceException, IOException {
        log.info("Creating report with code " + code + " and module " + sgMdl);
        SRReport.PK pk = new SRReport.PK();
        pk.setSgMdl(sgMdl);
        pk.setCode(code);
        SRReport reportFind = (SRReport) findByClass(SRReport.class, pk);
        if (reportFind != null) {
            throw new ServiceException("Report already exists with this code and module");
        }

        if (!(new File(sourceFile)).exists()) {
            throw new ServiceException("File " + sourceFile + " not found");
        }

        SRReport srReport = new SRReport();
        srReport.setCreatedAt(new Date());
        srReport.setMd5HashSourceFile(genReportService.getMD5FromFile(new File(sourceFile)));
        srReport.setDatasourceLookup(datasourceLookup);
        srReport.setDescription(desc);
        srReport.setGeneratedReportDir(generatedDir);
        srReport.setSourceFile(sourceFile);
        srReport.setId(pk);
        srReport.setDhAtu(new Date());
        srReport.setCdUsuAtu(cdUsu);
        return (SRReport) super.save(srReport);
    }

}
