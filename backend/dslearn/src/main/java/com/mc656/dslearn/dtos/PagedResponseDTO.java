package com.mc656.dslearn.dtos;

import java.util.List;

public class PagedResponseDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    private PagedResponseDTO() {}

    private PagedResponseDTO(List<T> content, int page, int size, long totalElements, int totalPages, boolean first, boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }

    public static <T> PagedResponseDTOBuilder<T> builder() {
        return new PagedResponseDTOBuilder<T>();
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public static class PagedResponseDTOBuilder<T> {

        private List<T> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean first;
        private boolean last;

        public PagedResponseDTOBuilder<T> content(List<T> content) {
            this.content = content;
            return this;
        }

        public PagedResponseDTOBuilder<T> page(int page) {
            this.page = page;
            return this;
        }

        public PagedResponseDTOBuilder<T> size(int size) {
            this.size = size;
            return this;
        }

        public PagedResponseDTOBuilder<T> totalElements(long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public PagedResponseDTOBuilder<T> totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public PagedResponseDTOBuilder<T> first(boolean first) {
            this.first = first;
            return this;
        }

        public PagedResponseDTOBuilder<T> last(boolean last) {
            this.last = last;
            return this;
        }

        public PagedResponseDTO<T> build() {
            return new PagedResponseDTO<>(content, page, size, totalElements, totalPages, first, last);
        }
    }

}
