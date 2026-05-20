import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { createReservation, getMyReservations } from '../services/reservations.service'
import { CreateReservationRequest, ReservationResponse } from '../types/reservation.types'
import { ReservationStatus } from '../constants/enums'

export const RESERVATIONS_QUERY_KEY = 'reservations'

export function useMyReservations() {
  const query = useQuery<ReservationResponse[], Error, ReservationResponse[], [string, string]>({
    queryKey: [RESERVATIONS_QUERY_KEY, 'my'],
    queryFn: getMyReservations,
    select: (data) => data.filter((r) => r.status === ReservationStatus.ATIVA),
    staleTime: 1000 * 60 * 5,
  })

  return {
    reservations: query.data ?? [],
    isLoading: query.isLoading,
    isError: query.isError,
  }
}

export function useCompletedReservations() {
  const query = useQuery<ReservationResponse[], Error, ReservationResponse[], [string, string]>({
    queryKey: [RESERVATIONS_QUERY_KEY, 'my'],
    queryFn: getMyReservations,
    select: (data) => data.filter((r) => r.status === ReservationStatus.CONCLUIDA),
    staleTime: 1000 * 60 * 5,
  })

  return {
    reservations: query.data ?? [],
    isLoading: query.isLoading,
    isError: query.isError,
  }
}

export function useCreateReservation() {
  const queryClient = useQueryClient()

  const mutation = useMutation<ReservationResponse, Error, CreateReservationRequest>({
    mutationFn: (payload) => createReservation(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [RESERVATIONS_QUERY_KEY] })
    },
  })

  return {
    createReservation: mutation.mutate,
    isPending: mutation.isPending,
    isSuccess: mutation.isSuccess,
    error: mutation.error,
  }
}
