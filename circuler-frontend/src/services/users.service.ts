import { api } from './api'
import { UserResponse } from '../types/user.types'

export async function getUserById(id: number): Promise<UserResponse> {
  const response = await api.get<UserResponse>(`/api/users/${id}`)
  return response.data
}
