package br.com.aquiles.report.ws;

import br.com.aquiles.core.exception.ServiceException;
import br.com.aquiles.report.bean.SRPrint;
import br.com.aquiles.report.bean.SRQueue;
import br.com.aquiles.report.bean.SRReport;
import br.com.aquiles.report.dto.SRPrintDTO;
import br.com.aquiles.report.service.MainReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "generator")
@Path(value = "aquilesreport/gen/v1")
@Log4j
public class GenWS {

    @Inject
    private MainReportService service;

    private static final String CDUSU = "WEBSERVICE";

    @POST
    @Path("/printAgain")
    @ApiOperation(value = "Print a Report Again")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK", response = SRPrint.class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response printAgain(@FormParam("sgMdl") @NotNull String sgMdl,
                               @FormParam("uuidPrinter") @NotNull String uuidPrinter) {
        SRPrint srPrint;
        try {
            srPrint = service.printAgain(sgMdl, uuidPrinter, CDUSU);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(srPrint).build();
    }

    @POST
    @Path("/printNow")
    @ApiOperation(value = "Print a Report")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK", response = SRPrint.class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response printNow(@NotNull SRPrintDTO print) {
        SRPrint srPrint;
        try {
            srPrint = service.printNow(print, CDUSU);
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(srPrint).build();
    }

    @POST
    @Path("/putOnQueue")
    @ApiOperation(value = "Put a Report on Queue")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK", response = SRQueue.class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response putOnQueue(@NotNull SRPrintDTO print) {
        SRPrint srPrint;
        try {
            srPrint = service.putOnQueue(print, print.getPriority(),CDUSU);
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(srPrint).build();
    }

    @POST
    @Path("/createReport")
    @ApiOperation(value = "Create a new report")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK", response = SRReport.class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReport(@FormParam("sgMdl") @NotNull String sgMdl,
                                 @FormParam("code") @NotNull String code,
                                 @FormParam("desc") @NotNull String desc,
                                 @FormParam("generatedDir") @NotNull String generatedDir,
                                 @FormParam("sourceFile") @NotNull String sourceFile,
                                 @FormParam("datasourceLookup") String datasourceLookup) {
        SRReport srReport;
        try {
            srReport = service.createReport(sgMdl, code, desc, generatedDir, sourceFile, datasourceLookup,
                    CDUSU);
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(srReport).build();
    }

}
