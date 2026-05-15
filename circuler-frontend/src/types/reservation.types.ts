import { ReservationStatus } from '../constants/enums'

export type CreateReservationRequest = {
  bookInstanceId: number
}

export type ReservationResponse = {
  id: number
  bookInstanceId: number
  userId: number
  status: ReservationStatus
  verificationCode: string
  createdAt: string
  updatedAt: string
}
