package br.com.aquiles.report.ws;

import br.com.aquiles.core.exception.ServiceException;
import br.com.aquiles.report.bean.SRQueue;
import br.com.aquiles.report.bean.SRReport;
import br.com.aquiles.report.service.MainReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "query-queue")
@Path(value = "aquilesreport/queryqueue/v1")
@Log4j
public class QueryQueueWs {

    @Inject
    private MainReportService service;

    @GET
    @Path("/getQueue")
    @ApiOperation(value = "Query Queue by Module")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK", response = SRQueue[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getReports(@QueryParam(value = "sgMdl") @NotNull String sgMdl) {
        List<SRQueue> reports;
        try {
            reports = service.findQueueByModule(sgMdl);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(reports).build();
    }

    @GET
    @Path("/getQueueByCode")
    @ApiOperation(value = "Query Queue by Module and Code")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK", response = SRQueue[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getReportsByCode(@QueryParam(value = "sgMdl") @NotNull String sgMdl,
                                     @QueryParam(value = "code") @NotNull String code) {
        List<SRQueue> reports;
        try {
            reports = service.findQueueByModuleAndCode(sgMdl, code);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(reports).build();
    }

    @GET
    @Path("/getQueueNotPrinted")
    @ApiOperation(value = "Query Queue by Module not printed ordering by priority")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK", response = SRQueue[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getReportsNotPrinted(@QueryParam(value = "sgMdl") @NotNull String sgMdl) {
        List<SRQueue> reports;
        try {
            reports = service.findQueueByModuleNotPrinted(sgMdl);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(reports).build();
    }
}
