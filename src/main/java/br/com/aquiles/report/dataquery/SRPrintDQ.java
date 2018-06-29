package br.com.aquiles.report.dataquery;

public class SRPrintDQ {
    public static final String FINBY_SGMDL = "SELECT c FROM SRPrint c WHERE c.id.sgMdl = :sgMdl " +
            "ORDER BY c.startedAt DESC";
    public static final String FINBY_SGMDL_LIKE_GENERATEDFILE = "SELECT c FROM SRPrint c " +
            "WHERE c.id.sgMdl = :sgMdl AND c.generatedFile like concat('%',:generatedFile,'%') " +
            "ORDER BY c.startedAt DESC";
    public static final String FINBY_SGMDL_LIKE_SOURCEFILE = "SELECT c FROM SRPrint c " +
            "WHERE c.id.sgMdl = :sgMdl AND c.sourceFile like concat('%',:sourceFile,'%') " +
            "ORDER BY c.startedAt DESC";
    public static final String FINBY_SGMDL_FILETYPE = "SELECT c FROM SRPrint c " +
            "WHERE c.id.sgMdl = :sgMdl AND c.fileType = :fileType " +
            "ORDER BY c.startedAt DESC";
    public static final String FINBY_SGMDL_NOTGENERATED = "SELECT c FROM SRPrint c " +
            "WHERE c.id.sgMdl = :sgMdl AND c.finishedAt is null " +
            "ORDER BY c.startedAt DESC";
    public static final String FINBY_SGMDL_WITH_ERROR = "SELECT c FROM SRPrint c " +
            "WHERE c.id.sgMdl = :sgMdl AND c.error is not null " +
            "ORDER BY c.startedAt DESC";
    public static final String FINBY_SGMDL_CODE = "SELECT c FROM SRPrint c " +
            "WHERE c.id.sgMdl = :sgMdl AND c.srReport.id.code = :code " +
            "ORDER BY c.startedAt DESC";
}
