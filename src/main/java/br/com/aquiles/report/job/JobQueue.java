package br.com.aquiles.report.job;

import br.com.aquiles.report.bean.SRQueue;
import br.com.aquiles.report.service.GenReportService;
import br.com.aquiles.report.service.MainReportService;
import lombok.extern.log4j.Log4j;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;
import java.util.List;

@Log4j
@DisallowConcurrentExecution
@Scheduled(cronExpression = "0 0/5 * 1/1 * ? *")
public class JobQueue implements Job {
    @Inject
    private MainReportService mainReportService;

    @Inject
    private GenReportService genReportService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<SRQueue> srQueueList = mainReportService.findQueueByModuleNotPrintedTop10("AW");
            for (SRQueue srQueue : srQueueList) {
                log.info("Executing SRQUEUE..." + srQueue.getId().getId());
                genReportService.printNow(srQueue.getSrPrint(), "AQUILES-REPORT-JOB");
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

}
