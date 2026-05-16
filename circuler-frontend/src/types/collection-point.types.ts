import { BookCategoryEnum, BookInstanceStatus, CollectionPointStatus } from '../constants/enums'

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
  bookCategory: BookCategoryEnum
  status: BookInstanceStatus
  userDonorId: number | null
  userDonorName: string | null
  bookThumbnailUrl: string | null
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
