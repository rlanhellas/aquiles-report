package br.com.aquiles.report.dataquery;

public class SRQueueDQ {
    public static final String FINDBY_SGMDL = "SELECT c FROM SRQueue c WHERE c.id.sgMdl = :sgMdl " +
            "ORDER BY c.srPrint.startedAt DESC";
    public static final String FINDBY_SGMDL_CODE = "SELECT c FROM SRQueue c WHERE c.id.sgMdl = :sgMdl " +
            "AND c.srReport.id.code = :code " +
            "ORDER BY c.srPrint.startedAt DESC";
    public static final String FINDBY_SGMDL_ORDER_PRIORITY_NOT_PRINTED = "SELECT c FROM SRQueue c WHERE c.id.sgMdl = :sgMdl " +
            "AND c.srPrint.startedAt is null " +
            "ORDER BY c.priority";

}
