package dev.aletheia.doctor.dtos;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PaginationDTO<T> {
    private final long totalElements;
    private final long totalPages;
    private final int page;
    private final int size;
    private final List<T> data;

    public PaginationDTO(Page<T> pageable) {
        this.totalElements = pageable.getTotalElements();
        this.totalPages = pageable.getTotalPages();
        this.size = pageable.getSize();
        this.data = pageable.getContent();
        this.page = pageable.getPageable().getPageNumber();
    }
}
