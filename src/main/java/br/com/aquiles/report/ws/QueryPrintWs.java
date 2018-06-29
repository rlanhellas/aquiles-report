package br.com.aquiles.report.ws;

import br.com.aquiles.core.dto.NamedParams;
import br.com.aquiles.core.exception.ServiceException;
import br.com.aquiles.report.bean.SRPrint;
import br.com.aquiles.report.bean.SRReport;
import br.com.aquiles.report.dataquery.SRPrintDQ;
import br.com.aquiles.report.enums.EnumSRFileType;
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

@Api(value = "query-print")
@Path(value = "aquilesreport/queryprint/v1")
@Log4j
public class QueryPrintWs {

    @Inject
    private MainReportService service;

    @GET
    @Path("/getPrints")
    @ApiOperation(value = "Query Prints by Module")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK",response = SRPrint[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getPrints(@QueryParam(value = "sgMdl") @NotNull String sgMdl) {
        List<SRPrint> prints;
        try {
            prints = service.findPrintsByModule(sgMdl);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(prints).build();
    }

    @GET
    @Path("/getPrintsLikeGeneratedFile")
    @ApiOperation(value = "Query Prints by Module and Like Generated File")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK",response = SRPrint[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getPrintsLikeGeneratedFile(@QueryParam(value = "sgMdl") @NotNull String sgMdl,
                                               @QueryParam(value = "generatedFile") @NotNull String generatedFile) {
        List<SRPrint> prints;
        try {
            prints = service.findPrintsByModuleAndLikeGeneratedFile(sgMdl, generatedFile);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(prints).build();
    }

    @GET
    @Path("/getPrintsLikeSourceFile")
    @ApiOperation(value = "Query Prints by Module and Like Source File")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK",response = SRPrint[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getPrintsLikeSourceFile(@QueryParam(value = "sgMdl") @NotNull String sgMdl,
                                            @QueryParam(value = "sourceFile") @NotNull String sourceFile) {
        List<SRPrint> prints;
        try {
            prints = service.findPrintsByModuleAndLikeSourceFile(sgMdl, sourceFile);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(prints).build();
    }

    @GET
    @Path("/getPrintsByFileType")
    @ApiOperation(value = "Query Prints by Module and File Type")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK",response = SRPrint[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getPrintsLikeSourceFile(@QueryParam(value = "sgMdl") @NotNull String sgMdl,
                                            @QueryParam(value = "fileType") @NotNull EnumSRFileType fileType) {
        List<SRPrint> prints;
        try {
            prints = service.findPrintsByModuleAndFileType(sgMdl, fileType);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(prints).build();
    }

    @GET
    @Path("/getPrintsNotGenerated")
    @ApiOperation(value = "Query Prints Not Generated")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK",response = SRPrint[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getPrintsNotGenerated(@QueryParam(value = "sgMdl") @NotNull String sgMdl) {
        List<SRPrint> prints;
        try {
            prints = service.findPrintsByModuleAndNotGenerated(sgMdl);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(prints).build();
    }

    @GET
    @Path("/getPrintsWithError")
    @ApiOperation(value = "Query Prints With Error")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK",response = SRPrint[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getPrintsWithError(@QueryParam(value = "sgMdl") @NotNull String sgMdl) {
        List<SRPrint> prints;
        try {
            prints = service.findPrintsByModuleAndWithError(sgMdl);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(prints).build();
    }

    @GET
    @Path("/getPrintsByCode")
    @ApiOperation(value = "Query Prints By Module and Code")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "OK",response = SRPrint[].class),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @Produces("application/json")
    public Response getPrintsByCode(@QueryParam(value = "sgMdl") @NotNull String sgMdl,
                                    @QueryParam(value = "code") @NotNull String code) {
        List<SRPrint> prints;
        try {
            prints = service.findPrintsByModuleAndCode(sgMdl, code);
        } catch (ServiceException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok(prints).build();
    }

}
