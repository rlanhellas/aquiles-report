package br.com.aquiles.report.dto;

import br.com.aquiles.report.enums.EnumSRFileType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SRPrintDTO implements Serializable {
    private String codeReport;
    private String sgMdl;
    private HashMap<String, Object> params = new HashMap<>();
    private ArrayList beans = new ArrayList();
    private EnumSRFileType fileType;
    private Integer priority = 0;

    public static SRPrintDTO fromString(String jsonRepresentation) {
        ObjectMapper mapper = new ObjectMapper(); //Jackson's JSON marshaller
        SRPrintDTO o;
        try {
            o = mapper.readValue(jsonRepresentation, SRPrintDTO.class);
        } catch (IOException e) {
            throw new WebApplicationException();
        }
        return o;
    }
}
