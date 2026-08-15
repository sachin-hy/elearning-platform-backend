package com.example.project.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roomId;
    private Map<String, Object> payload;
}