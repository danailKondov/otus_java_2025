package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientCreateDto {

    private Long id;
    private String name;
    private String address;
    private String phoneNumbers;

}
