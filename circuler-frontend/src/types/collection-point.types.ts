import { BookInstanceStatus, CollectionPointStatus } from '../constants/enums'

export type CollectionPoint = {
  id: number
  name: string
  addressStreet: string
  addressNeighborhood: string
  capacityLimit: number
  status: CollectionPointStatus
  userAdminId: number
  userAdminName: string
  userAdminEmail: string
  createdAt: string
  updatedAt: string
}

export type CollectionPointBook = {
  id: number
  bookId: number
  bookTitle: string
  bookAuthor: string
  bookCategory: string
  status: BookInstanceStatus
  userDonorId: number | null
  userDonorName: string | null
  bookThumbnailUrl: string | null
}

export type BookDonateRequest = {
  collectionPointId: number
  bookTitle: string
  bookAuthor: string
  bookPublisher?: string
  bookCategory: number
  bookIsbn?: string
}

export type CollectionPointDetail = {
  id: number
  name: string
  addressStreet: string
  addressNeighborhood: string
  capacityLimit: number
  status: CollectionPointStatus
  userAdminId: number
  userAdminName: string
  userAdminEmail: string
  createdAt: string
  updatedAt: string
  books: CollectionPointBook[]
}
