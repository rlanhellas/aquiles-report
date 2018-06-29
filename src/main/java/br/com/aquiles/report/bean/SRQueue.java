package br.com.aquiles.report.bean;

import br.com.aquiles.persistence.bean.AbstractEntityAtu;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Stores report in queue to JOB process
 */
@Entity
@Table(name = "sr_queue")
@Getter
@Setter
@JsonIgnoreProperties({ "novo", "selected", "uuid", "deletable", "editable" })
public class SRQueue extends AbstractEntityAtu {
    @EmbeddedId
    private PK id;

    @JsonIgnore
    @ManyToOne
    @JoinColumnsOrFormulas(value =
            {@JoinColumnOrFormula(column = @JoinColumn(name = "code_report")),
                    @JoinColumnOrFormula(formula = @JoinFormula(value = "sg_mdl"))
            })
    private SRReport srReport;

    @ManyToOne
    @JoinColumnsOrFormulas(value =
            {@JoinColumnOrFormula(column = @JoinColumn(name = "uuid_srprint")),
                    @JoinColumnOrFormula(formula = @JoinFormula(value = "sg_mdl"))
            })
    private SRPrint srPrint;

    @Column
    private int priority;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PK implements Serializable {
        @Column(name = "uuid_srqueue")
        private String id;

        @Column(name = "sg_mdl")
        private String sgMdl;
    }

}
