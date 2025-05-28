import React, { useEffect, useState, useRef, useCallback } from "react";
import Loading from "~/components/Loading";
import type { PageRequest, Pagination } from "~/types/pagination";

interface InfiniteScrollListProps<T> {
    hook: (req: PageRequest) => any;
    renderItem: (item: T) => React.ReactNode;
    emptyComponent?: React.ReactNode;
    loadingComponent?: React.ReactNode;
}

export default function InfiniteScrollList<T>({
                                                  hook,
                                                  renderItem,
                                                  emptyComponent,
                                                  loadingComponent,
                                              }: InfiniteScrollListProps<T>) {
    const [page, setPage] = useState(1);
    const [data, setData] = useState<T[]>([]);
    const [hasMore, setHasMore] = useState(true);
    const observerRef = useRef<HTMLDivElement>(null);

    const { data: currentData, isLoading, isSuccess } = hook({ page });

    // Reset state when the hook changes (e.g., search parameters updated)
    useEffect(() => {
        setPage(1);
        setData([]);
        setHasMore(true);
    }, [hook]);

    // Handle new data and pagination state
    useEffect(() => {
        if (!isLoading && isSuccess && currentData && currentData.data) {
            setData((prev) =>
                page === 1 ? currentData.data : [...prev, ...currentData.data]
            );
            setHasMore(currentData.page < currentData.totalPages);
        }
    }, [isLoading, currentData, page, isSuccess]);

    // Set up intersection observer for infinite scroll
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
    }, [hasMore, isLoading]); // Recreate observer when loading state changes

    return (
        <>
            {data.length === 0 && !hasMore ? (
                emptyComponent || (
                    <div className="text-center text-gray-500 py-8">
                        No items found
                    </div>
                )
            ) : data.map(renderItem)}

            {/* Observer element for triggering next page load */}
            {hasMore && <div ref={observerRef} />}

            {/* Loading indicator */}
            {isLoading && (loadingComponent || <Loading />)}
        </>
    );
}