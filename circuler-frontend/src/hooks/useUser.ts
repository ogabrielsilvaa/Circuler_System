import { useQuery } from '@tanstack/react-query'
import { getUserById } from '../services/users.service'
import { UserResponse } from '../types/user.types'

export const USER_QUERY_KEY = 'user'

export function useUser(id: number) {
  const query = useQuery<UserResponse, Error, UserResponse, [string, number]>({
    queryKey: [USER_QUERY_KEY, id],
    queryFn: () => getUserById(id),
    enabled: id > 0,
    staleTime: 1000 * 60 * 5,
  })

  return {
    user: query.data ?? null,
    isLoading: query.isLoading,
    isError: query.isError,
  }
}
