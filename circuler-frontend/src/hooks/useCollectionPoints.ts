import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { donateBook, getCollectionPoints, getCollectionPointDetail } from '../services/collection-points.service'
import { CollectionPoint, CollectionPointDetail, BookDonateRequest } from '../types/collection-point.types'
import { BookInstance } from '../types/book.types'
import { BOOK_INSTANCES_QUERY_KEY } from './useBookInstances'

export const COLLECTION_POINTS_QUERY_KEY = 'collection-points'

export function useCollectionPoints() {
  const query = useQuery<CollectionPoint[], Error, CollectionPoint[], [string]>({
    queryKey: [COLLECTION_POINTS_QUERY_KEY],
    queryFn: getCollectionPoints,
    staleTime: 1000 * 60 * 5,
  })

  return {
    collectionPoints: query.data ?? [],
    isLoading: query.isLoading,
    isError: query.isError,
  }
}

export function useCollectionPointDetail(id: number) {
  const query = useQuery<CollectionPointDetail, Error, CollectionPointDetail, [string, number]>({
    queryKey: [COLLECTION_POINTS_QUERY_KEY, id],
    queryFn: () => getCollectionPointDetail(id),
    enabled: !!id,
    staleTime: 1000 * 60 * 5,
  })

  return {
    collectionPoint: query.data ?? null,
    isLoading: query.isLoading,
    isError: query.isError,
  }
}

export function useDonateBook() {
  const queryClient = useQueryClient()

  const mutation = useMutation<BookInstance, Error, BookDonateRequest>({
    mutationFn: (payload) => donateBook(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [BOOK_INSTANCES_QUERY_KEY] })
      queryClient.invalidateQueries({ queryKey: [COLLECTION_POINTS_QUERY_KEY] })
    },
  })

  return {
    donateBook: mutation.mutate,
    isPending: mutation.isPending,
    isSuccess: mutation.isSuccess,
    error: mutation.error,
  }
}
