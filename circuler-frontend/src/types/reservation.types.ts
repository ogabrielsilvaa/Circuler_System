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

export const MOCK_RESERVATIONS: ReservationResponse[] = [
  {
    id: 1,
    bookInstanceId: 1,
    userId: 1,
    status: ReservationStatus.ATIVA,
    verificationCode: 'CIRC-7X2K9',
    bookTitle: 'O Cortiço',
    bookThumbnailUrl: null,
    collectionPointName: 'Ponto Duque de Caxias Centro',
    collectionPointAddressStreet: 'Rua Marechal Floriano',
    collectionPointAddressNeighborhood: 'Centro',
    createdAt: '2025-01-10T10:00:00',
    updatedAt: '2025-01-10T10:00:00',
  },
  {
    id: 2,
    bookInstanceId: 3,
    userId: 1,
    status: ReservationStatus.ATIVA,
    verificationCode: 'CIRC-4M8RQ',
    bookTitle: 'Dom Casmurro',
    bookThumbnailUrl: null,
    collectionPointName: 'Ponto Nova Iguaçu',
    collectionPointAddressStreet: 'Av. Abílio Augusto Távora',
    collectionPointAddressNeighborhood: 'Nova América',
    createdAt: '2025-01-12T14:00:00',
    updatedAt: '2025-01-12T14:00:00',
  },
]
