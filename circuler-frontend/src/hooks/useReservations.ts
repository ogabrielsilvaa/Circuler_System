import { useMutation, useQuery } from '@tanstack/react-query'
import { createReservation, getMyReservations } from '../services/reservations.service'
import { ReservationStatus } from '../constants/enums'

export function useMyReservations() {
  return useQuery({
    queryKey: ['reservations', 'my'],
    queryFn: getMyReservations,
    select: (data) => data.filter((r) => r.status === ReservationStatus.ATIVA),
  })
}

export function useCompletedReservations() {
  return useQuery({
    queryKey: ['reservations', 'my'],
    queryFn: getMyReservations,
    select: (data) => data.filter((r) => r.status === ReservationStatus.CONCLUIDA),
  })
}

export function useCreateReservation() {
  return useMutation({ mutationFn: createReservation })
}
