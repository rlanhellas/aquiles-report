package br.com.aquiles.report.ws;

import br.com.aquiles.core.exception.ServiceException;
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

@Api(value = "query-report")
@Path(value = "aquilesreport/queryreport/v1")
@Log4j
public class QueryReportWs {

    @Inject
    private MainReportService service;

    @GET
    @Path("/getReports")
    @ApiOperation(value = "Query Reports by Module")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK", response = SRReport[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getReports(@QueryParam(value = "sgMdl") @NotNull String sgMdl) {
        List<SRReport> reports;
        try {
            reports = service.findReportByModule(sgMdl);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(reports).build();
    }

    @GET
    @Path("/getReportsByCode")
    @ApiOperation(value = "Query Reports by Module and Code")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK", response = SRReport[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getReportsByCode(@QueryParam(value = "sgMdl") @NotNull String sgMdl,
                                     @QueryParam(value = "code") @NotNull String code) {
        List<SRReport> reports;
        try {
            reports = service.findReportByModuleAndCode(sgMdl, code);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(reports).build();
    }

    @GET
    @Path("/getReportsLikeDescription")
    @ApiOperation(value = "Query Reports by Module and Like Description")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK", response = SRReport[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getReportsLikeDescription(@QueryParam(value = "sgMdl") @NotNull String sgMdl,
                                              @QueryParam(value = "desc") @NotNull String desc) {
        List<SRReport> reports;
        try {
            reports = service.findReportByModuleAndLikeDescription(sgMdl, desc);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(reports).build();
    }

    @GET
    @Path("/getReportsLikeSourceFile")
    @ApiOperation(value = "Query Reports by Module and Like Source File")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK", response = SRReport[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getReportsByDescription(@QueryParam(value = "sgMdl") @NotNull String sgMdl,
                                            @QueryParam(value = "sourceFile") @NotNull String sourceFile) {
        List<SRReport> reports;
        try {
            reports = service.findReportByModuleAndLikeSourceFile(sgMdl, sourceFile);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(reports).build();
    }

}
