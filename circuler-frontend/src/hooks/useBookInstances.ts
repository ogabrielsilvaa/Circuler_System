import { useQuery } from '@tanstack/react-query'
import {
  getBookInstances,
  searchBookInstancesByTitle,
  getBookInstancesByCategory,
  getBookInstanceById,
} from '../services/bookInstances.service'
import { BookInstance } from '../types/book.types'

export const BOOK_INSTANCES_QUERY_KEY = 'book-instances'

export function useBookInstances() {
  const query = useQuery<BookInstance[], Error, BookInstance[], [string]>({
    queryKey: [BOOK_INSTANCES_QUERY_KEY],
    queryFn: getBookInstances,
    staleTime: 1000 * 60 * 5,
  })

  return {
    bookInstances: query.data ?? [],
    isLoading: query.isLoading,
    isError: query.isError,
  }
}

export function useBookInstanceSearch(title: string) {
  const query = useQuery<BookInstance[], Error, BookInstance[], [string, string, string]>({
    queryKey: [BOOK_INSTANCES_QUERY_KEY, 'search', title],
    queryFn: () => searchBookInstancesByTitle(title),
    enabled: title.trim().length > 0,
    staleTime: 1000 * 60 * 5,
  })

  return {
    bookInstances: query.data ?? [],
    isLoading: query.isLoading,
    isError: query.isError,
  }
}

export function useBookInstancesByCategory(category: number | null) {
  const query = useQuery<BookInstance[], Error, BookInstance[], [string, string, number]>({
    queryKey: [BOOK_INSTANCES_QUERY_KEY, 'filter', category!],
    queryFn: () => getBookInstancesByCategory(category!),
    enabled: category !== null,
    staleTime: 1000 * 60 * 5,
  })

  return {
    bookInstances: query.data ?? [],
    isLoading: query.isLoading,
    isError: query.isError,
  }
}

export function useBookInstanceById(id: number) {
  const query = useQuery<BookInstance, Error, BookInstance, [string, number]>({
    queryKey: [BOOK_INSTANCES_QUERY_KEY, id],
    queryFn: () => getBookInstanceById(id),
    enabled: !!id,
    staleTime: 1000 * 60 * 5,
  })

  return {
    bookInstance: query.data ?? null,
    isLoading: query.isLoading,
    isError: query.isError,
  }
}
