import { useQuery } from '@tanstack/react-query'
import { getUserById } from '../services/users.service'

export function useUser(id: number) {
  return useQuery({
    queryKey: ['user', id],
    queryFn: () => getUserById(id),
    enabled: id > 0,
  })
}
