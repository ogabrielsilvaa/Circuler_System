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
  bookTitle: string
  bookThumbnailUrl: string | null
  collectionPointName: string
  collectionPointAddressStreet: string
  collectionPointAddressNeighborhood: string
  createdAt: string
  updatedAt: string
}
