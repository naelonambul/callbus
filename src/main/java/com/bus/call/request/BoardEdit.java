package com.bus.call.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class BoardEdit {
    @NotBlank(message = "타이틀을 입력하세요.")
    private String title;

    @NotBlank(message="콘텐츠를 입력해주세요.")
    private String content;

    @Builder
    public BoardEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
