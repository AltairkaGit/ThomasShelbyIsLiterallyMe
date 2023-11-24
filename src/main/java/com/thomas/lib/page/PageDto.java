package com.thomas.lib.page;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

public class PageDto<T> {
    List<T> content;
    int size;
    int page;

    public static <T> PageDto<T> of(Page<T> page) {
        return new PageDto<>(page.getContent(), page.getSize(), page.getNumber());
    }

    public PageDto(List<T> content, int size, int page) {
        this.content = content;
        this.size = size;
        this.page = page;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageDto<?> pageDto = (PageDto<?>) o;
        return size == pageDto.size && page == pageDto.page && Objects.equals(content, pageDto.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, size, page);
    }

    @Override
    public String toString() {
        return "PageDto{" +
                "content=" + content +
                ", size=" + size +
                ", page=" + page +
                '}';
    }
}
