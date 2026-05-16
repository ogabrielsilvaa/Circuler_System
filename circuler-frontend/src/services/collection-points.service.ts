import { api } from './api'
import { CollectionPoint, CollectionPointDetail } from '../types/collection-point.types'

export async function getCollectionPoints(): Promise<CollectionPoint[]> {
  const response = await api.get<CollectionPoint[]>('/api/collection-points')
  return response.data
}

export async function getCollectionPointDetail(id: number): Promise<CollectionPointDetail> {
  const response = await api.get<CollectionPointDetail>(`/api/collection-points/${id}/books`)
  return response.data
}
