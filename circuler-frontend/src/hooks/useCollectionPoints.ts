import { useMutation, useQuery } from '@tanstack/react-query'
import { donateBook, getCollectionPoints, getCollectionPointDetail } from '../services/collection-points.service'

export function useCollectionPoints() {
  return useQuery({ queryKey: ['collection-points'], queryFn: getCollectionPoints })
}

export function useCollectionPointDetail(id: number) {
  return useQuery({
    queryKey: ['collection-points', id],
    queryFn: () => getCollectionPointDetail(id),
    enabled: !!id,
  })
}

export function useDonateBook() {
  return useMutation({ mutationFn: donateBook })
}
