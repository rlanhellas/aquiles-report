package br.com.aquiles.report.dataquery;

public class SRReportDQ {
    public static final String FINDBY_ID = "SELECT c FROM SRReport c WHERE c.id = :id";
    public static final String FINDBY_SGMDL = "SELECT c FROM SRReport c WHERE c.id.sgMdl = :sgMdl " +
            "ORDER BY c.createdAt DESC";
    public static final String FINDBY_SGMDL_CODE = "SELECT c FROM SRReport c WHERE c.id.sgMdl = :sgMdl " +
            "AND c.id.code = :code " +
            "ORDER BY c.createdAt DESC";
    public static final String FINDBY_SGMDL_LIKE_SOURCEFILE = "SELECT c FROM SRReport c WHERE c.id.sgMdl = :sgMdl " +
            "AND c.sourceFile like concat('%',:sourceFile,'%') " +
            "ORDER BY c.createdAt DESC";
    public static final String FINDBY_SGMDL_LIKE_DESC = "SELECT c FROM SRReport c WHERE c.id.sgMdl = :sgMdl " +
            "AND c.description like concat('%',:desc,'%') " +
            "ORDER BY c.createdAt DESC";
}
