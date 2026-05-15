import { useMutation, useQuery } from '@tanstack/react-query'
import { createReservation, getMyReservations } from '../services/reservations.service'

export function useMyReservations() {
  return useQuery({
    queryKey: ['reservations', 'my'],
    queryFn: getMyReservations,
  })
}

export function useCreateReservation() {
  return useMutation({ mutationFn: createReservation })
}
