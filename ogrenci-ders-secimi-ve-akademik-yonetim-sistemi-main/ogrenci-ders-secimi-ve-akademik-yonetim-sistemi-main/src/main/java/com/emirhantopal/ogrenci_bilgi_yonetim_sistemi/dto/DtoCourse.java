package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class DtoCourse  extends DtoBaseEntity {
    private String code;

    private String name;

    private String credit;

    private Integer akts;

    private Integer quota;

}
