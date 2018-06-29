package br.com.aquiles.report.bean;

import br.com.aquiles.persistence.bean.AbstractEntityAtu;
import br.com.aquiles.report.utils.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sr_report")
@Getter
@Setter
@JsonIgnoreProperties({ "novo", "selected", "uuid", "deletable", "editable" })
public class SRReport extends AbstractEntityAtu {

    @EmbeddedId
    private PK id;

    @Column
    private String description;

    @Column(name = "source_file")
    private String sourceFile;

    @Column(name = "md5_hash_source_file")
    private String md5HashSourceFile;


    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @Column(name = "datasource_lookup")
    private String datasourceLookup;

    @Column(name = "generated_report_dir")
    private String generatedReportDir;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PK implements Serializable {
        @Column(name = "code_report")
        private String code;
        @Column(name = "sg_mdl")
        private String sgMdl;
    }
}
