package br.com.aquiles.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EnumSRFileType {

    PDF("pdf");

    @Getter
    private String desc;
}
