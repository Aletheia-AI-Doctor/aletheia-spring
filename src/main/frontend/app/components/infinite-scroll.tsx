import React, { useEffect, useState, useRef, useCallback } from "react";
import Loading from "~/components/Loading";
import type { PageRequest, Pagination } from "~/types/pagination";

interface InfiniteScrollListProps<T> {
    hook: (req: PageRequest) => any;
    renderItem: (item: T) => React.ReactNode;
    emptyComponent?: React.ReactNode;
    loadingComponent?: React.ReactNode;
    refresh?: number;
}

export default function InfiniteScrollList<T>({
                                                  hook,
                                                  renderItem,
                                                  emptyComponent,
                                                  loadingComponent,
                                                  refresh
                                              }: InfiniteScrollListProps<T>) {
    refresh ??= 0;
    const [page, setPage] = useState(1);
    const [data, setData] = useState<T[]>([]);
    const [hasMore, setHasMore] = useState(true);
    const observerRef = useRef<HTMLDivElement>(null);
    const [lastPage, setLastPage] = useState(-1);

    const { data: currentData, isLoading, isSuccess, refetch } = hook({ page });

    useEffect(() => {
        setPage(1);
        setData([]);
        setLastPage(-1);
        setHasMore(true);
    }, [refresh]);

    useEffect(() => {
        if (page === 1) {
            setData([]);
            setLastPage(-1);
            setHasMore(true);
        }
    }, []);

    useEffect(() => {
        if (!isLoading && isSuccess && currentData && currentData.data) {
            setData((prev) => currentData.page === lastPage ? prev : [...prev, ...currentData.data]
            );

            setHasMore(currentData.page < currentData.totalPages -1);

            setLastPage(currentData.page);
        }
    }, [isLoading, currentData, page, isSuccess]);

    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting && hasMore && !isLoading) {
                    setPage((prev) => prev + 1);
                }
            },
            { rootMargin: '100px' }
        );

        const currentRef = observerRef.current;
        if (currentRef) {
            observer.observe(currentRef);
        }

        return () => {
            if (currentRef) {
                observer.unobserve(currentRef);
            }
            observer.disconnect();
        };
    }, [hasMore, isLoading]);

    return (
        <>
            {data.length === 0 && !hasMore ? (
                emptyComponent || (
                    <div className="text-center text-gray-500 py-8">
                        No items found
                    </div>
                )
            ) : data.map(renderItem)}

            {hasMore && <div ref={observerRef} />}

            {isLoading && (loadingComponent || <Loading />)}
        </>
    );
}