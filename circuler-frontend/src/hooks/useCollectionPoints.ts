import { useQuery } from '@tanstack/react-query'
import { getCollectionPoints, getCollectionPointDetail } from '../services/collection-points.service'

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
