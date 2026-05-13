import { useQuery } from '@tanstack/react-query'
import {
  getBookInstances,
  searchBookInstancesByTitle,
  getBookInstancesByCategory,
} from '../services/bookInstances.service'

export function useBookInstances() {
  return useQuery({
    queryKey: ['book-instances'],
    queryFn: getBookInstances,
  })
}

export function useBookInstanceSearch(title: string) {
  return useQuery({
    queryKey: ['book-instances', 'search', title],
    queryFn: () => searchBookInstancesByTitle(title),
    enabled: title.trim().length > 0,
  })
}

export function useBookInstancesByCategory(category: number | null) {
  return useQuery({
    queryKey: ['book-instances', 'filter', category],
    queryFn: () => getBookInstancesByCategory(category!),
    enabled: category !== null,
  })
}
