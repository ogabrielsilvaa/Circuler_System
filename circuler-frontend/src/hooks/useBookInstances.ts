import { useQuery } from '@tanstack/react-query'
import { getBookInstances } from '../services/bookInstances.service'

export function useBookInstances() {
  return useQuery({
    queryKey: ['book-instances'],
    queryFn: getBookInstances,
  })
}
