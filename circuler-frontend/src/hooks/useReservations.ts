import { useMutation } from '@tanstack/react-query'
import { createReservation } from '../services/reservations.service'

export function useCreateReservation() {
  return useMutation({ mutationFn: createReservation })
}
