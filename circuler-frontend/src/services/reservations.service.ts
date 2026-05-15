import { api } from './api'
import { CreateReservationRequest, ReservationResponse } from '../types/reservation.types'

export async function createReservation(data: CreateReservationRequest): Promise<ReservationResponse> {
  const response = await api.post<ReservationResponse>('/api/reservations', data)
  return response.data
}

export async function getMyReservations(): Promise<ReservationResponse[]> {
  const response = await api.get<ReservationResponse[]>('/api/reservations/my-reservations')
  return response.data
}
