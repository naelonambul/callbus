package com.bus.call.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="board_id")
    private Long id;

    private String title;

    @Lob
    private String content;

    @Column(updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private LocalDateTime deletedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Heart> hearts = new ArrayList<>();

    @Builder
    public Board(String title, String content, Member member){
        this.title = title;
        this.content = content;
        this.member = member;
        this.deletedDate = LocalDateTime.of(1990,01,01,00,00,00);
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }

    public BoardEditor.BoardEditorBuilder toEditor(){
        return BoardEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(BoardEditor boardEditor){
        this.title = boardEditor.getTitle();
        this.content = boardEditor.getContent();
        this.lastModifiedDate = LocalDateTime.now();
    }

    public void delete(){
        this.deletedDate = LocalDateTime.now();
    }

    public void addHeart(Heart heart){
        this.hearts.add(heart);
    }
}
