package pl.potat0x.nomock.inmemoryrepository.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.potat0x.nomock.inmemoryrepository.reflection.ReflectiveComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class InMemoryPagingAndSortingRepository<T, ID> extends InMemoryCrudRepository<T, ID> implements PagingAndSortingRepository<T, ID> {

    public InMemoryPagingAndSortingRepository(Supplier<ID> idSupplier) {
        super(idSupplier);
    }

    public InMemoryPagingAndSortingRepository(ID initialId, UnaryOperator<ID> idSuccessorFunction) {
        super(initialId, idSuccessorFunction);
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        List<T> entities = iterableToList(findAll());
        sortEntities(sort, entities);
        return entities;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        List<T> entities = iterableToList(findAll());
        sortEntities(pageable.getSort(), entities);
        return getPage(pageable, entities);
    }

    private void sortEntities(Sort sort, List<T> entities) {
        List<Sort.Order> orders = sort.stream()
                .collect(Collectors.toList());
        entities.sort((o1, o2) -> ReflectiveComparator.compareObjectsByMultipleFields(o1, o2, orders));
    }

    private Page<T> getPage(Pageable pageable, List<T> items) {
        if (pageable.isUnpaged()) {
            return new PageImpl<>(items);
        }

        final int pageStart = getPageStart(pageable);
        if (pageStart > items.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        int pageEnd = getPageEnd(pageable.getPageSize(), items.size(), pageStart);

        List<T> entitiesInPage = items.subList(pageStart, pageEnd);
        return new PageImpl<>(entitiesInPage, pageable, entitiesInPage.size());
    }

    private int getPageEnd(int pageSize, int numberOfAllEntities, int pageStart) {
        return numberOfAllEntities - pageStart < pageSize ? numberOfAllEntities : pageStart + pageSize;
    }

    private int getPageStart(Pageable pageable) {
        return pageable.getPageNumber() * pageable.getPageSize();
    }

    private static <T> List<T> iterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }
}
