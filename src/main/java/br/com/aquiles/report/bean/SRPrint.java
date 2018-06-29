package br.com.aquiles.report.bean;

import br.com.aquiles.persistence.bean.AbstractEntityAtu;
import br.com.aquiles.report.enums.EnumSRFileType;
import br.com.aquiles.report.utils.CompressGzipUtils;
import br.com.aquiles.report.utils.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.*;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import javax.persistence.*;
import java.beans.Transient;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Stores a report print, with param everything needed to print the report
 */
@Entity
@Table(name = "sr_print")
@Getter
@Setter
@JsonIgnoreProperties({ "novo", "selected", "uuid", "deletable", "editable" })
public class SRPrint extends AbstractEntityAtu {

    @EmbeddedId
    private PK id;

    @ManyToOne
    @JoinColumnsOrFormulas(value =
            {@JoinColumnOrFormula(column = @JoinColumn(name = "code_report")),
                    @JoinColumnOrFormula(formula = @JoinFormula(value = "sg_mdl"))
            })
    private SRReport srReport;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "started_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startedAt;


    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "finished_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedAt;

    @Column(name = "generated_file")
    private String generatedFile;

    @Column(name = "datasource_lookup")
    private String datasourceLookup;

    @Column(name = "source_file")
    private String sourceFile;

    @Column(name = "md5_hash_source_file")
    private String md5HashSourceFile;

    @Column(name = "params_gzip")
    @JsonIgnore
    private String paramsGzip;

    @Column(name = "beans_gzip")
    @JsonIgnore
    private String beansGzip;

    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    private EnumSRFileType fileType;

    @Column
    private String error;

    @Transient
    @JsonIgnore
    private ArrayList beans;

    @Transient
    @JsonIgnore
    private HashMap<String, Object> params;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PK implements Serializable {
        @Column(name = "uuid_srprint")
        private String id;
        @Column(name = "sg_mdl")
        private String sgMdl;
    }

    public ArrayList getBeans() throws IOException, ClassNotFoundException {
        if (CollectionUtils.isEmpty(beans)) {
            beans = (ArrayList) CompressGzipUtils.uncompress(beansGzip);
        }

        return beans;
    }

    public HashMap<String, Object> getParams() throws IOException, ClassNotFoundException {
        if (params == null) {
            params = (HashMap) CompressGzipUtils.uncompress(paramsGzip);
        }

        return params;
    }
}
